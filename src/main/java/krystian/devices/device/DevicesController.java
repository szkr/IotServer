package krystian.devices.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 12/19/2016 2:12 PM
 */
@Controller
public class DevicesController {

    private final DeviceRepository devices;
    private final ApplicationContext context;

    @Autowired
    public DevicesController(DeviceRepository devices, ApplicationContext context) {
        this.devices = devices;
        this.context = context;
    }

    @RequestMapping(value = "/devices")
    public String devices(Model m) {
        m.addAttribute("devices", devices.findAll());
        return "devices/devices";
    }


    @RequestMapping(value = "/devices/{id}")
    public String devices2(@PathVariable("id") int id, Model model) {
        Device found = devices.findOne(id);
        if (found == null)
            return "devices/test";

        for (DeviceComponent component : context.getBeansOfType(DeviceComponent.class).values())
            if (component.getDeviceType() == found.getDeviceType())
                return component.getPage(model, found);

        return "devices/test";
    }
}
