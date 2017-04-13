package krystian.devices.rfmgateway.dto;

import krystian.devices.device.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * 13.04.2017 14:03
 */
public interface RFMessageRepository extends CrudRepository<RFMessage, Long> {
    Page<RFMessage> findByDevice(Device dev, Pageable p);
}
