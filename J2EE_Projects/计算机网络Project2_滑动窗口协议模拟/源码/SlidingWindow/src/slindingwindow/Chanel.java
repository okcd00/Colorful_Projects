package slindingwindow;

import slindingwindow.Problem.problemType;
import slindingwindow.State.stateType;

public class Chanel extends Thread{
	//数据到达接收端
	private boolean arriveReceiver;
	//数据到达发送端
	private boolean arriveSender;
	//信道中的帧数据，以及临时存放数据的frame
	private Frame frame,tempFrame;
	//信道的状态
	private stateType state = null;
	//信道的问题
	private problemType problem;
	//要发送话
	String str;
	public Chanel(){
		state = state.free;
		problem = problemType.noProblem;
		arriveReceiver = false;
		arriveSender = false;
	}
	
	
	@Override
	public void run() {
		super.run();
		while(true){
			if(this.getStateType() == stateType.busy){
				
				switch(problem){
				case noProblem:
					//根据报头来决定发送到哪一方
					frame = tempFrame;
					if(frame.getMarkOfPc()==0){
						sendToReceive();
						str = "信道中的来自 发送端 帧序号为:\t"+tempFrame.getNumOfFrame();
						System.out.println(str);
					}
					else if(frame.getMarkOfPc()==1){
						sendToSender();
						str = "信道中的来自 接收端 帧序号为:\t"+tempFrame.getNumOfFrame();
						System.out.println(str);
					}
					break;
				case wrong:
					makeFrameWrong();
					break;
				case lost:
					makeFrameLost();
					break;
				case delay:
					makeFrameDelay();
					break;
				default:
					break;
				}
			}
			else{
				//当信道空闲的额时候 代表当前信道中没有数据
				str = "当前信道中没有数据";
				System.out.println(str);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Thread.sleep(500);
	}


	public String getStr() {
		return str;
	}


	public void setStr(String str) {
		this.str = str;
	}


	private void makeFrameDelay() {
		// TODO Auto-generated method stub
		
	}


	private void makeFrameLost() {
		// TODO Auto-generated method stub
		
	}


	private void makeFrameWrong() {
		// TODO Auto-generated method stub
		
	}


	private synchronized  void sendToReceive() {
		// 将frame送入接收方,不可以将信道变为空闲，这个是PC端任务
		 this.setArriveReceiver(true);
		 this.setArriveSender(false);
		 str = "到达接收端!";
		 System.out.println("sentToReceive!!");
		
	}


	private synchronized  void sendToSender() {
		this.setArriveSender(true);
		this.setArriveReceiver(false);
		str = "到达发送端!";
		System.out.println("sendToSender!!");
	}


	public synchronized  boolean isArriveReceiver() {
		return arriveReceiver;
	}
	public synchronized  void setArriveReceiver(boolean arriveReceiver) {
		this.arriveReceiver = arriveReceiver;
	}
	public synchronized  boolean isArriveSender() {
		return arriveSender;
	}
	public synchronized  void setArriveSender(boolean arriveSender) {
		this.arriveSender = arriveSender;
	}
	public synchronized  Frame getFrame() {
		return frame;
	}
	public synchronized  void setFrame(Frame frame) {
		this.frame = frame;
	}
	public synchronized  Frame getTempFrame() {
		return tempFrame;
	}
	public synchronized  void setTempFrame(Frame tempFrame) {
		this.tempFrame = tempFrame;
	}
	public synchronized stateType getStateType(){
		return this.state;
	}
	public synchronized void setStateType(stateType state) {
		this.state = state;
	}
	public synchronized  problemType getProblem() {
		return problem;
	}
	public synchronized  void setProblem(problemType problem) {
		this.problem = problem;
	}
}
