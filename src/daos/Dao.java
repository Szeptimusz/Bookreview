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
 * Az adatb�zism�veleteket kezel� r�teg.
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
	 * Bel�p�si adatok ellen�rz�se az adatb�zisban
	 * @param uname: Felhaszn�l�n�v
	 * @param pass: Jelsz�
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
	 * K�nyvadatok lek�rdez�se megadott szempontok alapj�n
	 * @param filter: A lek�rdez�st sz�r� SQL r�sz
	 * @return A lek�rdezett k�nyvekb�l �ll� lista
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
	 * Lek�rdezi az adatb�zisb�l, hogy az adott n�vhez milyen id tartozik
	 * @param username: Felhaszn�l�n�v
	 * @return Az adatb�zisban a felhaszn�l�n�vhez tartoz� id
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
	 * �j k�nyv hozz�ad�sa az adatb�zishoz
	 * @param author: K�nyv szerz�je
	 * @param title: K�nyv c�me
	 * @param reviewpoint: A k�nyv �rt�kel�se sz�mszer�en
	 * @return A hozz�adott k�nyv automatikusan gener�lt id-je az adatb�zisban
	 */
	public int addBook(String author, String title, float reviewpoint) {
		String into = "INSERT INTO books (author, title, reviewpoint) VALUES(?,?,?)";
		try (Connection conn = DriverManager.getConnection(url,user,password);
				PreparedStatement ps = conn.prepareStatement(into,Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, author);
			ps.setString(2, title);
			ps.setFloat(3, reviewpoint);
			ps.executeUpdate();
			// Az adatb�zisba �rt sor auto-increment mez��rt�k�nek meghat�roz�sa
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
	 * �j �rt�kel�s hozz�ad�sa az adatb�zishoz
	 * @param bookid: A k�nyv id-je
	 * @param userid: A felhaszn�l� id-je
	 * @param reviewpoint: Az �rt�kel�s sor�n adott pont
	 * @param reviewtext: Az �rt�kel�s sor�n adott sz�veges �zenet
	 * @return Az adatb�zishoz hozz�adott �rt�kel�s id-je
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
			// Az adatb�zisba �rt sor auto-increment mez��rt�k�nek meghat�roz�sa
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
	 * �j �rt�kel�s hozz�ad�sa ut�n ez a met�dus friss�ti az adott k�nyv �tlagos �rt�kel�si pontj�t.
	 * El�sz�r kisz�m�tja, hogy mennyi az �j �tlagos �rt�kel�se a k�nyvnek, majd be�ll�tja az �rt�ket
	 * @param id: A megv�ltozott �tlag �rt�kel�s� k�nyv id-je
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
	 * Sz�r�ssel lek�rdezi a k�nyv �s a hozz� tartoz� �rt�kel�s adatait
	 * @param filter: A lek�rdez�s sz�r�je (t�bl�k kapcsol�d�sa, felhaszn�l� sz�r�se)
	 * @return A lek�rdezett k�nyvekb�l �ll� lista
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
	 * Megadja, hogy az adott felhaszn�l� l�tezik-e az adatb�zisban
	 * @param username: A felhaszn�l� neve
	 * @return Megadja, hogy l�tezik-e a users-adatt�bl�ban a n�v
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
	 * Hozz�adja az adatb�zishoz az �j felhaszn�l� adatait
	 * @param username: A felhaszn�l� neve
	 * @param pass: A felhaszn�l� �ltal megadott jelsz� (Hash-k�dk�nt t�rolja el az adatb�zisban)
	 * @return Megadja, hogy sikeres volt-e az adatb�zisba �r�s
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