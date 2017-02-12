package krystian;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static krystian.IotServerTests.test;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(test)
public class IotServerTests {
	public static final String test = "test";
	@Test
	public void contextLoads() {
	}
	public static final String serverKey = "sKey";

}
