package krystian.devices.rfmdevice;

import krystian.devices.device.*;
import krystian.devices.rfmgateway.dto.RFMessageRepository;
import krystian.devices.sessions.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

/**
 * 12/22/2016 10:40 PM
 */
@RestController
public class RFMDeviceComponent implements DeviceComponent {
    private final static DeviceType deviceType = DeviceType.RFM_DEVICE;
    private final DeviceRepository deviceRepository;
    private final SessionHandler sessionHandler;
    private final RFMDeviceWsHandler wsHandler;

    @Autowired
    private RFMessageRepository repo;

    @Autowired
    public RFMDeviceComponent(DeviceRepository deviceRepository, SessionHandler sessionHandler, RFMDeviceWsHandler wsHandler) {
        this.deviceRepository = deviceRepository;
        this.sessionHandler = sessionHandler;
        this.wsHandler = wsHandler;
    }

    @Override
    public DeviceType getDeviceType() {
        return deviceType;
    }

    @Override
    public String getPage(Model model, Device device) {
        model.addAttribute("type", "RFMGateway");
        return "devices/RFMGateway";
    }

    @Override
    public DeviceSocketHandler getHandler() {
        return wsHandler;
    }


}
