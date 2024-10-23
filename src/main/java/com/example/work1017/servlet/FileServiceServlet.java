package com.example.work1017.servlet;

import sun.net.www.protocol.http.HttpURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

// 注解使其能够处理文件上传
@MultipartConfig
@WebServlet("/fileservice")
public class FileServiceServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "/usr/java/file";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        Part filePart = request.getPart("fileUpload");
        if (filePart == null || filePart.getSize() == 0) {
            response.getWriter().println("没有选择文件");
            return;
        }

        String fileName = filePart.getSubmittedFileName().replaceAll("[\\\\/:*?\"<>|]", "_");
        Path uploadDirPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadDirPath)) {
            Files.createDirectories(uploadDirPath);
        }
        Path filePath = uploadDirPath.resolve(fileName);

        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        response.setContentType("text/plain");
        response.getWriter().println("success: " + filePath.toString());
    }


}
