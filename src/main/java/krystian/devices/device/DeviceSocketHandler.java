package krystian.devices.device;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import krystian.IotServer;
import krystian.devices.device.dto.MessageWithId;
import krystian.devices.sessions.DeviceMessage;
import krystian.devices.sessions.SessionHandler;
import krystian.devices.sessions.WSSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 12/23/2016 11:47 PM
 */
public abstract class DeviceSocketHandler extends TextWebSocketHandler {

    protected final SessionHandler handler;
    protected final ObjectMapper mapper;

    private AtomicInteger msgId = new AtomicInteger();
    private ConcurrentHashMap<Integer, String> pending = new ConcurrentHashMap<>();
    private ConcurrentHashMap<WebSocketSession, Long> activeTimes = new ConcurrentHashMap<>();

    public abstract String getPath();

    public final long maxIdleTime = 30000;

    @Autowired
    public DeviceSocketHandler(SessionHandler handler, ObjectMapper mapper) {
        super();
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (Map.Entry<WebSocketSession, Long> e : activeTimes.entrySet())
                    if (e.getKey().isOpen() && e.getValue() + maxIdleTime < System.currentTimeMillis()) {
                        sendMessage(IotServer.serverKey, e.getKey(), new TextMessage("keepalive"));
                    }
            }
        });
        this.handler = handler;
        this.mapper = mapper;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        activeTimes.put(session, System.currentTimeMillis());
        DeviceMessage dm;
        try {
            dm = mapper.readValue(message.getPayload(), DeviceMessage.class);
            handler.updateSession(dm.getKey(), session);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            MessageWithId msg = mapper.readValue(message.getPayload(), MessageWithId.class);
            if (pending.containsKey(msg.id))
                pending.put(msg.id, message.getPayload());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            handleTxtMessage(session, message);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    public <T> T getResponse(String key, MessageWithId command, Class<T> valueType) {
        return getResponse(key, command, valueType, 200);
    }

    private <T> T getResponse(String key, MessageWithId command, Class<T> valueType, int timeout) {
        Optional<WSSession> sess = handler.getSession(key);
        if (!sess.isPresent())
            return null;
        int id = msgId.getAndIncrement();
        command.id = id;

        TextMessage msg = null;
        WebSocketSession se = sess.get().getSession();
        try {
            msg = new TextMessage(mapper.writeValueAsString(command));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        if (sendMessage(key, se, msg)) return null;
        pending.put(id, "");
        for (int i = 0; i < timeout; i++) {
            if (!pending.get(id).equals("")) {
                try {
                    return mapper.readValue(pending.remove(id), valueType);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        pending.remove(id);
        return null;
    }

    public boolean sendMessage(String key, WebSocketSession se, TextMessage msg) {
        try {
            AtomicBoolean completed = new AtomicBoolean(false);
            Thread t = new Thread(() -> {
                try {
                    se.sendMessage(msg);
                    activeTimes.put(se, System.currentTimeMillis());
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.disconnectSession(key);
                }
                completed.set(true);
            });
            t.start();

            int i;
            for (i = 0; i < 1000; i++) {
                Thread.sleep(1);
                if (completed.get())
                    break;
            }
            if (i == 1000) {
                t.stop();
                throw new Exception("SendMessage timeout");
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.disconnectSession(key);
            return true;
        }
        return false;
    }

    public abstract void handleTxtMessage(WebSocketSession session, TextMessage message);

}
