package com.example.otp_validation.controller;

import com.example.otp_validation.dto.OtpRequest;
import com.example.otp_validation.dto.OtpResponse;
import com.example.otp_validation.dto.VerifyOtpRequest;
import com.example.otp_validation.service.OtpService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/otp", produces = {MediaType.APPLICATION_JSON_VALUE})
public class OtpController {

    private  final OtpService otpService;

    @PostMapping("/generate")
    ResponseEntity<String> generateOtp(@Valid @RequestBody OtpRequest request) throws MessagingException {

        String result = otpService.generateOtp(request.getEmail());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/verify")
    ResponseEntity<OtpResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest){
        OtpResponse reponse = otpService.verifyOtp(verifyOtpRequest.getEmail(), verifyOtpRequest.getInputCode());
        return  ResponseEntity.ok(reponse);
    }

}
