package docbuddy.jkd.filters;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import docbuddy.jkd.commons.SecurityConstants;
import docbuddy.jkd.exceptions.InvalidTokenException;

@Component
public class SecurityFilter extends ZuulFilter {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private EurekaClient eurekaClient;

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();

		HttpServletRequest request = ctx.getRequest();

		String requestUri = request.getRequestURI();

		// If it's not a excluded URL
		if (StringUtils.endsWithAny(requestUri, SecurityConstants.UNSECURED_URLS)) {
			return null;
		}

		try {
			// If it's not, let's go ahead and validate token
			String token = request.getHeader(SecurityConstants.TOKEN_HEADER);

			validateToken(token);

		} catch (Exception ex) {
			throw new ZuulException("Access forbiden", 403, "Invalid token");
		}

		return null;
	}

	private void validateToken(String token) throws InvalidTokenException {

		Application usersApp = eurekaClient.getApplication("AUTH");

		InstanceInfo instanceInfo = usersApp.getInstances().get(0);

		List<String> toConcatenate = Arrays.asList("http://", instanceInfo.getIPAddr(), ":",
				new Integer(instanceInfo.getPort()).toString(), "/validateToken");

		String url = String.join("", toConcatenate);

		Boolean tokenValid = restTemplate.postForObject(url, token, Boolean.class);
		if (!tokenValid) {
			throw new InvalidTokenException();
		}

	}
}
