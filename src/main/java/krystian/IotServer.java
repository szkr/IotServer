package krystian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication

public class IotServer extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(IotServer.class, args);
    }
    public static final String serverKey = "sKey";
}


