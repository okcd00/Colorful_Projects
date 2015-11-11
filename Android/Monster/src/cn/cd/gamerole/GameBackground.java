package cn.cd.gamerole;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
//摘自csdn，非okcd00原创
public class GameBackground {
	//背景的X坐标
	public int bgX=0;
	//背景1的Y坐标
	public int bg1Y=0;
	//背景2的Y坐标
	public int bg2Y=0;
	//地图一
	private Rect rect0;
	private Bitmap map1;
	private Rect rect1;
	//地图二
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
