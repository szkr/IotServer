package krystian.devices.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import krystian.devices.GlobalWSMessageHandler;
import krystian.devices.WSMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

/**
 * 2/22/2017 10:40 AM
 */
@Service
public class DeviceInfoUpdater implements WSMessageListener {

    private final DeviceRepository repository;

    @Autowired
    private DeviceInfoUpdater(GlobalWSMessageHandler handler, DeviceRepository repository) {
        handler.register(this);
        this.repository = repository;
    }

    @Override
    public boolean messageReceived(TextMessage message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            WelcomeMessage m = mapper.readValue(message.getPayload(), WelcomeMessage.class);
            Device d = repository.findOneByKey(m.key);
            if (d == null || !d.getKey().equals(m.key)) return false;
            d.setFwName(m.fwName);
            repository.save(d);
        } catch (IOException ignored) {
        }
        return false;
    }

    private static class WelcomeMessage {
        public String key;
        public String fwName;
    }
}
