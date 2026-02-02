package com.example.otp_validation.service.imp;

import com.example.otp_validation.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImp implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendHtmlOtp(String email, String otp) throws MessagingException {
        log.info("Bat dau gui");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Xác thực OTP - [Tên App]");

        Context context = new Context();
        context.setVariable("otpCode", otp);
        context.setVariable("expiryMinutes", 5);

        String htmlContent = templateEngine.process("otp-email", context);

        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);

        log.info("Ket thuc gui");

    }
}
