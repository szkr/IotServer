package krystian.devices.rfmdevice;

import com.fasterxml.jackson.databind.ObjectMapper;
import krystian.devices.device.DeviceSocketHandler;
import krystian.devices.rfmgateway.dto.RFMessageRepository;
import krystian.devices.sessions.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 12/24/2016 2:36 PM
 */
@Component
public class RFMDeviceWsHandler extends DeviceSocketHandler {

    @Autowired
    RFMessageRepository repository;


    @Autowired
    public RFMDeviceWsHandler(SessionHandler handler, ObjectMapper mapper) {
        super(handler, mapper);
    }


    @Override
    public String getPath() {
        return "RFMGateway";
    }

    @Override
    public void handleTxtMessage(WebSocketSession session, TextMessage message) {


    }
}
