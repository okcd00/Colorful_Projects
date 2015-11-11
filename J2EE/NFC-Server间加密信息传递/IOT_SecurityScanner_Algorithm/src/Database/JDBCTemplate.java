package Database;

import java.sql.DriverManager;
import java.sql.Connection;

	abstract public class JDBCTemplate<T> {
		
		private String driverClass="com.mysql.jdbc.Driver";
		private String jdbcURL="jdbc:mysql://localhost/Gidb?useUnicode=true&characterEncoding=UTF-8";
		private String user="root";
		private String pwd="yanran0327";
		abstract public T execute() throws Exception;
		
		protected Connection getConnection()throws Exception {
				Class.forName(driverClass);
				Connection conn=DriverManager.getConnection(jdbcURL, user, pwd);
				return conn;
			
		}
	}


