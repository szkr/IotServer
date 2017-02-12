package krystian.devices.device;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 12/22/2016 3:10 PM
 */
@Repository
public interface DeviceRepository extends CrudRepository<Device, Integer> {
    Device findOneByKey(String key);
}
