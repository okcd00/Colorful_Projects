package V1;

import java.awt.Color;
import java.awt.Graphics;

public class Eraser extends Shape {

	public Eraser(int x3, int y3, Color c) {
		this.x1 = x3 - 5;
		this.y1 = y3 - 5;
		this.c = c;
	}

	void draw(Graphics g) {
		g.setColor(c);
		g.fillRect(x1, y1, 10, 10);
	}

}
