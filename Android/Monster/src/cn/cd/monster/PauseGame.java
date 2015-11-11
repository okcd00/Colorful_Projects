package cn.cd.monster;

import cn.cd.constant.GConstant;
import cn.cd.musicservice.GameMusic;
import cn.liwang.monster.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PauseGame extends Activity implements OnClickListener{
	private Button resumeb;
	private Button chongxinb;
	private Button backmainb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pause_gamex);
		resumeb=(Button)findViewById(R.id.resumeb);
		chongxinb=(Button)findViewById(R.id.chongxinb);
		backmainb=(Button)findViewById(R.id.backmainb);
		resumeb.setOnClickListener(this);
		chongxinb.setOnClickListener(this);
		backmainb.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.resumeb:
			GConstant.gameState=1001;
			GConstant.gameThread.interrupt();
			GameMusic.onMusicServiceAndThread(this);
			finish();
			break;
		case R.id.chongxinb:
			finish();
			EnterGame.getEnterGameInstance().finish();
			Intent entergame=new Intent(this,EnterGame.class);
			startActivity(entergame);
			break;
		case R.id.backmainb:
			finish();
			EnterGame.getEnterGameInstance().finish();
			GameMusic.offMusicServiceAndThread();
			break;
		}
	}
}
