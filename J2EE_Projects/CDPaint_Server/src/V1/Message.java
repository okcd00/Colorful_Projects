package V1;

import java.io.Serializable;

public class Message implements Serializable{
	// 0��ʾ����״��1��ʾ������״��2��ʾ��ʽ��3��ʾ�򿪵��ļ�·����4��ʾ���𰸣�5��ʾ������󣺻���P 1������C 2
	int count = 0;
	Shape sp = null;
	String str = null;

	/**
	 * countΪ0����״��1����
	 * 
	 * @param count
	 * @param sp
	 */
	public Message(int count, Shape sp) {
		this.count = count;
		this.sp = sp;
	}

	/**
	 * countΪ2Ϊ���屣���ʽ��3�ļ���·����4����
	 * 
	 * @param count
	 * @param answer
	 */
	public Message(int count, String str) {
		this.count = count;
		this.str = str;
	}
}
