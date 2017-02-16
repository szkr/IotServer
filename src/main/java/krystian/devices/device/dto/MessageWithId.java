package krystian.devices.device.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import krystian.devices.sessions.DeviceMessage;

/**
 * 12/25/2016 6:49 PM
 */
@JsonInclude(JsonInclude.Include.NON_NULL)//Server doesn't set the key of DeviceMessage
public class MessageWithId extends DeviceMessage {
    public int id;
}
