package Entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Database.OpUserInfo;
import Method.AES;

public class Server {
	private String IDHash;				//从Scanner传来的Hash值，Hash输入为userID，subKey，Time
	private String serialNumber;		//Scanner的序列号
	private String time;				//存储当前的系统时间
	private String subKey[];			//存储子密钥序列：0为上一分钟 1为本分钟 2为下一分钟动态口令
	private String keyForUserInfo;		//用于解码user数据库中的信息需要的AES密钥
	private String keyForScannerInfo;	//用于解码scanner数据库中的根密钥
	private List<UserInfo> uInfoList;	//存储从数据库得到所用的用户信息
	
	public Server(){
		IDHash = null;
		serialNumber = null;
		subKey = new String[3];
		keyForUserInfo = "123456"; 		//初始化AES解密密钥
	}
	
	/**
	 * 查询用户信息
	 * @param IDHash
	 * @param serialNumber
	 * @return Hash值中包涵的用户ID所对应的用户信息
	 */
	public UserInfo getUserInfo(String IDHash, String serialNumber){
		//获得Scanner传来的Hash值和Scanner的序列号
		this.IDHash = IDHash;
		this.serialNumber = serialNumber;
		
		//获得系统时间
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmm");
		time = format.format(new Date());
		
		//获得子密钥数组
		generateSubkey();
		
		//得到所有的UserInfo
		try {
			uInfoList = Database.OpUserInfo.getAll();
		} catch (Exception e) {
			System.out.println("获得用户数据失败！");
			return null;
		}
		
		//设置AES加密解密器
		AES aes = new AES();
		aes.setKeyValue(keyForUserInfo);
		
		
		for(UserInfo i:uInfoList){
			String idDecrypt = null;
			try {
				idDecrypt = aes.decrypt(i.getID());
				for(int j=0;j<3;j++)
				{
					String hash = Method.MD5.GetMD5(idDecrypt+subKey[j]+time);
					if(hash.equals(IDHash))
					{
						//返回解码后的用户信息
						i.setID(idDecrypt);
						i.setName(aes.decrypt(i.getName()));
						i.setGender(aes.decrypt(i.getGender()));
						i.setBalance(aes.decrypt(i.getBalance()));
						return i;
					}
				}
				
			} catch (Exception e) {
				System.out.println("用户名解密失败");
				return null;
			}
			
		}
		return null;
	}
	
	/**
	 * 获得在time时间下的动态口令，口令保存在subKey的List数据结构中
	 */
	private void generateSubkey(){
		ScannerInfo sInfo;
		AES a = new AES();
		a.setKeyValue(keyForScannerInfo);
		try {
			 sInfo = Database.OpScannerInfo.get(serialNumber);
			 sInfo.setRootKey(a.decrypt(sInfo.getRootKey()));	//解码得scanner的根密钥
		} catch (Exception e) {
			System.out.println("找不到该Scanner的根密钥！");
			return ;
		}
		
		//生成子密钥数组（上一分钟，下一分钟）
		int cnt = 0;
		for(String i:Method.DynamicPassword.getPossiblePassword(sInfo.getRootKey(),serialNumber, time)){
			subKey[cnt++]=i;
		}
		
	}
	
	public boolean addUserInfo(String ID,String name,String gender,String balance){
		if(ID == null || name == null || gender == null || balance == null)return false;
		
		UserInfo uInfo = new UserInfo();
		AES a = new AES();
		
		a.setKeyValue(keyForUserInfo);
		try {
			uInfo.setID(a.encrypt(ID));
			uInfo.setName(a.encrypt(name));
			uInfo.setGender(a.encrypt(gender));
			uInfo.setBalance(a.encrypt(balance));
			OpUserInfo.add(uInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean deleteUserInfo(String ID){
		if(ID == null)return false;
		
		AES a = new AES();
		
		a.setKeyValue(keyForUserInfo);
		
		try {
			OpUserInfo.delete(a.encrypt(ID));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean updateUserInfo(String ID,String name,String gender,String balance){
		if(ID == null || name == null || gender == null || balance == null)return false;
		
		UserInfo uInfo = new UserInfo();
		AES a = new AES();
		
		a.setKeyValue(keyForUserInfo);
		
		try {
			uInfo.setID(a.encrypt(ID));
			uInfo.setName(a.encrypt(name));
			uInfo.setGender(a.encrypt(gender));
			uInfo.setBalance(a.encrypt(balance));
			OpUserInfo.update(uInfo, ID);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	public void setKeyForScannerInfo(String keyForScannerInfo){
		this.keyForScannerInfo = keyForScannerInfo;
	}
	
	public void setKeyForUserInfo(String keyForUserInfo) {
		this.keyForUserInfo = keyForUserInfo;
	}


	public String getIDHash() {
		return IDHash;
	}

	public void setIDHash(String iDHash) {
		IDHash = iDHash;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public static void main(String[] args) {
		

	}

}
