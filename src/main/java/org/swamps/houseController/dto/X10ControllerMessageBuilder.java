package org.swamps.houseController.dto;

public class X10ControllerMessageBuilder {
    private String name;
    private String communicationAdapter;

    public static final X10ControllerMessageBuilder emptyMessage() {
        return new X10ControllerMessageBuilder();
    }


    public X10ControllerMessageBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public X10ControllerMessageBuilder withCommunicationAdapter(String communicationAdapter) {
        this.communicationAdapter = communicationAdapter;
        return this;
    }

    public X10ControllerMessage build() {
        return new X10ControllerMessage(name, communicationAdapter);
    }
}