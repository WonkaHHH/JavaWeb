package com.example.work1017.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class CommServiceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        updateViewCount(uri);//先根据url更新接口调用次数

        switch (uri) {
            case "/commservice/userIp.do":
                getUserIp(request, response);
                break;
            case "/commservice/userAgent.do":
                getUserAgent(request, response);
                break;
            case "/commservice/cookies.do":
                getCookies(request, response);
                break;
            case "/commservice/uriViews.do":
                getUriViews(response);
                break;
        }
    }
    //输出访问者ip
    private void getUserIp(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userIp = request.getRemoteAddr();
        response.setContentType("text/plain");
        response.getWriter().println(userIp);
    }
//    输出访问者浏览器信息
    private void getUserAgent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userAgent = request.getHeader("User-Agent");
        response.setContentType("text/plain");
        response.getWriter().println(userAgent);
    }
//    输出访问者全部cookie信息
    private void getCookies(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println("[");
        for (int i = 0; i < cookies.length; i++) {
            out.println("{\"name\":\"" + cookies[i].getName() + "\", \"value\":\"" + cookies[i].getValue() + "\"}");
            if (i < cookies.length - 1) out.println(",");
        }
        out.println("]");
    }
//    查看每个接口的总访问量
    private void getUriViews(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/work1017", "", "HYY");
             PreparedStatement stmt = conn.prepareStatement("SELECT uri, view_num FROM uri_views");
             ResultSet rs = stmt.executeQuery()) {

            PrintWriter out = response.getWriter();
            out.println("[");
            while (rs.next()) {
                out.println("{\"uri\":\"" + rs.getString("uri") + "\", \"viewNum\":" + rs.getInt("view_num") + "},");
            }
            out.println("]");
        } catch (SQLException e) {
            response.getWriter().println("{\"error\":\"数据库异常\"}");
        }
    }

    private void updateViewCount(String uri) {
        String selectSql = "SELECT view_num FROM uri_views WHERE uri = ?";
        String updateSql = "UPDATE uri_views SET view_num = view_num + 1 WHERE uri = ?";
        String insertSql = "INSERT INTO uri_views (uri, view_num) VALUES (?, 1)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/work1017", "root", "xxx")) {
            // 检查是否已经存在该 URI
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setString(1, uri);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        // 如果存在，更新 view_num
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, uri);
                            updateStmt.executeUpdate();
                        }
                    } else {
                        // 如果不存在，插入新的记录
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            insertStmt.setString(1, uri);
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

