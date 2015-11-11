package cn.cd.gamerole;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class MyPlane {

	//��Ļ�ĳߴ�
	public float screemX, screemY;
	// �ɻ���λ�ã���ʼ��λ�ù̶�
	public float planeX, planeY;
	// �ɻ��ĳߴ�
	public float width, height;
	// �ɻ��Ƿ���Ҫ������
	public boolean isFire = false;
	// �ɻ���λͼ
	public Bitmap planeMap;
	// �ɻ�λͼ��λ�þ���
	public Rect rectPlane;
	//�ɻ����������ֵ���̶�Ϊ100
	public final float maxblood = 500;
	//�ɻ���ʵʱ����ֵ
	public float blood;
	
	public MyPlane(Context context, int resId, float screemX, float screemY) {
		//��ȡ��Ļ�ߴ�
		this.screemX = screemX;
		this.screemY = screemY;
		//�õ�λͼ
		planeMap = BitmapFactory.decodeResource(context.getResources(), resId);
		//�õ��ɻ�����
		rectPlane = new Rect((int)planeX +30, (int)planeY+15, (int)(planeX+width)-30, (int)(planeY+height)-10);
		//���÷ɻ��ߴ�
		width = planeMap.getWidth();
		height = planeMap.getHeight();
		//��ʼ���ɻ���ǰ����ֵ
		blood = maxblood;
		//��ʼ���ɻ�����
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
		//���Ʒɻ����ɳ���Ļ
		if (planeX < 0) planeX = 0;
		else if (planeX > screemX - width) planeX = screemX - width;
		else if (planeY < 0) planeY = 0;
		else if (planeY > screemY - height) planeY = screemY - height;
		
	}
	

	public boolean planeDraw(Canvas canvas, Paint paint){
		canvas.drawBitmap(planeMap, planeX, planeY, paint);
		//���÷ɻ���λ�þ���
		rectPlane.set((int)planeX +30, (int)planeY+15, (int)(planeX+width)-30, (int)(planeY+height)-10);
		Paint p = new Paint();
		p.setColor(Color.RED);
//		canvas.drawRect(rectPlane, paint);
		if (blood >= 0)return true;
		else return false;
	}
}
