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
	 * ����hb��ʽͼƬ
	 * @param f �ļ�
	 * @param b ��״����
	 */
	public static void saveHb(File f,ArrayList<Shape> b){
		try{
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			size=b.size();
			oos.writeInt(size);//����size,�������´򿪳����֪��Ҫ�����ٸ�����,�����޷���ȡ����
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
	 * �����bmp�ķ���
	 * @param f bmp�ļ���ַ
	 */
public void write24bmp(File f){
		

		//BMP�ļ�ͷ
		int bfType1= 0x42; // λͼ�ļ������ͣ�����Ϊ ' B '' M '������ĸ (0-1�ֽ� ) �� 
		int bfType2= 0x4d; 
		
		int bfSize=14; // λͼ�ļ��Ĵ�С�����ֽ�Ϊ��λ (2-5 �ֽ� ) �� *********
		int bfReserved1=0; // λͼ�ļ������֣�����Ϊ 0(6-7 �ֽ� ) ���� 
		int  bfReserved2=0; // λͼ�ļ������֣�����Ϊ 0(8-9 �ֽ� ) �� 
		int  bfOffBits=54; // λͼ���ݵ���ʼλ�ã��������λͼ (10-13 �ֽ� ) 

		//Bmp��Ϣͷ
		int Size=40; // ���ṹ��ռ���ֽ��� (14-17 �ֽ� ) ��
		int image_width=PaintClient.colors[0].length; // λͼ�Ŀ�ȣ�������Ϊ��λ (18-21 �ֽ� ) ����
		int image_height=PaintClient.colors.length; // λͼ�ĸ߶ȣ�������Ϊ��λ (22-25 �ֽ� ) ��
		int Planes=1; // Ŀ���豸�ļ��𣬱���Ϊ 1(26-27 �ֽ� ) ����
		int biBitCount=24;// ÿ�����������λ���������� 1(˫ɫ),(28-29 �ֽ�) 4(16 ɫ ) �� 8(256 ɫ ) �� 24(// ���ɫ ) ֮һ����
		int biCompression=0; // λͼѹ�����ͣ������� 0( ��ѹ�� ),(30-33 �ֽ� ) 1(BI_RLE8 ѹ������ ) ��// 2(BI_RLE4 ѹ������ ) ֮һ����
		
		int SizeImage=height*width; // λͼ�Ĵ�С�����ֽ�Ϊ��λ (34-37 �ֽ� ) 
		int biXPelsPerMeter=0; // λͼˮƽ�ֱ��ʣ�ÿ�������� (38-41 �ֽ� ) ��0��Ĭ��ֵ��
		int biYPelsPerMeter=0; // λͼ��ֱ�ֱ��ʣ�ÿ�������� (42-45 �ֽ� ) ��0��Ĭ��ֵ
		int biClrUsed=0;// λͼʵ��ʹ�õ���ɫ���е���ɫ�� (46-49 �ֽ� ) ����     0˵��ȫ������
		int biClrImportant=0;// λͼ��ʾ��������Ҫ����ɫ�� (50-53 �ֽ� )    0˵��ȫ����Ҫ

		//�������,��byte������Щ���ԵĻ���Ȼ��һ��������Χ,����д���ݳ�ȥ��ʱ�򲻷�������ֽ���,����sizeռ4�ֽ�,Planesռ2�ֽ�,�����byte���廹Ҫ�ж���0
		//����ֱ����intȻ����תΪbyte[4],����write(byte[],��ʼλ��,����)��һ������ֱ��д��ȥ.
		
		//д��ȥ�ļ�ͷ��Ϣͷ
		
		try {
			 FileOutputStream fos = new FileOutputStream(f);
			 BufferedOutputStream bos = new BufferedOutputStream(fos); 

			 bos.write(bfType1);
			 bos.write(bfType2);
			 bos.write(int2byte(bfSize),0,4);
			 bos.write(int2byte(bfReserved1),0,2);
			 bos.write(int2byte(bfReserved2),0,2);
			 bos.write(int2byte(bfOffBits),0,4);
			 bos.write(int2byte(Size),0,4);// ������Ϣͷ���ݵ����ֽ���  
			 bos.write(int2byte(image_width),0,4);// ����λͼ�Ŀ�  
			 bos.write(int2byte(image_height),0,4);// ����λͼ�ĸ�
			 bos.write(int2byte(Planes),0,2);// ����λͼ��Ŀ���豸����
			 bos.write(int2byte(biBitCount),0,2);// ����ÿ������ռ�ݵ��ֽ���
			 bos.write(int2byte(biCompression),0,4);// ����λͼ��ѹ������
			 bos.write(int2byte(SizeImage),0,4);// ����λͼ��ʵ�ʴ�С  
			 bos.write(int2byte(biXPelsPerMeter),0,4);// ����λͼ��ˮƽ�ֱ���  
			 bos.write(int2byte(biYPelsPerMeter),0,4);// ����λͼ�Ĵ�ֱ�ֱ���  
			 bos.write(int2byte(biClrUsed),0,4);// ����λͼʹ�õ�����ɫ��
			 bos.write(int2byte(biClrImportant),0,4);// ����λͼʹ�ù�������Ҫ����ɫ��  
			 //24λû����ɫ��,���Խ����������ɫ��Ϣ.�����ǵ�int������byte[4]��b[0] b[1] b[2] ������Ϊblue green red���ȥ
			 // ���������ʱ��ע�⣬�ڼ�����ڴ���λͼ�����Ǵ����ң����µ���������ģ�  
			 // Ҳ����˵ʵ��ͼ��ĵ�һ�еĵ����ڴ������һ�� 
			 for (int h = image_height - 1; h >= 0; h--) { 
				 for (int w = 0; w < image_width; w++) { 
					 // ���ﻹ��Ҫע����ǣ�ÿ��������������RGB��ɫ������ɵģ�
					 // ��������windows����ϵͳ����С�˴洢���Զ��ֽ��������á�
					 byte b[]=new byte[4];
					 b= int2byte(PaintClient.colors[h][w]);
					 byte red = b[2];// �õ���ɫ����  
					 byte green = b[1];// �õ���ɫ����
					 byte blue = b[0];// �õ���ɫ���� 
					 
					 bos.write(blue);
					 bos.write(green);  
					 bos.write(red); 
				 } 
			 } 
			 //�ر����ݵĴ���  
			 bos.flush(); 
			 bos.close(); 
			 fos.close(); 
			 System.out.println("д��ɹ�!!!");
			 
			 
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
	 * ��ȡbmp�ķ���
	 * @param f
	 */
	public int[][] read24bmp(File f) {

		try {
			FileInputStream fis = new FileInputStream(f);
			BufferedInputStream bis = new BufferedInputStream(fis);
			try {
				if (bis.read() == 66 && bis.read() == 77) {
					bis.skip(16);// ��ȥ���ſ�ʼ18������ȡ��4��byteƴ��һ��int�͵Ŀ��
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
	 * ��ȡhb��ʽͼƬ
	 * @param f �ļ�
	 * @param b ��״����
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
			System.out.println("��ȡ�ϴ��Զ�������ļ���������.");
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
