<%@ page language="java" contentType="text/html; charset=ISO-8859-2"
    pageEncoding="ISO-8859-2"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-2">
	<link rel="stylesheet" type="text/css" href="bookresults.css">
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
	            <td><span class="aut">Author: </span>${book.author}</td>
	            <td><span class="tit">Title: </span>${book.title}</td>
	            <td><span class="avg">Average Rating: </span>${book.reviewpoint}</td>
	            <td><span class="rating">Rate this book (1-10):</span>
	            	<form action="AddReview" method="GET">
	            		<input type="hidden" name="bookId" value="${book.id}">
	            		<input type="number" name="reviewPoint">
	            		<textarea name="message"></textarea>
						<input type="submit" name="addReview" value="Review the book!">
	            	</form></td>
	        </tr>
	    </c:forEach>
	</table>
	
	<!-- Kijelentkezési felület -->
	<form action="Logout">
		<input type="submit" value="Logout">
	</form>
</body>
</html>