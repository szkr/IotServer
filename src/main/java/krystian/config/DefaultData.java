package krystian.config;

import krystian.devices.device.Device;
import krystian.devices.device.DeviceRepository;
import krystian.devices.device.DeviceType;
import krystian.security.user.Role;
import krystian.security.user.User;
import krystian.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 2/11/2017 10:49 AM
 */
@Component
public class DefaultData {
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    @Autowired
    public DefaultData(UserRepository userRepository, DeviceRepository deviceRepository) {

        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
    }

    @EventListener({ContextRefreshedEvent.class})
    void contextRefreshedEvent() {
        if (deviceRepository.count() == 0 && userRepository.count() == 0) {
            deviceRepository.save(new Device(DeviceType.LAMP_WITH_SENSOR, "Example device"));
            User u = new User();
            u.setLogin("Admin");
            u.setPassword("1877admin");
            u.setRole(Role.ADMIN);
            userRepository.save(u);
        }
    }
}
