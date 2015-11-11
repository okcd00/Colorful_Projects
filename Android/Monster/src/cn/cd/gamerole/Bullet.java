package cn.cd.gamerole;

import cn.cd.constant.GConstant;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 子弹类，每一列子弹都需要成为一个链表。
 * 子弹撞击到怪兽或飞出屏幕时，自动失效。
 * 子弹在射击的过程中是用线性表保存的
 * @author okcd00 部分摘自“v月下鸿鹄”
 */
public class Bullet {
	
	//子弹的位置
	public float bulletX=0, bulletY=0;
	//子弹的尺寸
	public float width, height;
	//子弹是否需要被绘制
	public boolean isFire=false;
	//下一颗子弹
	public Bullet nextBullet = null;
	//子弹的位图
	public Bitmap bulletMap;
	//子弹位图的位置矩形
	public Rect rectBullet;
	//子弹的两个方向的飞行速度
	public float speedX = 0f, speedY = 0-GConstant.speed_middle;
	
	public Bullet(Context context, int resId){
		//初始化子弹的位图
		bulletMap = BitmapFactory.decodeResource(context.getResources(), resId);
		//初始化子弹的位置矩形
		rectBullet = new Rect();
		//后续指针为空
		nextBullet = null;
		//设置子弹尺寸
		width = bulletMap.getWidth();
		height = bulletMap.getHeight();
	}
	
	public void flyBullet(Canvas canvas, Paint paint) {
		//更新子弹的横纵坐标
		bulletY+=speedY;
		bulletX+=speedX;
		//更新子弹的位置矩形
		rectBullet.set((int)bulletX, (int)bulletY, (int)(bulletX+width), (int)(bulletY+height));
		//绘制子弹
		canvas.drawBitmap(bulletMap, bulletX, bulletY, paint);
	}
	
	public void reliveBullet(float x, float y, float differ) {
		bulletX = x+differ;
		bulletY = y;
		isFire = true;
	}

	public void reliveBullet(float x, float y, float sx, float sy) {
		bulletX = x;
		bulletY = y;
		isFire = true;
		speedX = sx;
		speedY = sy;
	}
}
