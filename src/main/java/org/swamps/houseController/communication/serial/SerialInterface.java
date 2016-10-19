package org.swamps.houseController.communication.serial;

import gnu.io.*;
import org.swamps.houseController.communication.TransaportInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialInterface implements TransaportInterface {

    String portName;
    private boolean active;
    private SerialPort port;
    private InputStream inputStream;
    private OutputStream outputStream;

    public SerialInterface(String port, int baudrate, int databits, int stopBits, String parity) throws PortInUseException {
        try {
            this.portName = port;
            this.port = new RXTXPort(port);
            this.port.setBaudBase(baudrate);
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void activate() throws IOException {
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            if ( portIsUsed(portIdentifier) ) {

            }
            inputStream = port.getInputStream();
            outputStream = port.getOutputStream();
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        }
    }


    public boolean portIsUsed(CommPortIdentifier portIdentifier) {
        port = null;
        try {
            port = (SerialPort) portIdentifier.open("name", 10000);
        } catch(PortInUseException e) {
            return true;
        }
        return false;

    }

    @Override
    public void sendBytes(byte[] data) throws IOException {
        outputStream.write(data);
    }

    @Override
    public byte[] readBytes(int length) throws IOException {
        int numberOfBytesToRead = inputStream.available();
        byte buffer[] = new byte[numberOfBytesToRead + 10];
        inputStream.read(buffer);
        return buffer;
    }

    @Override
    public void writeByte(byte data) throws IOException {
        byte buffer[] = new byte[1];
        buffer[0] = data;
        outputStream.write(buffer);
    }

    @Override
    public byte readByte() throws IOException {
        byte buffer[] = new byte[1];
        inputStream.read(buffer,0,1);
        return buffer[0];
    }

    @Override
    public void writeShort(short shortdata) throws IOException {
        byte buffer[] = new byte[Short.BYTES];
        for ( int i = 0; i < Short.BYTES; i++)
            buffer[i] = (byte)(shortdata << ((i+1) * 8) & 0xFF);
        outputStream.write(buffer);
    }

    @Override
    public void shutdown() {
        port.close();
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
