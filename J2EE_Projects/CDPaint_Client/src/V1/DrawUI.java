package V1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import V1.PaintClient.DrawListener;

public class DrawUI extends JFrame {

	private JPanel contentPane;
	JTextField answer;
	File fAuto = new File("D:\\PicAutoBmp.hb");
	ButtonGroup group = new ButtonGroup();
	ButtonGroup group2 = new ButtonGroup();
	static JPanel center, bottom, left;
	FileSave fs = new FileSave();
	ObjectOutputStream oos;
	public byte player = 0;// 标记玩家角色role
	ArrayList<Shape> list;// static 可实现类名调用
	ArrayList<Shape> list1;// 保存被撤销的对象
	JTextField tip;
	DrawListener lis;

	/**
	 * Create the frame.
	 */
	public DrawUI(ObjectOutputStream oos, ArrayList<Shape> list,
			ArrayList<Shape> list1, DrawListener lis) {
		this.oos = oos;
		this.list = list;
		this.list1 = list1;
		this.lis = lis;
		System.out.println(lis);
		// lis = new DrawListener(this, group, oos, list, list1);
		setTitle("【=你画我猜=】 Ver 2.33");
		setIconImage(new ImageIcon("images\\icon.png").getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);

		Insets ins = new Insets(0, 0, 0, 0);

		addMenu();
		addJPanel();
		addShape(ins);
		addColor(ins);

		answer = new JTextField();
		answer.setBackground(Color.DARK_GRAY);
		answer.setBorder(new LineBorder(Color.gray));
		answer.setFont(new Font("微软雅黑", Font.BOLD, 12));
		answer.setForeground(Color.WHITE);
		answer.setText("在这里输入答案");
		answer.setBounds(485, 21, 198, 29);
		bottom.add(answer);
		answer.setColumns(10);

		JButton enter = new JButton("确定");
		enter.addActionListener(lis);
		enter.setFont(new Font("宋体", Font.BOLD, 13));
		enter.setMargin(ins);
		enter.setBackground(Color.lightGray);
		enter.setFocusable(false);
		enter.setBounds(693, 21, 60, 29);
		bottom.add(enter);

		tip = new JTextField();
		tip.setEditable(false);
		tip.setForeground(Color.WHITE);
		tip.setBackground(Color.GRAY);
		tip.setBorder(new LineBorder(Color.gray));
		tip.setBounds(10, 36, 465, 21);
		bottom.add(tip);
		tip.setColumns(10);

		addWindowListener(lis);
		setVisible(true);
	}

	public void addJPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		center = new MyPanel();
		center.setBackground(Color.WHITE);
		contentPane.add(center, BorderLayout.CENTER);

		left = new JPanel();
		left.setBackground(Color.LIGHT_GRAY);
		left.setPreferredSize(new Dimension(60, 0));
		contentPane.add(left, BorderLayout.WEST);
		left.setLayout(null);

		bottom = new JPanel();
		bottom.setBackground(Color.GRAY);
		bottom.setPreferredSize(new Dimension(0, 60));
		contentPane.add(bottom, BorderLayout.SOUTH);
		bottom.setLayout(null);
	}

	public void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu start = new JMenu("\u5F00\u59CB");
		menuBar.add(start);

		JMenu menu = new JMenu("\u65B0\u5EFA");
		menu.setIcon(null);
		start.add(menu);

		JMenuItem newHb = new JMenuItem("\u5BF9\u8C61\u683C\u5F0F");
		menu.add(newHb);
		newHb.addActionListener(lis);

		JMenuItem newBmp = new JMenuItem("bmp\u683C\u5F0F");
		menu.add(newBmp);
		newBmp.addActionListener(lis);

		JMenuItem open = new JMenuItem("\u6253\u5F00");
		open.setIcon(null);
		start.add(open);
		open.addActionListener(lis);

		JMenuItem save = new JMenuItem("\u4FDD\u5B58");
		save.setIcon(null);
		start.add(save);
		save.addActionListener(lis);

		JMenu menu_5 = new JMenu("\u53E6\u5B58\u4E3A");
		menu_5.setIcon(null);
		start.add(menu_5);

		JMenuItem saveHb = new JMenuItem("hb");
		menu_5.add(saveHb);
		saveHb.addActionListener(lis);

		JMenuItem saveBmp = new JMenuItem("bmp");
		menu_5.add(saveBmp);
		saveBmp.addActionListener(lis);

		JMenuItem exit = new JMenuItem("退出");
		exit.setIcon(null);
		start.add(exit);
		exit.addActionListener(lis);

		JMenu menu_1 = new JMenu("\u7F16\u8F91");
		menuBar.add(menu_1);

		JMenuItem cancel = new JMenuItem("\u64A4\u9500");
		menu_1.add(cancel);
		cancel.addActionListener(lis);

		JMenuItem recover = new JMenuItem("\u6062\u590D");
		menu_1.add(recover);
		recover.addActionListener(lis);

		JMenu menu_2 = new JMenu("\u5E2E\u52A9");
		menuBar.add(menu_2);

		JMenuItem about = new JMenuItem("\u5173\u4E8E");
		menu_2.add(about);
		about.addActionListener(lis);
	}

	public void addShape(Insets ins) {
		String command[] = { "wujiaoxin", "select", "eraser", "tianchong",
				"chosseColor", "fangda", "pen", "shuazi", "penwu", "wenzi",
				"line", "quxian", "rect", "prismatic", "oval" };
		String command2[] = { "kong", "fill", "onlyfill" };
		for (int i = 0; i < command.length; i++) {
			JRadioButton jrb = new JRadioButton();
			jrb.setContentAreaFilled(false);
			jrb.setMargin(ins);
			jrb.setIcon(new ImageIcon("images\\draw" + i + ".jpg"));
			jrb.setActionCommand(command[i]);
			if ((i + 1) % 2 == 0) {
				jrb.setBounds(32, 10 + 30 * (i - 1) / 2, 25, 25);
			} else {
				jrb.setBounds(2, 10 + 30 * i / 2, 25, 25);
			}
			left.add(jrb);
			group.add(jrb);
			if (i == 6) {
				jrb.setSelected(true);
			}
		}
		for (int i = 0; i < command2.length; i++) {
			JRadioButton jrb = new JRadioButton();
			jrb.setContentAreaFilled(false);
			jrb.setMargin(ins);
			jrb.setIcon(new ImageIcon("images\\shape_" + (i + 1) + ".png"));
			jrb.setActionCommand(command2[i]);
			jrb.setBounds(5, 260 + 20 * i, 40, 10);
			left.add(jrb);
			group2.add(jrb);
		}
	}

	public void addColor(Insets ins) {

		Color col[] = { Color.black, Color.darkGray, Color.red, Color.yellow,
				Color.green, Color.blue };
		String command[] = { "黑色", "深灰", "红色", "黄色", "绿色", "蓝色" };
		Color col2[] = { Color.white, Color.lightGray, Color.pink,
				Color.orange, Color.cyan, Color.white };
		String command2[] = { "白色", "浅灰", "粉红", "橙色", "天蓝", "其它" };
		for (int i = 0; i < col.length; i++) {
			JButton jbt = new JButton();
			jbt.setBackground(col[i]);
			jbt.setMargin(ins);
			jbt.setActionCommand(command[i]);
			jbt.setBounds(60 + 22 * i, 10, 20, 20);
			jbt.addActionListener(lis);
			bottom.add(jbt);
		}
		for (int i = 0; i < col2.length; i++) {
			JButton jbt = new JButton();
			jbt.setBackground(col2[i]);
			jbt.setMargin(ins);
			jbt.setActionCommand(command2[i]);
			jbt.setBounds(260 + 22 * i, 10, 20, 20);
			jbt.addActionListener(lis);
			bottom.add(jbt);
		}
	}

	/**
	 * 新建按钮执行的方法
	 */
	public void newPaint() {
		list.clear();
		this.repaint1();
	}

	public void repaint1() {
		center.repaint();
	}

	/**
	 * 打开bmp
	 * 
	 * @param f
	 */
	public void repaintData(File f) {
		Graphics g = center.getGraphics();
		newPaint();
		int colors[][] = fs.read24bmp(f);
		// int s = new Color(255, 255, 255).getRGB();
		if (colors != null) {
			for (int i = 0; i < colors.length; i++) {
				for (int j = 0; j < colors[i].length; j++) {
					Color c = new Color(colors[i][j]);
					Line line = new Line(j, i, j, i, c);
					line.draw(g);
					if (colors[i][j] != -1) {
						list.add(line);
					}
				}
			}
		}
	}

	class MyPanel extends JPanel {
		public void paint(Graphics g) {
			super.paint(g);
			if (list != null) {
				// 重绘窗口
				for (int i = 0; i < list.size(); i++) {
					// 取得一个形状对象
					Shape sh = list.get(i);
					// 绘制形状
					sh.draw(g);
				}
			}

		}
	}

}
