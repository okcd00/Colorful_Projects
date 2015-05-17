package cn.cd.monster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import cn.cd.constant.GConstant;
import cn.liwang.monster.R;

@SuppressLint("NewApi")
public class Anim extends Activity {
	private ImageView monAnim;
	private AnimationDrawable animDraw;
	private Animation imageAnim;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animx);
		initMonAnim();
		monsterRunning();
		new Handler().postDelayed(new Thread(){
			public void run(){
				Intent gameMainMenu=new Intent(Anim.this,GameMainMenu.class);
				Anim.this.startActivity(gameMainMenu);
				Anim.this.finish();
			}
		},GConstant.GAME_THREAD_DELAY);
	}

	public void initMonAnim(){	//png-gif 模拟
		monAnim=(ImageView)findViewById(R.id.animView);
		imageAnim=AnimationUtils.loadAnimation(this,R.anim.monster_running_anim);
		monAnim.setBackgroundResource(R.anim.monster_anim);
		Object bgObject=monAnim.getBackground();
		animDraw=(AnimationDrawable)bgObject;
		animDraw.setOneShot(false); //设置动画是否播放一次（true) 还是循环播放(false)
		animDraw.start();
	}

	public void monsterRunning(){
		monAnim.setAnimation(imageAnim);
		imageAnim.start();
		imageAnim.setRepeatCount(Animation.INFINITE);
		monAnim.setVisibility(ImageView.VISIBLE);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		monAnim.clearAnimation();
		monAnim=null;
	}
	
}
