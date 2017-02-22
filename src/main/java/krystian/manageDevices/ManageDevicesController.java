package krystian.manageDevices;

import krystian.devices.device.*;
import krystian.devices.device.dto.MessageWithId;
import krystian.devices.device.dto.UpdateFirmware;
import krystian.devices.sessions.SessionHandler;
import krystian.firmwares.storage.FileDescriptionRepository;
import krystian.firmwares.tempLink.LinkGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

/**
 * 12/19/2016 2:12 PM
 */
@Controller
public class ManageDevicesController {

    private final DeviceRepository devices;
    private final FileDescriptionRepository fileDescriptionRepository;
    private final ApplicationContext context;
    private final LinkGeneratorService linkGeneratorService;
    private final SessionHandler sessionHandler;

    @Autowired
    public ManageDevicesController(SessionHandler sessionHandler, DeviceRepository devices
            , FileDescriptionRepository fileDescriptionRepository, ApplicationContext context
            , LinkGeneratorService linkGeneratorService) {
        this.devices = devices;
        this.fileDescriptionRepository = fileDescriptionRepository;
        this.context = context;
        this.linkGeneratorService = linkGeneratorService;
        this.sessionHandler = sessionHandler;
    }


    @RequestMapping(value = "/manageDevices")
    public String devices(Model m, @RequestParam(required = false, defaultValue = "-1", name = "open") int idToLoad) {
        m.addAttribute("idToLoad", idToLoad);
        m.addAttribute("devices", devices.findAll());
        return "manageDevices/manageDevices";
    }

    @RequestMapping(value = "/manageDevices/new")
    public String newDev(Model model) {
        return "manageDevices/new";
    }

    @RequestMapping(value = "/manageDevices/get/{id}")
    public String get(@PathVariable("id") String id, Model model) {
        if (id.equals("new"))
            return newDev(model);
        Device found = devices.findOne(Integer.parseInt(id));
        if (found == null)
            return "devices/test";
        model.addAttribute("deviceIsOnline", sessionHandler.isDeviceOnline(devices.findOne(Integer.parseInt(id)).getKey()));
        model.addAttribute("files", fileDescriptionRepository.findAll());
        model.addAttribute("firmwareName", found.getFwName());
        model.addAttribute("curName", found.getName());
        model.addAttribute("curKey", found.getKey());
        return "manageDevices/device";
    }

    @RequestMapping(value = "/manageDevices/{id}/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public String remove(@PathVariable("id") String id, Model model) {
        try {
            devices.delete(Integer.parseInt(id));
        } catch (Exception e) {
            return e.getMessage();
        }
        return "ok";
    }

    @RequestMapping(value = "/manageDevices/{id}/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@PathVariable("id") String id, @RequestBody MultiValueMap<String, String> formData) {
        try {
            Device found = devices.findOne(Integer.parseInt(id));
            if (formData.containsKey("name"))
                found.setName(formData.getFirst("name"));
            if (formData.containsKey("key"))
                found.setKey(formData.getFirst("key"));
            devices.save(found);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "ok";
    }

    @RequestMapping(value = "/manageDevices/{id}/updateFirmware", method = RequestMethod.POST)
    @ResponseBody
    public String updateFirmware(@PathVariable("id") String id, @RequestBody MultiValueMap<String, String> formData) {

        Device d = devices.findOne(Integer.parseInt(id));
        if (d == null || !sessionHandler.isDeviceOnline(d.getKey())) return "device is offline";
        DeviceSocketHandler handler = null;
        for (DeviceComponent component : context.getBeansOfType(DeviceComponent.class).values())
            if (component.getDeviceType() == d.getDeviceType())
                handler = component.getHandler();
        if (handler == null) return "could not find wshandler";

        String mappedTo = linkGeneratorService.mapFile(10, formData.getFirst("fileId"));
        MessageWithId a = new MessageWithId() {
            public String command = "updateFirmware";
            public String firmwareName = mappedTo;
        };
        UpdateFirmware l = handler.getResponse(d.getKey(), a, UpdateFirmware.class);
        if (l == null) {
            l = new UpdateFirmware();
            l.update = false;
        }
        return "ok";
    }

    @RequestMapping(value = "/manageDevices/create", method = RequestMethod.POST)
    @ResponseBody
    public CreateDeviceResponse create(@RequestBody MultiValueMap<String, String> formData) {
        CreateDeviceResponse r = new CreateDeviceResponse();
        r.message = "empty";
        r.createdDeviceId = -1;
        if (!formData.containsKey("type") || formData.getFirst("type").isEmpty() || !formData.containsKey("name") || formData.getFirst("name").isEmpty()) {
            r.status = "error";
            return r;
        }
        Device d = new Device(DeviceType.valueOf(formData.getFirst("type")), formData.getFirst("name"));
        if (formData.containsKey("key") && !formData.getFirst("key").isEmpty())
            d.setKey(formData.getFirst("key"));
        devices.save(d);
        r.createdDeviceId = d.getId();
        r.status = "ok";
        return r;
    }

    private class CreateDeviceResponse {
        public String status;
        public String message;
        public int createdDeviceId;
    }
}
