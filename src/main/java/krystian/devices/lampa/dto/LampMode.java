package krystian.devices.lampa.dto;

/**
 * 12/25/2016 3:02 PM
 */
public class LampMode {
    public enum Mode {
        ON, OFF, AUTO, UNKNOWN
    }
    private Mode mode = Mode.UNKNOWN;

    public synchronized void setMode(Mode mode) {
        this.mode = mode;
    }

    public synchronized Mode getMode() {
        return mode;
    }

    public LampMode() {
    }
}
