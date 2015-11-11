package cn.cd.gameview;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import cn.cd.constant.GConstant;
import cn.cd.gameanim.Animation;
import cn.cd.gamerole.Bullet;
import cn.cd.gamerole.GameBackground;
import cn.cd.gamerole.Monster;
import cn.cd.gamerole.MyPlane;
import cn.liwang.monster.R;

public class GameMainView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	private SurfaceHolder holder;
	private float x = 40, y = 40; // 手指按下的坐标
	private boolean flag = true;
	private Canvas canvas;
	private Paint paint;
	private float screemX, screemY;
	private float a, b; // 微量移动后手指的坐标
	final static int PLAN_STEP = 10;
	public float chaX = 0;
	public float chaY = 0;
	private Monster aliveMonster = null; // 活着的怪兽的头指针，该队列有对头对象
	private Monster headDeadMonster = null; // 死怪兽队列（带头节点）的头指针,活怪兽死了的时候向这里添加
	private Monster tailDeadMonster = null; // 死怪兽队列的尾指针，死怪兽复活时用这个移出
	private int drawCount = 300; // 怪兽用的刷屏计数器
	public int drawMonsterCount = 300; // 刷几次屏出生一只怪兽
	public long drawAddMonsterCount = 0;
	public int drawInitPlane = 0; // 前几次刷屏
	private MyPlane plane; // 玩家控制的飞机
	private Bullet headUnuseBullet; //没有射出的子弹
	private Bullet headFireBullet;  //已射出的子弹
	private int drawBulletCount; // 子弹用的刷屏计数器
	private boolean isBulletLeft = true; // 控制下一颗子弹是否从左边射出
	private Rect rectMsg = new Rect(), // 飞机的血条方格矩形
			rectBlood = new Rect(); // 飞机的血条矩形
	private int lenBlood; // 飞机血条的长度
	private GameBackground gbground; // 背景对象
	private Bitmap[] deadPlane; // 飞机死亡后的爆炸位图

	public GameMainView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		holder = getHolder();
		holder.addCallback(this);
		paint = new Paint();
		// 设置画笔颜色为白色
		paint.setColor(Color.BLACK);
		// 设置文本大小
		paint.setTextSize(20);
		// 设置焦点
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		screemX = this.getWidth();
		screemY = this.getHeight();
		GConstant.initSoundPool();
		if(!GConstant.normalOrRandom)initBaseData();
		else GConstant.killCount=0;
		initScrollBackground();
		// 初始化游戏信息
		rectMsg.set((int) screemX - 105, 5, (int) screemX - 5, 20);
		// 初始化飞机
		plane = new MyPlane(getContext(), R.drawable.wsparticle_test_001, screemX, screemY);
		initBullet();
		initMonsters();
		deadPlane = new Bitmap[GConstant.PLANE_EXPLODE_IMAGE_ID.length];
		for (int i = 0; i < deadPlane.length; i++) {
			deadPlane[i] = BitmapFactory.decodeResource(getResources(),
					GConstant.PLANE_EXPLODE_IMAGE_ID[i]);
		}
		GConstant.gameThread = new Thread(this);
		GConstant.gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		flag = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = event.getX();
			y = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_MOVE:
			if(drawInitPlane>120) {
				a = event.getX();
				b = event.getY();
				plane.planeFly(x, y, a, b);
				x = a;
				y = b;
				break;
			}
		}
		return true;
	}

	public void myDraw() {
		switch (GConstant.gameState) {
		case 1001:
			canvas.drawColor(Color.GREEN);
			gbground.drawBackground(canvas, paint);
			if (GConstant.gameNoOver) {
				GConstant.gameNoOver = plane.planeDraw(canvas, paint);
				if (!GConstant.gameNoOver) {
					Animation pEAnim = new Animation(getContext(), deadPlane,
							false);
					pEAnim.DrawAnimation(canvas, paint, plane.planeX,
							plane.planeY);
				}
			} else {
				flag = false;
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
				}
				GConstant.handler.sendEmptyMessage(GConstant.endGame);
			}
			firstPlaneFly(); // 飞机的开场飞行
			drawMessage();
			bulletFire();
			ctrlMonster(); // 绘制并控制怪兽
			break;
		case 0:
			try {
				Thread.sleep(Integer.MAX_VALUE);
			} catch (Exception e) {
			}
			break;
		}
	}

	public void run() {
		while (flag) {
			long chatime = 0;
			long start = System.currentTimeMillis();
			synchronized (holder) {
				canvas = holder.lockCanvas();
				myDraw();
				holder.unlockCanvasAndPost(canvas);
			}
			long end = System.currentTimeMillis();
			chatime = end - start;
			try {
				if (chatime < 15) {
					Thread.sleep(15 - chatime);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			drawCount++;
			drawBulletCount++;
			drawAddMonsterCount++;
		}
	}
	
	private void initBaseData(){
		Random r=new Random();
		GConstant.bulletCount=r.nextInt(15)+3;
		GConstant.planePower=r.nextInt(15)+10;
		GConstant.monsterPower=r.nextInt(20)+15;
		GConstant.numMonsters=r.nextInt(13)+12;
		GConstant.numBullets=r.nextInt(10)+10;
		GConstant.killCount=0;
		GConstant.drawCount=r.nextInt(60)+60;
		GConstant.drawMonsterBullet=r.nextInt(10)+20;
		GConstant.extraDamage=r.nextInt(4)*0.01f+0.01f;
	}

	public void firstPlaneFly() {
		drawInitPlane++;
		if (drawInitPlane <= 70) {
			plane.planeY -= screemY/130;
		} else if (drawInitPlane > 70 && drawInitPlane < 120) {
			plane.planeY += screemY/110;
		}
	}
	
	private void initScrollBackground() {
		gbground = new GameBackground(getContext(), GConstant.GAME_BG_IMAGE_ID);
		gbground.bg2Y = -(int) screemY;
		gbground.screemX = (int) screemX;
		gbground.screemY = (int) screemY;
	}

	public void drawMessage() {
		// 绘制生命值信息
		Paint p = paint;
		canvas.drawText((String) getResources().getText(R.string.plane_blood),
				(int) screemX - 140, 20, p);
		canvas.drawText((String) getResources().getText(R.string.kill_num),
				(int) screemX - 140, 50, p);
		// 绘制当前生命值（血条）
		lenBlood = (int) (100 * plane.blood / plane.maxblood);
		rectBlood.set((int) screemX - 105, 5, (int) screemX - 105 + lenBlood,
				20);
		p.setStyle(Style.FILL);
		p.setColor(Color.RED);
		canvas.drawRect(rectBlood, paint);
		// 绘制血条的方格
		p.setStyle(Style.STROKE);
		p.setColor(Color.BLACK);
		canvas.drawRect(rectMsg, paint);
		// 绘制玩家的杀敌数
		canvas.drawText("" + GConstant.killCount, (int) screemX - 95, 50, p);
	}

	public void initBullet() {
		headFireBullet = new Bullet(getContext(), R.drawable.bullet);
		headUnuseBullet = new Bullet(getContext(), R.drawable.bullet);
		Bullet workB = headUnuseBullet;
		for (int i = 0; i < GConstant.numBullets; i++) {
			workB.nextBullet = new Bullet(getContext(), R.drawable.bullet);
			workB = workB.nextBullet;
			workB.isFire = false;
		}
	}

	public void bulletFire() {
		Bullet workB = headUnuseBullet.nextBullet;
		if (drawBulletCount >= GConstant.bulletCount && workB != null) {
			// 如果刷屏计时器达到规定值，则射出一颗子弹
			// 重置子弹的刷屏计时器
			drawBulletCount = 0;
			// 从无效子弹表中删掉一颗子弹
			headUnuseBullet.nextBullet = workB.nextBullet;
			// 把刚刚删掉的子弹插入到有效子弹表中
			workB.nextBullet = headFireBullet.nextBullet;
			headFireBullet.nextBullet = workB;
			// 设置子弹从左或者右射出
			float differ = plane.width / 6;
			if (!isBulletLeft)
				differ *= 5;
			// 执行子弹重新装填工作，装填后的子弹将在下一个循环射出
			workB.reliveBullet(plane.planeX-13, plane.planeY, differ);
			// 更改下次子弹的左右
			isBulletLeft = !isBulletLeft;
			if(GConstant.onOffFlag)GConstant.playSound(1,1);
		}
		// 更新已射出的子弹的位置
		workB = headFireBullet;
		while (workB.nextBullet != null) {
			if (workB.nextBullet.isFire) { // 如果这颗子弹有效
				if (workB.nextBullet.bulletY > 0 - workB.nextBullet.height) {
					// 如果子弹没有射出屏幕，继续飞行
					workB.nextBullet.flyBullet(canvas, paint);
				} else { // 如果子弹射出屏幕，失效
					workB.nextBullet.isFire = false;
				}
			} else { // 如果子弹被设置为无效
				// 将子弹从有效队列中移出
				Bullet outBullet = workB.nextBullet;
				workB.nextBullet = outBullet.nextBullet;
				// 将子弹移进无效队列中
				outBullet.nextBullet = headUnuseBullet.nextBullet;
				headUnuseBullet.nextBullet = outBullet;
				if (workB.nextBullet == null)
					break; // 预防溢出
			}
			workB = workB.nextBullet;
		}
	}

	public void initMonsters() {
		// 初始化死怪兽队列，死怪兽队列有头结点
		headDeadMonster = new Monster(getContext(), GConstant.monsterAnimation,GConstant.monsterDeadID,
				true, screemX, screemY, GConstant.monsterBullet, plane);
		// 定义这个函数中的工作指针
		Monster workM = headDeadMonster;
		// 初始化若干个怪兽
		for (int i = 0; i < GConstant.numMonsters; i++) {
			workM.nextMonster = new Monster(getContext(),
					GConstant.monsterAnimation,GConstant.monsterDeadID, true, screemX, screemY,
					GConstant.monsterBullet, plane);
			workM = workM.nextMonster;
		}
		tailDeadMonster = workM;
		// 初始化活怪兽队列的头结点
		aliveMonster = new Monster(getContext(), GConstant.monsterAnimation,GConstant.monsterDeadID,
				true, screemX, screemY, GConstant.monsterBullet, plane);
	}

	
	// * 通过碰撞检测，以及怪兽定位判断怪兽是否被子弹击中或者飞出屏幕 即控制怪兽的死亡。
	
	public void isMonsterDead() {
		// 初始化怪兽工作指针
		Monster workM = aliveMonster;
		while (workM.nextMonster != null) {
			workM = workM.nextMonster;
			// 判断怪兽是否在屏幕内
			if (workM.monsterX < screemX && workM.monsterY < screemY) { // 如果怪兽还在屏幕内，则进行碰撞检测
				// 初始化子弹工作指针
				Bullet workB = headFireBullet;
				while (workB.nextBullet != null) {
					workB = workB.nextBullet;
					if (workM.rectMonster.intersect(workB.rectBullet)
							|| workM.rectMonster.contains(workB.rectBullet)) { // 当子弹击中怪兽时
						workM.blood -= GConstant.planePower;
						workB.isFire = false;
					}
				}
			} else { // 如果怪兽已飞出屏幕
				workM.blood = -1234;
			}
		}
	}

	public void ctrlMonster() {
		// 判断是否调整怪兽出现速度
		if (drawAddMonsterCount >= GConstant.speedAddMonster) {
			drawMonsterCount /= 1.5;
			drawAddMonsterCount = 0;
		}
		if (drawCount >= drawMonsterCount
				&& headDeadMonster.nextMonster != null
				&& tailDeadMonster != null) {
			// 当刷屏线程已经执行了30次时，让最早的一只死了的怪兽复活
			drawCount = 0;
			// 将那只最早死亡的怪兽放到活怪兽队列中，让其成为队头
			tailDeadMonster.nextMonster = aliveMonster.nextMonster;
			aliveMonster.nextMonster = tailDeadMonster;
			// 重置将要复活的怪兽的属性
			tailDeadMonster.recoverMonster();
			// 将复活的怪兽从死怪兽队列中移出
			Monster workM = headDeadMonster;
			while (workM.nextMonster != tailDeadMonster) {
				workM = workM.nextMonster;
			}
			workM.nextMonster = null;
			tailDeadMonster = workM;
		}
		// 绘制所有的活着的怪兽，并让活怪兽队列中的死怪兽进入死怪兽队列
		Monster drawM = aliveMonster;
		while (drawM.nextMonster != null) {
			// 判断怪兽是否死亡
			if (drawM.nextMonster.blood <= 0) {
				// 判断怪兽是不是被玩家打死的
				if (drawM.nextMonster.blood != -1234) { // 如果是
					GConstant.killCount++; // 杀敌数+1
					// 每杀死一只怪兽，飞机子弹攻击力+0.01
					GConstant.planePower += GConstant.extraDamage;
					// 执行怪兽死亡效果方法。
//					drawM.nextMonster.toDeadMonster(canvas, paint, GConstant.DEAD_MONSTER_SOUND, drawAddMonsterCount);
				}
				// 如果怪兽已经死亡，则移至死亡怪兽队列
				deadMonster(drawM);
			} else { // 如果怪兽还活着，则继续绘制
				drawM.nextMonster.monsterFly(canvas, paint);
			}
			if (drawM.nextMonster != null)
				drawM = drawM.nextMonster;
			else
				break;
		}
		isMonsterDead();
	}

	public void deadMonster(Monster lastM) {
		Monster workM = lastM.nextMonster;
		// 移出活怪兽队列
		lastM.nextMonster = workM.nextMonster;
		// 放入死怪兽队列
		workM.nextMonster = headDeadMonster.nextMonster;
		headDeadMonster.nextMonster = workM;
		if (headDeadMonster == tailDeadMonster)
			tailDeadMonster = workM;
	}

	public void onPause() {
		GConstant.gameState = 0;
	}


	public void onResume() {
		flag = true;
		GConstant.gameState = 1001;
		//GConstant.gameThread.interrupt();
		//DELETE THIS　ＳＥＮＴＥＮＳＥ
	}

	public void onDestory() {
		flag = false;
		GConstant.gameState = 0;
		GConstant.gameThread.interrupt();
	}
}

/*
 * 飞机血条的设计方法： // 首先定义一个paint Paint paint = new Paint();
 * 
 * // 绘制矩形区域-实心矩形 // 设置颜色 paint.setColor(Color.WHITE); // 设置样式-填充
 * paint.setStyle(Style.FILL); // 绘制一个矩形 canvas.drawRect(new Rect(0, 0,
 * getWidth(), getHeight()), paint);
 * 
 * // 绘空心矩形 // 设置颜色 paint.setColor(Color.RED); // 设置样式-空心矩形
 * paint.setStyle(Style.STROKE); // 绘制一个矩形 canvas.drawRect(new Rect(10, 10, 50,
 * 20), paint);
 */
