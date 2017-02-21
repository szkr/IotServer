package krystian.devices.device;

import org.springframework.ui.Model;

/**
 * 12/22/2016 10:47 PM
 */
public interface DeviceComponent {
    DeviceType getDeviceType();
    String getPage(Model model, Device device);

    DeviceSocketHandler getHandler();
}
