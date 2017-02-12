package krystian.devices.lampa;

import krystian.IotServer;
import krystian.devices.device.Device;
import krystian.devices.device.DeviceRepository;
import krystian.devices.device.dto.DeviceStatus;
import krystian.devices.device.dto.MessageWithId;
import krystian.devices.lampa.dto.*;
import krystian.devices.sessions.WSSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import krystian.devices.device.DeviceComponent;
import krystian.devices.device.DeviceType;
import krystian.devices.sessions.SessionHandler;

import java.util.Optional;

/**
 * 12/22/2016 10:40 PM
 */
@RestController
public class LampaComponent implements DeviceComponent {
    private final static DeviceType deviceType = DeviceType.LAMP_WITH_SENSOR;

    private final DeviceRepository deviceRepository;
    private final MotionDetectRepository motionDetectRepository;
    private final SessionHandler sessionHandler;
    private final LampaWsHandler wsHandler;

    @Autowired
    public LampaComponent(DeviceRepository deviceRepository, MotionDetectRepository motionDetectRepository, SessionHandler sessionHandler, LampaWsHandler wsHandler) {
        this.deviceRepository = deviceRepository;
        this.motionDetectRepository = motionDetectRepository;
        this.sessionHandler = sessionHandler;
        this.wsHandler = wsHandler;
    }

    @Override
    public DeviceType getDeviceType() {
        return deviceType;
    }

    @Override
    public String getPage(Model model, Device device) {
        MessageWithId a = new MessageWithId() {
            public String command = "getAmbientBrightness";
        };
        a.setKey(IotServer.serverKey);
        LampAmbientBrightness l = wsHandler.getResponse(deviceRepository.findOne(device.getId()).getKey(), a, LampAmbientBrightness.class);
        if (l == null) {
            l = new LampAmbientBrightness();
            l.ambientBrightness = -1;
        }
        model.addAttribute("type", "lampa");
        model.addAttribute("envBrightness", l.ambientBrightness);
        model.addAttribute("detections", motionDetectRepository.findByDeviceOrderByDetectionTimeDesc(device));
        return "devices/lampa";
    }

    @RequestMapping("devices/lampa/{id}/status")
    public DeviceStatus getStatus(@PathVariable("id") int id) {
        Device d = deviceRepository.findOne(id);
        if (d == null) return new DeviceStatus(-1);
        Optional<WSSession> s = sessionHandler.getSession(d.getKey());
        return new DeviceStatus(s.isPresent() ? System.currentTimeMillis() - s.get().getLastMsgTime().get() : -1);
    }

    @RequestMapping("devices/lampa/{id}/getMode")
    public LampMode getMode(@PathVariable("id") int id) {
        MessageWithId a = new MessageWithId() {
            public String command = "getMode";
        };
        a.setKey(IotServer.serverKey);
        LampMode l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, LampMode.class);
        if (l == null) {
            l = new LampMode();
            l.setMode(LampMode.Mode.UNKNOWN);
        }
        return l;
    }

    @RequestMapping("devices/lampa/{id}/setMode/{state}")
    public LampMode setMode(@PathVariable("id") int id, @PathVariable("state") LampMode.Mode s) {
        MessageWithId a = new MessageWithId() {
            public LampMode.Mode mode = s;
            public String command = "setMode";
        };
        a.setKey(IotServer.serverKey);
        LampMode l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, LampMode.class);
        if (l == null) {
            l = new LampMode();
            l.setMode(LampMode.Mode.UNKNOWN);
        }
        return l;
    }

    @RequestMapping("devices/lampa/{id}/getBrightness")
    public LampBrightness getBrightness(@PathVariable("id") int id) {
        MessageWithId a = new MessageWithId() {
            public String command = "getBrightness";
        };
        a.setKey(IotServer.serverKey);
        LampBrightness l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, LampBrightness.class);
        if (l == null) {
            l = new LampBrightness();
        }
        return l;
    }

    @RequestMapping("devices/lampa/{id}/setBrightness/{state}")
    public LampBrightness setBrightness(@PathVariable("id") int id, @PathVariable("state") int s) {
        MessageWithId a = new MessageWithId() {
            public String command = "setBrightness";
            public int brightness = s;
        };
        a.setKey(IotServer.serverKey);
        LampBrightness l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, LampBrightness.class);
        if (l == null) {
            l = new LampBrightness();
        }
        return l;
    }

    @RequestMapping("devices/lampa/{id}/getTimeOnAfterMove")
    public LampTimeOnAfterMove getTimeOnAfterMove(@PathVariable("id") int id) {
        MessageWithId a = new MessageWithId() {
            public String command = "getBrightness";
        };
        a.setKey(IotServer.serverKey);
        LampTimeOnAfterMove l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, LampTimeOnAfterMove.class);
        if (l == null) {
            l = new LampTimeOnAfterMove();
        }
        return l;
    }

    @RequestMapping("devices/lampa/{id}/setTimeOnAfterMove/{state}")
    public LampTimeOnAfterMove setTimeOnAfterMove(@PathVariable("id") int id, @PathVariable("state") int s) {
        MessageWithId a = new MessageWithId() {
            public String command = "setTimeOnAfterMove";
            public int timeOnAfterMove = s;
        };
        a.setKey(IotServer.serverKey);
        LampTimeOnAfterMove l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, LampTimeOnAfterMove.class);
        if (l == null) {
            l = new LampTimeOnAfterMove();
        }
        return l;
    }

    @RequestMapping("devices/lampa/{id}/getAmbientBrightness")
    public LampAmbientBrightness getAmbientBrightness(@PathVariable("id") int id) {
        MessageWithId a = new MessageWithId() {
            public String command = "getAmbientBrightness";
        };
        a.setKey(IotServer.serverKey);
        Device d = deviceRepository.findOne(id);
        if (d == null)
            return new LampAmbientBrightness();
        LampAmbientBrightness l = wsHandler.getResponse(d.getKey(), a, LampAmbientBrightness.class);
        if (l == null)
            return new LampAmbientBrightness();
        return l;
    }

    @RequestMapping("devices/lampa/{id}/reset")
    public LampReset reset(@PathVariable("id") int id) {
        MessageWithId a = new MessageWithId() {
            public String command = "reset";
        };
        a.setKey(IotServer.serverKey);
        LampReset l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, LampReset.class);
        if (l == null) {
            l = new LampReset();
            l.reset = false;
        }
        return l;
    }

    @RequestMapping("devices/lampa/{id}/getDeviceInfo")
    public LampDeviceInfo getDeviceInfo(@PathVariable("id") int id) {
        MessageWithId a = new MessageWithId() {
            public String command = "getDeviceInfo";
        };
        a.setKey(IotServer.serverKey);
        LampDeviceInfo l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, LampDeviceInfo.class);
        if (l == null) {
            l = new LampDeviceInfo();
        }
        return l;
    }
}
