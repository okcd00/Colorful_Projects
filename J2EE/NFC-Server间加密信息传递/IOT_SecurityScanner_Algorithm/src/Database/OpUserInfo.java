package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Entity.UserInfo;

public class OpUserInfo {
	
		//增
		public static Boolean add(final UserInfo info) throws Exception {
			JDBCTemplate<Boolean> t = new Transaction<Boolean>() {
				@Override
				protected Boolean doTransaction(Connection conn) throws Exception {
					PreparedStatement ps = conn
							.prepareStatement("insert into user values(?,?,?,?)");
					ps.setString(1, info.getID());
					ps.setString(2, info.getName());
					ps.setString(2, info.getGender());
					ps.setString(2, info.getBalance());
					return ps.execute();
				}
			};
			return t.execute();
		}
		
		//删
		public static Boolean delete(final String ID) throws Exception {
			JDBCTemplate<Boolean> t = new Transaction<Boolean>() {
				@Override
				protected Boolean doTransaction(Connection conn) throws Exception {
					PreparedStatement ps = conn
							.prepareStatement("delete from user where id=?");
					ps.setString(1, ID);
					return ps.execute();
				}
			};
			return t.execute();
		}
		
		//改
		public static Boolean update(final UserInfo info, final String ID)
				throws Exception {
			JDBCTemplate<Boolean> t = new Transaction<Boolean>() {
				@Override
				protected Boolean doTransaction(Connection conn) throws Exception {
					PreparedStatement ps = conn
							.prepareStatement("update user set id=?,name=?,gender=?,balance=? where id=? ");
					ps.setString(1, info.getID());
					ps.setString(2, info.getName());
					ps.setString(3, info.getGender());
					ps.setString(4, info.getBalance());
					ps.setString(5, ID);
					
					return ps.execute();
				}
			};
			return t.execute();
		}
		
		//查（一个）
			public UserInfo get(final String ID ) throws Exception {
				JDBCTemplate<UserInfo> q = new Query<UserInfo>() {
					protected UserInfo doQuery(Connection conn) throws Exception {
						PreparedStatement ps = conn
								.prepareStatement("select * from user where id=?");
						ps.setString(1, ID);
						ps.execute();
						ResultSet rs = ps.getResultSet();
						
						UserInfo info = null;
						if (rs.next()) {
							info = new UserInfo();
							info.setID(rs.getString("id")); 
							info.setName(rs.getString("name"));
							info.setGender(rs.getString("gender"));
							info.setBalance(rs.getString("balance"));
							
						}
						return info;
					}
				};
				return q.execute();
			}
			
		//查所有
			public static List<UserInfo> getAll() throws Exception {
				JDBCTemplate<List<UserInfo>> q = new Query<List<UserInfo>>() {

					@Override
					protected List<UserInfo> doQuery(Connection conn) throws Exception {
						List<UserInfo> infoList = new ArrayList<UserInfo>();
						PreparedStatement ps = conn
								.prepareStatement("select * from user");
						
						ps.execute();
						ResultSet rs = ps.getResultSet();
						while (rs.next()) {
							UserInfo info = new UserInfo();
							
							info.setID(rs.getString("id"));
							info.setName(rs.getString("name"));
							info.setGender(rs.getString("gender"));
							info.setBalance(rs.getString("balance"));
					
							infoList.add(info);
						}
						return infoList;
					}
				};
				return q.execute();
			}

}
