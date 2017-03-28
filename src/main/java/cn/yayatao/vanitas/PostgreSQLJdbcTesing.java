package cn.yayatao.vanitas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * PostgreSqL 测试工具
 * @author fireflyhoo
 *
 */
public class PostgreSQLJdbcTesing {
	public static void main(String[] args) throws Exception {
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://127.0.0.1:54320/postgres";
		try {
			Connection conn = DriverManager.getConnection(url, "postgres", "postgres");
			
			
			System.out.println(conn);
			PreparedStatement ps = conn.prepareStatement("select * from user where name = ?");
			ps.setString(1, "胡雅辉");
			
			ResultSet req = ps.executeQuery();		
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
