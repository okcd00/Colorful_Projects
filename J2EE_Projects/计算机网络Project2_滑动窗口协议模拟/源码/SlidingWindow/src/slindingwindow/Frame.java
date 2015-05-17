package slindingwindow;

public class Frame {
	//0为发送端，1为接收端
	private int markOfPc;
	
	//帧序号，在发送端这，是帧包序号，在ack中是期望收到的帧，在nak中是未成功接收的帧
	private int numOfFrame;
	
	//0是ack，1为nak（markOfPc==1时有效）
	private int ackOrNak = 0;
	
	//是帧头错误还是数据错误（markOfPc==1&&ackOrNak==1时有效），突然发现NAK貌似只能是数据错误
	//private int wrongDataOrHead = 0;
	
	//数据帧，最多56个字节，最少1个字节，每个字节有一个校验位
	private byte[] data;
	private boolean endFlag;
	
	public Frame(int markOfPc, int numOfFrame) {
		super();
		this.markOfPc = markOfPc;
		this.numOfFrame = numOfFrame;
		endFlag = false;
	}
	public Frame(int markOfPc, int numOfFrame, int ackOrNak) {
		super();
		this.markOfPc = markOfPc;
		this.numOfFrame = numOfFrame;
		this.ackOrNak = ackOrNak;
		endFlag = false;
	}
	public Frame(int markOfPc, int numOfFrame, int ackOrNak, int wrongDataOrHead) {
		super();
		this.markOfPc = markOfPc;
		this.numOfFrame = numOfFrame;
		this.ackOrNak = ackOrNak;
		endFlag = false;
		//this.wrongDataOrHead = wrongDataOrHead;
	}
	public Frame(int markOfPc, int numOfFrame, int ackOrNak,
			int wrongDataOrHead, byte[] data) {
		super();
		this.markOfPc = markOfPc;
		this.numOfFrame = numOfFrame;
		this.ackOrNak = ackOrNak;
		endFlag = false;
		
		//this.wrongDataOrHead = wrongDataOrHead;
		this.data = data;
	}
	public int getMarkOfPc() {
		return markOfPc;
	}

	public boolean isEndFlag() {
		return endFlag;
	}
	public void setEndFlag(boolean endFlag) {
		this.endFlag = endFlag;
	}
	public void setMarkOfPc(int markOfPc) {
		this.markOfPc = markOfPc;
	}

	public int getNumOfFrame() {
		return numOfFrame;
	}

	public void setNumOfFrame(int numOfFrame) {
		this.numOfFrame = numOfFrame;
	}

	public int getAckOrNak() {
		return ackOrNak;
	}

	public void setAckOrNak(int ackOrNak) {
		this.ackOrNak = ackOrNak;
	}

	/*public int getWrongDataOrHead() {
		return wrongDataOrHead;
	}

	public void setWrongDataOrHead(int wrongDataOrHead) {
		this.wrongDataOrHead = wrongDataOrHead;
	}*/

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		//this.data = data;
		byte[] tempData = data;
		//计算校验和每7位+1位校验和
		this.data = countCheckBit(tempData);
	}
	
	private byte[] countCheckBit(byte[] tempData) {
		//计算校验和并返回一个已经计算好校验和的64个字节的数据
		
		return tempData;//测试用，先不计算校验和
	}
	
	
	
	
}
