package com.blogger.backend.service.Impl;

import com.blogger.backend.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender emailSender;
    private final String changePasswordUrl = "http://localhost:5173/login";
    private final String activationToken = "http://localhost:5173/active";
    @Autowired
    public MailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // HTML içerik olarak işaretle
            emailSender.send(message);
        } catch (MessagingException e) {
            // Hata yönetimi yapılabilir
            e.printStackTrace();
        }
    }

    @Override
    public void sendAccountActivationEmail(String to, String token) {
        String subject = "BLOGGER Hesap Aktivasyonu";
        String activationUrl = activationToken + "/" + token; // Bu şekilde olmalı
        String text = String.format(
                "Merhaba,<br><br>" +
                        "Hesabınızı aktifleştirmek için lütfen <a href=\"%s\">buraya tıklayın</a>.<br><br>" +
                        "Eğer bu işlemi siz gerçekleştirmediyseniz, lütfen hemen destek ekibimizle iletişime geçiniz.<br><br>" +
                        "Saygılarımızla,<br>" +
                        "Blogger",
                activationUrl
        );
        sendSimpleMessage(to, subject, text);
    }

    @Override
    public boolean isValidEmailFormat(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void sendAccountLockedEmail(String to) {
        String subject = "BLOGGER Hesap Kilitlendi";
        String text = "Merhaba,<br><br>" +
                "Hesabınız, 5 kez başarısız giriş denemesinin ardından kilitlenmiştir. " +
                "Hesabınızın kilidini açmak için lütfen destek ekibimizle iletişime geçiniz.<br><br>" +
                "Saygılarımızla,<br>" +
                "Blogger";
        sendSimpleMessage(to, subject, text);
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        String subject = "BLOGGER Şifre Sıfırlama";
        String resetUrl = changePasswordUrl+ "?token=" + token;
        String text = String.format(
                "Merhaba,<br><br>" +
                        "Şifrenizi sıfırlamak için lütfen <a href=\"%s\">buraya tıklayın</a>.<br><br>" +
                        "Eğer bu işlemi siz gerçekleştirmediyseniz, lütfen hemen destek ekibimizle iletişime geçiniz.<br><br>" +
                        "Saygılarımızla,<br>" +
                        "Blogger",
                resetUrl
        );
        sendSimpleMessage(to, subject, text);
    }
}
