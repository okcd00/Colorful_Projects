package slindingwindow;



public class testMain {


	public static void main(String[] args) {
		Chanel chanel = new Chanel();
		PC sender = new PC(5,8,"D:\\test.txt",chanel);
		PC receiver = new PC(8,"D:\\receive.txt",chanel);
		chanel.start();
		sender.start();
		receiver.start();
	
		}

}
