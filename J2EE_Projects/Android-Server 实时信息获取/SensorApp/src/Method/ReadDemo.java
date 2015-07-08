package Method;


import java.io.*;
import java.util.*;

import gnu.io.CommPortIdentifier;  
import gnu.io.PortInUseException;  
import gnu.io.SerialPort;  
import gnu.io.SerialPortEvent;  
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;  

public class ReadDemo implements Runnable, SerialPortEventListener {
    static CommPortIdentifier portId;
    @SuppressWarnings("rawtypes")
	static Enumeration portList;

    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;
    int count=0;
    String E="0000";

    public static void main(String[] args) {
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals("COM3")) {
                    @SuppressWarnings("unused")
					ReadDemo reader = new ReadDemo();
                }
            }
        }
    }

    public ReadDemo() {
    	
        try {
            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
        } catch (PortInUseException e) {}
        try {
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {}
	try {
            serialPort.addEventListener(this);
	} catch (TooManyListenersException e) {}
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(115200,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {}
        readThread = new Thread(this);
        readThread.start();
    }

    public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {}
    }

    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
        case SerialPortEvent.DATA_AVAILABLE:
            byte[] readBuffer = new byte[4];
            try {
            	inputStream.read(readBuffer);
                String D =new String(readBuffer);

                	count++;
                	this.E=D;
                	System.out.print("20150707+"+D+"+"+count+"\n");
            } catch (IOException e) {}
            break;
        }
    }
}
