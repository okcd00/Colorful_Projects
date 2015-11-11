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
	private float x = 40, y = 40; // ��ָ���µ�����
	private boolean flag = true;
	private Canvas canvas;
	private Paint paint;
	private float screemX, screemY;
	private float a, b; // ΢���ƶ�����ָ������
	final static int PLAN_STEP = 10;
	public float chaX = 0;
	public float chaY = 0;
	private Monster aliveMonster = null; // ���ŵĹ��޵�ͷָ�룬�ö����ж�ͷ����
	private Monster headDeadMonster = null; // �����޶��У���ͷ�ڵ㣩��ͷָ��,��������˵�ʱ�����������
	private Monster tailDeadMonster = null; // �����޶��е�βָ�룬�����޸���ʱ������Ƴ�
	private int drawCount = 300; // �����õ�ˢ��������
	public int drawMonsterCount = 300; // ˢ����������һֻ����
	public long drawAddMonsterCount = 0;
	public int drawInitPlane = 0; // ǰ����ˢ��
	private MyPlane plane; // ��ҿ��Ƶķɻ�
	private Bullet headUnuseBullet; //û��������ӵ�
	private Bullet headFireBullet;  //��������ӵ�
	private int drawBulletCount; // �ӵ��õ�ˢ��������
	private boolean isBulletLeft = true; // ������һ���ӵ��Ƿ��������
	private Rect rectMsg = new Rect(), // �ɻ���Ѫ���������
			rectBlood = new Rect(); // �ɻ���Ѫ������
	private int lenBlood; // �ɻ�Ѫ���ĳ���
	private GameBackground gbground; // ��������
	private Bitmap[] deadPlane; // �ɻ�������ı�ըλͼ

	public GameMainView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		holder = getHolder();
		holder.addCallback(this);
		paint = new Paint();
		// ���û�����ɫΪ��ɫ
		paint.setColor(Color.BLACK);
		// �����ı���С
		paint.setTextSize(20);
		// ���ý���
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
		// ��ʼ����Ϸ��Ϣ
		rectMsg.set((int) screemX - 105, 5, (int) screemX - 5, 20);
		// ��ʼ���ɻ�
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
			firstPlaneFly(); // �ɻ��Ŀ�������
			drawMessage();
			bulletFire();
			ctrlMonster(); // ���Ʋ����ƹ���
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
		// ��������ֵ��Ϣ
		Paint p = paint;
		canvas.drawText((String) getResources().getText(R.string.plane_blood),
				(int) screemX - 140, 20, p);
		canvas.drawText((String) getResources().getText(R.string.kill_num),
				(int) screemX - 140, 50, p);
		// ���Ƶ�ǰ����ֵ��Ѫ����
		lenBlood = (int) (100 * plane.blood / plane.maxblood);
		rectBlood.set((int) screemX - 105, 5, (int) screemX - 105 + lenBlood,
				20);
		p.setStyle(Style.FILL);
		p.setColor(Color.RED);
		canvas.drawRect(rectBlood, paint);
		// ����Ѫ���ķ���
		p.setStyle(Style.STROKE);
		p.setColor(Color.BLACK);
		canvas.drawRect(rectMsg, paint);
		// ������ҵ�ɱ����
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
			// ���ˢ����ʱ���ﵽ�涨ֵ�������һ���ӵ�
			// �����ӵ���ˢ����ʱ��
			drawBulletCount = 0;
			// ����Ч�ӵ�����ɾ��һ���ӵ�
			headUnuseBullet.nextBullet = workB.nextBullet;
			// �Ѹո�ɾ�����ӵ����뵽��Ч�ӵ�����
			workB.nextBullet = headFireBullet.nextBullet;
			headFireBullet.nextBullet = workB;
			// �����ӵ�������������
			float differ = plane.width / 6;
			if (!isBulletLeft)
				differ *= 5;
			// ִ���ӵ�����װ�����װ�����ӵ�������һ��ѭ�����
			workB.reliveBullet(plane.planeX-13, plane.planeY, differ);
			// �����´��ӵ�������
			isBulletLeft = !isBulletLeft;
			if(GConstant.onOffFlag)GConstant.playSound(1,1);
		}
		// ������������ӵ���λ��
		workB = headFireBullet;
		while (workB.nextBullet != null) {
			if (workB.nextBullet.isFire) { // �������ӵ���Ч
				if (workB.nextBullet.bulletY > 0 - workB.nextBullet.height) {
					// ����ӵ�û�������Ļ����������
					workB.nextBullet.flyBullet(canvas, paint);
				} else { // ����ӵ������Ļ��ʧЧ
					workB.nextBullet.isFire = false;
				}
			} else { // ����ӵ�������Ϊ��Ч
				// ���ӵ�����Ч�������Ƴ�
				Bullet outBullet = workB.nextBullet;
				workB.nextBullet = outBullet.nextBullet;
				// ���ӵ��ƽ���Ч������
				outBullet.nextBullet = headUnuseBullet.nextBullet;
				headUnuseBullet.nextBullet = outBullet;
				if (workB.nextBullet == null)
					break; // Ԥ�����
			}
			workB = workB.nextBullet;
		}
	}

	public void initMonsters() {
		// ��ʼ�������޶��У������޶�����ͷ���
		headDeadMonster = new Monster(getContext(), GConstant.monsterAnimation,GConstant.monsterDeadID,
				true, screemX, screemY, GConstant.monsterBullet, plane);
		// ������������еĹ���ָ��
		Monster workM = headDeadMonster;
		// ��ʼ�����ɸ�����
		for (int i = 0; i < GConstant.numMonsters; i++) {
			workM.nextMonster = new Monster(getContext(),
					GConstant.monsterAnimation,GConstant.monsterDeadID, true, screemX, screemY,
					GConstant.monsterBullet, plane);
			workM = workM.nextMonster;
		}
		tailDeadMonster = workM;
		// ��ʼ������޶��е�ͷ���
		aliveMonster = new Monster(getContext(), GConstant.monsterAnimation,GConstant.monsterDeadID,
				true, screemX, screemY, GConstant.monsterBullet, plane);
	}

	
	// * ͨ����ײ��⣬�Լ����޶�λ�жϹ����Ƿ��ӵ����л��߷ɳ���Ļ �����ƹ��޵�������
	
	public void isMonsterDead() {
		// ��ʼ�����޹���ָ��
		Monster workM = aliveMonster;
		while (workM.nextMonster != null) {
			workM = workM.nextMonster;
			// �жϹ����Ƿ�����Ļ��
			if (workM.monsterX < screemX && workM.monsterY < screemY) { // ������޻�����Ļ�ڣ��������ײ���
				// ��ʼ���ӵ�����ָ��
				Bullet workB = headFireBullet;
				while (workB.nextBullet != null) {
					workB = workB.nextBullet;
					if (workM.rectMonster.intersect(workB.rectBullet)
							|| workM.rectMonster.contains(workB.rectBullet)) { // ���ӵ����й���ʱ
						workM.blood -= GConstant.planePower;
						workB.isFire = false;
					}
				}
			} else { // ��������ѷɳ���Ļ
				workM.blood = -1234;
			}
		}
	}

	public void ctrlMonster() {
		// �ж��Ƿ�������޳����ٶ�
		if (drawAddMonsterCount >= GConstant.speedAddMonster) {
			drawMonsterCount /= 1.5;
			drawAddMonsterCount = 0;
		}
		if (drawCount >= drawMonsterCount
				&& headDeadMonster.nextMonster != null
				&& tailDeadMonster != null) {
			// ��ˢ���߳��Ѿ�ִ����30��ʱ���������һֻ���˵Ĺ��޸���
			drawCount = 0;
			// ����ֻ���������Ĺ��޷ŵ�����޶����У������Ϊ��ͷ
			tailDeadMonster.nextMonster = aliveMonster.nextMonster;
			aliveMonster.nextMonster = tailDeadMonster;
			// ���ý�Ҫ����Ĺ��޵�����
			tailDeadMonster.recoverMonster();
			// ������Ĺ��޴������޶������Ƴ�
			Monster workM = headDeadMonster;
			while (workM.nextMonster != tailDeadMonster) {
				workM = workM.nextMonster;
			}
			workM.nextMonster = null;
			tailDeadMonster = workM;
		}
		// �������еĻ��ŵĹ��ޣ����û���޶����е������޽��������޶���
		Monster drawM = aliveMonster;
		while (drawM.nextMonster != null) {
			// �жϹ����Ƿ�����
			if (drawM.nextMonster.blood <= 0) {
				// �жϹ����ǲ��Ǳ���Ҵ�����
				if (drawM.nextMonster.blood != -1234) { // �����
					GConstant.killCount++; // ɱ����+1
					// ÿɱ��һֻ���ޣ��ɻ��ӵ�������+0.01
					GConstant.planePower += GConstant.extraDamage;
					// ִ�й�������Ч��������
//					drawM.nextMonster.toDeadMonster(canvas, paint, GConstant.DEAD_MONSTER_SOUND, drawAddMonsterCount);
				}
				// ��������Ѿ��������������������޶���
				deadMonster(drawM);
			} else { // ������޻����ţ����������
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
		// �Ƴ�����޶���
		lastM.nextMonster = workM.nextMonster;
		// ���������޶���
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
		//DELETE THIS���ӣţΣԣţΣӣ�
	}

	public void onDestory() {
		flag = false;
		GConstant.gameState = 0;
		GConstant.gameThread.interrupt();
	}
}

/*
 * �ɻ�Ѫ������Ʒ����� // ���ȶ���һ��paint Paint paint = new Paint();
 * 
 * // ���ƾ�������-ʵ�ľ��� // ������ɫ paint.setColor(Color.WHITE); // ������ʽ-���
 * paint.setStyle(Style.FILL); // ����һ������ canvas.drawRect(new Rect(0, 0,
 * getWidth(), getHeight()), paint);
 * 
 * // ����ľ��� // ������ɫ paint.setColor(Color.RED); // ������ʽ-���ľ���
 * paint.setStyle(Style.STROKE); // ����һ������ canvas.drawRect(new Rect(10, 10, 50,
 * 20), paint);
 */
