package com.process;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import daos.Dao;

/**
 * A keresés után kilistázott könyvekhez adott saját értékelést írja adatbázisba
 */
public class AddReview extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		int bookid = Integer.parseInt(request.getParameter("bookId"));
		String username = (String) session.getAttribute("username");
		float reviewpoint = Float.parseFloat(request.getParameter("reviewPoint"));
		String reviewtext = request.getParameter("message");
		
		Dao dao = new Dao();
		int userid = dao.getUserId(username);
		dao.addReview(bookid, userid, reviewpoint, reviewtext);
		dao.updateBookAvgPoint(bookid);
		
		response.sendRedirect("search.jsp");
	}

}
