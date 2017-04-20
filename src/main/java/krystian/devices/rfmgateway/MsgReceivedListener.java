package krystian.devices.rfmgateway;

/**
 * 20.04.2017 14:41
 */
public interface MsgReceivedListener {
    void msgReceived(String devID, String msg);
}
