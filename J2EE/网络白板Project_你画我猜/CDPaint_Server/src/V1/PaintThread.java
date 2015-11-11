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
			// 连上了马上将角色消息发给客户端
			if (num == 1) {
				// 只允许第一个人画，其他人猜
				this.sent(new Message(5, "P"));
				System.out.println("Player1");
			} else {
				this.sent(new Message(5, "C"));
				System.out.println("Player" + num);
			}
			while (bConnected) {
				Message msg = (Message) ois.readObject();
				if (msg.count == 4) {
					// 如果是发答案，在服务器进行处理
					if (this.num == 1) {
						// 设置答案
						PaintServer.theAnswer = msg.str;
						this.sent(new Message(5, "P"));
					} else {
						if (msg.str.equals(PaintServer.theAnswer)) {
							// 猜对了，给每个人发一条消息告诉别人this.name猜对了
							// 结束游戏，开始新的，给每个客户端发送清空屏幕消息
							Message mes = new Message(6, this.name + "猜答案是："
									+ msg.str + "  恭喜回答正确。");
							Message clear = new Message(6, "#clear");
							for (int i = 0; i < PaintServer.slist.size(); i++) {
								pt = PaintServer.slist.get(i);
								pt.sent(mes);
								pt.sent(clear);
							}
						} else {
							System.out.println(PaintServer.theAnswer);
							// 猜错了，给每个人发一条消息告诉别人this.name猜错了
							Message mes = new Message(6, this.name + "猜答案是："
									+ msg.str + "  还差一点点。");
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
	 * 发消息的方法
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
