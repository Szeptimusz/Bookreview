package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.api.jdbc.Statement;
import com.mysql.cj.jdbc.PreparedStatement;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Scalar.String;
import com.pojos.Book;

/**
 * Az adatbázismûveleteket kezelõ réteg.
 * @author Szept
 *
 */
public class Dao {
	String url = "jdbc:mysql://localhost:3308/bookreview?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	String user = "root";
	String password = "";
	
	public Dao() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Belépési adatok ellenõrzése az adatbázisban
	 * @param uname: Felhasználónév
	 * @param pass: Jelszó
	 * @return Visszaadja, hogy helyesek az adatok vagy nem
	 */
	public boolean check(String uname, String pass) {
		String sql = "SELECT * FROM users WHERE name=? AND password=SHA2(CONCAT(?),256);";
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
	 */
	public List<Book> getBook(String filter) {
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
	
	/**
	 * Lekérdezi az adatbázisból, hogy az adott névhez milyen id tartozik
	 * @param username: Felhasználónév
	 * @return Az adatbázisban a felhasználónévhez tartozó id
	 */
	public int getUserId(String username) {
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
	
	/**
	 * Új könyv hozzáadása az adatbázishoz
	 * @param author: Könyv szerzõje
	 * @param title: Könyv címe
	 * @param reviewpoint: A könyv értékelése számszerûen
	 * @return A hozzáadott könyv automatikusan generált id-je az adatbázisban
	 */
	public int addBook(String author, String title, float reviewpoint) {
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
	
	/**
	 * Új értékelés hozzáadása az adatbázishoz
	 * @param bookid: A könyv id-je
	 * @param userid: A felhasználó id-je
	 * @param reviewpoint: Az értékelés során adott pont
	 * @param reviewtext: Az értékelés során adott szöveges üzenet
	 * @return Az adatbázishoz hozzáadott értékelés id-je
	 */
	public int addReview(int bookid, int userid, float reviewpoint, String reviewtext) {
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
	
	/**
	 * Új értékelés hozzáadása után ez a metódus frissíti az adott könyv átlagos értékelési pontját.
	 * Elõször kiszámítja, hogy mennyi az új átlagos értékelése a könyvnek, majd beállítja az értéket
	 * @param id: A megváltozott átlag értékelésû könyv id-je
	 */
	public void updateBookAvgPoint(int id) {
		float newAvgPoint = -1;
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
	
	/**
	 * Szûréssel lekérdezi a könyv és a hozzá tartozó értékelés adatait
	 * @param filter: A lekérdezés szûrõje (táblák kapcsolódása, felhasználó szûrése)
	 * @return A lekérdezett könyvekbõl álló lista
	 */
	public List<Book> getBooksWithMyReview(String filter) {
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

	/**
	 * Megadja, hogy az adott felhasználó létezik-e az adatbázisban
	 * @param username: A felhasználó neve
	 * @return Megadja, hogy létezik-e a users-adattáblában a név
	 */
	public boolean userExist(String username)  {
		String query = "SELECT * FROM users WHERE name = ?";
		try (Connection conn = DriverManager.getConnection(url,user,password);
				PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Hozzáadja az adatbázishoz az új felhasználó adatait
	 * @param username: A felhasználó neve
	 * @param pass: A felhasználó által megadott jelszó (Hash-kódként tárolja el az adatbázisban)
	 * @return Megadja, hogy sikeres volt-e az adatbázisba írás
	 */
	public boolean addNewUser(String username, String pass) {
		String into = "INSERT INTO users (name, password) "
				+ "VALUES (?, SHA2(CONCAT(?),256))";
		try (Connection conn = DriverManager.getConnection(url,user,password);
				PreparedStatement ps = conn.prepareStatement(into)) {
			ps.setString(1, username);
			ps.setString(2, pass);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}