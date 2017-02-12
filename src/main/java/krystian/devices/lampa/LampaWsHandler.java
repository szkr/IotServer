package krystian.devices.lampa;

import com.fasterxml.jackson.databind.ObjectMapper;
import krystian.devices.device.Device;
import krystian.devices.device.DeviceSocketHandler;
import krystian.devices.device.dto.MessageWithId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import krystian.devices.sessions.SessionHandler;

import java.util.Optional;

/**
 * 12/24/2016 2:36 PM
 */
@Component
public class LampaWsHandler extends DeviceSocketHandler {
    private final MotionDetectRepository motionDetectRepository;

    @Autowired
    public LampaWsHandler(SessionHandler handler, ObjectMapper mapper, MotionDetectRepository motionDetectRepository) {
        super(handler, mapper);
        this.motionDetectRepository = motionDetectRepository;
    }


    @Override
    public String getPath() {
        return "lampa";
    }

    @Override
    public void handleTxtMessage(WebSocketSession session, TextMessage message) {
        storeMotions(session, message);

    }

    private static class MotionDetectionDTO extends MessageWithId {
        public boolean motionDetect;
    }

    private void storeMotions(WebSocketSession session, TextMessage message) {
        Optional<Device> d = handler.getDevice(session);
        if (d.isPresent()) {
            try {
                MotionDetectionDTO m = mapper.readValue(message.getPayload(), MotionDetectionDTO.class);
                if (m.motionDetect) {
                    System.out.println(message.getPayload());
                    motionDetectRepository.save(new MotionDetect(d.get()));
                }
            } catch (Throwable e) {
            }
        }
    }
}
