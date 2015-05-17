package host;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;




import network.Config;

public class Host extends Thread implements ActionListener {

	Socket serverSocket;
	JFrame Host = new JFrame();// The main host frame.
	JPanel selectPanel = new JPanel();// Used to contain send button and host
										// select combo.
	JPanel messageTextPanel = new JPanel();// Used to contain message text area.

	NetworkData networkData;
	OutputStream serverOutputStream;
	InputStream serverInputStream;


	public static JTextArea sendAndReceiveMessageTextArea = new JTextArea();

	JLabel destinationLabel = new JLabel("Destination");
	JLabel nextHopLabel = new JLabel("Next Hop");
	JButton sayToH1Button = new JButton("Say Hi to H1");
	JButton sayToH2Button = new JButton("Say Hi to H2");
	JButton sayToH3Button = new JButton("Say Hi to H3");
	ButtonGroup hostButtonGroup = new ButtonGroup();

	public static String sendAndReceiveMessage = new String("");
	String serverReceiveDataSender;// The original sender of the data.
	String serverReceiveDataDestination;// The original destination of the data.
	String serverReceiveDataMessage;// The original message data.

	public Host(Socket serverSocket) {
		this.serverSocket = serverSocket;
	}
	public Host() {
		Host.setTitle("H3");
		Host.setLayout(new BorderLayout());
		int frameHeight = 300;
		int frameWidth = 400;
		Host.setBounds(0, 0, frameWidth, frameHeight);

		sendAndReceiveMessageTextArea.setLineWrap(true);// Switch row
														// automatically.
		sendAndReceiveMessageTextArea.setWrapStyleWord(true);// Prevent words
																// from being
																// isolating.

		messageTextPanel.setLayout(new GridLayout());//2012
		sendAndReceiveMessageTextArea.setEditable(false);
		messageTextPanel.add(new JScrollPane(sendAndReceiveMessageTextArea));// Add
																		
		

		sayToH1Button.setFocusable(false);
		sayToH2Button.setFocusable(false);
		sayToH3Button.setFocusable(false);

		sayToH1Button.addActionListener(this);
		sayToH2Button.addActionListener(this);
		sayToH3Button.addActionListener(this);

		selectPanel.add(sayToH1Button);
		selectPanel.add(sayToH2Button);
		selectPanel.add(sayToH3Button);
		Host.add(messageTextPanel, BorderLayout.CENTER);
		Host.add(selectPanel, BorderLayout.SOUTH);

		Host.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Host.setResizable(false);
		Host.setVisible(true);
		server();
		

	}

	/**
	 * This is the server part, it is responsible for the request acceptance.
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
			serverReceiveDataSender = stringTokenizer.nextToken();
			serverReceiveDataMessage = stringTokenizer.nextToken();

			sendAndReceiveMessage = sendAndReceiveMessage
					+ serverReceiveDataSender +":\t"+serverReceiveDataMessage+"\n"+ date + "\n\n";
			
			sendAndReceiveMessageTextArea.setText(sendAndReceiveMessage);

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
			ServerSocket severSocket = new ServerSocket(new Config().host3Port);
			while (true) {
				Socket socket = severSocket.accept();
				new Host(socket).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand() != "") {
			
				String message = "Hi! I'm H3~";
				String destination = new String();
				if (e.getActionCommand() == "Say Hi to H1") {
					destination = "Host 1";
				} else if (e.getActionCommand() == "Say Hi to H2") {
					destination = "Host 2";
				} else if (e.getActionCommand() == "Say Hi to H3") {
					destination = "Host 3";
				}
				if (destination != "") {
					try {
						String strNetwordData;
						Date date = new Date();
						UserData userData = new UserData(destination, message);
						networkData = new NetworkData(userData.destination,
								"Host 3", userData.message);
						strNetwordData = networkData.destination + "."
								+ networkData.sender + "."
								+ networkData.message;// Data string transported
														// between two nodes.
						Socket socket = new Socket(
								InetAddress.getByName(new Config().host3IP), new Config().router3Port);
						OutputStream outputStream = socket.getOutputStream();

						byte[] sendByteData = strNetwordData.getBytes();
						outputStream.write(sendByteData);



						sendAndReceiveMessage = sendAndReceiveMessage
								+ "Send:\t" + message + "\n"+ date +"\n\n";
						sendAndReceiveMessageTextArea
								.setText(sendAndReceiveMessage);
						outputStream.close();
						socket.close();

					} catch (Exception e2) {
						// TODO: handle exception
					}

				}

		} 
	}

	public static void main(String[] args) {
		new Host();
	}
}
