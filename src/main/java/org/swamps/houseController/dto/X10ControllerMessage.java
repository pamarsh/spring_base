package org.swamps.houseController.dto;


public class X10ControllerMessage {

    private String name;
    private String communicationAdapter;

    public X10ControllerMessage() {}

    public X10ControllerMessage( String name, String communicationAdapter) {
        this.name = name;
        this.communicationAdapter = communicationAdapter;
    }

    public String getName() {
        return name;
    }

    public String getCommunicationAdapter() {
        return communicationAdapter;
    }
}
