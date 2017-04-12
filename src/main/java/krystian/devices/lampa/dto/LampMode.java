package krystian.devices.lampa.dto;

/**
 * 12/25/2016 3:02 PM
 */
public class LampMode {
    private Mode mode = Mode.UNKNOWN;

    public LampMode() {
    }

    public synchronized Mode getMode() {
        return mode;
    }

    public synchronized void setMode(Mode mode) {
        this.mode = mode;
    }

    public enum Mode {
        ON, OFF, AUTO, UNKNOWN
    }
}
