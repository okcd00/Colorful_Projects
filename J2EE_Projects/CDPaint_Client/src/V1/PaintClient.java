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
	ArrayList<Shape> list = new ArrayList<Shape>();// static 可实现类名调用
	ArrayList<Shape> list1 = new ArrayList<Shape>();// 保存被撤销的对象
	static int colors[][];
	public static boolean isHb = true;
	static Point p = new Point();// 用來保存center左上角的座标
	static Dimension dim;
	DrawListener lis = new DrawListener();

	public static void main(String[] args) {
		PaintClient pc = new PaintClient();
		pc.connect();

	}

	public void connect() {
		try {
			// 链接服务器
			st = new Socket("127.0.0.1", 9999);
			// 获得客户端名字
			name = st.getInetAddress().getHostName();
			// 获得流
			oos = new ObjectOutputStream(st.getOutputStream());
			ois = new ObjectInputStream(st.getInputStream());
			// 创建画图窗体
			// ********************************************************//
			du = new DrawUI(oos, list, list1, lis);
			// 开启接收线程
			Receive rc = new Receive();
			rc.start();
			bConnected = true;
		} catch (ConnectException ce) {
			System.out.println("检查服务器");
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

					// 如果是1表示要添加，0为撤销，接收到了对象就保存起来
					if (msg.count == 1) {
						list.add(msg.sp);
					} else if (msg.count == 0) {
						list.remove(msg.sp);
					} else if (msg.count == 2) {
						if (msg.str.equals("hb")) {
							// 新建对象文件
							isHb = true;
						} else {
							// 新建bmp文件
							isHb = false;
						}
						du.newPaint();
					} else if (msg.count == 3) {
						// 打开文件，路径为string
						File f = new File(msg.str);
						// 判断是bmp还是hb文件
						if (msg.str.endsWith("bmp")) {
							du.repaintData(f);
						} else {
							list.clear();
							list = FileSave.readHb(f);
							du.repaint1();
						}
					} else if (msg.count == 4) {
						// 原则上是不会从服务器接收到答案的，到了这里说明有错误
						System.out.println("逻辑错误，接收到了未处理的答案。");
					} else if (msg.count == 5) {
						// System.out.println("接收到角色分配的消息");
						if (du.player == 0 && msg.str.equals("C")) {
							du.player = 2;
						} else if (msg.str.equals("P")) {
							if (du.player == 0) {
								// 画画的角色
								du.player = 1;
								du.answer.setBackground(Color.white);
								du.answer.setForeground(Color.black);
							} else {
								du.answer.setBackground(Color.gray);
								du.answer.setForeground(Color.white);
								System.out.println("添加监听器");
								DrawUI.center.addMouseListener(lis);
								DrawUI.center.addMouseMotionListener(lis);
							}
						}
					} else if (msg.count == 6) {
						// 接收到了答案处理消息
						if (!msg.str.equals("#clear")) {
							// 不是重来消息
							// 将消息显示在DrawUI上
							du.tip.setText(msg.str);
							System.out.println(msg.str);
						} else {
							// 接收到重新开始的消息，清空画布，即清空队列，重新开始猜测答案
							System.out.println("重新开始游戏");
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
				String str = "服务器连接失败\r\n";
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
		// 声明单选按钮组
		private String form;// 颜色,形状的标记.
		private Graphics g;
		JFileChooser jfc = new JFileChooser();
		boolean chexiaobiaoji = true;

		/**
		 * MouseEvent 鼠标事件对象,可以得到触发事件的对象
		 */
		public void mousePressed(MouseEvent e) {
			// 获得事件源对象
			DrawUI.center = (JPanel) e.getSource();

			// 得到按钮组中被选中的按钮的动作命令,可以用来区分同名字的按钮.

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

			// 获得center相对于屏幕的绝对坐标
			// 相对于窗体的用getLocation

			p = DrawUI.center.getLocationOnScreen();
			dim = DrawUI.center.getSize();// 获得center（JPanel）的大小，为Dimension的对象，宽度和高度
			try {
				Robot robot = new Robot();
				// 定义矩形区域
				Rectangle rect = new Rectangle(p, dim);

				// 调用截取屏幕的方法
				BufferedImage img = robot.createScreenCapture(rect);

				// 创建二维数组保存每个点的颜色。先高度后宽度（二维数组和屏幕x，y轴相反）。

				// 每种颜色由ARGB四个byte组成，每种颜色都可以用int来表示。总共2^32-1种颜色。(32位)
				// Color[][] colors=new Color[dim.height][dim.width];
				colors = new int[dim.height][img.getWidth()];// 此处可以用dim获得高度，也可以用img获得。
				for (int i = 0; i < colors.length; i++) {
					for (int j = 0; j < colors[i].length; j++) {
						colors[i][j] = img.getRGB(j, i);// img获得的坐标和数组下标正好相反

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
		 * 用于画点的方法
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
				// 此处撤销办法
				// 增加一个属性记录line是不是画的点,将这一次加进去的line的下标保存起来,点击撤销的时候将他们全部移到list1;
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
			String str = e.getActionCommand();// 获得产生动作的按钮
			System.out.println(str);
			g = DrawUI.center.getGraphics();// 重新得到g.
			try {
				if (str.equals("对象格式")) {
					du.newPaint();
					oos.writeObject(new Message(2, "hb"));
					oos.flush();
				} else if (str.equals("bmp格式")) {
					isHb = false;
					du.newPaint();
					oos.writeObject(new Message(2, "bmp"));
					oos.flush();
				} else if (str.equals("退出")) {
					System.exit(0);
				} else if (str.equals("撤销")) {
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

				} else if (str.equals("恢复")) {
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

				} else if (str.equals("关于")) {
					JOptionPane
							.showMessageDialog(
									null,
									"版本号:Bmp\r\n功能:\r\n画点,直线,矩形,椭圆\r\n新建撤销前进操作\r\n更改颜色\r\n保存bmp,hb格式图片\r\n增加保存打开bmp和hb格式图片功能,暂时不能自定义打开路径.",
									"关于", JOptionPane.CLOSED_OPTION);
				} else if (str.equals("保存")) {
					// 判断是不是新建的对象文件
					if (isHb) {
						FileSave.saveHb(f, list);
					} else {
						// 否则保存为bmp格式
						bmpsave(f);
					}
				} else if (str.equals("hb")) {
					FileSave.saveHb(f, list);
				} else if (str.equals("bmp")) {
					int state = jfc.showOpenDialog(du);
					if (state == JFileChooser.APPROVE_OPTION) {// 点击了打开按钮
						File f = new File(jfc.getSelectedFile()
								.getAbsolutePath());
						bmpsave(f);
					}
				} else if (str.equals("打开")) {
					int state = jfc.showOpenDialog(du);
					if (state == 0) {// 点击了打开按钮
						String st = jfc.getSelectedFile().getAbsolutePath();
						File f = new File(st);
						oos.writeObject(new Message(3, st));
						oos.flush();
						// 判断是bmp还是hb文件
						if (st.endsWith("bmp")) {
							du.repaintData(f);
						} else {
							list.clear();
							list = FileSave.readHb(f);
							du.repaint1();
						}
					}
				} else if (str.equals("确定")) {
					if (du.player == 1) {
						System.out.println("aaaaa");
						oos.writeObject(new Message(4, du.answer.getText()
								.trim()));
						// 画家定义答案完成禁止编辑
						du.answer.setEditable(false);
					} else if (du.player == 2) {
						// 猜的发答案
						oos.writeObject(new Message(4, du.answer.getText()
								.trim()));
						oos.flush();
					} else {
						System.out.println("未分配到角色");
					}
				} else if (str.equals("其它")) {
					c = chooser.showDialog(du, "颜色", Color.BLACK);
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
			if (str.equals("黑色")) {
				c = Color.black;
			} else if (str.equals("深灰")) {
				c = Color.darkGray;
			} else if (str.equals("红色")) {
				c = Color.red;
			} else if (str.equals("黄色")) {
				c = Color.yellow;
			} else if (str.equals("绿色")) {
				c = Color.green;
			} else if (str.equals("蓝色")) {
				c = Color.blue;
			} else if (str.equals("白色")) {
				c = Color.white;
			} else if (str.equals("浅灰")) {
				c = Color.lightGray;
			} else if (str.equals("粉红")) {
				c = Color.pink;
			} else if (str.equals("橙色")) {
				c = Color.orange;
			} else if (str.equals("天蓝")) {
				c = Color.cyan;
			}
		}

		public void bmpsave(File f) {
			fs.write24bmp(f);
		}

		public void windowClosing(WindowEvent e) {
			FileSave.saveHb(fAuto, list);
			System.out.println("保存成功");
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
