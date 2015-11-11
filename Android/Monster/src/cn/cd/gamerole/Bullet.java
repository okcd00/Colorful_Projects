package cn.cd.gamerole;

import cn.cd.constant.GConstant;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * �ӵ��࣬ÿһ���ӵ�����Ҫ��Ϊһ������
 * �ӵ�ײ�������޻�ɳ���Ļʱ���Զ�ʧЧ��
 * �ӵ�������Ĺ������������Ա����
 * @author okcd00 ����ժ�ԡ�v���º�����
 */
public class Bullet {
	
	//�ӵ���λ��
	public float bulletX=0, bulletY=0;
	//�ӵ��ĳߴ�
	public float width, height;
	//�ӵ��Ƿ���Ҫ������
	public boolean isFire=false;
	//��һ���ӵ�
	public Bullet nextBullet = null;
	//�ӵ���λͼ
	public Bitmap bulletMap;
	//�ӵ�λͼ��λ�þ���
	public Rect rectBullet;
	//�ӵ�����������ķ����ٶ�
	public float speedX = 0f, speedY = 0-GConstant.speed_middle;
	
	public Bullet(Context context, int resId){
		//��ʼ���ӵ���λͼ
		bulletMap = BitmapFactory.decodeResource(context.getResources(), resId);
		//��ʼ���ӵ���λ�þ���
		rectBullet = new Rect();
		//����ָ��Ϊ��
		nextBullet = null;
		//�����ӵ��ߴ�
		width = bulletMap.getWidth();
		height = bulletMap.getHeight();
	}
	
	public void flyBullet(Canvas canvas, Paint paint) {
		//�����ӵ��ĺ�������
		bulletY+=speedY;
		bulletX+=speedX;
		//�����ӵ���λ�þ���
		rectBullet.set((int)bulletX, (int)bulletY, (int)(bulletX+width), (int)(bulletY+height));
		//�����ӵ�
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
