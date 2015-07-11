package Method;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Method.ReadDemo;

/**
 * Servlet implementation class ReadDemo
 */
@WebServlet("/ReadDemo")
public class ReadDemo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public Boolean flag = false;
	public String last="default";
    /**
     * Default constructor. 
     */
	
	static CommPortIdentifier portId;
    @SuppressWarnings("rawtypes")
	static Enumeration portList;

    Read_Demo reader;
    public ReadDemo() {
    	portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals("COM3")) {
					this.reader = new Read_Demo();
                }
            }
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//url形式：http://xxx.xxx.xxx.xxx:8080/项目名称/COMServlet?op=getValue
		String op = request.getParameter("op");
		if(op!=null){
			switch(op){
			case "getValue":
				sendValue(request,response);
			}
		}
	}
    
    public String changeToDist(String str)
    {
    	double x=(double)Integer.parseInt(str);
		double val = 1944.0 * Math.exp(-0.02985*x) + 51.71 * Math.exp(-0.002019*x);
		String ret="";
		if(val>80.0)ret = " >80.00 cm";
			else if(val<5.0) ret = " <5.00 cm";
				else{
						ret = val + "";
						ret = ret.substring(0,6) + "cm";
					}
		System.out.println("[Log] Info_sensor="+ret);
		return ret;
    }
    
    private void sendValue(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer;
		
		try {
			if(last==this.reader.E) flag=true;
			writer = response.getWriter();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
			if(flag) writer.println("\n" + sdf.format(new Date()) + "\n" + "Sensor_Alpha: " + changeToDist(this.reader.E) + "\nSensor_Beta : Offline" );
			else writer.println("\n" + sdf.format(new Date()) + "\n" + "Sensor_Alpha: " + changeToDist(this.reader.E) + "<NEW>\nSensor_Beta : Offline" );
			last=this.reader.E;
			flag = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	
	 static class Read_Demo implements Runnable, SerialPortEventListener {
	   // static CommPortIdentifier portId;
	    InputStream inputStream;
	    SerialPort serialPort;
	    Thread readThread;
	    int count=0;
	    String E="0000";
	    
	    /*public static void main(String[] args) {
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
	        
	        
	    }*/

	    public Read_Demo() {
	    	
	        try {
	            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
	        } catch (PortInUseException e) {
	        	e.printStackTrace();
	        }
	        try {
	            inputStream = serialPort.getInputStream();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
		try {
	            serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
	        serialPort.notifyOnDataAvailable(true);
	        try {
	            serialPort.setSerialPortParams(115200,
	                SerialPort.DATABITS_8,
	                SerialPort.STOPBITS_1,
	                SerialPort.PARITY_NONE);
	        } catch (UnsupportedCommOperationException e) {
	        	e.printStackTrace();
	        }
	        readThread = new Thread(this);
	        readThread.start();
	    }

	    public void run() {
	        try {
	            Thread.sleep(20000);
	        } catch (InterruptedException e) {
	        	e.printStackTrace();
	        }
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
	                	System.out.print("[static] SM001:"+D+" >"+count+"\n");
	            } catch (IOException e) {
	            	e.printStackTrace();
	            }
	            break;
	        }
	    }
	}


}
