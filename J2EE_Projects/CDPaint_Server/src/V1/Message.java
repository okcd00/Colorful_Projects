package V1;

import java.io.Serializable;

public class Message implements Serializable{
	// 0表示画形状，1表示撤销形状，2表示格式，3表示打开的文件路径，4表示发答案，5表示玩家形象：画者P 1，猜者C 2
	int count = 0;
	Shape sp = null;
	String str = null;

	/**
	 * count为0画形状，1撤销
	 * 
	 * @param count
	 * @param sp
	 */
	public Message(int count, Shape sp) {
		this.count = count;
		this.sp = sp;
	}

	/**
	 * count为2为定义保存格式，3文件打开路径，4发答案
	 * 
	 * @param count
	 * @param answer
	 */
	public Message(int count, String str) {
		this.count = count;
		this.str = str;
	}
}
