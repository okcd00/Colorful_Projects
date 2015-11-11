package cn.cd.musicservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import cn.cd.constant.GConstant;

public class GameMusic extends Service {
	// 音乐启动线程
	public static Thread musicThread;
	// 判断音乐是否在运行
	public static boolean isRunning = true;
	// 声明一个音乐对象
	private MediaPlayer player;
	// 启动音乐时的类对象
	private static Context startcontext;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		setMusicOptions(this, isRunning, GConstant.R_VOLUM, GConstant.L_VOLUM,
				GConstant.GAME_BACKGROUNG_MUSIC_ID);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		player.stop();
		player.release();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		player.stop();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		try {
			player.start();
			isRunning = true;
		} catch (Exception e) {
			isRunning = false;
			player.stop();
		}
		return 1;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	// 停止音乐
	public void onStop() {
		isRunning = false;
	}

	public void setMusicOptions(Context context, boolean isLooped, int rVolume,
			int lVolume, int soundFile) {
		player = MediaPlayer.create(context, soundFile);
		player.setLooping(isLooped);
		player.setVolume(lVolume, rVolume);
	}

	public static void offMusicServiceAndThread() {
		if (GConstant.onOffFlag) {
			Intent bgmusic = new Intent(startcontext, GameMusic.class);
			startcontext.stopService(bgmusic);
			musicThread.interrupt();
		}

	}


	public static void onMusicServiceAndThread(Context context) {
		if (GConstant.onOffFlag) {
			startcontext = context;
			musicThread = new Thread() {
				public void run() {
					Intent musicService = new Intent(startcontext,
							GameMusic.class);
					startcontext.startService(musicService);
				}
			};
			musicThread.start();
		}
	}
}
