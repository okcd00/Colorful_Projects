package cn.cd.monster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.cd.constant.GConstant;
import cn.liwang.monster.R;

@SuppressLint("HandlerLeak")
public class GameMainMenu extends Activity implements OnClickListener{
	private ImageView loading; //���ؽ���
	private Button goCombat;  //ȥս����ť
	private Button setb;   //���ð�ť
	private TextView gameDeacribe; //��Ϸ˵���ı�
	private static Activity ac;  //������� Ŀ���������������Ҳ�ܽ����û
	private Animation loadingAnim; //��������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamemainmenu);
		ac=this;
		loading=(ImageView)findViewById(R.id.loading);
		goCombat=(Button)findViewById(R.id.combat_button);
		setb=(Button)findViewById(R.id.setb);
		gameDeacribe=(TextView)findViewById(R.id.gameDescribe);
		gameDeacribe.setText(GConstant.GAME_DESCRIBE);
		loadingAnim=AnimationUtils.loadAnimation(this,R.anim.loading_animx);
		goCombat.setOnClickListener(this);
		setb.setOnClickListener(this);
		dealHandler();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
			Intent exit_game=new Intent(this,ExitGame.class);
			startActivity(exit_game);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public static Activity getGameMainMenuInstance(){
		return ac;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.combat_button:
			enterGame();
			break;
		case R.id.setb:
			Intent setgame=new Intent(this,SetGame.class);
			startActivity(setgame);
//			playSound(1,2);
			break;
		}
	}
	
	private void enterGame(){
		goCombat.setVisibility(Button.INVISIBLE);
		loading.setVisibility(ImageView.VISIBLE);
		loading.startAnimation(loadingAnim);
		new Handler().postDelayed(new Thread(){
			public void run(){
				GConstant.gameNoOver=true;
				Intent entergame=new Intent(GameMainMenu.this,EnterGame.class);
				GameMainMenu.this.startActivity(entergame);
				loading.clearAnimation();
				loading.setVisibility(ImageView.INVISIBLE);
				goCombat.setVisibility(Button.VISIBLE);
			}
		},GConstant.GAME_THREAD_DELAY);
		
	}
	
	private void dealHandler(){
		GConstant.mainHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				String warMessage="";
				if(GConstant.normalOrRandom)warMessage="\t\t\t\t\t\t����ģʽ";
				else warMessage="\t\t\t\t\t���ģʽ";
				warMessage+="\n��ɱ����:"+GConstant.killCount;
				warMessage+="\n������:"+GConstant.planePower;
				warMessage+="\n���⹥����:"+GConstant.extraDamage;
				warMessage+="\n�ӵ�������ʱƵ��:"+GConstant.bulletCount;
				warMessage+="\n\n���޳�Ц�Ķ���˵��\n\t\t"+getMonsterSayLanguage();
				warMessage+="\n\n\t\t\t\t\t\t��ʷ��óɼ�:"+getHositoryMostGrade();
				switch(msg.what){
				case GConstant.endGame:
					gameDeacribe.setText(warMessage);
					goCombat.setText("����ս��");
					break;
				}
			}
		};
	}
	
	public String getMonsterSayLanguage(){
		int count=GConstant.killCount;
		String say="��ļ��վ��ڽ�����";
		if(count>=0&&count<10){
			say="�������ˣ���������"+count+"����ADC���Ǳ����ˣ�������!";
		}else if(count>=10&&count<=30){
			say="������г�����װô����������Ѫ����������";
		}else if(count>30&&count<=60){
			say="���ǲ���������Ϊ��̫���ˣ�ע��Ҫ��С����ɱ��";
		}else if(count>60&&count<=120){
			say="����������˳�ǧ������ֵܣ��㻹�ǹԹԻ�ȥ�ظߵذɣ�";
		}else if(count>120&&count<=300){
			say="��׷������ˣ���̫���ˣ�˳��˵һ��������·ˮ���������ˣ�";
		}else if(count>300&&count<=1000){
			say="��Ѫ���뷴ɱ������������˰�����������";
		}else if(count>1000&&count<10000){
			say="���������������ȳ��ˣ�Ӣ�ۺ�����ڣ�";
		}
		return say;
	}

	public int getHositoryMostGrade(){
		SharedPreferences hgrade=getSharedPreferences("hmgrade",MODE_PRIVATE);
		int hkillCount=0;
		if(GConstant.normalOrRandom)hkillCount=hgrade.getInt("normal", 0);
		else hkillCount=hgrade.getInt("random", 0);
		if(GConstant.killCount>hkillCount){
			Editor e=hgrade.edit();
			if(GConstant.normalOrRandom)e.putInt("normal",GConstant.killCount);
			else e.putInt("random",GConstant.killCount);
			e.commit();
			hkillCount=GConstant.killCount;
		}
		return hkillCount;
	}
	 
}
