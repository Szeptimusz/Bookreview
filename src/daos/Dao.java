package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pojos.Book;

/**
 * Az adatb�zism�veleteket kezel� r�teg.
 * @author Szept
 *
 */
public class Dao {
	String sql = "SELECT * FROM users WHERE name=? AND password=?;";
	String url = "jdbc:mysql://localhost:3306/bookreview?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	String user = "admin";
	String password = "admin";
	
	/**
	 * Bel�p�si adatok ellen�rz�se az adatb�zisban
	 * @param uname: Felhaszn�l�n�v
	 * @param pass: Jelsz�
	 * @return Visszaadja, hogy helyesek az adatok vagy nem
	 * @throws ClassNotFoundException
	 */
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
	
	/**
	 * K�nyvadatok lek�rdez�se megadott szempontok alapj�n
	 * @param filter: A lek�rdez�st sz�r� SQL r�sz
	 * @return A lek�rdezett k�nyvekb�l �ll� lista
	 * @throws ClassNotFoundException
	 */
	public List<Book> getBook(String filter) throws ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String query = "SELECT DISTINCT books.id, author, title FROM books JOIN reviews ON "
				+ "books.id = bookid JOIN users ON userid = users.id " + filter;
		List<Book> booklist = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(url,user,password);
				PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				booklist.add(new Book(
						rs.getInt("id"),
						rs.getString("author"),
						rs.getString("title")
				));
			}
			return booklist;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}

	}
}
