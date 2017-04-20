package krystian.devices.rfmdevice;

import krystian.devices.device.*;
import krystian.devices.rfmgateway.MsgReceivedListener;
import krystian.devices.rfmgateway.RFMGatewayWsHandler;
import krystian.devices.rfmgateway.dto.RFMessageRepository;
import krystian.devices.sessions.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * 12/22/2016 10:40 PM
 */
@RestController
public class RFMDeviceComponent implements DeviceComponent, MsgReceivedListener {
    private final static DeviceType deviceType = DeviceType.RFM_DEVICE;
    private final DeviceRepository deviceRepository;
    private final SessionHandler sessionHandler;
    private final TemperatureReadingRepository temperatureReadingRepository;

    @Autowired
    private RFMessageRepository repo;

    @Autowired
    private RFMGatewayWsHandler wsHandler;

    @Autowired
    public RFMDeviceComponent(DeviceRepository deviceRepository, SessionHandler sessionHandler, TemperatureReadingRepository temperatureReadingRepository) {
        this.deviceRepository = deviceRepository;
        this.sessionHandler = sessionHandler;
        this.temperatureReadingRepository = temperatureReadingRepository;
        wsHandler.addListener(this);
    }

    @Override
    public DeviceType getDeviceType() {
        return deviceType;
    }

    @Override
    public String getPage(Model model, Device device) {
        model.addAttribute("type", "RFMDevice");
        model.addAttribute("temperatureReadings", temperatureReadingRepository.findByDeviceOrderByTimeDesc(device));
        return "devices/RFMDevice";
    }

    @Override
    public DeviceSocketHandler getHandler() {
        return null;
    }


    @Override
    public void msgReceived(String devID, String msg) {
        Optional<Device> d = Optional.ofNullable(deviceRepository.findOneByKey(devID));
        if(!d.isPresent()) return;
        temperatureReadingRepository.save(new TemperatureReading(d.get(), Double.parseDouble(msg)));
    }
}
