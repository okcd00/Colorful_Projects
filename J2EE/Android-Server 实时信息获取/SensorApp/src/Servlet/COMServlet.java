package Servlet;  
  
import java.io.IOException;  
import java.io.PrintWriter;  
  
import javax.servlet.ServletException;  
import javax.servlet.annotation.WebServlet;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
import Method.ReturnValue;  
  
//import com.sun.org.apache.xml.internal.serialize.Printer;  
  
/** 
 * Servlet implementation class COMServlet 
 */  
@WebServlet("/COMServlet")  
public class COMServlet extends HttpServlet {  
    private static final long serialVersionUID = 1L;  
         
    /** 
     * @see HttpServlet#HttpServlet() 
     */  
    public COMServlet() {  
        super();  
    }  
  
    /** 
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response) 
     */  
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
        //url形式：http://xxx.xxx.xxx.xxx:8080/项目名称/COMServlet?op=getValue  
        //8080端口是不一定的 得看看你电脑是不是用这个作为http协议的端口 一般是这个啦  
        String op = request.getParameter("op");  
        if(op!=null){  
            switch(op){  
            case "getValue":  
                sendValue(request,response);  
            }  
        }  
    }  
  
      
    private void sendValue(HttpServletRequest request,  
            HttpServletResponse response) {  
        response.setContentType("text/html;charset=utf-8");  
        response.setCharacterEncoding("UTF-8");  
        PrintWriter writer;  
          
        //获得系统中的值  
        ReturnValue r = new ReturnValue();  
          
        try {  
            writer = response.getWriter();  
            writer.println(r.getValue());  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response) 
     */  
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
        // TODO Auto-generated method stub  
    }  
  
}  