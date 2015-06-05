import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;



public class RfidConnection {
	
	int retCode;//判断成功与否状态的全局变量
	boolean connActive, validATS; 
	static String VALIDCHARS = "0123456789";
	static String VALIDCHARSHEX = "ABCDEFabcdef0123456789";
	Timer timer;
	
	int [] PrefProtocols = new int[1]; 
	int [] ATRLen = new int[1]; 
	int [] hContext = new int[1]; 
	int [] cchReaders = new int[1];
	int [] hCard = new int[1];
	int [] RecvLen = new int[1];
	int SendLen = 0;
	int [] nBytesRet =new int[1];
	byte [] SendBuff = new byte[262];//全局数组，存发送指令
	byte [] RecvBuff = new byte[262];//全局数组，存收到数据（应答）
	byte [] szReaders = new byte[1024];
	int reqType;
	
	ACSModule.SCARD_READERSTATE rdrState = new ACSModule.SCARD_READERSTATE();
	
	static JacspcscLoader jacs = new JacspcscLoader();

	//初始化 获取智能卡读卡器并连接
	public int initRfidConnection()
	{
//		获取智能卡读卡器列表
		retCode = jacs.jSCardEstablishContext(ACSModule.SCARD_SCOPE_USER, 0, 0, hContext);
		System.out.println("Establish context and obtain hContext handle --- status: " + retCode);
		
		retCode = jacs.jSCardListReaders(hContext, 0, szReaders, cchReaders);//获取设备列表。szReaders数组存储设备名对应的ASCII编码，cchReaders是长度
		System.out.println("List PC/SC card readers installed in the system --- status:   " + retCode);
		java.util.List<String> readerList = new ArrayList<String>();
				
				
		int offset = 0;
		for (int i = 0; i < cchReaders[0]-1; i++)
		{
			
		  	if (szReaders[i] == 0x00)//设备名用0x00隔开
			{			  		
		  		readerList.add(new String(szReaders, offset, i - offset));
				 offset = i+1;
				  		
			}
		}
				
		if (readerList.size() == 0)
		{
			System.out.println("no canner");
		}else{
			System.out.println("find scanner : " + readerList.get(0));
		}
				
				
		//连接
				
		String rdrcon =readerList.get(0);//获取设备名
		retCode = jacs.jSCardConnect(hContext, 
						rdrcon, 
						ACSModule.SCARD_SHARE_SHARED,
						ACSModule.SCARD_PROTOCOL_T1 | ACSModule.SCARD_PROTOCOL_T0,
						hCard, 
						PrefProtocols);
		if (retCode != ACSModule.SCARD_S_SUCCESS)
		{
			System.out.println("connection failed : " + retCode);
			return retCode;
				    
		} 
		else 
		{	      	      
			System.out.println("Successful connection to: " + rdrcon);
				      	
		}
		
		System.out.println("\n");
		return retCode;
	}
	public int writeIntoRfidConnection(Integer id,byte blockNum)//写卡
	{ //认证
		
		SendBuff[0] = (byte)0xFF;
		SendBuff[1] = (byte)0x86;
		SendBuff[2] = (byte)0x00;
		SendBuff[3] = (byte)0x00;
		SendBuff[4] = (byte)0x05;
		SendBuff[5] = (byte)0x01;
		SendBuff[6] = (byte)0x00;
		SendBuff[7] = (byte)blockNum;
		SendBuff[8] = (byte)0x60;
		SendBuff[9] = (byte)0x01;
		SendLen = 10; 
		RecvLen[0] = 2;
		retCode = sendAPDUandDisplay();
		
		int amt;
		amt = id;
		clearBuffers();
		//写卡
		SendBuff[0] = (byte)0xFF;
		SendBuff[1] = (byte)0xD7;
		SendBuff[2] = (byte)0x00;
		SendBuff[3] = (byte)blockNum;
		SendBuff[4] = (byte)0x05;
		SendBuff[5] = (byte)0x00;
		SendBuff[6] = (byte) ((amt >> 24) & 0xFF);//右移24位 
		SendBuff[7] = (byte) ((amt >> 16) & 0xFF);
		SendBuff[8] = (byte) ((amt >> 8) & 0xFF);//右移8位（1000/256=3 ∴03）
		SendBuff[9]=(byte)(amt & 0xFF);//直接取后两位
		
		SendLen = 10; 
		RecvLen[0] = 2;
		
		retCode = sendAPDUandDisplay();
		return retCode;
	}

	public String readFromRfidConnection(byte blockNum)//读卡
	{
		//认证
		
		SendBuff[0] = (byte)0xFF;
		SendBuff[1] = (byte)0x86;
		SendBuff[2] = (byte)0x00;
		SendBuff[3] = (byte)0x00;
		SendBuff[4] = (byte)0x05;
		SendBuff[5] = (byte)0x01;
		SendBuff[6] = (byte)0x00;
		SendBuff[7] = (byte)blockNum;
		SendBuff[8] = (byte)0x60;
		SendBuff[9] = (byte)0x01;
		SendLen = 10; 
		RecvLen[0] = 2;
		retCode = sendAPDUandDisplay();//发送指令
//		if(retCode != ACSModule.SCARD_S_SUCCESS)
		
		
	 	clearBuffers();
	 	
	 	//读卡
		SendBuff[0] = (byte)0xFF;
		SendBuff[1] = (byte)0xB1;
		SendBuff[2] = (byte)0x00;
		SendBuff[3] = (byte)blockNum;
		SendBuff[4] = (byte)0x04;
		int amt;
		String tmpStr2="", tmpHex2="";
		SendLen = 5;
		RecvLen[0] = 16 + 2;
		
		retCode = sendAPDUandDisplay();
		if(retCode != ACSModule.SCARD_S_SUCCESS)
			return "";
		else
		{
			
			
			for(int i =RecvLen[0]-2; i<RecvLen[0]; i++)
			{
				
				tmpHex2 = Integer.toHexString(((Byte)RecvBuff[i]).intValue() & 0xFF).toUpperCase();//把数组RecvBuff中十进制数转化为十六进制字符串
				
				//For single character hex
				if (tmpHex2.length() == 1) 
					tmpHex2 = "0" + tmpHex2;
				
				tmpStr2 += " " + tmpHex2; 
				
			}
			
			//check for response
			if(tmpStr2.trim().equals("90 00"))
			{
				tmpStr2 = "";
				for(int i =0; i<RecvLen[0]-1; i++)
					if(RecvBuff[i] <= 0)
						tmpStr2 = tmpStr2 + " ";
					else
						tmpStr2 = tmpStr2 + (char)RecvBuff[i];
					
				System.out.println("Read Block Success!");
			}
			else{
				System.out.println("Read Block Error! :" + retCode);
				return "";
			}
			System.out.println("\n");
		}
		return byteArrayToInt(RecvBuff, 0).toString();//把返回的Byte数组RecvBuff转化为int，即十六进制转化为十进制
	}
	
	private Integer byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for(int i = 0;i < 4;i++){
			int shift = (4-1-i)*8;
			value += (b[i+offset]&0x000000FF)<<shift;
		}
		return value;
	}
	
	public RfidConnection()
	{	
		
	}
	
	public int sendAPDUandDisplay()
	{
		
		ACSModule.SCARD_IO_REQUEST IO_REQ = new ACSModule.SCARD_IO_REQUEST(); 
		ACSModule.SCARD_IO_REQUEST IO_REQ_Recv = new ACSModule.SCARD_IO_REQUEST(); 
		IO_REQ.dwProtocol = PrefProtocols[0];
		IO_REQ.cbPciLength = 8;
		IO_REQ_Recv.dwProtocol = PrefProtocols[0];
		IO_REQ_Recv.cbPciLength = 8;
		
		String tmpStr = "", tmpHex="";
		
		for(int i =0; i<SendLen; i++)
		{
			
			tmpHex = Integer.toHexString(((Byte)SendBuff[i]).intValue() & 0xFF).toUpperCase();//把数组RecvBuff中十进制数转化为十六进制字符串
			//JOptionPane.showMessageDialog(this, SendBuff[4]);
			//For single character hex
			if (tmpHex.length() == 1) 
				tmpHex = "0" + tmpHex;
			
			tmpStr += " " + tmpHex;  
			
		}
		
//		displayOut(2, 0, tmpStr);
		System.out.println("Send:" +  tmpStr);
		retCode = jacs.jSCardTransmit(hCard, 
									 IO_REQ, 
									 SendBuff, 
									 SendLen, 
									 null, 
									 RecvBuff, 
									 RecvLen);//jSCardTransmit发送指令
				
		if (retCode != ACSModule.SCARD_S_SUCCESS)
		{
			
//			displayOut(1, retCode, "");
			System.out.println("retCode != ACSModule.SCARD_S_SUCCESS :" +  retCode);
			return retCode;
			
		}
	
			tmpStr="";
			
			
				for(int i =0; i<RecvLen[0]; i++)
				{
					
					tmpHex = Integer.toHexString(((Byte)RecvBuff[i]).intValue() & 0xFF).toUpperCase();
					
					//For single character hex
					if (tmpHex.length() == 1) 
						tmpHex = "0" + tmpHex;
					
					tmpStr += " " + tmpHex;  
					
				}

//		displayOut(4, 0, tmpStr);
		System.out.println("Revieve:" +  tmpStr);
		return retCode;
	}
	
	public void clearBuffers()
	{
		
		for(int i=0; i<262; i++)
		{
			
			SendBuff[i]=(byte) 0x00;
			RecvBuff[i]= (byte) 0x00;
			
		}
		
	}
}
