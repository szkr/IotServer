package krystian.manageDevices;

import krystian.devices.device.Device;
import krystian.devices.device.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import krystian.devices.device.DeviceType;

/**
 * 12/19/2016 2:12 PM
 */
@Controller
public class ManageDevicesController {

    @Autowired
    DeviceRepository devices;

    @Autowired
    ApplicationContext context;


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

    private class CreateDeviceResponse {
        public String status;
        public String message;
        public int createdDeviceId;
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
}
