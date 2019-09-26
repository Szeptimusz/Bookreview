package com.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pojos.Book;

import daos.Dao;

/**
 * A search.jsp keresési ûrlapja által meghívott servlet. A kapott szerzõ és/vagy 
 * könyvcím alapján meghatározza a keresési találatokat majd az eredményeket átadva
 * megjeleníti a BookResults.jsp-oldalt.
 * @author Szept
 *
 */
public class SearchBook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Dao dao = new Dao();
		List<Book> books = new ArrayList<>();

		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		String author = request.getParameter("author");
		String title = request.getParameter("title");
		
		// A segédlekérdezés megadja azoknak a bookid-knek a listáját, amiket a felhasználó értékelt
		String filter = "WHERE books.id NOT IN (SELECT DISTINCT books.id FROM books JOIN reviews ON "
				+ "books.id = bookid JOIN users ON userid = users.id WHERE name = '" + username + "') AND ";
		
		if (author.isEmpty()) filter += "title LIKE '%" + title + "%'";
		else if (title.isEmpty()) filter += "author LIKE '%" + author + "%'";
		else filter += "(author LIKE '%" + author + "%' OR title LIKE '%" + title + "%')";

		try {
			books = dao.getBook(filter);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		request.setAttribute("books", books);
		RequestDispatcher rd = request.getRequestDispatcher("BookResults.jsp");
		rd.forward(request, response);
	}

}
