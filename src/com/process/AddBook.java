package com.process;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import daos.Dao;

/**
 * A kapott paraméterek alapján új könyvet ad hozzá az adatbázishoz
 */
public class AddBook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Dao dao = new Dao();

		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		String author = request.getParameter("author2");
		String title = request.getParameter("title2");
		float reviewpoint = Float.parseFloat(request.getParameter("rate"));
		String reviewtext = request.getParameter("message");
		
		int userid = dao.getUserId(username);
		int bookid = dao.addBook(author, title, reviewpoint);
		dao.addReview(bookid, userid, reviewpoint, reviewtext);
		
		response.sendRedirect("search.jsp");
	}

}
