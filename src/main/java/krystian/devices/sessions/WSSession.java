package krystian.devices.sessions;

import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 12/25/2016 12:15 PM
 */
public class WSSession {
    private final WebSocketSession session;
    private final AtomicLong lastMsgTime;

    public WebSocketSession getSession() {
        return session;
    }

    public AtomicLong getLastMsgTime() {
        return lastMsgTime;
    }

    public WSSession(WebSocketSession session) {
        this.session = session;
        this.lastMsgTime = new AtomicLong(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object obj) {
        return session.equals(obj);
    }

    @Override
    public int hashCode() {
        return session.hashCode();
    }
}
