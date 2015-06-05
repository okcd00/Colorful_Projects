package Method;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 用来进行AES的加密和解密程序
 * 
 * @author Steven
 * 
 */
public class AES {

	// 加密算法
	private String ALGO;

	
	// 16位的加密密钥
	private byte[] keyValue;

	public AES(){
		ALGO = "AES";
		String key = "123456";
		keyValue = changeKey56(key);
	}
	/**
	 * 用来进行加密的操作
	 * 
	 * @param Data
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String Data) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encVal);
		return encryptedValue;
	}

	/**
	 * 用来进行解密的操作
	 * 
	 * @param encryptedData
	 * @return
	 * @throws Exception
	 */
	public String decrypt(String encryptedData) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	/**
	 * 根据密钥和算法生成Key
	 * 
	 * @return
	 * @throws Exception
	 */
	private Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGO);
		return key;
	}

	/**
	 * 将输入密钥转换为56位密钥
	 * @param keyValue
	 * @return
	 */
	private byte[] changeKey56(String keyValue){
		String keyHash = MD5.GetMD5(keyValue);
		return keyHash.substring(0, 16).getBytes();
	}
	
	public String getALGO() {
		return ALGO;
	}

	public void setALGO(String aLGO) {
		ALGO = aLGO;
	}

	public byte[] getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		
		this.keyValue = changeKey56(keyValue);
	}
	
	
	
	public static void main(String[] args){
		AES a = new AES();
		/*
		 * 加密示例程序
		 */
		/*String key = "abcdef";
		a.setKeyValue(key);	//设置AES密钥,若不设置，则为初始密钥123456
		String code = null;
		
		try {
			code = a.encrypt("gali");//加密文字信息
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		System.out.println(code);*/
		
		/*
		 * 解密实例程序
		 */
		/*String key = "abcdef";
		a.setKeyValue(key);	//设置AES密钥,若不设置，则为初始密钥123456
		String plaint = null;
		try {
			plaint = a.decrypt("W1nfbGxtskP9Bz33sUHcjg==");//解密文字信息
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(plaint);*/
		
	}
}