package krystian.devices.lampa;

import krystian.devices.device.Device;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * 2/7/2017 1:14 AM
 */
public interface MotionDetectRepository extends CrudRepository<MotionDetect, Long> {
    List<MotionDetect> findByDevice(Device dev);

    List<MotionDetect> findByDeviceOrderByDetectionTimeDesc(Device dev);

    @Query("SELECT o FROM MotionDetect o WHERE o.device.id=:devID AND o.detectionTime> :since ORDER BY o.detectionTime DESC")
    List<MotionDetect> findMotionDetect(@Param("devID") int devID, @Param("since") Timestamp from);
}
