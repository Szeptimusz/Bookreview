package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		String query = "SELECT DISTINCT books.id, author, title, books.reviewpoint FROM books " + filter;
		List<Book> booklist = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(url,user,password);
				PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				booklist.add(new Book(
						rs.getInt("id"),
						rs.getString("author"),
						rs.getString("title"),
						rs.getFloat("reviewpoint")
				));
			}
			return booklist;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}

	}
	
	public int getUserId(String username) throws ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String query = "SELECT users.id FROM users WHERE name = ?";
		try (Connection conn = DriverManager.getConnection(url,user,password);
				PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt("id");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public int addBook(String author, String title, float reviewpoint) throws ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String into = "INSERT INTO books (author, title, reviewpoint) VALUES(?,?,?)";
		try (Connection conn = DriverManager.getConnection(url,user,password);
				PreparedStatement ps = conn.prepareStatement(into,Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, author);
			ps.setString(2, title);
			ps.setFloat(3, reviewpoint);
			ps.executeUpdate();
			// Az adatbázisba írt sor auto-increment mezõértékének meghatározása
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public int addReview(int bookid, int userid, float reviewpoint, String reviewtext) throws ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String into = "INSERT INTO reviews (bookid, userid, reviewpoint, reviewtext) "
				+ "VALUES (?,?,?,?)";
		try (Connection conn = DriverManager.getConnection(url,user,password);
				PreparedStatement ps = conn.prepareStatement(into,Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, bookid);
			ps.setInt(2, userid);
			ps.setFloat(3, reviewpoint);
			ps.setString(4, reviewtext);
			ps.executeUpdate();
			// Az adatbázisba írt sor auto-increment mezõértékének meghatározása
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public void updateBookAvgPoint(int id) throws ClassNotFoundException {
		float newAvgPoint = -1;
		Class.forName("com.mysql.cj.jdbc.Driver");
		String query = "SELECT ROUND(SUM(reviews.reviewpoint) / COUNT(*),1) FROM reviews JOIN books ON books.id = bookid"
				+ " WHERE books.id = ?";
		try (Connection conn = DriverManager.getConnection(url,user,password);
				PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				newAvgPoint = rs.getFloat(1);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		String update = "UPDATE books SET books.reviewpoint = ? WHERE books.id = ?";
		try (Connection conn = DriverManager.getConnection(url,user,password);
				PreparedStatement ps = conn.prepareStatement(update)) {
			ps.setFloat(1, newAvgPoint);
			ps.setInt(2, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public List<Book> getBooksWithMyReview(String filter) throws ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String query = "SELECT DISTINCT books.id, author, title, books.reviewpoint, reviews.reviewpoint"
				+ ", reviewtext FROM books " + filter;
		List<Book> booklist = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(url,user,password);
				PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				booklist.add(new Book(
						rs.getInt("id"),
						rs.getString("author"),
						rs.getString("title"),
						rs.getFloat("books.reviewpoint"),
						rs.getFloat("reviews.reviewpoint"),
						rs.getString("reviewtext")
				));
			}
			return booklist;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}

	}
}
