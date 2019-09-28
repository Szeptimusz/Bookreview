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
 * A felhaszn�l�n�vre kattintva lek�rdezi a felhaszn�l� �ltal m�r �rt�kelt k�nyveket �s megjelen�ti a
 * Profile.jsp-oldalt (ami ezeket az adatokat kiolvassa �s megjelen�ti)
 */
public class UserProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Dao dao = new Dao();
		List<Book> books = new ArrayList<>();
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		String filter = "JOIN reviews ON "
				+ "books.id = bookid JOIN users ON userid = users.id WHERE name = '" + username + "'";
		books = dao.getBooksWithMyReview(filter);
		
		request.setAttribute("books", books);
		RequestDispatcher rd = request.getRequestDispatcher("Profile.jsp");
		rd.forward(request, response);
	}

}
