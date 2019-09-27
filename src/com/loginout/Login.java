package com.loginout;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import daos.Dao;

/**
 * Login gombbal meghívódó servlet. Adatbázisban felhasználónév és jelszó ellenõrzése.
 * Helyes adatok esetén a felhasználónév elmentése a session-ben, különben átirányítja
 * a kezdõoldalra.
 * @author Szept
 *
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uname = request.getParameter("uname");
		String pass = request.getParameter("pass");
		
		Dao dao = new Dao();
			try {
				if (dao.check(uname,pass)) {
					HttpSession session = request.getSession();
					session.setAttribute("username", uname);
					response.sendRedirect("search.jsp");
				} else {
					response.sendRedirect("index.jsp");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}