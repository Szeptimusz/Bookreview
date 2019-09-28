var Message = '<%=session.getAttribute("message")%>';
if (Message != "null") {
	function alertMessage() {
		alert(Message);
	}
}