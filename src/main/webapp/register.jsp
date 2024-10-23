<%--
  Created by IntelliJ IDEA.
  User: administered
  Date: 2024/10/17
  Time: 09:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>注册</title>
</head>
<body>
<h2>注册页面</h2>

<form action="UserServlet?action=register" method="post">
    <label for="username">用户名:</label>
    <input type="text" id="username" name="username" required><br><br>

    <label for="password">密码:</label>
    <input type="password" id="password" name="password" required><br><br>

    <input type="submit" value="Register">
</form>

<br>
<a href="login.jsp">前往登录</a>
</body>
</html>

