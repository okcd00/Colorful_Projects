package cn.cd.gamerole;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
//ժ��csdn����okcd00ԭ��
public class GameBackground {
	//������X����
	public int bgX=0;
	//����1��Y����
	public int bg1Y=0;
	//����2��Y����
	public int bg2Y=0;
	//��ͼһ
	private Rect rect0;
	private Bitmap map1;
	private Rect rect1;
	//��ͼ��
	private Bitmap map2;
	private Rect rect2;
	public int screemX,screemY;
	public GameBackground(Context context,int[] maps){
		map1=BitmapFactory.decodeResource(context.getResources(), maps[0]);
		map2=BitmapFactory.decodeResource(context.getResources(), maps[1]);
		rect0=new Rect(0,0,map1.getWidth(),map1.getHeight());
		rect1=new Rect(bgX,bg1Y,screemX,screemY);
		rect2=new Rect(bgX,bg2Y,screemX,0);
	}
	public void drawBackground(Canvas canvas,Paint paint){
		rect1=new Rect(bgX,bg1Y,screemX,screemY+bg1Y);
		rect2=new Rect(bgX,bg2Y,screemX,screemY+bg2Y);
		canvas.drawBitmap(map1,rect0,rect1,null);
		canvas.drawBitmap(map2,rect0,rect2,null);
		scrollBackground(screemY);
	}
	private void scrollBackground(int screemY){
		bg1Y+=1;
		bg2Y+=1;
		if(bg1Y>=screemY){
			bg1Y=-screemY;
		}
		if(bg2Y>=screemY){
			bg2Y=-screemY;
		}		
	}
}
