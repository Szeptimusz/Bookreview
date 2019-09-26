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
 * A search.jsp keres�si �rlapja �ltal megh�vott servlet. A kapott szerz� �s/vagy 
 * k�nyvc�m alapj�n meghat�rozza a keres�si tal�latokat majd az eredm�nyeket �tadva
 * megjelen�ti a BookResults.jsp-oldalt.
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
		String filter;
		if (author.isEmpty()) filter = "WHERE name != '" + username + "' AND title LIKE '%" + title + "%'";
		else if (title.isEmpty()) filter = "WHERE name != '" + username + "' AND author LIKE '%" + author + "%'";
		else filter = "WHERE name != '" + username + "' AND (author LIKE '%" + author + "%' OR "
				+ "title LIKE '%" + title + "%')";

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
