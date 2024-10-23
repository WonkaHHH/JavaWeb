package com.example.work1017.servlet;



import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class CaptchaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应类型为图片
        response.setContentType("image/jpeg");

        // 创建验证码图片
        int width = 160;
        int height = 40;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = bufferedImage.getGraphics();
        Random random = new Random();

        // 设置背景颜色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 设置边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);

        // 生成随机验证码字符串
        String captcha = generateCaptchaString(random);
        request.getSession().setAttribute("captcha", captcha);

        // 设置字体
        g.setFont(new Font("Arial", Font.BOLD, 24));

        // 绘制验证码字符串
        g.setColor(Color.BLACK);
        g.drawString(captcha, 30, 30);

        // 添加一些干扰线
        g.setColor(Color.GRAY);
        for (int i = 0; i < 8; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }
        //绘制操作完成
        g.dispose();

        //  将生成的图片以 JPEG 格式写入响应流，发送给客户端浏览器。
        ImageIO.write(bufferedImage, "jpeg", response.getOutputStream());
    }

    private String generateCaptchaString(Random random) {
        int length = 6;
        StringBuilder captcha = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < length; i++) {
            captcha.append(characters.charAt(random.nextInt(characters.length())));
        }

        return captcha.toString();
    }
}

