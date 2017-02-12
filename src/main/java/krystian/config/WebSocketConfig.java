package krystian.config;

/**
 * 12/23/2016 6:42 PM
 */

import krystian.devices.device.DeviceSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Component
public class WebSocketConfig implements WebSocketConfigurer {
    public static final String WS_PREFIX = "/dev/";
    private final ApplicationContext context;

    @Autowired
    public WebSocketConfig(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        context.getBeansOfType(DeviceSocketHandler.class).values().forEach(
                val -> registry.addHandler(val, WS_PREFIX + val.getPath()).setAllowedOrigins("*"));
    }
}