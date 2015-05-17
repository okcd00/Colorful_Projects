package V1;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Shape implements java.io.Serializable{
	int x1,x2,y1,y2;
	Color c;
	Graphics g;
	abstract void draw(Graphics g);
}
