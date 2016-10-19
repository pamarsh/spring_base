package org.swamps.houseController.communication;

import java.io.IOException;

public interface TransaportInterface {

    public void sendBytes(byte[] data) throws IOException;

    public byte[] readBytes(int length) throws IOException;

    void writeByte(byte data) throws IOException;

    byte readByte() throws IOException;

    void writeShort(short function) throws IOException;

    void shutdown();

    boolean isActive();
}
