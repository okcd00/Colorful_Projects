package V1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class PaintThread extends Thread {

	Socket st;
	PaintThread pt;
	String name;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	boolean bConnected = true;
	Integer num;

	public PaintThread(Socket st) {
		this.st = st;
		this.num = PaintServer.num;
		name = "player" + num;
	}

	public void run() {
		try {
			oos = new ObjectOutputStream(st.getOutputStream());
			ois = new ObjectInputStream(st.getInputStream());
			// ���������Ͻ���ɫ��Ϣ�����ͻ���
			if (num == 1) {
				// ֻ�����һ���˻��������˲�
				this.sent(new Message(5, "P"));
				System.out.println("Player1");
			} else {
				this.sent(new Message(5, "C"));
				System.out.println("Player" + num);
			}
			while (bConnected) {
				Message msg = (Message) ois.readObject();
				if (msg.count == 4) {
					// ����Ƿ��𰸣��ڷ��������д���
					if (this.num == 1) {
						// ���ô�
						PaintServer.theAnswer = msg.str;
						this.sent(new Message(5, "P"));
					} else {
						if (msg.str.equals(PaintServer.theAnswer)) {
							// �¶��ˣ���ÿ���˷�һ����Ϣ���߱���this.name�¶���
							// ������Ϸ����ʼ�µģ���ÿ���ͻ��˷��������Ļ��Ϣ
							Message mes = new Message(6, this.name + "�´��ǣ�"
									+ msg.str + "  ��ϲ�ش���ȷ��");
							Message clear = new Message(6, "#clear");
							for (int i = 0; i < PaintServer.slist.size(); i++) {
								pt = PaintServer.slist.get(i);
								pt.sent(mes);
								pt.sent(clear);
							}
						} else {
							System.out.println(PaintServer.theAnswer);
							// �´��ˣ���ÿ���˷�һ����Ϣ���߱���this.name�´���
							Message mes = new Message(6, this.name + "�´��ǣ�"
									+ msg.str + "  ����һ��㡣");
							for (int i = 0; i < PaintServer.slist.size(); i++) {
								pt = PaintServer.slist.get(i);
								pt.sent(mes);
							}
						}
					}
				} else {
					for (int i = 0; i < PaintServer.slist.size(); i++) {
						pt = PaintServer.slist.get(i);
						if (pt != this) {
							pt.sent(msg);
						}
					}
				}
			}
		} catch (SocketException se) {
			this.bConnected = false;
			PaintServer.roleList.remove(this.num);
			PaintServer.num = 1;
			System.out.println(PaintServer.num);
			PaintServer.slist.remove(this);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����Ϣ�ķ���
	 * 
	 * @param msg
	 */
	public void sent(Message msg) {
		try {
			oos.writeObject(msg);
			oos.flush();
		} catch (SocketException se) {
			this.bConnected = false;
			PaintServer.slist.remove(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
