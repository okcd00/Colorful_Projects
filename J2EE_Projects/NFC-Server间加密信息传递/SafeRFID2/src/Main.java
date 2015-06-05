import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;




public class Main {

	public static void main(String[] args) {
		ControlPanel c = new ControlPanel();
		//c.start();
		while(true){
			System.out.println("后台操作：\n\t（1-1）添加用户信息\n\t（1-2）添加读写器信息\n\n");
			System.out.println("情景模拟：\n\t（2-1）使用默认读写器1(服务器中存在)\n\t（2-2）使用默认读写器2(服务器中不存在)\n\t（2-3）自定义读写器\n");
			System.out.println("输入 q 退出程序\n");
			System.out.println("输入您想要的操作的序号：");
			System.out.print(">>");
			//BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			Scanner bf = new Scanner(System.in);
			String operation = null;
			operation = bf.nextLine();
			
			if(operation.equals("1-1")){
				//添加用户信息
				String stuID = null,name = null,gender = null,balance = null; 
				
				System.out.print("输入学号：\n>>");
				stuID = bf.nextLine();
				System.out.print("输入姓名：\n>>");
				name = bf.nextLine();
				System.out.print("输入性别(F/M)：\n>>");
				gender = bf.nextLine();
				System.out.print("输入金额：\n>>");
				balance = bf.nextLine();
				c.addNewUser(stuID, name, gender, balance);
				
			}else if(operation.equals("1-2")){
				String RID = null,rootKey = null;
				
				System.out.print("输入序列号：\n>>");
				RID = bf.nextLine();
				System.out.print("输入根密钥：\n>>");
				rootKey = bf.nextLine();
				//rootKey = new String(rootKey.getBytes("GB2312"),"UTF-8");
				c.addNewScanner(RID,rootKey);
			}else if(operation.equals("2-1")){
				c.init1();
				c.start();
			}else if(operation.equals("2-2")){
				c.init2();
				c.start();
			}else if(operation.equals("2-3")){
				String RID = null,rootKey = null;
				
				System.out.print("输入序列号：\n>>");
				RID = bf.nextLine();
				System.out.print("输入根密钥：\n>>");
				rootKey = bf.nextLine();
				c.init(RID,rootKey);
				c.start();
			}else if(operation.equals("q")){
				System.out.println("退出程序……");
				break;
			}else{
				System.out.println("你输入的指令有误！");
			
			}
			System.out.println("按任意键继续……");
			bf.nextLine();
			
		}
	}

}
