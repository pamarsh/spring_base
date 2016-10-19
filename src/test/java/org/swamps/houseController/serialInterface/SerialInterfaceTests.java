package org.swamps.houseController.serialInterface;

import gnu.io.PortInUseException;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.swamps.houseController.communication.TransaportInterface;
import org.swamps.houseController.communication.serial.SerialInterface;

import java.io.IOException;

import static org.hamcrest.core.Is.is;


public class SerialInterfaceTests {

    String testSerialPort  = "/dev/ttyUSB0";
    String productionSerialPort = "/dev/ttyUSB1";

    private TransaportInterface testedInterface;
    private TransaportInterface testerSerialInterface;

    @Before
    public void setupTesterSerial() throws PortInUseException {
        testerSerialInterface = new SerialInterface(testSerialPort, 1400, 8 , 1 , "NONE");
    }

    @Before
    public void setupProductSeialPort() throws PortInUseException {
        testedInterface = new SerialInterface(productionSerialPort, 1400, 8 , 1 , "NONE");
    }

    @Test
    @Ignore(value="Needs OS setups for seral interfaces")
    public void ensureThatWeCanRecieveDataThroughTheSerialPort() throws IOException {
        byte data[] = {0x01,0x02,0x03,0x0E,0x2F};
        testerSerialInterface.sendBytes(data);
        byte readData[] = testedInterface.readBytes(data.length);
        MatcherAssert.assertThat(readData, is(data));
    }

}
