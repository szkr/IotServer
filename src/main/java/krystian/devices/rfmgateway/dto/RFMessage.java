package krystian.devices.rfmgateway.dto;

import krystian.devices.device.Device;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 2/6/2017 11:47 PM
 */
@Entity
public class RFMessage {
    private Long id;
    private Timestamp receiveTime;
    private Device device;
    private String content;
    private int rssi;
    RFMessage(Device device) {
        setReceiveTime(new Timestamp(System.currentTimeMillis()));
        this.device = device;
    }

    public RFMessage() {
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

    public Timestamp getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Timestamp receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RFMessage that = (RFMessage) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
