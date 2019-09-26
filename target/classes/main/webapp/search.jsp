<%@ page language="java" contentType="text/html; charset=ISO-8859-2"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-2">
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
	
	Welcome ${username}
	<form action="Logout">
		<input type="submit" value="Logout">
	</form>
	<br>
	<h3>Here you can search a book:</h3>
	<!-- Szerző és/vagy könyvcím alapján keresni lehet a könyvek között -->
	<form action="SearchBook" method="GET">
		Search by Author: <input type="text" name="author">
		Search by Title: <input type="text" name="title">
		<input type="submit" name="search" value="Search">
	</form>
	<br><br>
	<h3>Or add your own book:</h3>
	<form action="AddBook" method=GET>
		<span>Author: </span><input type="text" name="author2">
		<span>Title: </span><input type="text" name="title2">
		<span>Rating: </span><input type="number" name="rate">
		<span>Text review: </span><br>
		<textarea name="message"></textarea>
		<input type="submit" name="addbook" value="Add a new book">
	</form>
</body>
</html>