package org.swamps.houseController.dto;

public class SerialInterfaceDtoBuilder {
    private String name;
    private String port;
    private String baudRate;
    private int dataBits;
    private int stopBits;
    private String parity;


    public static SerialInterfaceDtoBuilder emptySerialInterface() {
        return new SerialInterfaceDtoBuilder();
    }

    public SerialInterfaceDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SerialInterfaceDtoBuilder withPort(String port) {
        this.port = port;
        return this;
    }

    public SerialInterfaceDtoBuilder withBaudRate(String baudRate) {
        this.baudRate = baudRate;
        return this;
    }

    public SerialInterfaceDtoBuilder withDataBits(int dataBits) {
        this.dataBits = dataBits;
        return this;
    }

    public SerialInterfaceDtoBuilder withStopBits(int stopBits) {
        this.stopBits = stopBits;
        return this;
    }

    public SerialInterfaceDtoBuilder withParity(String parity) {
        this.parity = parity;
        return this;
    }

    public SerialInterfaceDto build() {
        return new SerialInterfaceDto(name, port, baudRate, dataBits, stopBits, parity);
    }
}