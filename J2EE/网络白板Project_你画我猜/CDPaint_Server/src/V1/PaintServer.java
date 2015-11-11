package V1;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PaintServer {
	public static ArrayList<PaintThread> slist = new ArrayList<PaintThread>();
	public static int num = 1;// 保存有多少个人连上了
	public static ArrayList<Integer> roleList = new ArrayList<Integer>();
	public static String theAnswer;

	public static void main(String[] args) {
		// 套接字，指定端口
		try {
			ServerSocket ss = new ServerSocket(9999);// 不要使用80等其他程序常用端口
			System.out.println("服务器已启动，等待连接。。。");
			while (true) {
				// 必须等到有客户端连上，该方法才能执行完毕（阻塞方法）。接收到一个客户端对象
				Socket st = ss.accept();
				while (roleList.contains(num)) {
					num++;// 连上一个就加一
				}
				roleList.add(num);
				System.out.println(st.getInetAddress().getHostName() + ":9999  [Status]已连接上");
				PaintThread pt = new PaintThread(st);
				pt.start();
				slist.add(pt);
			}
		} catch (BindException e) {
			System.out.println("端口使用中,关闭服务器程序重新开始");
			System.exit(0);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
