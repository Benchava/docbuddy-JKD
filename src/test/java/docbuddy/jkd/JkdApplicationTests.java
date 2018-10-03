package docbuddy.jkd;

import docbuddy.jkd.filters.SecurityFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JkdApplicationTests {
    @Autowired
    private SecurityFilter securityFilter;

	@Test
	public void contextLoads() {
        assertNotNull(securityFilter);
	}

}
