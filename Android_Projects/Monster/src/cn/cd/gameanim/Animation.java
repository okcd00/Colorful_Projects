package cn.cd.gameanim;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
//��������������ڡ��һ��������Ķ���ʵ��
public class Animation {

    /** ��һ֡����ʱ�� **/
    private long mLastPlayTime = 0;
    /** ���ŵ�ǰ֡��ID **/
    private int mPlayID = 0;
    /** ����frame���� **/
    private int mFrameCount = 0;
    /** ���ڴ��涯����ԴͼƬ **/
    private Bitmap[] mframeBitmap = null;
    /** �Ƿ�ѭ������ **/
    private boolean mIsLoop = false;
    /** ���Ž��� **/
    public boolean mIsend = false;
    /** �������ż�϶ʱ�� **/
    private static final int ANIM_TIME = 100;

    public Animation(Context context, int [] frameBitmapID, boolean isloop) {
	mFrameCount = frameBitmapID.length;
	mframeBitmap = new Bitmap[mFrameCount];
	for(int i =0; i < mFrameCount; i++) {
	    mframeBitmap[i] = ReadBitMap(context,frameBitmapID[i]);
	}
	mIsLoop = isloop;
    }

    public Animation(Context context, Bitmap [] frameBitmap, boolean isloop) {
	mFrameCount = frameBitmap.length;
	mframeBitmap = frameBitmap;
	mIsLoop = isloop;
    }
    
    //���ö���
    public void reset() {
	mLastPlayTime = 0;
	mPlayID =0;
	mIsend= false;
    }

    public void DrawFrame(Canvas Canvas, Paint paint, int x, int y,int frameID) {
	Canvas.drawBitmap(mframeBitmap[frameID], x, y, paint);
    }

    public void DrawAnimation(Canvas Canvas, Paint paint, float x, float y) {
	//���û�в��Ž������������
	if (!mIsend) {
	    Canvas.drawBitmap(mframeBitmap[mPlayID], x, y, paint);
	    long time = System.currentTimeMillis();
	    if (time - mLastPlayTime > ANIM_TIME) {
		mPlayID++;
		mLastPlayTime = time;
		if (mPlayID >= mFrameCount) {
		    //��־�������Ž���
		    mIsend = true;
		    if (mIsLoop) {
			//����ѭ������
			mIsend = false;
			mPlayID = 0;
		    }
		}
	    }
	}
    }
    
    public Bitmap ReadBitMap(Context context, int resId) {
	BitmapFactory.Options opt = new BitmapFactory.Options();
	opt.inPreferredConfig = Bitmap.Config.RGB_565;
	opt.inPurgeable = true;
	opt.inInputShareable = true;
	// ��ȡ��ԴͼƬ
	InputStream is = context.getResources().openRawResource(resId);
	return BitmapFactory.decodeStream(is, null, opt);
    }
}