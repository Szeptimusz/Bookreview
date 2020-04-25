package com.loginout;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import daos.Dao;

/**
 * Ha az adott n�vvel nincs m�g felhaszn�l� akkor hozz�adja az adatb�zishoz a jelsz�b�l k�pzett 
 * hash k�ddal egy�tt. Ha sikeres a hozz�ad�s, akkor bel�pteti az oldalra �s �tir�ny�tja a 
 * k�nyv-keres�s/hozz�ad�s oldalra. Ha b�rmi rosszul siker�lt, akkor visszair�ny�tja a f�oldalra
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
