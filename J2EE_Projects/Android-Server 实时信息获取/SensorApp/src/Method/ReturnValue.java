package Method;  
  
import java.text.SimpleDateFormat;  
import java.util.Date;  
  
public class ReturnValue   
{  
    private String info = null;  
      
    public void setString(String t)  
    {  
        this.info=t;  
    }  
    public String getValue()  
    {  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
        this.info = sdf.format(new Date());  
        return info;  
    }  
}  