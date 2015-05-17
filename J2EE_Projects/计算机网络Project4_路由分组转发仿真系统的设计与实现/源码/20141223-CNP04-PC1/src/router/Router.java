package router;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import newtwork.Config;

public class Router extends Thread {

	public static JPanel routingInfoTextPanel = new JPanel();// Used to contain
																// routing
	// information text area.
	public static JPanel routerMessageRecordPanel = new JPanel();// Used to
																	// contain
																	// the
																	// router
	public static JLabel hostRoutingTableLabel = new JLabel("Routing Table");
	// message.
	public static JPanel routingTablePanel = new JPanel();
	public static JTextArea routerMessageRecordTextArea = new JTextArea();
	String nextHop;

	String fullMessage;
	RouterSelect routerSelect;
	String destinationIP;
	int destinationProcessPort;

	String strSendToHost;
	private Socket serverSocket;
	OutputStream serverOutputStream;
	InputStream serverInputStream;

	byte[] sendBuffer;

	int sendBufferLength;

	String serverReceiveDataSender;
	String serverReceiveDataDestination;
	String serverReceiveDataMessage;
	public static String routerMessageRecord = new String();

	public static JFrame Router = new JFrame();

	public Router(Socket severSocket) {

		this.serverSocket = severSocket;

	}

	public Router() {

		strSendToHost = new String();

		sendBuffer = new byte[500];
		routerMessageRecordPanel.setLayout(new BorderLayout());

		routerMessageRecordTextArea.setLineWrap(true);
		routerMessageRecordTextArea.setWrapStyleWord(true);
		routerMessageRecordTextArea.setEditable(false);
		routerMessageRecordPanel.add(new JScrollPane(
				routerMessageRecordTextArea), BorderLayout.CENTER);

		routerMessageRecordPanel.add(hostRoutingTableLabel, BorderLayout.SOUTH);

		routingTablePanel.setLayout(new GridLayout(4, 3));
		//routingTablePanel.setBackground(Color.);
		routingTablePanel.add(new JLabel("Destination"));
		routingTablePanel.add(new JLabel("Distance"));
		routingTablePanel.add(new JLabel("Next Hop"));
		routingTablePanel.add(new JLabel("Host 3"));
		routingTablePanel.add(new JLabel("2"));
		routingTablePanel.add(new JLabel("Router 2"));
		routingTablePanel.add(new JLabel("Host 2"));
		routingTablePanel.add(new JLabel("1"));
		routingTablePanel.add(new JLabel("Router 2"));
		routingTablePanel.add(new JLabel("Host 1"));
		routingTablePanel.add(new JLabel("0"));
		routingTablePanel.add(new JLabel("-"));

		routingInfoTextPanel.setLayout(new GridLayout(2,1));
		routingInfoTextPanel.add(routerMessageRecordPanel);
		routingInfoTextPanel.add(routingTablePanel);

		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int frameHeight = 300;
		int frameWidth = 400;
		int x = (screenWidth - frameWidth) / 2;
		int y = (screenHeight - frameHeight) / 2;
		Router.setBounds(x, y, frameWidth, frameHeight);

		Router.setBounds(x, y, frameWidth, frameHeight);
		Router.setLayout(new BorderLayout());
		Router.setTitle("R1");

		Router.add(routingInfoTextPanel, BorderLayout.CENTER);

		Router.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Router.setVisible(true);
		server();

	}

	/**
	 * This is the server part, it is responsible for the request
	 * acceptance,forwarding or sending.
	 */
	public void run() {
		try {

			Date date = new Date();
			serverOutputStream = serverSocket.getOutputStream();
			serverInputStream = serverSocket.getInputStream();
			byte[] receiveBuffer = new byte[500];
			int receiveBufferLength = serverInputStream.read(receiveBuffer);
			String receiveStringData = new String(receiveBuffer, 0,
					receiveBufferLength);

			StringTokenizer stringTokenizer = new StringTokenizer(
					receiveStringData, ".");
			serverReceiveDataDestination = stringTokenizer.nextToken();
			serverReceiveDataSender = stringTokenizer.nextToken();
			serverReceiveDataMessage = stringTokenizer.nextToken();

			routerMessageRecord = routerMessageRecord + serverReceiveDataSender
					+ "->" + serverReceiveDataDestination + ": " +
					serverReceiveDataMessage+ "\n" + date 
					+ "\n";
			routerMessageRecordTextArea.setText(routerMessageRecord);

			String nextHop = new String();
			nextHop = new RouterSelect(serverReceiveDataDestination).select();

			if (nextHop.equals("-")) {

				Socket socket = new Socket(InetAddress.getByName(new Config().host1IP),
						new Config().host1Port);
				OutputStream outputStream = socket.getOutputStream();
				InputStream inputStream = socket.getInputStream();
				String strNetwordData = serverReceiveDataSender + "."
						+ serverReceiveDataMessage;
				byte[] sendByteData = strNetwordData.getBytes();
				outputStream.write(sendByteData);

				inputStream.close();
				outputStream.close();
				socket.close();
			} else if (nextHop.equals("Router 2") ) {
				Socket socket = new Socket(InetAddress.getByName(new Config().host2IP),
						new Config().router2Port);
				OutputStream outputStream = socket.getOutputStream();
				InputStream inputStream = socket.getInputStream();
				String strNetwordData = receiveStringData;
				byte[] sendByteData = strNetwordData.getBytes();
				outputStream.write(sendByteData);

				inputStream.close();
				outputStream.close();
				socket.close();
			} else {

			}

			strSendToHost = date + "Send message successfully!";

			serverOutputStream.write(strSendToHost.getBytes());

			serverOutputStream.close();

			serverInputStream.close();

			serverSocket.close();

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	/**
	 * This server keep accepting any request from the port.
	 */
	public static void server() {
		try {
			ServerSocket severSocket = new ServerSocket(8082);
			while (true) {
				Socket socket = severSocket.accept();
				new Router(socket).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Router();
	}

}
