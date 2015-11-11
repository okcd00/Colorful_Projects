package com.example.sensor_app;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
	private Button butt3 = null; 
	private TextView text1 = null;
	String value = null;
	private MyTask mTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         
         butt3 = (Button) this.findViewById(R.id.button1);
         text1 = (TextView) this.findViewById(R.id.text1);
         
         butt3.setOnClickListener(new Button.OnClickListener()
     	{
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				mTask = new MyTask();
 				mTask.execute("http://192.168.191.1:8080/SensorApp/COMServlet?op=getValue");
 				butt3.setEnabled(false);
 			}			
     	});
    
    }
    
    private class MyTask extends AsyncTask<String,Integer,String>{
    	
    	
		@Override
		protected void onPreExecute() {
			//Log.i(TAG,"onPreExecute() called");
			text1.setText("loading");
			
		}

		@Override
		protected String doInBackground(String... params) {
			//Log.i(, "doInBackGround()call");
			
			try {  
                HttpClient client = new DefaultHttpClient();  
                HttpGet get = new HttpGet(params[0]);  
                HttpResponse response = null;
              
                while(true){
                	response = client.execute(get); 
	                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
	                    HttpEntity entity = response.getEntity();  
	                    InputStream is = entity.getContent();  
	                    long total = entity.getContentLength();  
	                    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	                    byte[] buf = new byte[1024];  
	                    int count = 0;  
	                    int length = -1;  
	                    while ((length = is.read(buf)) != -1) {  
	                        baos.write(buf, 0, length);  
	                        count += length;  
	                        //调用publishProgress公布进度,最后onProgressUpdate方法将被执行  
	                       // publishProgress((int) ((count / (float) total) * 100));  
	                        //为了演示进度,休眠500毫秒 
	                       
	                        
	                    }  
	                    //更新当前值
	                    value = new String(baos.toByteArray(), "gb2312");
	                    //调用onProgressUpdate(Integer... values)
	                    publishProgress((int) ((count / (float) total) * 100));
	                   //一秒后继续更新
	                    Thread.sleep(1000); 
	                    //return new String(baos.toByteArray(), "gb2312");  
	                }
	                /*Thread.sleep(1000);  
	                i++;
	                publishProgress(i,5);*/
                }  
            } catch (Exception e) {  
                //Log.e(TAG, e.getMessage());  
            }  
            return null;  
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			text1.setText(value);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			text1.setText(result); 
		}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
