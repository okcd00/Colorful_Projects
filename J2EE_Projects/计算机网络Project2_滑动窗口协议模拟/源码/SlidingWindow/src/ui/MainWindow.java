package ui;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
public class MainWindow extends JFrame {
	public static void main(String[] args){
		final JFrame f = new JFrame();
		f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent evt){
				f.setVisible(true);
				f.dispose();
				System.exit(0);
			}
		});

		f.setLayout(null);
		f.setSize(800, 400);
		
		PCJpanel PCJ = new PCJpanel(0,125,340,110);
		ChanelJPanel chJ = new ChanelJPanel(345,125,600,250);
		PCReceiver PCR = new PCReceiver(400,125,340,50);
		ControlJPanel cJ = new ControlJPanel(0,0,f.getWidth(),120,PCJ,chJ,PCR);
		f.add(cJ);
		f.add(PCJ);
		f.add(PCR);
		f.setVisible(true);
		
		
	}
}
