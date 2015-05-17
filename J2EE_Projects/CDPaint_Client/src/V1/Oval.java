package V1;

import java.awt.Color;
import java.awt.Graphics;

public class Oval extends Shape{

	public Oval(int x3, int y3, int x4, int y4, Color c) {
		this.x1 = x3;
		this.y1 = y3;
		this.x2 = x4;
		this.y2 = y4;
		this.c = c;
	}
	
	void draw(Graphics g) {
		g.setColor(c);
		 g.drawOval(x1, y1, x2-x1, y2-y1);
	}

	
}