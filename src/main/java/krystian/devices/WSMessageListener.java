package krystian.devices;

import org.springframework.web.socket.TextMessage;

/**
 * 2/22/2017 10:23 AM
 */
public interface WSMessageListener {
    boolean messageReceived(TextMessage message);
}
