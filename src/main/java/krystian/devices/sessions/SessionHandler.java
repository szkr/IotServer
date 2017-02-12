package krystian.devices.sessions;

import krystian.devices.device.Device;
import krystian.devices.device.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 12/24/2016 3:57 PM
 */
@Service
public class SessionHandler {
    private final ConcurrentHashMap<String, WSSession> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<WebSocketSession, Device> sessions2 = new ConcurrentHashMap<>();
    private final DeviceRepository deviceRepository;

    @Autowired
    public SessionHandler(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public synchronized void updateSession(String key, WebSocketSession session) {
        if (sessions.containsKey(key)) {
            if (sessions.get(key).getSession() != session) {
                disconnectSession(key);
                sessions.put(key, new WSSession(session));
                Device d = deviceRepository.findOneByKey(key);
                if (d != null)
                    sessions2.put(session, d);
                return;
            }
            sessions.get(key).getLastMsgTime().set(System.currentTimeMillis());
        } else {
            sessions.put(key, new WSSession(session));
            Device d = deviceRepository.findOneByKey(key);
            if (d != null)
                sessions2.put(session, d);
        }
    }

    public synchronized void disconnectSession(String key) {
        if (sessions.containsKey(key)) {
            WebSocketSession wss = sessions.get(key).getSession();
            if (sessions2.containsKey(wss))
                sessions2.remove(wss);
            try {
                wss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            sessions.remove(key);

        }
    }

    public Optional<WSSession> getSession(String deviceKey) {
        return Optional.ofNullable(sessions.get(deviceKey));
    }

    public Optional<Device> getDevice(WebSocketSession session) {
        return Optional.ofNullable(sessions2.get(session));
    }

    public Map<String, WSSession> getSessions() {
        return new HashMap<>(sessions);
    }

}
