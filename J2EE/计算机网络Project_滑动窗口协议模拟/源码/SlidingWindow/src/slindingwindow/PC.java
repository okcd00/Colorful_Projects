package slindingwindow;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import slindingwindow.State.stateType;


public class PC extends Thread{
	//0是发送端，1是接收端
	private int sendOrReceive;
	//设置可接收窗口大小
	private int windowSize;
	//当前可发送的窗口大小
	private int presentWinSize;
	//最大帧数
	private int totalFrameNum;
	//待发送的帧链表
	List<Frame> frameWindow  = new ArrayList<Frame>();
	//等待接收确认的帧
	List<Integer> waitACKNum = new ArrayList<Integer>();
	//每个等待却仍的帧都有对应的一个计时器
	List<Timer> frameTimers = new ArrayList<Timer>();
	//接下来要发送的帧号，为接收端时有效
	private int preparedSendFrame;
	//接下来要添加的帧号，为发送端时有效
	private int preparedAddFrame;
	//要发送的文件或者要接收的文件
	private File file = null; 
	//信道，所有pc共享
	public Chanel chanel;
	//是否到达文件末尾
	public boolean endOfFile;
	//期望接收的的帧的序号，接收端有效
	public int expectedFrame;
	//错误数据帧的标识
	boolean wrongData;
	

	//是否发送过当前的确认帧
	boolean haveSendACK;
	//接收端的重传定时器
	Timer receiverTimer = new Timer();
	//文件读写
	InputStream is;
	OutputStream os;
	//当前文件位置
	//long fileLoc = 0;
	public PC(int windowSize,int totalFrameNum,String path,Chanel chanel){
		//发送端初始化
		sendOrReceive = 0;
		this.windowSize = windowSize;
		file = new File(path);
		this.chanel = chanel;
		this.totalFrameNum = totalFrameNum;
		endOfFile = false;
		preparedAddFrame = 0;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			addFrameToWindow(this.windowSize);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("增加帧到窗口失败！！");
			e.printStackTrace();
			}
			
		
		
	}
	public PC(int totalFrameNum,String path,Chanel chanel){
		//接收端初始化方法
		sendOrReceive = 1;
		windowSize = 1;
		//接收文件位置
		file = new File(path);
		this.chanel = chanel;
		this.totalFrameNum = totalFrameNum;
		//下一个期望收到的帧的序号，ACK的内容；
		expectedFrame = 0;
		wrongData = false;
		//因为最开始的0号的ACK是不需要发送的
		haveSendACK = true;
		try {
			os = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void run() {
		super.run();
		while(!endOfFile){
			watchOnChanel();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	
	private void watchOnChanel() {
		//监听信道
		if(sendOrReceive == 0){
			//发送端监听信道
			if(chanel.getStateType()==stateType.busy){
				//如果信道繁忙
				if(chanel.isArriveSender()){
					//加入数据到达了发送端
					Frame f = chanel.getFrame();
					chanel.setArriveSender(false);
					//使信道状态变为空闲
					chanel.setStateType(stateType.free);
					//处理接收到的消息
					if(f.getAckOrNak()==0&&f.getMarkOfPc()==1){
						//得到的是来自接收端的ACK应答
						//执行得到ACK方法
						AfterGetingAck(f.getNumOfFrame());
					}
					else if(f.getAckOrNak()==1&&f.getMarkOfPc()==1){
						AfterGetingNak(f.getNumOfFrame());
					}
				}
			}
			else{
				//如果信道不繁忙。看是否有要发送的帧，有就占据信道发送，没有就默默等待吧~
				//不一次全部发送所有的帧到信道里，免得出现接收ACK0但是还在重传3号帧的情况
				if(presentWinSize >0 && presentWinSize>(windowSize/3)){
					//发送一个帧到信道中
					sendFrameToChanel();
					chanel.setStateType(stateType.busy);
					//如果有要发送的帧,让信道变繁忙
					
				}
				else{
						return;
				
				}
			}
		}
		else if(sendOrReceive==1){
			//如果是接收端
			//接收端监听信道
			if(chanel.getStateType()==stateType.busy){
				//如果信道繁忙
				if(chanel.isArriveReceiver()){
					//加入数据到达了接收端
					//从信道中获取数据
					Frame f = chanel.getFrame();
					//数据已经取回，到达接收端标志置为false
					chanel.setArriveReceiver(false);
					//使信道状态变为空闲
					chanel.setStateType(stateType.free);
					//如果是结束帧，则接收端线程暂停
					if(f.isEndFlag()){
						System.out.println("接收到了结束帧！！");
						try {
							os.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						receiverTimer.cancel();
						endOfFile = true;
						/*while(true){
							if(chanel.getStateType()==stateType.free);
							chanel.setStateType(stateType.busy);
							Frame ACK= new Frame(1,expectedFrame,0);
							chanel.setTempFrame(ACK);
							System.out.println("发送ACK"+expectedFrame+"到信道中");
							break;
						}*/
						return;
					}
					//处理来自发送端的消息
					dealWithFrame(f);
				}
			}
			else{
				//如果信道不繁忙。且当前的ACK或者NAK没有发送过 ，怎占据信道发送帧
				if(!haveSendACK){
					haveSendACK = true;
					receiverTimer.cancel();
					chanel.setStateType(stateType.busy);
					if(wrongData){
						//发送NAK
						Frame NAK = new Frame(1,expectedFrame,1);
						chanel.setTempFrame(NAK);
						System.out.println("发送NAK"+expectedFrame+"到信道中");
					}
					else{
						//发送ACK，表明期待接收的下一帧
						Frame ACK= new Frame(1,expectedFrame,0);
						chanel.setTempFrame(ACK);
						System.out.println("发送ACK"+expectedFrame+"到信道中");
					}
					
					receiverTimer = new Timer();
					TimerTask task2 = new TimerTask(){
						public void run() {
							// 到点就认为帧丢失，重传,启动Go Back N协议
							haveSendACK = false;				
						}		
					};
					receiverTimer.schedule(task2, (long)5000);
				
				}
			}
		}
		
	}



	private void dealWithFrame(Frame f) {
		/*
		 * 当接收到一个来自发送端的帧的时候，接收端应该将这个帧的序号与当前期望接收到的帧序号做比较
		 * 如果两者的序号相等，
		 	1.如果帧的数据部分是错误的（没有通过奇偶校验）那么丢弃这个帧，并且发回NAK，表明数据有误
		 	2.如果帧的数据部分是正确的（通过了奇偶校验）那么接收端的期望接收的帧序号模totalFrameNum加1
		 * 如果两者序号不匹配，则说明有丢包的情况
		 	1.接收端发送的ACK或者NAK丢了，发送端选择了重传（接收端选择丢弃，发送端重传）
		 	2.发送端之前发送的帧丢失了（接收端丢弃，发送端等到计时器超时重传）
		           或者延迟的情况
		    1.接收端发送的ACK或者NAK延时了，发送端选择了重传	
		    2.发送端发送的帧延迟了，其他的先到了（接收端继续等待就可以接收到）
		           或者处理延迟或者错包的时候重传的包
		     	接收端	丢弃
		           综上 当序号不匹配的时候，就死守着，等待发送端传过来正确的帧
		 */
		if(f.getNumOfFrame()==expectedFrame){
		//如果帧序号匹配的时候
			haveSendACK = false;
			if(CheckBits(f.getData())){
				//如果通过了奇偶校验
				expectedFrame = (expectedFrame+1)%totalFrameNum;
				//将帧内容传输进文件中
				sendFrameToFile(f);
	
			}
			else{
				wrongData = true;
			}
		}
		
			//如果帧序号不匹配
			//等待呗，不做任何操作
		
		
	}
	private void sendFrameToFile(Frame f) {
		//将帧内容的的奇偶校验码去除
		byte[] sourceFrameData = recoverFrame(f.getData());
	
		
			try {
				os.write(sourceFrameData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		
	}
	private byte[] recoverFrame(byte[] b) {
		// TODO Auto-generated method stub
		return b;
	}
	private boolean CheckBits(byte[] data) {
		//计算校验和，看数据部分是否有错误
		
		return true;
	}
	private void sendFrameToChanel() {
		//发送一个帧到信道中
		//将要发送的帧号移动到下一位,减少当前窗口的大小，给发送帧加上一个定时器，加入需要等待ACK的list行列
		//int count = 0;
		for(Frame f:frameWindow){
		//	count++;
			//如果找到了要发送的帧，就将它发送到信道内，发送到tempFrame是为了对帧还要进行相应的操作
			if(f.getNumOfFrame() == preparedSendFrame){
				chanel.setTempFrame(f);
				System.out.println("sendFrame"+preparedSendFrame+"ToChanel!!\n");
				waitACKNum.add(Integer.valueOf(preparedSendFrame));
				Timer t = new Timer();
				TimerTask task = new TimerTask(){
					public void run() {
						// 到点就认为帧丢失，重传,启动Go Back N协议
						if(waitACKNum.size()>0){
						GO_BACK_N(waitACKNum.get(0));	
						}				
					}		
				};
				t.schedule(task, (long)12000);
				frameTimers.add(t);
				//将发送的帧号加入等待
				
				preparedSendFrame =  (preparedSendFrame+1)%totalFrameNum;
				presentWinSize--;
				
				//如果传输了结束帧
				if(f.isEndFlag()){
					List<Timer> removeTimer = new ArrayList<Timer>();
					for(Timer rt:frameTimers){
						rt.cancel();
						removeTimer.add(rt);
					}
					frameTimers.removeAll(removeTimer);
					System.out.println("发送结束帧！！");	
					endOfFile = true;
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				System.out.println("当前准备发送的帧序号是："+preparedSendFrame+" "+" 当前的窗口大小为："+presentWinSize);
				
				
				break;
			}
		}
	
	}



	private void AfterGetingNak(int numOfFrame) {
		// 得到NAK应答以后，实现Go-Back-N协议
		GO_BACK_N(numOfFrame);
		
	}



	private synchronized void GO_BACK_N(int numOfFrame) {
		//当前窗口大小增大到preparedSendFrame == numOfFrame时候的状态
		System.out.println("重传！！！"+numOfFrame);
		if( preparedSendFrame-numOfFrame<0){
			presentWinSize +=preparedSendFrame-numOfFrame+totalFrameNum;
		}
		else{
			presentWinSize +=preparedSendFrame-numOfFrame;
		}
		
		//将waitACKNum链表中的相应内容恢复和计时器删除
		int count = 0;
		List<Integer> removeWaitACK = new ArrayList<Integer>();
		//List<Frame> removeFrameInWin = new ArrayList<Frame>();
		List<Timer> removeTimer = new ArrayList<Timer>();
		for(Integer num:waitACKNum){
			if(num.intValue()==numOfFrame){
				count++;
				//如果落在待接收应答的范围内
				for(int i=waitACKNum.size()-1;i>=count-1;i--){
					//删除在此之后所有的待接收应答的序号，包括退回的序号为numOfFrame的帧
					removeWaitACK.add(waitACKNum.get(i));
					//删除对应的计时器
					frameTimers.get(i).cancel();
					removeTimer.add(frameTimers.get(i));
					
				}
				break;
			}
		}
		waitACKNum.removeAll(removeWaitACK);
		frameTimers.removeAll(removeTimer);
		// 退回到preparedSendFrame == numOfFrame时候
		preparedSendFrame =numOfFrame;
	
		
		
		
		
		
	}



	private boolean AfterGetingAck(int numOfFrame) {
		//得到ACK应答之后，看是否是在等待接收应答的范围内
		int count = 0;
		//ACK序号代表的是希望接收的下一个帧的序号
		numOfFrame-=1;
		if(numOfFrame<0){
			numOfFrame+=totalFrameNum;
		}
		for(Integer num:waitACKNum){
			count++;
			if(num.intValue()==numOfFrame){
				for(int i = count-1;i>=0;i--){
					//删除在此之前所有的待接收应答的序号
					waitACKNum.remove(i);
					//删除对应的计时器
					frameTimers.get(i).cancel();
					frameTimers.remove(i);	
					//删除对应的帧
					frameWindow.remove(i);
				}
				//如果落在待接收应答的范围内
				/*for(int i = count-1;i>=0;i--){
					//删除在此之前所有的待接收应答的序号
					waitACKNum.remove(i);
					//删除对应的计时器
					frameTimers.get(i).cancel();
					frameTimers.remove(i);	
					//删除对应的帧
					frameWindow.remove(i);
				}*/
				
				
				//增加窗口大小，将待发送的帧添加进窗口中直至窗口变满,增加count个
				try {
					addFrameToWindow(count);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//解除循环,正确得到了ACK
				
				return true;
			}
		}
		//如果不在等待接收应答的范围内,丢弃
		return false;
		
	}



	private int addFrameToWindow(int numOfFrame) throws FileNotFoundException {
		// 将帧按照指定的个数加进窗口内
		Frame f = null;
		
		int i;
		//当添加了帧数之后presentWinSize不大于设定的winodowSize才可以
		for(i = 0;i<numOfFrame&&presentWinSize<=windowSize;i++){
			if((f = addFrame())!=null){
			frameWindow.add(f);
			presentWinSize++;
			}
			if(f.isEndFlag()){
				break;
			}
		}
		//加入进窗口成功的个数.
		return i;
	}

	private Frame addFrame() throws FileNotFoundException {
		//返回一个帧
		Frame f= new Frame(sendOrReceive,preparedAddFrame);
		//新建56个字节的数据部分
		preparedAddFrame = (preparedAddFrame+1)%totalFrameNum;
		byte[] data = new byte[56];
		//成功读取的字节数
		int readLen;
		//从文件中读入
		
		try {
			//is.skip(fileLoc);
			readLen=is.read(data, 0, 56);
			//fileLoc+=readLen;
			//System.out.println(Integer.toString((int)fileLoc));
			if(readLen==-1){
				f.setEndFlag(true);
				System.out.println("End-ing");
			}
			else{
				f.setData(data);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return f;
	}

	public int getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	public int getSendOrReceive() {
		return sendOrReceive;
	}

	public void setSendOrReceive(int sendOrReceive) {
		this.sendOrReceive = sendOrReceive;
	}
	public int getPresentWinSize() {
		return presentWinSize;
	}

	public void setPresentWinSize(int presentWinSize) {
		this.presentWinSize = presentWinSize;
	}
	public int getPreparedSendFrame() {
		return preparedSendFrame;
	}

	public void setPreparedSendFrame(int preparedSendFrame) {
		this.preparedSendFrame = preparedSendFrame;
	}

	public int getPreparedAddFrame() {
		return preparedAddFrame;
	}

	public void setPreparedAddFrame(int preparedAddFrame) {
		this.preparedAddFrame = preparedAddFrame;
	}
	
	public synchronized void threadSleep(){
		try {
			System.out.println("线程进入等待~！");
			this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Integer> getWaitACKNum() {
		return waitACKNum;
	}
	public void setWaitACKNum(List<Integer> waitACKNum) {
		this.waitACKNum = waitACKNum;
	}
	public boolean isEndOfFile() {
		return endOfFile;
	}
	public void setEndOfFile(boolean endOfFile) {
		this.endOfFile = endOfFile;
	}
	public int getExpectedFrame() {
		return expectedFrame;
	}
	public void setExpectedFrame(int expectedFrame) {
		this.expectedFrame = expectedFrame;
	}
	public List<Frame> getFrameWindow() {
		return frameWindow;
	}
	public void setFrameWindow(List<Frame> frameWindow) {
		this.frameWindow = frameWindow;
	}
}
