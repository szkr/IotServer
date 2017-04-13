package krystian.devices.lampa;

import krystian.devices.device.Device;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 2/6/2017 11:47 PM
 */
@Entity
public class MotionDetect {
    private Long id;
    private Timestamp detectionTime;
    private Device device;

    MotionDetect(Device device) {
        setDetectionTime(new Timestamp(System.currentTimeMillis()));
        this.device = device;
    }

    public MotionDetect() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(cascade=CascadeType.REMOVE)
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Timestamp getDetectionTime() {
        return detectionTime;
    }

    public void setDetectionTime(Timestamp detectionTime) {
        this.detectionTime = detectionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MotionDetect that = (MotionDetect) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
