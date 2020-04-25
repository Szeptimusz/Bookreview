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
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	
		if (session.getAttribute("username") == null) {
			response.sendRedirect("index.jsp");
		}
	%>
	
	Welcome ${username} <br><br>
	Unrated books for you:
	<table>
	    <c:forEach items="${books}" var="book">
	        <tr>
	            <td>${book.author}</td>
	            <td>${book.title}</td>
	        </tr>
	    </c:forEach>
	</table>
	
	<form action="Logout">
		<input type="submit" value="Logout">
	</form>
</body>
</html>