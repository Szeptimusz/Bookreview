package com.kremmer.log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDao {
	String sql = "SELECT * FROM users WHERE name=? AND password=?;";
	String url = "jdbc:mysql://localhost:3306/bookreview?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	String user = "admin";
	String password = "admin";
	
	public boolean check(String uname, String pass) throws ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		try (Connection conn = DriverManager.getConnection(url,user,password);
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, uname);
			ps.setString(2, pass);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return true;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return false;
	}
}
