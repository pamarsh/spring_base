package org.swamps.houseController.dto;

/**
 * Created by paul on 16/05/15.
 */
public class SerialInterfaceDto {

    private String name;

    private String port;

    private String baudRate ;

    private int dataBits;

    private int stopBits;

    private String parity ;

    public SerialInterfaceDto(String name, String port, String baudRate, int dataBits, int stopBits, String parity) {
        this.name = name;
        this.port = port;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
    }

    public String getName() {
        return name;
    }

    public String getPort() {
        return port;
    }

    public String getBaudRate() {
        return baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public String getParity() {
        return parity;
    }
}
