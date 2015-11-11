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
	public static SoundPool sp;							//�õ�һ������������
	public static HashMap<Integer,Integer> spMap;		//�õ�һ��map������
	public static Context context;
	public static final String GAME_DESCRIBE=
			"һ�����ϣ�ʥǹ�����ں�������������" +
			"���ſ���һ������Ļһ����������ߡ�\n" +
			"ԭ����һȺ����׷�������࣬�������ٻ�ʦϿ�ȣ�" +
			"���ǻ����ž޴�ĳ�򣬿��»���˲����Ұ��ӳ����һƬ�𺣡�\n" +
			"���ⲻ�����;����ô��¬������ʾҪ����Щ����֮���ֻع�����\n" +
			"ע���� ��Ϸ����ͼ����mmm_m����ͼ������ʽ���ִ���ժ��CSDN������ͼԴ������ϷӢ�����ˣ�����ѧϰ�ò�����ҵ��; By okcd00";
	public static final int GAME_THREAD_DELAY=4000; //����Ϸ�����������浽��Ϸ���˵�����ʱʱ��
	//������鱣����޵ķ��ж�����֡����Դid
    public static final int[] monsterAnimation = new int[] {R.drawable.monster_03,R.drawable.monster_06};
    //����������Ķ���
    public static final int[] monsterDeadID={R.drawable.monster_03,R.drawable.monster_03_01,
		R.drawable.monster_03_02,R.drawable.monster_03_03,R.drawable.monster_03_03,R.drawable.monster_03_05};
    public static final int MY_WARPLANE_ID=R.drawable.peal;
    public static int gameState=1001; //��Ϸ״ֵ̬�� 1001��ʾ��Ϸ��������
    public static Thread gameThread; //��Ϸ����
    public static final int monsterBullet = R.drawable.monster_fire; //�����ӵ�ͼƬID
    //���Ĺ�������
  	public  static  int numMonsters =20;
  //�������ӵ��ٶȣ���ˢ���������޳����ٶȼӿ�
    public static final long speedAddMonster = 1000;
   //ˢ����������һֻ����
  	public static int drawCount =120;
  	//����ӵ�����
  	public static  int numBullets = 20;
   //ˢ�������������һ���ӵ�
  	public static  int drawMonsterBullet =30;
  	//ˢ���������һ���ӵ�
  	public static  int bulletCount = 4;
   //�ӵ������ٶ�
  	public static  final int speed_slow = 3, speed_middle = 10, speed_fast = 14;
  	public static float extraDamage=0.01f;
  	//��Ϸ������ԴId
  	public static final int[] GAME_BG_IMAGE_ID={R.drawable.img_bg_level_1,R.drawable.img_bg_level_1};
  	//��Ϸ����Id
  	public static final int GAME_BACKGROUNG_MUSIC_ID=R.raw.combatribesboss1;
  	
  	//��Ϸ���ֳ�ʼ����
  	public static final int R_VOLUM=50;
  	public static final int L_VOLUM=50;
  	//������Ϸ��������ѭ����־
  	public static final boolean LOOP_BACKGROUNG_MUSIC=true;
  	
  	
  	//�ж���Ϸ�Ƿ����
  	public static boolean gameNoOver=true;
  	//������Ϸ�ص����˵��ı�־
  	public static final int endGame=1010;
  	public static Handler handler;
  	//�ɻ���ըЧ��ͼId
  	public static final int[] PLANE_EXPLODE_IMAGE_ID={R.drawable.bomb_enemy_0,R.drawable.bomb_enemy_1,
  		R.drawable.bomb_enemy_2,R.drawable.bomb_enemy_3,R.drawable.bomb_enemy_4,R.drawable.bomb_enemy_5
  	};
  	public static float planePower = 5; //�ɻ��ӵ�������
	public static float monsterPower = 20; //�����ӵ�������
	public static int killCount = 0;  //ɱ����
	
	//����ս����ʾ���ı����handler
	public static Handler mainHandler;
	
	
	//�����Ƿ�������ģʽ �������ģʽ true��ʾ����ģʽ��false��ʾ���ģʽ
	public static boolean normalOrRandom=true; 
	//�������ֿ����
	public static boolean onOffFlag=true;
	
	public static final int[] dead_monsters = new int[]{  //����������λͼ
	};
	//��������ʱ����Ч
	public static final int DEAD_MONSTER_SOUND = 0; 

	public static void initSoundPool(){			//��ʼ��������
    	sp=new SoundPool(
    			5, 				//maxStreams�������ò���Ϊ����ͬʱ�ܹ����Ŷ�����Ч
    			AudioManager.STREAM_MUSIC,	//streamType�������ò���������Ƶ���ͣ�����Ϸ��ͨ������Ϊ��STREAM_MUSIC
    			0				//srcQuality�������ò���������Ƶ�ļ���������Ŀǰ��û��Ч��������Ϊ0ΪĬ��ֵ��
    	);
    	spMap=new HashMap<Integer,Integer>();
    	spMap.put(1, sp.load(context, R.raw.bullet, 1));
    	spMap.put(2, sp.load(context, R.raw.enemy4_out, 1));
    	spMap.put(3, sp.load(context, R.raw.game_over, 1));
    }
    @SuppressWarnings("static-access")
	public static void playSound(int sound,int number){	//��������,����sound�ǲ�����Ч��id������number�ǲ�����Ч�Ĵ���
    	AudioManager am=(AudioManager)context.getSystemService(context.AUDIO_SERVICE);//ʵ����AudioManager����
    	float audioMaxVolumn=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);	//���ص�ǰAudioManager������������ֵ
    	float audioCurrentVolumn=am.getStreamVolume(AudioManager.STREAM_MUSIC);//���ص�ǰAudioManager���������ֵ
    	float volumnRatio=audioCurrentVolumn/audioMaxVolumn;
    	sp.play(
    			spMap.get(sound), 					//���ŵ�����id
    			volumnRatio, 						//����������
    			volumnRatio, 						//����������
    			1, 									//���ȼ���0Ϊ���
    			number, 							//ѭ��������0Ϊ��ѭ����-1Ϊ��Զѭ��
    			1									//�ط��ٶ� ����ֵ��0.5-2.0֮�䣬1Ϊ�����ٶ�
    	);
    }

}
