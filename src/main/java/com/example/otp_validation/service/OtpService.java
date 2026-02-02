package com.example.otp_validation.service;

import com.example.otp_validation.dto.OtpResponse;

public interface OtpService {

    String generateOtp(String email);

    OtpResponse verifyOtp(String email, String code);



}
