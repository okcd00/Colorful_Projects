package cn.cd.gamerole;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class MyPlane {

	//屏幕的尺寸
	public float screemX, screemY;
	// 飞机的位置，初始化位置固定
	public float planeX, planeY;
	// 飞机的尺寸
	public float width, height;
	// 飞机是否需要被绘制
	public boolean isFire = false;
	// 飞机的位图
	public Bitmap planeMap;
	// 飞机位图的位置矩形
	public Rect rectPlane;
	//飞机的最大生命值，固定为100
	public final float maxblood = 500;
	//飞机的实时生命值
	public float blood;
	
	public MyPlane(Context context, int resId, float screemX, float screemY) {
		//获取屏幕尺寸
		this.screemX = screemX;
		this.screemY = screemY;
		//得到位图
		planeMap = BitmapFactory.decodeResource(context.getResources(), resId);
		//得到飞机矩形
		rectPlane = new Rect((int)planeX +30, (int)planeY+15, (int)(planeX+width)-30, (int)(planeY+height)-10);
		//设置飞机尺寸
		width = planeMap.getWidth();
		height = planeMap.getHeight();
		//初始化飞机当前生命值
		blood = maxblood;
		//初始化飞机坐标
		planeX = screemX/2-width/2;
		planeY = screemY;
	}
	

	public void planeFly(float x, float y, float a, float b) {
		float chaX=Math.abs((x - a)), chaY=Math.abs((y - b));
		if (x - a < 0 && y - b < 0) {
			planeX += chaX;
			planeY += chaY;
		} else if (x - a > 0 && y - b > 0) {
			planeX -= chaX;
			planeY -= chaY;
		} else if (x - a > 0 && y - b < 0) {
			planeX -= chaX;
			planeY += chaY;
		} else if (x - a < 0 && y - b > 0) {
			planeX += chaX;
			planeY -= chaY;
		} else if (x - a == 0 && y - b > 0) {
			planeY -= chaY;
		} else if (x - a == 0 && y - b < 0) {
			planeY += chaY;
		} else if (x - a > 0 && y - b == 0) {
			planeX -= chaX;
		} else if (x - a < 0 && y - b == 0) {
			planeX += chaX;
		}
		//控制飞机不飞出屏幕
		if (planeX < 0) planeX = 0;
		else if (planeX > screemX - width) planeX = screemX - width;
		else if (planeY < 0) planeY = 0;
		else if (planeY > screemY - height) planeY = screemY - height;
		
	}
	

	public boolean planeDraw(Canvas canvas, Paint paint){
		canvas.drawBitmap(planeMap, planeX, planeY, paint);
		//设置飞机的位置矩形
		rectPlane.set((int)planeX +30, (int)planeY+15, (int)(planeX+width)-30, (int)(planeY+height)-10);
		Paint p = new Paint();
		p.setColor(Color.RED);
//		canvas.drawRect(rectPlane, paint);
		if (blood >= 0)return true;
		else return false;
	}
}
