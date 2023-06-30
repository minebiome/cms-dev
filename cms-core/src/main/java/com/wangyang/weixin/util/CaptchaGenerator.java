package com.wangyang.weixin.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class CaptchaGenerator {
    private static final int EXPIRE_AFTER_WRITE_MINUTES = 10;
    private static final int CAPTCHA_WIDTH = 120;
    private static final int CAPTCHA_HEIGHT = 40;
    private static final int CAPTCHA_LENGTH = 4;
    private static final String CACHE_KEY_PREFIX = "captcha_";
    private static final Random random = new Random();
    private final Cache<String, String> captchaCache;

    public CaptchaGenerator() {
        captchaCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_AFTER_WRITE_MINUTES, TimeUnit.MINUTES)
                .build();
    }

    public void generateCaptchaImage(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Generate random captcha string
            StringBuilder captchaBuilder = new StringBuilder();
            for (int i = 0; i < CAPTCHA_LENGTH; i++) {
                captchaBuilder.append(random.nextInt(10));
            }
            String captcha = captchaBuilder.toString();

            // Save captcha to cache
            HttpSession session = request.getSession();
            String sessionId = session.getId();
            captchaCache.put(CACHE_KEY_PREFIX + sessionId, captcha);

            // Generate captcha image
            BufferedImage image = new BufferedImage(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, CAPTCHA_WIDTH, CAPTCHA_HEIGHT);
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Arial", Font.BOLD, 20));
            graphics.drawString(captcha, 10, 25);

            // Set content type and write image to response output stream
            response.setContentType("image/png");
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateCaptcha(HttpServletRequest request, String userInput) {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        String cachedCaptcha = captchaCache.getIfPresent(CACHE_KEY_PREFIX + sessionId);
        return cachedCaptcha != null && cachedCaptcha.equals(userInput);
    }
}
