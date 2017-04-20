package krystian.devices.rfmdevice;

import krystian.devices.device.Device;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 2/6/2017 11:47 PM
 */
@Entity
public class TemperatureReading {
    private Long id;
    private Timestamp time;
    private Device device;
    private double temperature;

    TemperatureReading(Device device, double temperature) {
        setTemperature(temperature);
        setTime(new Timestamp(System.currentTimeMillis()));
        this.device = device;
    }

    public TemperatureReading() {
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemperatureReading that = (TemperatureReading) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
