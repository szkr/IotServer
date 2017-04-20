package krystian.devices.rfmdevice;

import krystian.devices.device.Device;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * 2/7/2017 1:14 AM
 */
public interface TemperatureReadingRepository extends CrudRepository<TemperatureReading, Long> {
    List<TemperatureReading> findByDevice(Device dev);

    List<TemperatureReading> findByDeviceOrderByTimeDesc(Device dev);

    @Query("SELECT o FROM TemperatureReading o WHERE o.device.id=:devID AND o.time> :since ORDER BY o.time DESC")
    List<TemperatureReading> findTemperatureReading(@Param("devID") int devID, @Param("since") Timestamp from);
}
