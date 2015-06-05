package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Entity.ScannerInfo;

public class OpScannerInfo {
	//增
	public static Boolean add(final ScannerInfo info) throws Exception {
		JDBCTemplate<Boolean> t = new Transaction<Boolean>() {
			@Override
			protected Boolean doTransaction(Connection conn) throws Exception {
				PreparedStatement ps = conn
						.prepareStatement("insert into Scanner values(?,?)");
				ps.setString(1, info.getSerialNumber());
				ps.setString(2, info.getRootKey());
				
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
						.prepareStatement("delete from Scanner where id=?");
				ps.setString(1, ID);
				return ps.execute();
			}
		};
		return t.execute();
	}
	
	//改
	public static Boolean update(final ScannerInfo s, final String ID)
			throws Exception {
		JDBCTemplate<Boolean> t = new Transaction<Boolean>() {
			@Override
			protected Boolean doTransaction(Connection conn) throws Exception {
				PreparedStatement ps = conn
						.prepareStatement("update Scanner set rid=?,rootkey=? where rid=? ");
				ps.setString(1, s.getSerialNumber());
				ps.setString(2, s.getRootKey());
				ps.setString(3, ID);
				
				return ps.execute();
			}
		};
		return t.execute();
	}
	
	//查（一个）
		public static ScannerInfo get(final String ID ) throws Exception {
			JDBCTemplate<ScannerInfo> q = new Query<ScannerInfo>() {
				protected ScannerInfo doQuery(Connection conn) throws Exception {
					PreparedStatement ps = conn
							.prepareStatement("select * from scanner where rid=?");
					ps.setString(1, ID);
					ps.execute();
					ResultSet rs = ps.getResultSet();
					
					ScannerInfo s = null;
					if (rs.next()) {
						s = new ScannerInfo();
						s.setSerialNumber(rs.getString("rid")); 
						s.setRootKey(rs.getString("rootkey"));
						
					}
					return s;
				}
			};
			return q.execute();
		}
}
