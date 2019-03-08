package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/*
 * 连接数据库
 */
public class DBConn {
	static String url="jdbc:mysql://localhost:3306/qq?useSSL=false";
	static String driver="com.mysql.jdbc.Driver";//驱动程序;
	static String user = "root";
	static String password = "19980821";
	static Connection conn=null;
	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public Connection dbConn(){
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
