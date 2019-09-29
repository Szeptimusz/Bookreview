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
	<fieldset>
		<h2>Log in to your account!</h2>
		<!-- Bejelentkezési űrlap -->
		<form action="Login" method="POST">
			<label for="username1">Username: </label><input type="text" name="uname" id="username1" maxlength="15" required><br>
			<label for="username2">Password: </label><input type="password" name="pass" id="username2" required><br>
			<input type="submit" name="login" value="Login">
		</form>
		<br>
		<h2>Or sign up!</h2>
		<!-- Regisztrációs űrlap -->
		<form action="Registration" method="POST" onSubmit = "return checkPassword(this)">
			<label for="username3">Enter username:</label><input type="text" name="uname" id="username3" maxlength="15" required><br>
			<label for="password1">Enter password: </label><input type="password" name="pass1"  id="password1" required><br>
			<label for="password2">Confirm password: </label><input type="password" name="pass2" id="password2" required><br>
			<input type="submit" name="regist" value="Registration">
		</form>
	</fieldset>
</body>
</html>
<script type="text/javascript"> window.onload = alertMessage </script>