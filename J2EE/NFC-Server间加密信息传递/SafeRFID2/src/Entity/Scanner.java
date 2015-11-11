package Entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import Method.AES;

public class Scanner {
	private String IDFromCard;				//从卡内读取到的ID信息（被AES加密）
	private String keyForIDEcrypt;			//用于解密ID信息的AES密钥
	private String subKey;					//用于hash函数的子密钥 rootKey生成
	private String serialNumber;			//scanner的序列号
	private String time;					//存储当前系统时间	
	private String rootKey;					//根密钥，用于生成子密钥
	private String ID;						//解密后的ID信息
	private String hash;					//生成的hash值，用于发送给server端
	
	public Scanner(){
		IDFromCard = null;
		serialNumber = null;
		hash = null;
		keyForIDEcrypt = "123456";
	}
	
	/**
	 * 得到由卡内信息和读卡器Scanner的序列号而生成的hash值
	 * @param IDFromCard
	 * @param serialNumber
	 * @return hash值
	 */
	public String getHashCode(String IDFromCard,String serialNumber){
		//获得卡内信息和Scanner序列号
		this.IDFromCard = IDFromCard;
		this.serialNumber = serialNumber;
		
		//解密卡内的ID信息
		AES aes = new AES();
		aes.setKeyValue(keyForIDEcrypt);
		try {
			ID = aes.decrypt(IDFromCard);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		//获得系统时间
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmm");
		time = format.format(new Date());
				
		//生成子密钥
		generateSubkey();
		
		//得到Hash值
		return hash = Method.MD5.GetMD5(ID+subKey+time);
	}
	
	/**
	 * 生成本次用于hash加密的子密钥
	 */
	private void generateSubkey(){
		//生成子密钥
		int pwd = Method.DynamicPassword.getPassword(rootKey,serialNumber, time);
		
		//当动态口令小于6位数时应该补零
		subKey=Integer.toString(pwd);
		while(subKey.length()<6){
			subKey="0"+subKey;
		}
		
	}
	
	
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getHash() {
		return hash;
	}

	public void setRootKey(String rootKey) {
		this.rootKey = rootKey;
	}

	public String getSerialNumber() {
		return serialNumber;
	}


	public String getIDFromCard() {
		return IDFromCard;
	}

	public void setKeyForIDEcrypt(String keyForIDEcrypt){
		this.keyForIDEcrypt = keyForIDEcrypt;
	}
	
	public String getKeyForIDEcrypt() {
		return keyForIDEcrypt;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
