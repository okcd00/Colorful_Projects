package cn.cd.sharesdk;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.cd.constant.GConstant;
import cn.liwang.monster.R;
import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.onekeyshare.ShareAllGird;

public class ShareResult extends Activity {
	public static String shareTitle="�����С���"; //����ı���
	public static String shareContent="�µ����˸��ÿӵ���Ϸ����Ҫ��Ҫ������"; //���������
	public static String shareImagePath; //����ͼƬ��·��
	private Button shareButton;
	private Button quit;
	private TextView showShare;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_gamex);
		//��ʼ��ShareSDK
		AbstractWeibo.initSDK(this);	
	    shareButton=(Button)findViewById(R.id.cancel);
	    quit=(Button)findViewById(R.id.quit);
	    showShare=(TextView)findViewById(R.id.exit_text);
	    showShare.setText(R.string.share_text);
	    shareButton.setText("GO->����");
	    quit.setText("N0!");
	    initImagePath();
	    shareButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String sharecon="���ڡ�������¬��������Ӣ�·�ս������"+GConstant.killCount+"ֻ����������Ŷ!";
				sharecon+=shareContent;
				showGrid(true,sharecon);
			}
		});
	    quit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				GConstant.mainHandler.sendEmptyMessage(GConstant.endGame);
			}
		});
	}
	private void initImagePath() {
		try {//�ж�SD�����Ƿ���ڴ��ļ���
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				shareImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/guide_three.png";
			}
			else {
				shareImagePath = getApplication().getFilesDir().getAbsolutePath() + "/guide_three.png";
			}
			File file = new File(shareImagePath);
			//�ж�ͼƬ�Ƿ����ļ�����
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.zhanjitu);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch(Throwable t) {
			t.printStackTrace();
			shareImagePath = null;
		}
	}
	public void showGrid(boolean silent,String shareresult) {
		Intent i = new Intent(this,ShareAllGird.class);
		// ����ʱNotification��ͼ��
		i.putExtra("notif_icon",R.drawable.exit_background);
		// ����ʱNotification�ı���
		i.putExtra("notif_title", this.getString(R.string.app_name));

		// title���⣬��ӡ��ʼǡ����䡢��Ϣ��΢�ţ��������Ѻ�����Ȧ������������QQ�ռ�ʹ�ã�������Բ��ṩ
		i.putExtra("title",shareTitle);
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		i.putExtra("text",shareresult);
		// imagePath�Ǳ��ص�ͼƬ·��������ƽ̨��֧������ֶΣ����ṩ�����ʾ������ͼƬ
		i.putExtra("imagePath",shareImagePath);
		//��ֱ�ӷ�����Ҫ����ķ���
		i.putExtra("silent", silent);
		this.startActivity(i);
	}

}
