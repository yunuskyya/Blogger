package com.blogger.backend.service;

import com.blogger.backend.model.User;

public interface MailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendAccountActivationEmail(String to, String token);
    void sendAccountLockedEmail(User user);
    void sendPasswordResetEmail(String to, String token);
    boolean isValidEmailFormat(String email);
}
