package com.dfs.thirdparties.util;

import com.dfs.thirdparties.commons.HelperClass;
import com.dfs.thirdparties.dto.GenerateNotificationRequest;
import com.dfs.thirdparties.dto.GenerateOtpRequest;
import com.dfs.thirdparties.dto.VerifyOtpRequest;
import com.dfs.thirdparties.dto.common.ValidationError;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestValidator  {
    public static <T> boolean isNullOrEmpty(T input) {
        return input == null || (input instanceof String && ((String) input).isEmpty()) ||
                (input instanceof List && ((List<?>) input).isEmpty()) ||
                (input instanceof Map && ((Map<?, ?>) input).isEmpty()) ||
                (input instanceof BigDecimal && ((BigDecimal) input).compareTo(BigDecimal.ZERO) <= 0);
    }

    public static void validateGenerateOtp(GenerateOtpRequest generateOtpRequest) {

        if(isNullOrEmpty(generateOtpRequest.getOtpType())){
            throw new ValidationException("INVALID OTP TYPE");
        }
        if(generateOtpRequest.getOtpType().equalsIgnoreCase("EOV") && isNullOrEmpty(generateOtpRequest.getEmail())){
            throw new ValidationException("INVALID EMAIL");
        }
        if(isNullOrEmpty(generateOtpRequest.getMobileNumber())){
            throw new ValidationException("INVALID MOBILE NUMBER");
        }



    }

    public static void validateVerifyOtp(VerifyOtpRequest verifyOtpRequest) {
        if(isNullOrEmpty(verifyOtpRequest.getOtpType())){
            throw new ValidationException("INVALID OTP TYPE");
        }
        if(verifyOtpRequest.getOtpType().equalsIgnoreCase("EOV") && isNullOrEmpty(verifyOtpRequest.getEmail())){
            throw new ValidationException("INVALID EMAIL");
        }
        if(isNullOrEmpty(verifyOtpRequest.getMobileNumber())){
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if(isNullOrEmpty(verifyOtpRequest.getOtpId())){
            throw new ValidationException("INVALID Otp ID");
        }
        if(isNullOrEmpty(verifyOtpRequest.getOtpPin())){
            throw new ValidationException("INVALID Otp Pin");
        }
    }

    public static void validateGenerateNotificationRequest(GenerateNotificationRequest generateNotificationRequest) {
        if(isNullOrEmpty(generateNotificationRequest.getType())){
            throw new ValidationException("INVALID TYPE");
        }
        if(generateNotificationRequest.getType().equalsIgnoreCase("E") && isNullOrEmpty(generateNotificationRequest.getEmail())){
            throw new ValidationException("INVALID EMAIL");
        }
        if(generateNotificationRequest.getType().equalsIgnoreCase("M") && isNullOrEmpty(generateNotificationRequest.getMobileNumber())){
            throw new ValidationException("INVALID MOBILE NUMBER");
        }

        if(generateNotificationRequest.getType().equalsIgnoreCase("E") && isNullOrEmpty(generateNotificationRequest.getSubject())){
            throw new ValidationException("INVALID EMAIL SUBJECT");
        }
        if(isNullOrEmpty(generateNotificationRequest.getSms())){
            throw new ValidationException("INVALID SMS");
        }
    }
}
