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
	private ImageView loading; //加载进度
	private Button goCombat;  //去战斗按钮
	private Button setb;   //设置按钮
	private TextView gameDeacribe; //游戏说明文本
	private static Activity ac;  //本类对象 目的在于在其他活动中也能结束该活动
	private Animation loadingAnim; //动画对象
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
				if(GConstant.normalOrRandom)warMessage="\t\t\t\t\t\t正常模式";
				else warMessage="\t\t\t\t\t随机模式";
				warMessage+="\n击杀怪兽:"+GConstant.killCount;
				warMessage+="\n攻击力:"+GConstant.planePower;
				warMessage+="\n额外攻击力:"+GConstant.extraDamage;
				warMessage+="\n子弹出膛延时频率:"+GConstant.bulletCount;
				warMessage+="\n\n怪兽嘲笑的对你说：\n\t\t"+getMonsterSayLanguage();
				warMessage+="\n\n\t\t\t\t\t\t历史最好成绩:"+getHositoryMostGrade();
				switch(msg.what){
				case GConstant.endGame:
					gameDeacribe.setText(warMessage);
					goCombat.setText("继续战斗");
					break;
				}
			}
		};
	}
	
	public String getMonsterSayLanguage(){
		int count=GConstant.killCount;
		String say="你的忌日就在今天了";
		if(count>=0&&count<10){
			say="你弱爆了，补刀数才"+count+"个的ADC还是别玩了，哈哈哈!";
		}else if(count>=10&&count<=30){
			say="你真的有出攻击装么？根本不费血啊哈哈哈哈";
		}else if(count>30&&count<=60){
			say="我们不怕你是因为你太脆了，注定要被小兵击杀！";
		}else if(count>60&&count<=120){
			say="我们这次来了成千上万个兄弟，你还是乖乖回去守高地吧！";
		}else if(count>120&&count<=300){
			say="别白费力气了，你太弱了，顺便说一句你们三路水晶都被破了！";
		}else if(count>300&&count<=1000){
			say="空血还想反杀？别痴心妄想了啊哈哈哈哈！";
		}else if(count>1000&&count<10000){
			say="算你厉害，我们先撤了，英雄后会有期！";
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
