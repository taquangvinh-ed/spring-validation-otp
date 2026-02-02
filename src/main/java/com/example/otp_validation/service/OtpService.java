package com.example.otp_validation.service;

import com.example.otp_validation.dto.OtpResponse;
import jakarta.mail.MessagingException;

public interface OtpService {

    String generateOtp(String email) throws MessagingException;

    OtpResponse verifyOtp(String email, String code);



}
