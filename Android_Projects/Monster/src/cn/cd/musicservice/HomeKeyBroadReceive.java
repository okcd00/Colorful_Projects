package cn.cd.musicservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HomeKeyBroadReceive extends BroadcastReceiver {
	private static final String SYSTEM_REASON = "reason";
	private static final String SYSTEM_HOME_KEY = "homekey";// home key
	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
			String reason = intent.getStringExtra(SYSTEM_REASON);
			if (reason != null) {
				if (reason.equals(SYSTEM_HOME_KEY)) {
					// home key¥¶¿Ìµ„
					GameMusic.offMusicServiceAndThread();
				} 
			}
		}
	}

}
