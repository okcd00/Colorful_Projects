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
 * �������࣬��Ϊ��Ϸ�����Ҫ����Ķ��������Ĳ�ͬ���ԵĶ��������Ϸ���в�ͬ�Ĺ��޳��֡�
 * ���޵ĳ�ʼ������ʼ�������ڴ���Ĺ��캯�����У����޵ķ���·���Լ�����״̬�ı仯��������൱�еķ���ִ�С�
 * �����������������ڣ��ֱ����ǣ��ٳ���->����->����ɱ->�������ڳ���->�ɳ���Ļ->���»ص�ս����
 * �ɳ���Ļ�Ĺ�������ֵ�Զ���Ϊ-1234��������ֵ����Ϊ�����Ƿ���ı�����ԡ� ����ֻ���е���������Ķ��壬�Թ���������Ĺ�����GameMainView�н��С�
 * ����Ҳ���Է����ӵ����ɵ��÷�����ƹ��޺�ʱ�����ӵ���ÿֻ����ֻ������Ļ�ϱ���һ���ӵ���
 * 
 * @author okcd00
 **/
public class Monster {

	// ���÷���������
	public Context context;
	// ���޵�λ�����꣬�Լ���ʼ������
	public float monsterX = 0f, monsterY = 0f;
	// �����ϴ�������λ��
	public float deadX = 0f, deadY = 0f;
	// ���޵ĳߴ�
	public float width, height;
	// ���޵��������ֵ����������ֵ����������ֵҲ�����жϹ����Ƿ���
	public int blood = 0;
	private final int maxblood;
	// ����λͼ
	public Animation monsterAnim = null;
	public Animation monsterDeadAnim=null;
	// �����޻��ŵ�ʱ�����ֵΪtrue��ʹ�ö�����������������������ɺ�ֵΪfalse��������ͣ��
	public boolean isLoop = true;
	// ��ֻ���޵���һֻ����
	public Monster nextMonster = null;
	// �趨���޺����ƶ����ƶ�����
	public float xMove = 0;
	// ���޵ĵ���ƫ����
	public float perMove = 0;
	// �����Ѿ�ƫ�Ƶ���
	public float hasMove = 0;
	// ���޵�λ�þ��Σ���ײ���ʱʹ��
	public Rect rectMonster = null;
	// ������������ӵ�
	public Bullet monsterBullet;
	// ��ҵķɻ�
	public MyPlane plane;
	// ����ˢ��������
	public int drawMonsterCount = 0;
	// ��Ļ�ߴ�
	public float screemX, screemY;
	// ������ʾ����Ѫ�����ο�ľ���
	public Rect rectMaxBlood;
	// ������ʾ����Ѫ���ľ���
	public Rect rectBlood;

	public Monster(Context context, int[] resId,int[] deadID, boolean isLoop, float screemX,
			float screemY, int resMonsterBulletId, MyPlane plane) {
		// ��ʼ����Ļ�ߴ�
		this.screemX = screemX;
		this.screemY = screemY;
		isLoop = true;
		monsterAnim = new Animation(context, resId, isLoop);
		monsterDeadAnim=new Animation(context,deadID,false);
		// �趨���޵ĳߴ磬����Ҫ����޵Ķ���֡ͼ�ߴ綼һ��
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.monster_03);
		// Bitmap bmp2 = BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.monster_06);
		// float w = bmp2.getWidth(),h=bmp2.getHeight();
		width = bmp.getWidth();
		height = bmp.getHeight();
		// ����������ù��޵ĳ�ʼX����ֵ
		Random r = new Random();
		monsterX = r.nextInt((int) (screemX - width));
		// ���ù��޵ĳ�ʼY����
		monsterY = 0 - height;
		// �趨���޵�����ֵ,�������ֵ
		maxblood = blood = 100;
		// ��ʼ�����޵�λ�þ���
		rectMonster = new Rect();
		// ��ʼ��������������ӵ�������������
		monsterBullet = new Bullet(context, resMonsterBulletId);
		initMonsterBullet();
		// ��ʼ����ҵķɻ�
		this.plane = plane;
		// ��ʼ��context
		this.context = context;
		// ��ʼ������Ѫ��������κ͹���Ѫ������
		rectMaxBlood = new Rect();
		rectBlood = new Rect();
		// ��ʼ������������Ч
	}

	/**
	 * ��ʼ�������ӵ�״̬
	 */
	public void initMonsterBullet() {
		monsterBullet.isFire = false;
		monsterBullet.bulletX = monsterX + (width / 2);
		monsterBullet.bulletY = monsterY + (height / 4); // ���ӵ����ڹ��޵����
	}

	public void monsterFly(Canvas canvas, Paint paint) {
		// �������
		monsterY += 2;
		if (blood > 0 && blood <= maxblood / 2
				&& Math.abs(hasMove) < Math.abs(xMove)) {
			// ������޵�Ѫ������һ�룬��ƫ�Ʒ���
			monsterX += perMove;
			hasMove += perMove;
		}
		// ���¹���λ�þ���
		rectMonster.set((int) (monsterX + width * 0.25), (int) monsterY,
				(int) (monsterX + width * 0.8),
				(int) ((monsterY + height) - height * 0.3));

		// ���ƾ���
		monsterAnim.DrawAnimation(canvas, paint, monsterX, monsterY);
//		canvas.drawRect(rectMonster, paint);
		drawMonsterCount++;
		// �жϹ����Ƿ���Բ����ӵ���
		if (drawMonsterCount >= GConstant.drawMonsterBullet
				&& !monsterBullet.isFire) {
			drawMonsterCount = 0;
			// �����ӵ�Ӧ�������������ϵ�·��
			float dx = plane.planeX + plane.width / 2 - monsterBullet.bulletX, dy = plane.planeY
					+ plane.height / 2 - monsterBullet.bulletY;
			// ���ù����ӵ��ĺ��ݷ����ٶ�
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
			// �����ӵ����ԣ�����׼�������
			monsterBullet.reliveBullet(monsterX + (width / 2), monsterY
					+ (height / 4), // ���ӵ����ڹ��޵����
					speedX, speedY);
			monsterBullet.isFire = true;
		}
		// ���������ù��޵��ӵ�
		if (monsterBullet.isFire) { // ���ӵ���Чʱ
			if (monsterBullet.bulletY > 0 - monsterBullet.height
					&& monsterBullet.bulletY <= screemY
					&& monsterBullet.bulletX > 0 - monsterBullet.width
					&& monsterBullet.bulletX < screemX) {
				// ����ӵ�û�������Ļ����������
				monsterBullet.flyBullet(canvas, paint);
			} else { // ����ӵ������Ļ��ʧЧ
				initMonsterBullet();
			}
			// ��ײ���
			if (plane.rectPlane.intersect(monsterBullet.rectBullet)
					|| plane.rectPlane.contains(monsterBullet.rectBullet)) { // ��������ӵ����зɻ�
				plane.blood -= GConstant.monsterPower;
				monsterBullet.isFire = false;
			}
		}
		// ������ɻ�����ײ���
		if (plane.rectPlane.intersect(rectMonster)
				|| plane.rectPlane.contains(rectMonster)) { // ������޺ͷɻ���ײ
			plane.blood = -1; // �ɻ�ֱ������
		}
		drawMonsterMessage(canvas);
	}

	public void drawMonsterMessage(Canvas canvas) {
		Paint p = new Paint();
		float lenBlood = (float) (width * 0.55 * blood / maxblood); // Ѫ������
		// ����Ѫ��������κ�����Ѫ������
		rectMaxBlood.set((int) (monsterX + 0.2 * width),
				(int) (monsterY + height), (int) (monsterX + 0.75 * width),
				(int) (monsterY + height + 5));
		rectBlood.set((int) (monsterX + 0.2 * width),
				(int) (monsterY + height),
				(int) (monsterX + 0.2 * width + lenBlood), (int) (monsterY
						+ height + 5));
		// ���û��ʣ�������Ѫ�������Ѫ��
		p.setColor(Color.RED);
		p.setStyle(Style.FILL);
		canvas.drawRect(rectBlood, p);
		p.setColor(Color.BLACK);
		p.setStyle(Style.STROKE);
		canvas.drawRect(rectMaxBlood, p);
	}

	public void toDeadMonster(Canvas canvas, Paint paint, int soundID,long drawCount) {
		// �����������ʱ������
		monsterDeadAnim.DrawAnimation(canvas, paint, monsterX, monsterY);
		// ���޵�������Ч
		if(GConstant.onOffFlag)if(drawCount%2==0)GConstant.playSound(2,1);
	}

	public void recoverMonster() {
		// �������X����
		Random r = new Random();
		monsterX = r.nextInt((int) (screemX - width));
		if (new Random().nextBoolean()) { // ��һ��ļ���ʹ�ɻ����Ժ������
			xMove = screemX / 2 - monsterX;
			// �������ƫ������С�����ʵ�����
			if (Math.abs(xMove) < Math.abs(screemX / 6))
				xMove *= 2.8;
			// ����ÿ��ˢ��ʱ��ƫ������ʹ��20��ˢ���󣬷ɻ���ɺ���ƫ��
			perMove = xMove / 20;
		} else { // �ɻ��޺����ƶ�
			xMove = 0;
		}
		monsterY = -100;
		blood = maxblood;
		hasMove = 0;
		initMonsterBullet();
	}
}