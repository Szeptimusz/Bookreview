<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		// Megakadályozza, hogy a kijelentkezés után a vissza-nyíllal be lehessen jelentkezni
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	
		// Megakadályozza, hogy védett lapot bejelentkezés nélkül, url beírásával meg lehessen nyitni
		if (session.getAttribute("username") == null) {
			response.sendRedirect("index.jsp");
		}
	%>
	
	<h2>Welcome ${username}</h2> <br><br>
	<h4>Search result(s):</h4>
	<!-- Megjeleníti a keresésnek megfelelő könyveket -->
	<table>
	    <c:forEach items="${books}" var="book">
	        <tr>
	            <td>${book.author}</td>
	            <td>${book.title}</td>
	        </tr>
	    </c:forEach>
	</table>
	
	<!-- Kijelentkezési felület -->
	<form action="Logout">
		<input type="submit" value="Logout">
	</form>
</body>
</html>