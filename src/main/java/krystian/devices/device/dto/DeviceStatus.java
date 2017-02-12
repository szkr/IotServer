package krystian.devices.device.dto;

/**
 * 12/24/2016 8:52 PM
 */
public class DeviceStatus {
    private long lastActivity;

    public long getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(long lastActivity) {
        this.lastActivity = lastActivity;
    }

    public DeviceStatus(long lastActivity) {
        this.lastActivity = lastActivity;
    }

    public DeviceStatus() {
    }
}
