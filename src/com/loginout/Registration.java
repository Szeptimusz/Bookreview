package com.loginout;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import daos.Dao;

/**
 * Ha az adott névvel nincs még felhasználó akkor hozzáadja az adatbázishoz a jelszóból képzett 
 * hash kóddal együtt. Ha sikeres a hozzáadás, akkor belépteti az oldalra és átirányítja a 
 * könyv-keresés/hozzáadás oldalra. Ha bármi rosszul sikerült, akkor visszairányítja a fõoldalra
 */
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("uname");
		Dao dao = new Dao();
		if (dao.userExist(username)) {
			HttpSession session = request.getSession();
			session.setAttribute("message", "Username is already taken");
			response.sendRedirect("index.jsp");
		} else {
			String password = request.getParameter("pass1");
			HttpSession session = request.getSession();
			if (dao.addNewUser(username, password)) {
				session.setAttribute("username", username);
				session.setAttribute("message", "Successful registration and login!");
				response.sendRedirect("search.jsp");
			} else {
				session.setAttribute("message", "Registration failed!");
				response.sendRedirect("index.jsp");
			}
		}
	}

}
