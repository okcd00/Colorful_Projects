package V1;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PaintServer {
	public static ArrayList<PaintThread> slist = new ArrayList<PaintThread>();
	public static int num = 1;// �����ж��ٸ���������
	public static ArrayList<Integer> roleList = new ArrayList<Integer>();
	public static String theAnswer;

	public static void main(String[] args) {
		// �׽��֣�ָ���˿�
		try {
			ServerSocket ss = new ServerSocket(9999);// ��Ҫʹ��80�����������ö˿�
			System.out.println("���������������ȴ����ӡ�����");
			while (true) {
				// ����ȵ��пͻ������ϣ��÷�������ִ����ϣ����������������յ�һ���ͻ��˶���
				Socket st = ss.accept();
				while (roleList.contains(num)) {
					num++;// ����һ���ͼ�һ
				}
				roleList.add(num);
				System.out.println(st.getInetAddress().getHostName() + ":9999  [Status]��������");
				PaintThread pt = new PaintThread(st);
				pt.start();
				slist.add(pt);
			}
		} catch (BindException e) {
			System.out.println("�˿�ʹ����,�رշ������������¿�ʼ");
			System.exit(0);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
