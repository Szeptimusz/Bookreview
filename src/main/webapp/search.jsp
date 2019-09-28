<%@ page language="java" contentType="text/html; charset=ISO-8859-2"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-2">
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
	<h1>Welcome <a href="UserProfile" style="text-decoration: none">${username}</a></h1>
	<form action="Logout">
		<input type="submit" value="Logout">
	</form>
	<br>
	<fieldset>
		<h3>Here you can search a book:</h3>
		<fieldset>
			<!-- Szerző és/vagy könyvcím alapján keresni lehet a könyvek között -->
			<form action="SearchBook" method="GET">
				Search by Author: <input type="text" name="author" maxlength= "100">
				Search by Title: <input type="text" name="title" maxlength="500">
				<input type="submit" name="search" value="Search">
			</form>
		</fieldset>
		<br><br>
		<h3>Or add your own book:</h3>
		<fieldset>
			<!-- Saját könyv hozzáadásához űrlap kitöltése -->
			<form action="AddBook" method=GET>
				<span>Author: </span><input type="text" name="author2" maxlength="100" required>
				<span>Title: </span><input type="text" name="title2" maxlength="500" required>
				<span>Rating: </span><input type="number" name="rate" min="1.0" max="10.0" step="0.1" required>
				<span>Text review: </span>
				<textarea name="message" maxlength="2000"></textarea>
				<input type="submit" name="addbook" value="Add a new book">
			</form>
		</fieldset>
	</fieldset>
</body>
</html>
<script type="text/javascript"> window.onload = alertMessage </script>