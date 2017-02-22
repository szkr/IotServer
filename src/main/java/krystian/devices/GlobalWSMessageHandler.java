package krystian.devices;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 2/22/2017 10:21 AM
 */
@Service
public class GlobalWSMessageHandler {
    private CopyOnWriteArrayList<WSMessageListener> listeners;

    private GlobalWSMessageHandler() {
        listeners = new CopyOnWriteArrayList<>();
    }

    public void register(WSMessageListener l) {
        listeners.add(l);
    }

    public void remove(WSMessageListener l) {
        listeners.remove(l);
    }


    public boolean messageReceived(TextMessage message) {
        for (WSMessageListener l : listeners)
            if (l.messageReceived(message)) return true;
        return false;
    }

}
