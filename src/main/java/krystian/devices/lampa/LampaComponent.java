package krystian.devices.lampa;

import krystian.devices.device.*;
import krystian.devices.device.dto.DeviceStatus;
import krystian.devices.device.dto.MessageWithId;
import krystian.devices.device.dto.UpdateFirmware;
import krystian.devices.lampa.dto.*;
import krystian.devices.sessions.SessionHandler;
import krystian.devices.sessions.WSSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Override
    public DeviceSocketHandler getHandler() {
        return wsHandler;
    }

    @RequestMapping("devices/lampa/{id}/status")
    public DeviceStatus getStatus(@PathVariable("id") int id) {
        Device d = deviceRepository.findOne(id);
        if (d == null) return new DeviceStatus(-1);
        Optional<WSSession> s = sessionHandler.getSession(d.getKey());
        return new DeviceStatus(s.isPresent() ? System.currentTimeMillis() - s.get().getLastRcvMsgTime().get() : -1);
    }

    @RequestMapping("devices/lampa/{id}/getMode")
    public LampMode getMode(@PathVariable("id") int id) {
        MessageWithId a = new MessageWithId() {
            public String command = "getMode";
        };
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
        Device d = deviceRepository.findOne(id);
        if (d == null)
            return new LampAmbientBrightness();
        LampAmbientBrightness l = wsHandler.getResponse(d.getKey(), a, LampAmbientBrightness.class);
        if (l == null)
            return new LampAmbientBrightness();
        return l;
    }

    @RequestMapping("devices/lampa/{id}/restart")
    public LampRestart reset(@PathVariable("id") int id) {
        MessageWithId a = new MessageWithId() {
            public String command = "restart";
        };
        LampRestart l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, LampRestart.class);
        if (l == null) {
            l = new LampRestart();
            l.restart = false;
        }
        return l;
    }

    @RequestMapping("devices/lampa/{id}/getDeviceInfo")
    public LampDeviceInfo getDeviceInfo(@PathVariable("id") int id) {
        MessageWithId a = new MessageWithId() {
            public String command = "getDeviceInfo";
        };
        LampDeviceInfo l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, LampDeviceInfo.class);
        if (l == null) {
            l = new LampDeviceInfo();
        }
        return l;
    }

    @RequestMapping("devices/lampa/{id}/updateFirmware/{name}")
    public UpdateFirmware enableOta(@PathVariable("id") int id, @PathVariable("name") String name) {
        MessageWithId a = new MessageWithId() {
            public String command = "updateFirmware";
            public String firmwareName = name;
        };
        UpdateFirmware l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, UpdateFirmware.class);
        if (l == null) {
            l = new UpdateFirmware();
            l.update = false;
        }
        return l;
    }

    @RequestMapping("devices/lampa/{id}/getDeviceAddress")
    public String getDeviceAddress(@PathVariable("id") int id) {
        Optional<WSSession> x = sessionHandler.getSession(deviceRepository.findOne(id).getKey());
        return x.map(wsSession -> wsSession.getSession().getRemoteAddress().getHostName()).orElse("unknown");
    }
}
