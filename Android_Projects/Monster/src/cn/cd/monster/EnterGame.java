package cn.cd.monster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import cn.cd.constant.GConstant;
import cn.cd.gameview.GameMainView;
import cn.cd.musicservice.GameMusic;
import cn.cd.musicservice.HomeKeyBroadReceive;
import cn.cd.sharesdk.ShareResult;

@SuppressLint("HandlerLeak")
public class EnterGame extends Activity {
	private GameMainView game;
	private static Activity activity;
	private HomeKeyBroadReceive HKBR;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GameMusic.onMusicServiceAndThread(this);
		game=new GameMainView(this);
		GConstant.context=this;
		setContentView(game);
		HKBR=new HomeKeyBroadReceive();
		registerReceiver(HKBR, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
		activity=this;
		GConstant.handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case GConstant.endGame:
					GameMusic.offMusicServiceAndThread();
					EnterGame.this.finish();
					Intent shareSdk=new Intent(EnterGame.this,ShareResult.class);
					EnterGame.this.startActivity(shareSdk);
					break;
				}
				super.handleMessage(msg);
			}
		};
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		game.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		game.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		game.onDestory();
		this.unregisterReceiver(HKBR);
		GameMusic.offMusicServiceAndThread();
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
			GameMusic.offMusicServiceAndThread();
			GConstant.gameState=0;
			Intent pause_game=new Intent(this,PauseGame.class);
			startActivity(pause_game);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public static Activity getEnterGameInstance(){
		return activity;
	}
}
