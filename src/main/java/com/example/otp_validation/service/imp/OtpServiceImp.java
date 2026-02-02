package com.example.otp_validation.service.imp;

import com.example.otp_validation.constant.AppContants;
import com.example.otp_validation.dto.OtpResponse;
import com.example.otp_validation.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpServiceImp implements OtpService {

    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate redisTemplate;

    @Override
    public String generateOtp(String email) {

        Integer otp = ThreadLocalRandom.current().nextInt(100000, 999999);

        String hashedOtp = passwordEncoder.encode(otp.toString());

        String otpKey = AppContants.OTP_PREFIX + email.toLowerCase();

        redisTemplate.opsForValue().set(otpKey, hashedOtp, AppContants.OTP_TTL_SECONDS, TimeUnit.SECONDS);

        redisTemplate.delete(AppContants.RETRY_PREFIX+email.toLowerCase());
        return otp.toString();
    }

    @Override
    public OtpResponse verifyOtp(String email, String inputCode) {

        String emailLowerCase = email.toLowerCase();
        String otpKey = AppContants.OTP_PREFIX+emailLowerCase;
        String retryKey=AppContants.RETRY_PREFIX + emailLowerCase;

        Object storeHash =redisTemplate.opsForValue().get(otpKey);

        if(storeHash == null){
            return OtpResponse.builder().success(false).message("OTP is expired or not exists").build();
        }

        String storeHashString = storeHash.toString();

       if (passwordEncoder.matches((CharSequence) inputCode, storeHashString)){
           redisTemplate.delete(otpKey);
           redisTemplate.delete(retryKey);
           return OtpResponse.builder().success(true).message("Authenticate successfully").build();
       }

       Long retryCount = redisTemplate.opsForValue().increment(retryKey);

       if (retryCount == 1L){
            redisTemplate.expire(retryKey, 15, TimeUnit.MINUTES);

       }

        if(retryCount.intValue() > AppContants.MAX_RETRY){
            return  OtpResponse.builder().success(false).message("Quá số lần thử sai. Thử lại sau 15 phút.").build();
        }else {
           int remainingTries = AppContants.MAX_RETRY - retryCount.intValue();
           String message = "Your OTP is not right. You have " + remainingTries + " more tries";
           return new OtpResponse(false, message, retryCount.intValue());
        }

    }
}
