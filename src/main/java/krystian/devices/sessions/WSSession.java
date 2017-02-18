package krystian.devices.sessions;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 12/25/2016 12:15 PM
 */
public class WSSession {
    private final WebSocketSession session;
    private final AtomicLong lastRcvMsgTime;
    private final AtomicLong lastSentMsgTime;


    public WSSession(WebSocketSession session) {
        this.session = session;
        this.lastRcvMsgTime = new AtomicLong(System.currentTimeMillis());
        this.lastSentMsgTime = new AtomicLong(System.currentTimeMillis());
    }

    public WebSocketSession getSession() {
        return session;
    }

    public AtomicLong getLastRcvMsgTime() {
        return lastRcvMsgTime;
    }

    public void sendMessage(WebSocketMessage message) throws IOException {
        session.sendMessage(message);
        lastSentMsgTime.set(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object obj) {
        return session.equals(obj);
    }

    @Override
    public int hashCode() {
        return session.hashCode();
    }

    public AtomicLong getLastSentMsgTime() {
        return lastSentMsgTime;
    }
}
