package com.example.work1017.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class FileDownloadServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "/usr/java/file";
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final int SLICE_SIZE = 1024 * 1024;
    private static final Logger logger = Logger.getLogger(FileDownloadServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取文件名
        String fileName = request.getPathInfo().substring(1); // 去掉开头的 '/'
        File file = Paths.get(UPLOAD_DIR, fileName).toFile();

        // 验证文件是否存在
        if (!file.exists() || file.isDirectory()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "文件不存在");
            return;
        }

        // 设置响应头，告诉浏览器这是一个文件下载请求
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        response.setContentLengthLong(file.length());

        int numSlices = (int) ((file.length() + SLICE_SIZE - 1) / SLICE_SIZE);
        for (int i = 0; i < numSlices; i++) {
            int sliceIndex = i;
            executorService.submit(() -> {
                try {
                    downloadSlice(file, response, sliceIndex);
                } catch (IOException e) {
                    logger.severe("切片下载失败：" + e.getMessage());
                    try {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误");
                    } catch (IOException ex) {
                        logger.severe("发送错误响应失败：" + ex.getMessage());
                    }
                }
            });
        }
    }

    private void downloadSlice(File file, HttpServletResponse response, int sliceIndex) throws IOException {
        int start = sliceIndex * SLICE_SIZE;
        long end = Math.min(start + SLICE_SIZE - 1, file.length() - 1);
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + file.length());
        try (FileInputStream inStream = new FileInputStream(file);
             OutputStream outStream = response.getOutputStream()) {
            inStream.skip(start);
            byte[] buffer = new byte[4096];
            long remaining = end - start + 1;
            while (remaining > 0) {
                int bytesRead = inStream.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                if (bytesRead == -1) {
                    break;
                }
                outStream.write(buffer, 0, bytesRead);
                remaining -= bytesRead;
            }
        }
    }
}