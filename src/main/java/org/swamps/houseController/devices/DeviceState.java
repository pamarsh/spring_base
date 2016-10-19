package org.swamps.houseController.devices;

/**
 * Created by paul on 18/12/12.
 */
public class DeviceState {

    private String deviceName;
    private String deviceState;

    public DeviceState(String deviceName, String deviceState) {
        this.deviceName = deviceName;
        this.deviceState = deviceState;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(String deviceState) {
        this.deviceState = deviceState;
    }
}
