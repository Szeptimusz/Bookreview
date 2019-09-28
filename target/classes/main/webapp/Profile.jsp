<%@ page language="java" contentType="text/html; charset=ISO-8859-2"
	pageEncoding="ISO-8859-2"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<!-- <link rel="stylesheet" type="text/css" href="bookResults.css"> -->
	<meta charset="ISO-8859-2">
	<style>
     <%@ include file="bookResults.css"%>
	</style>
	<title>Insert title here</title>
</head>
<body>
	<%
		// Megakad�lyozza, hogy a kijelentkez�s ut�n a vissza-ny�llal be lehessen jelentkezni
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	
		// Megakad�lyozza, hogy v�dett lapot bejelentkez�s n�lk�l, url be�r�s�val meg lehessen nyitni
		if (session.getAttribute("username") == null) {
			response.sendRedirect("index.jsp");
		}
	%>
	<h1>${username}'s Account</h1>
	<!-- Kijelentkez�si fel�let -->
	<form action="Logout">
		<input type="submit" value="Logout">
	</form><br>
	
	
	<h4>Rated books:</h4>
	<!-- Megjelen�ti a felhaszn�l� �ltal m�r �rt�kelt k�nyveket -->
	<table>
	    <c:forEach items="${books}" var="book">
	        <tr>
	            <td><span class="blue">Author: </span>${book.author}</td>
	            <td><span class="blue">Title: </span>${book.title}</td>
	            <td><span class="blue">Average Rating: </span>${book.reviewpoint}</td>
	            <td><span class="red">My rating: </span>${book.myPoint}</td>
	            <td><span class="red">Text message: </span>${book.message}</td>
	        </tr>
	    </c:forEach>
	</table>
	
	
	<!-- Visszal�p�s a bel�p�s ut�ni f�oldalra -->
	<form action="http://localhost:8080/bookreview/search.jsp">
    <input type="submit" value="Back to my main page" />
	</form>
</body>
</html>