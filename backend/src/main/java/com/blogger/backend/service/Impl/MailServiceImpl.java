package com.blogger.backend.service.Impl;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.blogger.backend.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender emailSender;
    private final String changePasswordUrl = "http://localhost:5173/login";
    private final String resetPasswordUrl = "http://localhost:5173/set-password";

    @Autowired
    public MailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendAccountActivationEmail(String to, String token) {
        String subject = "BLOGGER Hesap Aktivasyonu";
        String activationUrl = resetPasswordUrl + "?token=" + token;
        String text = String.format(
                "Merhaba,\n\n" +
                        "Hesabınızı aktifleştirmek için lütfen aşağıdaki bağlantıyı kullanın:\n" +
                        "%s\n\n" +
                        "Eğer bu işlemi siz gerçekleştirmediyseniz, lütfen hemen destek ekibimizle iletişime geçiniz.\n\n" +
                        "Saygılarımızla,\n" +
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
        String text = "Merhaba,\n\n" +
                "Hesabınız, 5 kez başarısız giriş denemesinin ardından kilitlenmiştir. " +
                "Hesabınızın kilidini açmak için lütfen destek ekibimizle iletişime geçiniz.\n\n" +
                "Saygılarımızla,\n" +
                "Blogger";
        sendSimpleMessage(to, subject, text);
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        String subject = "BLOGGER Şifre Sıfırlama";
        String resetUrl = resetPasswordUrl + "?token=" + token;
        String text = String.format(
                "Merhaba,\n\n" +
                        "Şifrenizi sıfırlamak için lütfen aşağıdaki bağlantıyı kullanın:\n" +
                        "%s\n\n" +
                        "Eğer bu işlemi siz gerçekleştirmediyseniz, lütfen hemen destek ekibimizle iletişime geçiniz.\n\n" +
                        "Saygılarımızla,\n" +
                        "Blogger",
                resetUrl
        );
        sendSimpleMessage(to, subject, text);
    }
}
