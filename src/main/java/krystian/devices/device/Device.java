package krystian.devices.device;

import javax.persistence.*;
import java.util.UUID;

/**
 * 12/21/2016 5:02 PM
 */
@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Integer id;

    @Column(name = "type", nullable = false, updatable = false)
    @Enumerated(EnumType.ORDINAL)
    private DeviceType type;

    @Column(name = "name")
    private String name;


    @Column(name = "key")
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DeviceType getDeviceType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Device(DeviceType type, String name) {
        this.type = type;
        this.name = name;
    }

    public Device() {
    }

    @PrePersist
    public void generateKey() {
        if (key == null)
            key = UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        return id.equals(device.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
