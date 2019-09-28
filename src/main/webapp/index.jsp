<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta charset="ISO-8859-2">
	<title>Review books!</title>
	<style>
    	<%@ include file="index.css"%>
	</style>  
	<script>
		<!-- Regisztrációnál két jelszó egyezésének vizsgálata -->
		<%@ include file="confirmPassword.js"%>
		<%@ include file="message.js"%>
		<% session.setAttribute("message", null); %>
	</script>
</head>
<body>
	<h1>Welcome to my page!</h1>
	<h2>Log in to your account!</h2>
	<!-- Bejelentkezési űrlap -->
	<form action="Login" method="POST">
		Enter username: <input type="text" name="uname" maxlength="15" required><br>
		Enter password: <input type="password" name="pass" required><br>
		<input type="submit" name="login" value="Login">
	</form>
	<br>
	<h2>Or sign up!</h2>
	<!-- Regisztrációs űrlap -->
	<form action="Registration" method="POST" onSubmit = "return checkPassword(this)">
		Enter username: <input type="text" name="uname" maxlength="15" required><br>
		Enter password: <input type="password" name="pass1" required><br>
		Confirm password: <input type="password" name="pass2" required><br>
		<input type="submit" name="regist" value="Registration">
	</form>
</body>
</html>
<script type="text/javascript"> window.onload = alertMessage </script>