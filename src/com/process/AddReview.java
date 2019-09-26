package com.process;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import daos.Dao;

/**
 * Servlet implementation class AddReview
 */
public class AddReview extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Dao dao = new Dao();
		
		HttpSession session = request.getSession();
		int bookid = Integer.parseInt(request.getParameter("bookId"));
		String username = (String) session.getAttribute("username");
		float reviewpoint = Float.parseFloat(request.getParameter("reviewPoint"));
		String reviewtext = request.getParameter("message");
		int userid = -1;
		try {
			 userid = dao.getUserId(username);
			 dao.addReview(bookid, userid, reviewpoint, reviewtext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			dao.updateBookAvgPoint(userid);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		response.sendRedirect("search.jsp");
	}

}
