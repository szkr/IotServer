package krystian.devices.rfmgateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import krystian.devices.device.Device;
import krystian.devices.device.DeviceSocketHandler;
import krystian.devices.device.dto.MessageWithId;
import krystian.devices.rfmgateway.dto.RFMessage;
import krystian.devices.rfmgateway.dto.RFMessageRepository;
import krystian.devices.sessions.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 12/24/2016 2:36 PM
 */
@Component
public class RFMGatewayWsHandler extends DeviceSocketHandler {

    @Autowired
    RFMessageRepository repository;
    @Autowired
    private ChartStorage storage;


    @Autowired
    public RFMGatewayWsHandler(SessionHandler handler, ObjectMapper mapper) {
        super(handler, mapper);
    }


    @Override
    public String getPath() {
        return "RFMGateway";
    }

    @Override
    public void handleTxtMessage(WebSocketSession session, TextMessage message) {
        storeChartPoints(session, message);

    }

    private void storeChartPoints(WebSocketSession session, TextMessage message) {
        Optional<Device> d = handler.getDevice(session);
        if (d.isPresent()) {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode n = null;
            try {
                n = objectMapper.readTree(message.getPayload());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String msg = n.get("msg").asText();
                RFMessage m = new RFMessage();
                m.setContent("-> " + msg);
                m.setDevice(handler.getDevice(session).get());
                m.setRssi(n.get("rssi").asInt());
                m.setReceiveTime(new Timestamp(System.currentTimeMillis()));
                repository.save(m);
            } catch (Exception e) {
            }

            try {
                JsonNode freqBuffer = n.get("f");
                JsonNode rssiBuffer = n.get("s");

                List<ChartStorage.Point> pts = new ArrayList<>();
                for (int i = 0; i < freqBuffer.size(); i++) {
                    ChartStorage.Point poi = new ChartStorage.Point();
                    poi.x = (float) (freqBuffer.get(i).asInt() / 1000.0);
                    int x = rssiBuffer.get(i).asInt();
                    poi.y = (float) (0.0000059686374 * x * x * x - 0.00222775234 * x * x + 0.751206817 * x - 130.7611261);
                    pts.add(poi);
                }
                storage.addPoints(handler.getDevice(session).get().getKey(), pts);
            } catch (Exception e) {
            }
        }
    }

    private static class MotionDetectionDTO extends MessageWithId {
        public boolean motionDetect;
    }
}
