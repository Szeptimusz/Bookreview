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
 * Az adatbázismûveleteket kezelõ réteg.
 * @author Szept
 *
 */
public class Dao {
	String sql = "SELECT * FROM users WHERE name=? AND password=?;";
	String url = "jdbc:mysql://localhost:3306/bookreview?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	String user = "admin";
	String password = "admin";
	
	/**
	 * Belépési adatok ellenõrzése az adatbázisban
	 * @param uname: Felhasználónév
	 * @param pass: Jelszó
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
	 * Könyvadatok lekérdezése megadott szempontok alapján
	 * @param filter: A lekérdezést szûrõ SQL rész
	 * @return A lekérdezett könyvekbõl álló lista
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
