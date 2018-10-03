package docbuddy.jkd.filters;

import docbuddy.jkd.exceptions.InvalidTokenException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import docbuddy.jkd.commons.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Component
public class SecurityFilter extends ZuulFilter {

    @Value("${jwt.token.lifetime}")
    private Integer tokenExpiredDays;

    @Value("${jwt.secret}")
    private String secret;


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

        //If it's not a excluded URL
        if (StringUtils.endsWithAny(requestUri, SecurityConstants.UNSECURED_URLS)) {
            return null;
        }

        try {
            //If it's not, let's go ahead and validate token
            String token = request.getHeader(SecurityConstants.TOKEN_HEADER);

            validateToken(token);

        } catch (Exception ex) {
            throw new ZuulException("Access forbiden", 500, "Invalid token");
        }


        return null;
    }

    private void validateToken(String token) throws InvalidTokenException {

        Claims claims = Jwts.parser()
                .setSigningKey(TextCodec.BASE64.encode(secret))
                .parseClaimsJws(token).getBody();

        //Validate subject
        if (claims.getSubject() == null) {
            throw new InvalidTokenException();
        }


        //Validate expiraton date

        Calendar calendar = Calendar.getInstance();

        Date generatedDate = claims.getIssuedAt();

        Date today = calendar.getTime();

        calendar.setTime(generatedDate);

        calendar.add(Calendar.DATE, tokenExpiredDays);

        Date expirationDate = calendar.getTime();

        if (expirationDate.before(today)) {
            throw new InvalidTokenException();
        }

    }
}
