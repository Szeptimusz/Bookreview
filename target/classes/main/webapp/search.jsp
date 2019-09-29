<%@ page language="java" contentType="text/html; charset=ISO-8859-2"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-2">
	<style>
    	<%@ include file="search.css"%>
	</style>  
	<script>
		<!-- A session-ben tárolt üzenet megjelenítése (Bejelentkezés és regisztráció után) -->
		<%@ include file="message.js"%>
	</script>
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
		
		// Üzenet törlése a session-ből azért, hogy csak egyszer ugorjon fel
		session.removeAttribute("message");
	%>
	<h1>Welcome <a href="UserProfile">${username}</a></h1>
	<form action="Logout" name="logout">
		<input type="submit" value="Logout" name="logoutB">
	</form>
	<div>
	<fieldset>
		<fieldset>
			<legend>Here you can search a book</legend>
			<!-- Szerző és/vagy könyvcím alapján keresni lehet a könyvek között -->
			<form action="SearchBook" method="GET">
				Search by Author: <input type="text" name="author" maxlength= "100"><br>
				Search by Title: <input type="text" name="title" maxlength="500"><br>
				<input type="submit" name="search" value="Search">
			</form>
		</fieldset>
		<fieldset>
			<legend>Or add your own book</legend>
			<!-- Saját könyv hozzáadásához űrlap kitöltése -->
			<form action="AddBook" method=GET>
				<span>Author: </span><input type="text" name="author2" maxlength="100" required><br>
				<span>Title: </span><input type="text" name="title2" maxlength="500" required><br>
				<span>Rating: </span><input type="number" name="rate" min="1.0" max="10.0" step="0.1" required><br>
				<span>Text review: </span><br>
				<textarea name="message" maxlength="2000"></textarea><br>
				<input type="submit" name="addbook" value="Add a new book">
			</form>
		</fieldset>
	</fieldset>
	</div>
</body>
</html>
<script type="text/javascript"> window.onload = alertMessage </script>