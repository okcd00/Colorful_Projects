
import Entity.Scanner;
import Entity.Server;
import Entity.UserInfo;



public class ControlPanel {
	
	RfidConnection connection;
	Server server;
	Scanner scanner;
	String IDFromCard;
	String hashCode;
	public ControlPanel(){
		server = new Server();
		server.setKeyForUserInfo("Security");
		server.setKeyForScannerInfo("Safety");
		
		scanner = new Scanner();
		scanner.setKeyForIDEcrypt("ytiruceS");
		init1();
		
		connection = new RfidConnection();
		while(connection.initRfidConnection()!=ACSModule.SCARD_S_SUCCESS)
		{
			System.out.println("");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		IDFromCard = null;
	}
	
	public  void init1(){
		//设置默认读写器，在数据库中存在
		scanner.setSerialNumber("first");
		scanner.setRootKey("01070504");
	}
	
	public void init2(){
		//设置读写器 在数据库中不存在
		scanner.setSerialNumber("123456");
		scanner.setRootKey("second");
	}
	
	public void init(String RID,String rootKey){
		scanner.setSerialNumber(RID);
		scanner.setRootKey(rootKey);
	}
	
	public void start(){
		//ID信息由卡传到scanner里 
		IDFromCard = readIDInfo();
		
		//得到由scanner生成的散列值
		hashCode = scanner.getHashCode(IDFromCard, scanner.getSerialNumber());
		
		//hashCode由scanner传到server端
		//得到用户信息
		UserInfo uInfo = server.getUserInfo(hashCode, scanner.getSerialNumber());
		if(uInfo==null){
			System.out.println("无法获得用户信息:\n\t1.用户可能不存在\n\t2.使用了非授权的读写器\n ");
			return;
			}
		
		//输出用户信息
		System.out.println("您的用户信息如下：");
		System.out.println("\t学号："+uInfo.getID()+"\n\t名字："+uInfo.getName()+"\n\t性别："+uInfo.getGender()+"\n\t金额："+uInfo.getBalance());
		
	}
	
	public String readIDInfo(){
		String[] blocks = new String[6];
		
		blocks[0] = intToString(Integer.parseInt(connection.readFromRfidConnection((byte)0x24)));
		blocks[1] = intToString(Integer.parseInt(connection.readFromRfidConnection((byte)0x28)));
		blocks[2] = intToString(Integer.parseInt(connection.readFromRfidConnection((byte)0x36)));
		blocks[3] = intToString(Integer.parseInt(connection.readFromRfidConnection((byte)0x2C)));
		blocks[4] = intToString(Integer.parseInt(connection.readFromRfidConnection((byte)0x18)));
		blocks[5] = intToString(Integer.parseInt(connection.readFromRfidConnection((byte)0x19)));
		
		String result = blocks[0];
		for(int i = 1;i < 6;i++){
			result += blocks[i];
		}
		
		return result;
	}
	
	public Integer byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for(int i = 0;i < 4;i++){
			int shift = (4-1-i)*8;
			value += (b[i+offset]&0x000000FF)<<shift;
		}
		return value;
	}
	
	public  byte[] intToByteArray(int i){
		byte[] result = new byte[4];
		result[0] = (byte)((i>>24)&0xFF);
		result[1] = (byte)((i>>16)&0xFF);
		result[2] = (byte)((i>>8)&0xFF);
		result[3] = (byte)((i)&0xFF);
		return result;
	}
	
	public  String intToString(int b){
		byte[] byteResult = intToByteArray(b);
		String result =  String.valueOf((char)byteResult[0]) ;
		for(int i = 1;i<4;i++){
			result += String.valueOf((char)byteResult[i]);
		}
		
		return result;
	}
	
	public void addNewScanner(String RID,String rootKey){
		server.addScannerInfo(RID, rootKey);
		System.out.println("\n添加成功！\n");
		System.out.println("您添加的读写器信息如下");
		System.out.println("序列号："+RID);
		System.out.println("根密钥"+rootKey);
	}
	
	public void addNewUser(String stuID,String name,String gender,String balance){
		server.addUserInfo(stuID,name, gender, balance);
		System.out.println("\n添加成功！\n");
		System.out.println("您添加的用户信息如下");
		System.out.println("学号："+stuID);
		System.out.println("姓名："+name);
		System.out.println("性别："+gender);
		System.out.println("金额："+balance);
	}
	
	public static void main(String[] args) {
		ControlPanel c = new ControlPanel();
		c.start();
			
	}

}
