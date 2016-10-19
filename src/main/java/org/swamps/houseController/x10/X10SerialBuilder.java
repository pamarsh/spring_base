package org.swamps.houseController.x10;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by paul on 31/12/14.
 */
public class X10SerialBuilder {

    private String port;

    @Value("${x10.serial.baud.rate}")
    private int baudRate;

    @Value("${x10.serial.databits}")
    private int databits;

    @Value("${x10.serial.stop.bits}")
    private int stopBits ;

    @Value("${x10.serial.parity}")
    private String parity;

    public int getBaudRate() {
        return baudRate;
    }

    public int getDatabits() {
        return databits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public String getParity() {
        return parity;
    }

    public String getPort() {
        return port;
    }

    public void withPort(String port) {
        this.port = port;
    }


}
