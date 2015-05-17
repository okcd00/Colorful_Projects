package V1;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PaintClient {
	Socket st = null;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	String name;
	boolean bConnected = false;
	DrawUI du;
	ArrayList<Shape> list = new ArrayList<Shape>();// static ��ʵ����������
	ArrayList<Shape> list1 = new ArrayList<Shape>();// ���汻�����Ķ���
	static int colors[][];
	public static boolean isHb = true;
	static Point p = new Point();// �Á���center���Ͻǵ�����
	static Dimension dim;
	DrawListener lis = new DrawListener();

	public static void main(String[] args) {
		PaintClient pc = new PaintClient();
		pc.connect();

	}

	public void connect() {
		try {
			// ���ӷ�����
			st = new Socket("127.0.0.1", 9999);
			// ��ÿͻ�������
			name = st.getInetAddress().getHostName();
			// �����
			oos = new ObjectOutputStream(st.getOutputStream());
			ois = new ObjectInputStream(st.getInputStream());
			// ������ͼ����
			// ********************************************************//
			du = new DrawUI(oos, list, list1, lis);
			// ���������߳�
			Receive rc = new Receive();
			rc.start();
			bConnected = true;
		} catch (ConnectException ce) {
			System.out.println("��������");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disConnected() {
		try {
			ois.close();
			oos.close();
			st.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class Receive extends Thread {

		public void run() {
			try {
				while (bConnected) {
					Message msg = (Message) ois.readObject();

					// �����1��ʾҪ��ӣ�0Ϊ���������յ��˶���ͱ�������
					if (msg.count == 1) {
						list.add(msg.sp);
					} else if (msg.count == 0) {
						list.remove(msg.sp);
					} else if (msg.count == 2) {
						if (msg.str.equals("hb")) {
							// �½������ļ�
							isHb = true;
						} else {
							// �½�bmp�ļ�
							isHb = false;
						}
						du.newPaint();
					} else if (msg.count == 3) {
						// ���ļ���·��Ϊstring
						File f = new File(msg.str);
						// �ж���bmp����hb�ļ�
						if (msg.str.endsWith("bmp")) {
							du.repaintData(f);
						} else {
							list.clear();
							list = FileSave.readHb(f);
							du.repaint1();
						}
					} else if (msg.count == 4) {
						// ԭ�����ǲ���ӷ��������յ��𰸵ģ���������˵���д���
						System.out.println("�߼����󣬽��յ���δ����Ĵ𰸡�");
					} else if (msg.count == 5) {
						// System.out.println("���յ���ɫ�������Ϣ");
						if (du.player == 0 && msg.str.equals("C")) {
							du.player = 2;
						} else if (msg.str.equals("P")) {
							if (du.player == 0) {
								// �����Ľ�ɫ
								du.player = 1;
								du.answer.setBackground(Color.white);
								du.answer.setForeground(Color.black);
							} else {
								du.answer.setBackground(Color.gray);
								du.answer.setForeground(Color.white);
								System.out.println("��Ӽ�����");
								DrawUI.center.addMouseListener(lis);
								DrawUI.center.addMouseMotionListener(lis);
							}
						}
					} else if (msg.count == 6) {
						// ���յ��˴𰸴�����Ϣ
						if (!msg.str.equals("#clear")) {
							// ����������Ϣ
							// ����Ϣ��ʾ��DrawUI��
							du.tip.setText(msg.str);
							System.out.println(msg.str);
						} else {
							// ���յ����¿�ʼ����Ϣ����ջ���������ն��У����¿�ʼ�²��
							System.out.println("���¿�ʼ��Ϸ");
							list.clear();
							list1.clear();
							if (du.player == 1) {
								du.answer.setEditable(true);
								du.answer.setBackground(Color.white);
								du.answer.setForeground(Color.black);
								DrawUI.center.removeMouseListener(lis);
								DrawUI.center.removeMouseMotionListener(lis);
							}
						}
					}
					DrawUI.center.repaint();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SocketException ee) {
				String str = "����������ʧ��\r\n";
				System.out.print(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public class DrawListener extends MouseAdapter implements ActionListener,
			WindowListener {
		File f = new File("D:\\Pic.hb");
		FileSave fs = new FileSave();
		File fAuto = new File("D:\\PicAutoBmp.hb");
		private int x1, x2, x3, x4, y1, y2, y3, y4;
		Color c = Color.black;
		JColorChooser chooser = new JColorChooser();
		ArrayList<Line> drag = new ArrayList<Line>();
		// ������ѡ��ť��
		private String form;// ��ɫ,��״�ı��.
		private Graphics g;
		JFileChooser jfc = new JFileChooser();
		boolean chexiaobiaoji = true;

		/**
		 * MouseEvent ����¼�����,���Եõ������¼��Ķ���
		 */
		public void mousePressed(MouseEvent e) {
			// ����¼�Դ����
			DrawUI.center = (JPanel) e.getSource();

			// �õ���ť���б�ѡ�еİ�ť�Ķ�������,������������ͬ���ֵİ�ť.

			form = du.group.getSelection().getActionCommand();
			System.out.println(form);
			g = DrawUI.center.getGraphics();
			x1 = e.getX();
			y1 = e.getY();
			drag.clear();
		}

		public void mouseReleased(MouseEvent e) {

			x2 = e.getX();
			y2 = e.getY();
			x3 = (x1 > x2) ? x2 : x1;// x3=Math.min(x1,x2):
			y3 = (y1 > y2) ? y2 : y1;
			x4 = (x1 < x2) ? x2 : x1;// x4=Math.max(x1,x2);
			y4 = (y1 < y2) ? y2 : y1;

			// ���center�������Ļ�ľ�������
			// ����ڴ������getLocation

			p = DrawUI.center.getLocationOnScreen();
			dim = DrawUI.center.getSize();// ���center��JPanel���Ĵ�С��ΪDimension�Ķ��󣬿�Ⱥ͸߶�
			try {
				Robot robot = new Robot();
				// �����������
				Rectangle rect = new Rectangle(p, dim);

				// ���ý�ȡ��Ļ�ķ���
				BufferedImage img = robot.createScreenCapture(rect);

				// ������ά���鱣��ÿ�������ɫ���ȸ߶Ⱥ��ȣ���ά�������Ļx��y���෴����

				// ÿ����ɫ��ARGB�ĸ�byte��ɣ�ÿ����ɫ��������int����ʾ���ܹ�2^32-1����ɫ��(32λ)
				// Color[][] colors=new Color[dim.height][dim.width];
				colors = new int[dim.height][img.getWidth()];// �˴�������dim��ø߶ȣ�Ҳ������img��á�
				for (int i = 0; i < colors.length; i++) {
					for (int j = 0; j < colors[i].length; j++) {
						colors[i][j] = img.getRGB(j, i);// img��õ�����������±������෴

					}
				}
			} catch (AWTException e1) {
				e1.printStackTrace();
			}

			try {
				if (form.equals("line")) {
					Line line = new Line(x1, y1, x2, y2, c);
					list.add(line);

					oos.writeObject(new Message(1, line));
					oos.flush();

					line.draw(g);
				} else if (form.equals("rect")) {
					Rect rect = new Rect(x3, y3, x4, y4, c);
					list.add(rect);

					oos.writeObject(new Message(1, rect));
					oos.flush();

					rect.draw(g);
				} else if (form.equals("oval")) {
					Oval oval = new Oval(x3, y3, x4, y4, c);
					list.add(oval);

					oos.writeObject(new Message(1, oval));
					oos.flush();

					oval.draw(g);
				} else if (form.equals("round")) {
					Round round = new Round(x3, y3, x4, y4, c);
					list.add(round);

					oos.writeObject(new Message(1, round));
					oos.flush();

					round.draw(g);
				} else if (form.equals("prismatic")) {
					Prismatic prismatic = new Prismatic(x3, y3, x4, y4, c);
					list.add(prismatic);

					oos.writeObject(new Message(1, prismatic));
					oos.flush();
					prismatic.draw(g);
				} else if (form.equals("eraser")) {
					Eraser es = new Eraser(x1, y1, Color.white);
					list.add(es);
					oos.writeObject(new Message(1, es));
					oos.flush();
					es.draw(g);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		/**
		 * ���ڻ���ķ���
		 */
		public void mouseDragged(MouseEvent e) {
			x2 = e.getX();
			y2 = e.getY();

			if (form.equals("pen")) {
				Line line = new Line(x1, y1, x2, y2, c);
				line.draw(g);
				list.add(line);
				try {
					oos.writeObject(new Message(1, line));
					oos.flush();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
				x1 = x2;
				y1 = y2;
				// �˴������취
				// ����һ�����Լ�¼line�ǲ��ǻ��ĵ�,����һ�μӽ�ȥ��line���±걣������,���������ʱ������ȫ���Ƶ�list1;
			} else if (form.equals("eraser")) {
				Eraser es = new Eraser(x1, y1, Color.white);
				list.add(es);
				try {
					oos.writeObject(new Message(1, es));
					oos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				es.draw(g);
				x1 = x2;
				y1 = y2;
			}
			chexiaobiaoji = true;
		}

		public void actionPerformed(ActionEvent e) {
			String str = e.getActionCommand();// ��ò��������İ�ť
			System.out.println(str);
			g = DrawUI.center.getGraphics();// ���µõ�g.
			try {
				if (str.equals("�����ʽ")) {
					du.newPaint();
					oos.writeObject(new Message(2, "hb"));
					oos.flush();
				} else if (str.equals("bmp��ʽ")) {
					isHb = false;
					du.newPaint();
					oos.writeObject(new Message(2, "bmp"));
					oos.flush();
				} else if (str.equals("�˳�")) {
					System.exit(0);
				} else if (str.equals("����")) {
					if (drag.size() > 0 && chexiaobiaoji) {
						chexiaobiaoji = false;
						System.out.println(drag.size());
						for (Line line : drag) {
							list.remove(line);
							oos.writeObject(new Message(0, line));
							oos.flush();
							// list1.add(line);
						}
						du.repaint1();
					} else if (list.size() != 0) {
						Shape sp = list.remove(list.size() - 1);
						list1.add(sp);
						oos.writeObject(new Message(0, sp));
						oos.flush();
						du.repaint1();
					}

				} else if (str.equals("�ָ�")) {
					if (drag.size() > 0 && chexiaobiaoji) {
						for (Line line : drag) {
							list.add(line);
							oos.writeObject(new Message(1, line));
							oos.flush();
						}
						// drag.clear();
						du.repaint1();
					} else if (list1.size() > 0) {
						Shape sp = list1.remove(list1.size() - 1);
						list.add(sp);

						oos.writeObject(new Message(1, sp));
						oos.flush();

						du.repaint1();
					} else {
						chexiaobiaoji = true;
					}

				} else if (str.equals("����")) {
					JOptionPane
							.showMessageDialog(
									null,
									"�汾��:Bmp\r\n����:\r\n����,ֱ��,����,��Բ\r\n�½�����ǰ������\r\n������ɫ\r\n����bmp,hb��ʽͼƬ\r\n���ӱ����bmp��hb��ʽͼƬ����,��ʱ�����Զ����·��.",
									"����", JOptionPane.CLOSED_OPTION);
				} else if (str.equals("����")) {
					// �ж��ǲ����½��Ķ����ļ�
					if (isHb) {
						FileSave.saveHb(f, list);
					} else {
						// ���򱣴�Ϊbmp��ʽ
						bmpsave(f);
					}
				} else if (str.equals("hb")) {
					FileSave.saveHb(f, list);
				} else if (str.equals("bmp")) {
					int state = jfc.showOpenDialog(du);
					if (state == JFileChooser.APPROVE_OPTION) {// ����˴򿪰�ť
						File f = new File(jfc.getSelectedFile()
								.getAbsolutePath());
						bmpsave(f);
					}
				} else if (str.equals("��")) {
					int state = jfc.showOpenDialog(du);
					if (state == 0) {// ����˴򿪰�ť
						String st = jfc.getSelectedFile().getAbsolutePath();
						File f = new File(st);
						oos.writeObject(new Message(3, st));
						oos.flush();
						// �ж���bmp����hb�ļ�
						if (st.endsWith("bmp")) {
							du.repaintData(f);
						} else {
							list.clear();
							list = FileSave.readHb(f);
							du.repaint1();
						}
					}
				} else if (str.equals("ȷ��")) {
					if (du.player == 1) {
						System.out.println("aaaaa");
						oos.writeObject(new Message(4, du.answer.getText()
								.trim()));
						// ���Ҷ������ɽ�ֹ�༭
						du.answer.setEditable(false);
					} else if (du.player == 2) {
						// �µķ���
						oos.writeObject(new Message(4, du.answer.getText()
								.trim()));
						oos.flush();
					} else {
						System.out.println("δ���䵽��ɫ");
					}
				} else if (str.equals("����")) {
					c = chooser.showDialog(du, "��ɫ", Color.BLACK);
					JButton jbt = (JButton) e.getSource();
					jbt.setBackground(c);
				} else {
					judgeColor(str);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		public void judgeColor(String str) {
			if (str.equals("��ɫ")) {
				c = Color.black;
			} else if (str.equals("���")) {
				c = Color.darkGray;
			} else if (str.equals("��ɫ")) {
				c = Color.red;
			} else if (str.equals("��ɫ")) {
				c = Color.yellow;
			} else if (str.equals("��ɫ")) {
				c = Color.green;
			} else if (str.equals("��ɫ")) {
				c = Color.blue;
			} else if (str.equals("��ɫ")) {
				c = Color.white;
			} else if (str.equals("ǳ��")) {
				c = Color.lightGray;
			} else if (str.equals("�ۺ�")) {
				c = Color.pink;
			} else if (str.equals("��ɫ")) {
				c = Color.orange;
			} else if (str.equals("����")) {
				c = Color.cyan;
			}
		}

		public void bmpsave(File f) {
			fs.write24bmp(f);
		}

		public void windowClosing(WindowEvent e) {
			FileSave.saveHb(fAuto, list);
			System.out.println("����ɹ�");
		}

		public void windowDeactivated(WindowEvent e) {
		}

		public void windowDeiconified(WindowEvent e) {
		}

		public void windowIconified(WindowEvent e) {
		}

		public void windowOpened(WindowEvent e) {
		}

		public void windowActivated(WindowEvent e) {
		}

		public void windowClosed(WindowEvent e) {
		}
	}
}
