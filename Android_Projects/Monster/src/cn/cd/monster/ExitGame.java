package cn.cd.monster;

import cn.liwang.monster.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ExitGame extends Activity implements OnClickListener{
	private Button cancelb; //取消按钮
	private Button exitb;  //确定按钮
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_gamex);
		cancelb=(Button)findViewById(R.id.cancel);
		exitb=(Button)findViewById(R.id.quit);
		cancelb.setOnClickListener(this);
		exitb.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.cancel:
			finish();
			break;
		case R.id.quit:
			GameMainMenu.getGameMainMenuInstance().finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			finish();
			break;
		}
	}
}
