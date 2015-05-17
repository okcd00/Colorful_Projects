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
	public static String shareTitle="分享给小伙伴"; //分享的标题
	public static String shareContent="陈点做了个好坑的游戏你们要不要看看？"; //分享的内容
	public static String shareImagePath; //分享图片的路径
	private Button shareButton;
	private Button quit;
	private TextView showShare;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_gamex);
		//初始化ShareSDK
		AbstractWeibo.initSDK(this);	
	    shareButton=(Button)findViewById(R.id.cancel);
	    quit=(Button)findViewById(R.id.quit);
	    showShare=(TextView)findViewById(R.id.exit_text);
	    showShare.setText(R.string.share_text);
	    shareButton.setText("GO->分享");
	    quit.setText("N0!");
	    initImagePath();
	    shareButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String sharecon="你在《进击的卢锡安》中英勇奋战补刀数"+GConstant.killCount+"只。继续加油哦!";
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
		try {//判断SD卡中是否存在此文件夹
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				shareImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/guide_three.png";
			}
			else {
				shareImagePath = getApplication().getFilesDir().getAbsolutePath() + "/guide_three.png";
			}
			File file = new File(shareImagePath);
			//判断图片是否存此文件夹中
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
		// 分享时Notification的图标
		i.putExtra("notif_icon",R.drawable.exit_background);
		// 分享时Notification的标题
		i.putExtra("notif_title", this.getString(R.string.app_name));

		// title标题，在印象笔记、邮箱、信息、微信（包括好友和朋友圈）、人人网和QQ空间使用，否则可以不提供
		i.putExtra("title",shareTitle);
		// text是分享文本，所有平台都需要这个字段
		i.putExtra("text",shareresult);
		// imagePath是本地的图片路径，所有平台都支持这个字段，不提供，则表示不分享图片
		i.putExtra("imagePath",shareImagePath);
		//是直接分享还是要界面的分享
		i.putExtra("silent", silent);
		this.startActivity(i);
	}

}
