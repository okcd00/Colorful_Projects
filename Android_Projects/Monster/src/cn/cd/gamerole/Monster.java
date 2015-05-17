package cn.cd.gamerole;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import cn.cd.constant.GConstant;
import cn.cd.gameanim.Animation;
import cn.liwang.monster.R;

/**
 * 泛怪兽类，作为游戏中玩家要消灭的对象，这个类的不同属性的对象会让游戏中有不同的怪兽出现。
 * 怪兽的初始化（初始化）都在此类的构造函数当中，怪兽的飞行路线以及生命状态的变化都由这个类当中的方法执行。
 * 怪兽有两种生命周期，分别是是：①出生->负伤->被击杀->重生。②出生->飞出屏幕->重新回到战场。
 * 飞出屏幕的怪兽生命值自动变为-1234，而生命值是作为怪兽是否存活的标杆属性。 本类只进行单个怪兽类的定义，对怪兽类数组的管理在GameMainView中进行。
 * 怪兽也可以发射子弹，由调用方类控制怪兽何时发射子弹，每只怪兽只可在屏幕上保留一颗子弹。
 * 
 * @author okcd00
 **/
public class Monster {

	// 调用方的上下文
	public Context context;
	// 怪兽的位置坐标，以及初始横坐标
	public float monsterX = 0f, monsterY = 0f;
	// 怪兽上次死亡的位置
	public float deadX = 0f, deadY = 0f;
	// 怪兽的尺寸
	public float width, height;
	// 怪兽的最大生命值和现有生命值，现有生命值也用来判断怪兽是否存活
	public int blood = 0;
	private final int maxblood;
	// 怪兽位图
	public Animation monsterAnim = null;
	public Animation monsterDeadAnim=null;
	// 当怪兽活着的时候，这个值为true，使得动画持续，怪兽生命周期完成后值为false，动画暂停。
	public boolean isLoop = true;
	// 这只怪兽的下一只怪兽
	public Monster nextMonster = null;
	// 设定怪兽横向移动的移动幅度
	public float xMove = 0;
	// 怪兽的单次偏移量
	public float perMove = 0;
	// 怪兽已经偏移的量
	public float hasMove = 0;
	// 怪兽的位置矩形，碰撞检测时使用
	public Rect rectMonster = null;
	// 怪兽所发射的子弹
	public Bullet monsterBullet;
	// 玩家的飞机
	public MyPlane plane;
	// 怪兽刷屏计数器
	public int drawMonsterCount = 0;
	// 屏幕尺寸
	public float screemX, screemY;
	// 用于显示怪兽血条矩形框的矩形
	public Rect rectMaxBlood;
	// 用于显示怪兽血量的矩形
	public Rect rectBlood;

	public Monster(Context context, int[] resId,int[] deadID, boolean isLoop, float screemX,
			float screemY, int resMonsterBulletId, MyPlane plane) {
		// 初始化屏幕尺寸
		this.screemX = screemX;
		this.screemY = screemY;
		isLoop = true;
		monsterAnim = new Animation(context, resId, isLoop);
		monsterDeadAnim=new Animation(context,deadID,false);
		// 设定怪兽的尺寸，这里要求怪兽的动画帧图尺寸都一样
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.monster_03);
		// Bitmap bmp2 = BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.monster_06);
		// float w = bmp2.getWidth(),h=bmp2.getHeight();
		width = bmp.getWidth();
		height = bmp.getHeight();
		// 用随机数设置怪兽的初始X坐标值
		Random r = new Random();
		monsterX = r.nextInt((int) (screemX - width));
		// 设置怪兽的初始Y坐标
		monsterY = 0 - height;
		// 设定怪兽的生命值,最大生命值
		maxblood = blood = 100;
		// 初始化怪兽的位置矩形
		rectMonster = new Rect();
		// 初始化怪兽所发射的子弹，并设置属性
		monsterBullet = new Bullet(context, resMonsterBulletId);
		initMonsterBullet();
		// 初始化玩家的飞机
		this.plane = plane;
		// 初始化context
		this.context = context;
		// 初始化怪兽血条方格矩形和怪兽血量矩形
		rectMaxBlood = new Rect();
		rectBlood = new Rect();
		// 初始化怪兽死亡音效
	}

	/**
	 * 初始化怪兽子弹状态
	 */
	public void initMonsterBullet() {
		monsterBullet.isFire = false;
		monsterBullet.bulletX = monsterX + (width / 2);
		monsterBullet.bulletY = monsterY + (height / 4); // 让子弹处于怪兽的嘴边
	}

	public void monsterFly(Canvas canvas, Paint paint) {
		// 常规飞行
		monsterY += 2;
		if (blood > 0 && blood <= maxblood / 2
				&& Math.abs(hasMove) < Math.abs(xMove)) {
			// 如果怪兽的血量低于一半，则偏移飞行
			monsterX += perMove;
			hasMove += perMove;
		}
		// 更新怪兽位置矩形
		rectMonster.set((int) (monsterX + width * 0.25), (int) monsterY,
				(int) (monsterX + width * 0.8),
				(int) ((monsterY + height) - height * 0.3));

		// 绘制矩形
		monsterAnim.DrawAnimation(canvas, paint, monsterX, monsterY);
//		canvas.drawRect(rectMonster, paint);
		drawMonsterCount++;
		// 判断怪兽是否可以产生子弹了
		if (drawMonsterCount >= GConstant.drawMonsterBullet
				&& !monsterBullet.isFire) {
			drawMonsterCount = 0;
			// 计算子弹应该在两个方向上的路程
			float dx = plane.planeX + plane.width / 2 - monsterBullet.bulletX, dy = plane.planeY
					+ plane.height / 2 - monsterBullet.bulletY;
			// 设置怪兽子弹的横纵飞行速度
			float speedX, speedY, a = dy / dx;
			if (dx > 0) {
				speedX = (float) Math.sqrt(GConstant.speed_slow
						* GConstant.speed_slow / (1 + a * a));
			} else {
				speedX = 0 - (float) Math.sqrt(GConstant.speed_slow
						* GConstant.speed_slow / (1 + a * a));
			}
			if (dy > 0) {
				speedY = Math.abs(speedX * a);
			} else {
				speedY = (0 - Math.abs(speedX)) * Math.abs(a);
			}
			// 重置子弹属性，让其准备好射出
			monsterBullet.reliveBullet(monsterX + (width / 2), monsterY
					+ (height / 4), // 让子弹处于怪兽的嘴边
					speedX, speedY);
			monsterBullet.isFire = true;
		}
		// 在这里设置怪兽的子弹
		if (monsterBullet.isFire) { // 当子弹有效时
			if (monsterBullet.bulletY > 0 - monsterBullet.height
					&& monsterBullet.bulletY <= screemY
					&& monsterBullet.bulletX > 0 - monsterBullet.width
					&& monsterBullet.bulletX < screemX) {
				// 如果子弹没有射出屏幕，继续飞行
				monsterBullet.flyBullet(canvas, paint);
			} else { // 如果子弹射出屏幕，失效
				initMonsterBullet();
			}
			// 碰撞检测
			if (plane.rectPlane.intersect(monsterBullet.rectBullet)
					|| plane.rectPlane.contains(monsterBullet.rectBullet)) { // 如果怪兽子弹击中飞机
				plane.blood -= GConstant.monsterPower;
				monsterBullet.isFire = false;
			}
		}
		// 怪兽与飞机的碰撞检测
		if (plane.rectPlane.intersect(rectMonster)
				|| plane.rectPlane.contains(rectMonster)) { // 如果怪兽和飞机相撞
			plane.blood = -1; // 飞机直接死亡
		}
		drawMonsterMessage(canvas);
	}

	public void drawMonsterMessage(Canvas canvas) {
		Paint p = new Paint();
		float lenBlood = (float) (width * 0.55 * blood / maxblood); // 血量长度
		// 设置血条方框矩形和现有血量矩形
		rectMaxBlood.set((int) (monsterX + 0.2 * width),
				(int) (monsterY + height), (int) (monsterX + 0.75 * width),
				(int) (monsterY + height + 5));
		rectBlood.set((int) (monsterX + 0.2 * width),
				(int) (monsterY + height),
				(int) (monsterX + 0.2 * width + lenBlood), (int) (monsterY
						+ height + 5));
		// 设置画笔，并绘制血条方格和血量
		p.setColor(Color.RED);
		p.setStyle(Style.FILL);
		canvas.drawRect(rectBlood, p);
		p.setColor(Color.BLACK);
		p.setStyle(Style.STROKE);
		canvas.drawRect(rectMaxBlood, p);
	}

	public void toDeadMonster(Canvas canvas, Paint paint, int soundID,long drawCount) {
		// 保存怪兽死亡时的坐标
		monsterDeadAnim.DrawAnimation(canvas, paint, monsterX, monsterY);
		// 怪兽的死亡音效
		if(GConstant.onOffFlag)if(drawCount%2==0)GConstant.playSound(2,1);
	}

	public void recoverMonster() {
		// 随机生成X坐标
		Random r = new Random();
		monsterX = r.nextInt((int) (screemX - width));
		if (new Random().nextBoolean()) { // 有一半的几率使飞机可以横向飞行
			xMove = screemX / 2 - monsterX;
			// 如果横向偏移量过小，则适当增大
			if (Math.abs(xMove) < Math.abs(screemX / 6))
				xMove *= 2.8;
			// 设置每次刷屏时的偏移量，使得20次刷屏后，飞机完成横向偏移
			perMove = xMove / 20;
		} else { // 飞机无横向移动
			xMove = 0;
		}
		monsterY = -100;
		blood = maxblood;
		hasMove = 0;
		initMonsterBullet();
	}
}