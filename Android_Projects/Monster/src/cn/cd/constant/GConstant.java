package cn.cd.constant;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import cn.liwang.monster.R;

@SuppressLint("UseSparseArrays")
public class GConstant {
	public static SoundPool sp;							//得到一个声音池引用
	public static HashMap<Integer,Integer> spMap;		//得到一个map的引用
	public static Context context;
	public static final String GAME_DESCRIBE=
			"一天早上，圣枪游侠在轰鸣声中醒来，" +
			"出门看，一架陨落的灰机进入了视线。\n" +
			"原来是一群飞龙追赶着人类，误入了召唤师峡谷，" +
			"他们挥舞着巨大的翅膀，口吐火球，瞬间让野区映成了一片火海。\n" +
			"（这不是来送经验的么）卢锡安表示要向这些不速之客讨回公道。\n" +
			"注明： 游戏背景图来自mmm_m，地图滚动方式部分代码摘自CSDN，人物图源来自游戏英雄联盟，交流学习用不作商业用途 By okcd00";
	public static final int GAME_THREAD_DELAY=4000; //从游戏启动动画界面到游戏主菜单的延时时间
	//这个数组保存怪兽的飞行动画的帧的资源id
    public static final int[] monsterAnimation = new int[] {R.drawable.monster_03,R.drawable.monster_06};
    //怪兽死亡后的动画
    public static final int[] monsterDeadID={R.drawable.monster_03,R.drawable.monster_03_01,
		R.drawable.monster_03_02,R.drawable.monster_03_03,R.drawable.monster_03_03,R.drawable.monster_03_05};
    public static final int MY_WARPLANE_ID=R.drawable.peal;
    public static int gameState=1001; //游戏状态值， 1001表示游戏正在运行
    public static Thread gameThread; //游戏进程
    public static final int monsterBullet = R.drawable.monster_fire; //怪兽子弹图片ID
    //最大的怪兽数量
  	public  static  int numMonsters =20;
  //怪兽增加的速度，即刷几次屏怪兽出现速度加快
    public static final long speedAddMonster = 1000;
   //刷几次屏出生一只怪兽
  	public static int drawCount =120;
  	//最大子弹数量
  	public static  int numBullets = 20;
   //刷几次屏怪兽射出一颗子弹
  	public static  int drawMonsterBullet =30;
  	//刷几次屏射出一颗子弹
  	public static  int bulletCount = 4;
   //子弹飞行速度
  	public static  final int speed_slow = 3, speed_middle = 10, speed_fast = 14;
  	public static float extraDamage=0.01f;
  	//游戏背景资源Id
  	public static final int[] GAME_BG_IMAGE_ID={R.drawable.img_bg_level_1,R.drawable.img_bg_level_1};
  	//游戏音乐Id
  	public static final int GAME_BACKGROUNG_MUSIC_ID=R.raw.combatribesboss1;
  	
  	//游戏音乐初始音量
  	public static final int R_VOLUM=50;
  	public static final int L_VOLUM=50;
  	//设置游戏背景音乐循环标志
  	public static final boolean LOOP_BACKGROUNG_MUSIC=true;
  	
  	
  	//判断游戏是否结束
  	public static boolean gameNoOver=true;
  	//结束游戏回到主菜单的标志
  	public static final int endGame=1010;
  	public static Handler handler;
  	//飞机爆炸效果图Id
  	public static final int[] PLANE_EXPLODE_IMAGE_ID={R.drawable.bomb_enemy_0,R.drawable.bomb_enemy_1,
  		R.drawable.bomb_enemy_2,R.drawable.bomb_enemy_3,R.drawable.bomb_enemy_4,R.drawable.bomb_enemy_5
  	};
  	public static float planePower = 5; //飞机子弹攻击力
	public static float monsterPower = 20; //怪兽子弹攻击力
	public static int killCount = 0;  //杀敌数
	
	//处理将战绩显示到文本框的handler
	public static Handler mainHandler;
	
	
	//设置是否是正常模式 还是随机模式 true表示正常模式，false表示随机模式
	public static boolean normalOrRandom=true; 
	//背景音乐开与关
	public static boolean onOffFlag=true;
	
	public static final int[] dead_monsters = new int[]{  //怪兽死亡的位图
	};
	//怪兽死亡时的音效
	public static final int DEAD_MONSTER_SOUND = 0; 

	public static void initSoundPool(){			//初始化声音池
    	sp=new SoundPool(
    			5, 				//maxStreams参数，该参数为设置同时能够播放多少音效
    			AudioManager.STREAM_MUSIC,	//streamType参数，该参数设置音频类型，在游戏中通常设置为：STREAM_MUSIC
    			0				//srcQuality参数，该参数设置音频文件的质量，目前还没有效果，设置为0为默认值。
    	);
    	spMap=new HashMap<Integer,Integer>();
    	spMap.put(1, sp.load(context, R.raw.bullet, 1));
    	spMap.put(2, sp.load(context, R.raw.enemy4_out, 1));
    	spMap.put(3, sp.load(context, R.raw.game_over, 1));
    }
    @SuppressWarnings("static-access")
	public static void playSound(int sound,int number){	//播放声音,参数sound是播放音效的id，参数number是播放音效的次数
    	AudioManager am=(AudioManager)context.getSystemService(context.AUDIO_SERVICE);//实例化AudioManager对象
    	float audioMaxVolumn=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);	//返回当前AudioManager对象的最大音量值
    	float audioCurrentVolumn=am.getStreamVolume(AudioManager.STREAM_MUSIC);//返回当前AudioManager对象的音量值
    	float volumnRatio=audioCurrentVolumn/audioMaxVolumn;
    	sp.play(
    			spMap.get(sound), 					//播放的音乐id
    			volumnRatio, 						//左声道音量
    			volumnRatio, 						//右声道音量
    			1, 									//优先级，0为最低
    			number, 							//循环次数，0为不循环，-1为永远循环
    			1									//回放速度 ，该值在0.5-2.0之间，1为正常速度
    	);
    }

}
