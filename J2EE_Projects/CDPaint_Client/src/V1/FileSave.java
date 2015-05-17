package V1;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileSave {
	static int height,width;
	static int size;
	/**
	 * 保存hb格式图片
	 * @param f 文件
	 * @param b 形状对象
	 */
	public static void saveHb(File f,ArrayList<Shape> b){
		try{
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			size=b.size();
			oos.writeInt(size);//保存size,避免重新打开程序后不知道要读多少个对象,导致无法读取数据
			for(int i=0;i<size;i++){
				oos.writeObject(b.get(i));
			}
			
			oos.flush();
			oos.close();
			fos.close();
			
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 保存成bmp的方法
	 * @param f bmp文件地址
	 */
public void write24bmp(File f){
		

		//BMP文件头
		int bfType1= 0x42; // 位图文件的类型，必须为 ' B '' M '两个字母 (0-1字节 ) 　 
		int bfType2= 0x4d; 
		
		int bfSize=14; // 位图文件的大小，以字节为单位 (2-5 字节 ) 　 *********
		int bfReserved1=0; // 位图文件保留字，必须为 0(6-7 字节 ) 　　 
		int  bfReserved2=0; // 位图文件保留字，必须为 0(8-9 字节 ) 　 
		int  bfOffBits=54; // 位图数据的起始位置，以相对于位图 (10-13 字节 ) 

		//Bmp信息头
		int Size=40; // 本结构所占用字节数 (14-17 字节 ) 　
		int image_width=PaintClient.colors[0].length; // 位图的宽度，以像素为单位 (18-21 字节 ) 　　
		int image_height=PaintClient.colors.length; // 位图的高度，以像素为单位 (22-25 字节 ) 　
		int Planes=1; // 目标设备的级别，必须为 1(26-27 字节 ) 　　
		int biBitCount=24;// 每个像素所需的位数，必须是 1(双色),(28-29 字节) 4(16 色 ) ， 8(256 色 ) 或 24(// 真彩色 ) 之一　　
		int biCompression=0; // 位图压缩类型，必须是 0( 不压缩 ),(30-33 字节 ) 1(BI_RLE8 压缩类型 ) 或// 2(BI_RLE4 压缩类型 ) 之一　　
		
		int SizeImage=height*width; // 位图的大小，以字节为单位 (34-37 字节 ) 
		int biXPelsPerMeter=0; // 位图水平分辨率，每米像素数 (38-41 字节 ) 　0是默认值　
		int biYPelsPerMeter=0; // 位图垂直分辨率，每米像素数 (42-45 字节 ) 　0是默认值
		int biClrUsed=0;// 位图实际使用的颜色表中的颜色数 (46-49 字节 ) 　　     0说明全部用了
		int biClrImportant=0;// 位图显示过程中重要的颜色数 (50-53 字节 )    0说明全部重要

		//个人理解,用byte定义这些属性的话虽然不一定超出范围,但是写数据出去的时候不方便控制字节数,例如size占4字节,Planes占2字节,如果用byte定义还要判断填0
		//不如直接用int然后再转为byte[4],再用write(byte[],开始位置,长度)这一个方法直接写出去.
		
		//写出去文件头信息头
		
		try {
			 FileOutputStream fos = new FileOutputStream(f);
			 BufferedOutputStream bos = new BufferedOutputStream(fos); 

			 bos.write(bfType1);
			 bos.write(bfType2);
			 bos.write(int2byte(bfSize),0,4);
			 bos.write(int2byte(bfReserved1),0,2);
			 bos.write(int2byte(bfReserved2),0,2);
			 bos.write(int2byte(bfOffBits),0,4);
			 bos.write(int2byte(Size),0,4);// 输入信息头数据的总字节数  
			 bos.write(int2byte(image_width),0,4);// 输入位图的宽  
			 bos.write(int2byte(image_height),0,4);// 输入位图的高
			 bos.write(int2byte(Planes),0,2);// 输入位图的目标设备级别
			 bos.write(int2byte(biBitCount),0,2);// 输入每个像素占据的字节数
			 bos.write(int2byte(biCompression),0,4);// 输入位图的压缩类型
			 bos.write(int2byte(SizeImage),0,4);// 输入位图的实际大小  
			 bos.write(int2byte(biXPelsPerMeter),0,4);// 输入位图的水平分辨率  
			 bos.write(int2byte(biYPelsPerMeter),0,4);// 输入位图的垂直分辨率  
			 bos.write(int2byte(biClrUsed),0,4);// 输入位图使用的总颜色数
			 bos.write(int2byte(biClrImportant),0,4);// 输入位图使用过程中重要的颜色数  
			 //24位没有颜色表,所以接下来输出颜色信息.将我们的int数组变成byte[4]后将b[0] b[1] b[2] 依次作为blue green red输出去
			 // 这里遍历的时候注意，在计算机内存中位图数据是从左到右，从下到上来保存的，  
			 // 也就是说实际图像的第一行的点在内存是最后一行 
			 for (int h = image_height - 1; h >= 0; h--) { 
				 for (int w = 0; w < image_width; w++) { 
					 // 这里还需要注意的是，每个像素是有三个RGB颜色分量组成的，
					 // 而数据在windows操作系统下是小端存储，对多字节数据有用。
					 byte b[]=new byte[4];
					 b= int2byte(PaintClient.colors[h][w]);
					 byte red = b[2];// 得到红色分量  
					 byte green = b[1];// 得到绿色分量
					 byte blue = b[0];// 得到蓝色分量 
					 
					 bos.write(blue);
					 bos.write(green);  
					 bos.write(red); 
				 } 
			 } 
			 //关闭数据的传输  
			 bos.flush(); 
			 bos.close(); 
			 fos.close(); 
			 System.out.println("写入成功!!!");
			 
			 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		 
		
		
		
	}
	
	public byte[] int2byte(int data){
//		10101010 10101010 10101010 00000000
//		00000000 00000000 00000000 10101010
		byte b4 = (byte)((data)>>24); 
		byte b3 = (byte)(((data)<<8)>>24); 
		byte b2= (byte)(((data)<<16)>>24); 
		byte b1 = (byte)(((data)<<24)>>24);  
		byte[] bytes = {b1,b2,b3,b4};
		//{BGRA}
		return bytes; 
	}
	
	public int byte2int(byte b[]) {
		int b3 = b[3] & 0xff;
		int b2 = b[2] & 0xff;
		int b1 = b[1] & 0xff;
		int b0 = b[0] & 0xff;
		int i = b3 << 24 | b2 << 16 | b1 << 8 | b0;
		return i;
	}
  

	
	/**
	 * 读取bmp的方法
	 * @param f
	 */
	public int[][] read24bmp(File f) {

		try {
			FileInputStream fis = new FileInputStream(f);
			BufferedInputStream bis = new BufferedInputStream(fis);
			try {
				if (bis.read() == 66 && bis.read() == 77) {
					bis.skip(16);// 先去除才开始18个，再取出4个byte拼成一个int型的宽度
					byte wi[] = new byte[4];
					bis.read(wi);
					width = byte2int(wi);
					byte he[] = new byte[4];
					bis.read(he);
					height = byte2int(he);
					int [][]data= new int[height][width];
					bis.skip(28);
					int skipNum=4-width*3%4;
					for (int h = height - 1; h >= 0; h--) {
						for (int w = 0; w < width; w++) {
							
							//
							int b = bis.read();
							int g = bis.read();
							int r = bis.read();
							Color c = new Color(r,g,b);
							data[h][w] = c.getRGB();
						}
						if (skipNum != 4) {
							bis.skip(skipNum);
						}


						
					}
					bis.close();
					fis.close();
					DrawUI.center.setPreferredSize(new Dimension(width,height));  
		            javax.swing.SwingUtilities.updateComponentTreeUI(DrawUI.center);
					return data;
					
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	/**
	 * 读取hb格式图片
	 * @param f 文件
	 * @param b 形状对象
	 */
	public static ArrayList readHb(File f){
		ArrayList<Shape> b=new ArrayList<Shape>();
		try{
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			size = ois.readInt();
			for(int i=0;i<size;i++){
					try {
						b.add((Shape) ois.readObject());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				
		
			}	
			ois.close();
			fis.close();
			return b;
		}catch(EOFException e){
			System.out.println("读取上次自动保存的文件发生错误.");
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
