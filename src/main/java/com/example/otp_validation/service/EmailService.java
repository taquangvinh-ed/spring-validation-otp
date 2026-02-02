package com.example.otp_validation.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendHtmlOtp(String email, String otp) throws MessagingException;

}
