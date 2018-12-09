package cn.yayatao.vanitas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * PostgreSqL 测试工具
 * @author fireflyhoo
 *
 */
public class PostgreSQLJdbcTesing {
	public static void main(String[] args) throws Exception {
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://127.0.0.1:6666/postgres";
		try {
			Connection conn = DriverManager.getConnection(url, "postgres", "postgres");
			
			
			 Statement statem = conn.createStatement();
			
			 ResultSet re = statem.executeQuery("select * frmo user");
			 while(re.next()){
				System.out.println(re.getString(1));
			 }
			 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
