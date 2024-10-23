package com.example.work1017.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.security.MessageDigest;

public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
//            用来加载 MySQL JDBC 驱动类的
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if ("register".equals(action)) {
            registerUser(request, response);
        } else if ("login".equals(action)) {
            loginUser(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            response.sendRedirect("register.jsp?error=invalid");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://xxxxxx:3306/work1017", "root", "xxx");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {

            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));
            stmt.executeUpdate();
            response.sendRedirect("login.jsp");
        } catch (SQLException e) {
            response.sendRedirect("register.jsp?error=duplicate");
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String captcha = request.getParameter("captcha");
        String sessionCaptcha = (String) request.getSession().getAttribute("captcha");

        // 验证验证码
        if (captcha == null || !captcha.equalsIgnoreCase(sessionCaptcha)) {
            response.sendRedirect("login.jsp?error=captcha");
            return;
        }

        // 验证用户名和密码
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/work1017", "root", "xxx");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {//检查结果集是否有数据
                request.getSession().setAttribute("user", username);
                response.sendRedirect("welcome.jsp");
            } else {
                response.sendRedirect("login.jsp?error=invalid");
            }
        } catch (SQLException e) {
            response.sendRedirect("login.jsp?error=db");
        }
    }

//    对密码进行哈希加密
    private String hashPassword(String password) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}

