<%@ page language="java" contentType="text/html; charset=ISO-8859-2"
    pageEncoding="ISO-8859-2"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-2">
	<style>
     	<%@ include file="bookResults.css"%>
	</style>
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
	
	<!-- Kijelzi a bejelentkezett felhasználó nevét, a névre kattintva megnyitja a profilt -->
	<span class="loggedUser">Logged user:</span>
	<a href="UserProfile">${username}</a>
	
	
	<!-- Kijelentkezési felület -->
	<form action="Logout">
		<input type="submit" value="Logout">
	</form>
	<br><br>
	
	
	<h4>Search result(s):</h4>
	<!-- Megjeleníti a keresésnek megfelelő könyveket -->
	<table>
	    <c:forEach items="${books}" var="book">
	        <tr>
	            <td><span class="blue">Author: </span>${book.author}</td>
	            <td><span class="blue">Title: </span>${book.title}</td>
	            <td><span class="blue">Average Rating: </span>${book.reviewpoint}</td>
	            <td><form action="AddReview" method="GET">
	            		<input type="hidden" name="bookId" value="${book.id}">
	            		<span class="rating">Rate this book (1.0 - 10.0):</span><input type="number" name="reviewPoint"
	            				min="1.0" max="10.0" step="0.1" required>
	            		<span>Text review: </span><textarea name="message" maxlength="2000"></textarea>
						<input type="submit" name="addReview" value="Review the book!">
	            	</form></td>
	        </tr>
	    </c:forEach>
	</table>
	
	
	<!-- Visszalépés a belépés utáni főoldalra -->
	<form action="http://localhost:8080/bookreview/search.jsp">
    <input type="submit" value="Back to my main page" />
	</form>
</body>
</html>