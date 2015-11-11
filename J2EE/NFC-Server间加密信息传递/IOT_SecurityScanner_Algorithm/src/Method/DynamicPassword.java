package Method;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DynamicPassword {
	
	/**
	 * 获得Scanner的子密钥
	 * @param psd Scanner对应的根密钥
	 * @param Num Scanner的序列号
	 * @param time 当前系统时间
	 * @return int数组，包括上一分钟的动态口令，这一分钟的动态口令，下一分钟的动态口令，
	 */
	public static String[] getPossiblePassword(String rootKey,String Num,String time){
		int password[] = {0,0,0};
		
		//获得当前系统时间
		/*SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmm");
		String time = format.format(new Date());*/
		String lastMinute = getLastMinute(time);
		String nextMinute = getNextMinute(time);
		password[0]=getPassword(rootKey,Num,lastMinute);
		password[1]=getPassword(rootKey, Num, time);
		password[2]=getPassword(rootKey, Num, nextMinute);
		//System.out.println(lastMinute+" "+time+" "+nextMinute);
		String zero = "0";
		String[] psdStr = new String[3];
		//得到三个口令的String类型
		for(int i = 0;i<3;i++){
			psdStr[i]=Integer.toString(password[i]);
			//当动态口令小于6位数时应该补零
			while(psdStr[i].length()<6){
				psdStr[i]=zero+psdStr[i];
			}
		}
		return psdStr;
	}
	
	public static int getPassword(String psd,String Num,String time){
		int password = 0;
		//将口令，序列号，时间作为输入得到MD5值
	    String md5Value = MD5.GetMD5(psd+Num+time);
	    
	    //将md5值选择三个字节出来
	    int minute = Integer.parseInt(time.substring(10, 12), 10);
	    String psdHex = md5Value.substring((minute%10)*2,(minute%10)*2+2)+
	    		md5Value.substring((minute%10+3)*2,(minute%10+3)*2+2)+
	    		md5Value.substring((minute%10+6)*2,(minute%10+6)*2+2);
	    
	    //将选择出来的三个字节十六进制的md5码转换成6位的十进制动态口令
	    password = Integer.parseInt(psdHex,16)%1000000;
	    
	    //返回6位动态口令
	   // System.out.println(password);
		return password;
	}
	public static String getLastMinute(String time){
		//输入yyyyMMddhhmm的字符串
		String minuteStr = time.substring(time.length()-2,time.length());
		String hourStr = time.substring(time.length()-4, time.length()-2);
		String dayStr = time.substring(time.length()-6,time.length()-4);
		String monthStr = time.substring(time.length()-8,time.length()-6);
		String yearStr = time.substring(0,4);
		
		//将字符串改成int型数字
		int minute = Integer.parseInt(minuteStr);
		int hour = Integer.parseInt(hourStr);
		int day = Integer.parseInt(dayStr);
		int month = Integer.parseInt(monthStr);
		int year = Integer.parseInt(yearStr);
		
		//得到上一分钟，注意临界时间的变化
		if(minute!=0){
			return yearStr+monthStr+dayStr+hourStr+(minute<=10?"0"+Integer.toString(minute-1):Integer.toString(minute-1));
		}
		if(minute==0&&hour!=0){
			return yearStr+monthStr+dayStr+(hour<=10?"0"+Integer.toString(hour-1):Integer.toString(hour-1))+"59";
			
		}
		if(minute==0&&hour==0&&day!=1){
			return yearStr+monthStr+(day<=10?"0"+Integer.toString(day-1):Integer.toString(day-1))+"23"+"59";
		}
		if(minute==0&&hour==0&&day==1&&month!=1){
			return yearStr+(month<=10?"0"+Integer.toString(month-1):Integer.toString(month-1))+lastDay(year, month)+"23"+"59";
		}
		
		return Integer.toString(year-1)+"12312359";
	}
	
	
	public static String getNextMinute(String time){
		//输入yyyyMMddhhmm的字符串
		String minuteStr = time.substring(time.length()-2,time.length());
		String hourStr = time.substring(time.length()-4, time.length()-2);
		String dayStr = time.substring(time.length()-6,time.length()-4);
		String monthStr = time.substring(time.length()-8,time.length()-6);
		String yearStr = time.substring(0,4);
		
		//将字符串改成int型数字
		int minute = Integer.parseInt(minuteStr);
		int hour = Integer.parseInt(hourStr);
		int day = Integer.parseInt(dayStr);
		int month = Integer.parseInt(monthStr);
		int year = Integer.parseInt(yearStr);
		
		//得到下一分钟，注意临界时间的变化
		if(minute!=59){
			return yearStr+monthStr+dayStr+hourStr+(minute<9?"0"+Integer.toString(minute+1):Integer.toString(minute+1));
		}
		if(minute==59&&hour!=23){
			return yearStr+monthStr+dayStr+(hour<9?"0"+Integer.toString(hour+1):Integer.toString(hour+1))+"00";
			
		}
		if(minute==59&&hour==23&&day!=lastDayOfThisMonth(year, month)){
			return yearStr+monthStr+(day<9?"0"+Integer.toString(day+1):Integer.toString(day+1))+"0000";
		}
		if(minute==59&&hour==23&&day==lastDayOfThisMonth(year, month)&&month!=12){
			return yearStr+(month<9?"0"+Integer.toString(month+1):Integer.toString(month+1))+"010000";
		}
		
		return Integer.toString(year+1)+"01010000";
	}
	
	public static int lastDayOfThisMonth(int year,int month){
		if(month==2){
			if(judgeYear(year)){
				//是闰年二月
				return 29;
			}else{
				//平年二月
				return 28;
			}
		}else if(month==4||month==6||month==9||month==11){
			return 30;
		}
		else 
			return 31;
	}

	public static String lastDay(int year,int month){
		if((month-1)==2){
			if(judgeYear(year)){
				//是闰年二月
				return "29";
			}else{
				//平年二月
				return "28";
			}
		}else if(month==2||month==4||month==6||month==8||month==9||month==11){
			return "31";
		}else{
			return "30";
		}
		
		
	}
	public static boolean judgeYear(int year){
		if(year%100==0){
			if(year%400==0){
				return true;
			}
		}else{
			if(year%4==0){
				return true;
			}
		}
		return false;
	}
	public static void main(String args[]){
		
		System.out.println(DynamicPassword.getNextMinute("201504302359"));
	}
}
