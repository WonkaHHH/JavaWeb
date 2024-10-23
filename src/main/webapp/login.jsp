<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>登录</title>
</head>
<body>
<h2>登录页面</h2>

<form action="UserServlet" method="post">
    <input type="hidden" name="action" value="login">

    <label for="username">用户名:</label>
    <input type="text" id="username" name="username" required><br>

    <label for="password">密码:</label>
    <input type="password" id="password" name="password" required><br>

    <label for="captcha">验证码:</label>
    <input type="text" id="captcha" name="captcha" required><br>
    <img src="CaptchaServlet" alt="Captcha"><br>

    <input type="submit" value="Login">

    <p>
        ${param.error}
    </p>

</form>

<br>
<a href="register.jsp">注册</a>
</body>
</html>
