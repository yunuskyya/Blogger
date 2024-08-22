package com.blogger.backend.service;

public interface MailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendAccountActivationEmail(String to, String token);
    void sendAccountLockedEmail(String to);
    void sendPasswordResetEmail(String to, String token);
    boolean isValidEmailFormat(String email);
}
