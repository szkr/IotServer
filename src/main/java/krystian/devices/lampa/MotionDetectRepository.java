package krystian.devices.lampa;

import krystian.devices.device.Device;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 2/7/2017 1:14 AM
 */
public interface MotionDetectRepository extends CrudRepository<MotionDetect, Long> {
    List<MotionDetect> findByDevice(Device dev);
    List<MotionDetect> findByDeviceOrderByDetectionTimeDesc(Device dev);
}
