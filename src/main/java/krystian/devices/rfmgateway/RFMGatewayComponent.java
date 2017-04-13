package krystian.devices.rfmgateway;

import krystian.devices.device.*;
import krystian.devices.device.dto.DeviceStatus;
import krystian.devices.device.dto.MessageWithId;
import krystian.devices.device.dto.UpdateFirmware;
import krystian.devices.rfmgateway.dto.*;
import krystian.devices.sessions.SessionHandler;
import krystian.devices.sessions.WSSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 12/22/2016 10:40 PM
 */
@RestController
public class RFMGatewayComponent implements DeviceComponent {
    private final static DeviceType deviceType = DeviceType.RFM_GATEWAY;
    private final DeviceRepository deviceRepository;
    private final SessionHandler sessionHandler;
    private final RFMGatewayWsHandler wsHandler;

    @Autowired
    private RFMessageRepository repo;
    @Autowired
    private ChartStorage storage;

    @Autowired
    public RFMGatewayComponent(DeviceRepository deviceRepository, SessionHandler sessionHandler, RFMGatewayWsHandler wsHandler) {
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

    @RequestMapping("devices/RFMGateway/{id}/status")
    public DeviceStatus getStatus(@PathVariable("id") int id) {
        Device d = deviceRepository.findOne(id);
        if (d == null) return new DeviceStatus(-1);
        Optional<WSSession> s = sessionHandler.getSession(d.getKey());
        return new DeviceStatus(s.isPresent() ? System.currentTimeMillis() - s.get().getLastRcvMsgTime().get() : -1);
    }


    @RequestMapping("devices/RFMGateway/{id}/restart")
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

    @RequestMapping("devices/RFMGateway/{id}/getDeviceInfo")
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

    @RequestMapping("devices/RFMGateway/{id}/updateFirmware/{name}")
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

    @RequestMapping("devices/RFMGateway{id}/getDeviceAddress")
    public String getDeviceAddress(@PathVariable("id") int id) {
        Optional<WSSession> x = sessionHandler.getSession(deviceRepository.findOne(id).getKey());
        return x.map(wsSession -> wsSession.getSession().getRemoteAddress().getHostName()).orElse("unknown");
    }

    @RequestMapping("devices/RFMGateway/{id}/getScanParameters")
    public RFMGatewayScanParameters getScanParameters(@PathVariable("id") int id) {
        MessageWithId a = new MessageWithId() {
            public String command = "getScanParameters";
        };
        RFMGatewayScanParameters l = null;
        try {
            l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, RFMGatewayScanParameters.class);
        } finally {

        }

        if (l == null) {
            l = new RFMGatewayScanParameters();
            l.from = 433050;
            l.to = 434790;
            l.samples = 600;
            l.stime = 5;
        }
        return l;
    }

    @RequestMapping(value = "devices/RFMGateway/{id}/setScanParameters", method = RequestMethod.POST)
    public RFMGatewayScanParameters setScanParameters(@PathVariable("id") int id, @RequestParam("from") String fromp
            , @RequestParam("to") String top
            , @RequestParam("samples") String samplesp, @RequestParam("stime") String stimep) {
        RFMGatewayScanParameters l = null;
        try {
            MessageWithId a = new MessageWithId() {
                public String command = "setScanParameters";
                public float from = Math.round(Float.parseFloat(fromp) * 1000);
                public float to = Math.round(Float.parseFloat(top) * 1000);
                public int samples = Integer.parseInt(samplesp);
                public int stime = Integer.parseInt(stimep);
            };
            l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, RFMGatewayScanParameters.class);

        } finally {
        }
        if (l == null) {
            l = new RFMGatewayScanParameters();
            l.from = 433050;
            l.to = 434790;
            l.samples = 600;
            l.stime = 5;
        } else {
            storage.clearPoints(deviceRepository.findOne(id).getKey());
        }
        return l;
    }

    @RequestMapping(value = "devices/RFMGateway/{id}/setRegisters", method = RequestMethod.POST)
    public List<Integer> setRegisters(@PathVariable("id") int id, @RequestParam("") List<Integer> registers) {
        RFMGatewayRegisters r = new RFMGatewayRegisters();
        r.reg = registers;
        return registers;
    }

    @RequestMapping("devices/RFMGateway/{id}/getPoints")
    public List<ChartStorage.Point> getPoints(@PathVariable("id") int id) {
        Optional<List<ChartStorage.Point>> o = storage.getChart(deviceRepository.findOne(id).getKey());
        return o.isPresent() ? o.get() : new ArrayList<>();
    }

    @RequestMapping(value = "devices/RFMGateway/{id}/getMessages", method = RequestMethod.GET)
    public ModelAndView getMessages(@PathVariable("id") int id, Model m) {
        ModelAndView mav = new ModelAndView("devices/RFMGatewayMessages");

        PageRequest pageRequest =
                new PageRequest(0, 10, Sort.Direction.DESC, "buildNumber");
        Page<RFMessage> artifactsPage =
                repo.findByDevice(deviceRepository.findOne(id), pageRequest);
        m.addAttribute("messages", artifactsPage.getContent());
        return mav;
    }

    @RequestMapping("devices/RFMGateway/{id}/setMode")
    public boolean getPoints(@PathVariable("id") int id, @RequestParam("mode") String mod) {
        RFMGatewayMode l = null;
        try {
            MessageWithId a = new MessageWithId() {
                public String command = "setMode";
                public String mode = mod;
            };
            l = wsHandler.getResponse(deviceRepository.findOne(id).getKey(), a, RFMGatewayMode.class);

            return true;
        } finally {

        }
    }
}
