package com.mfs.pricingprofile.utils;

import com.mfs.pricingprofile.dto.CommissionProfileSearch;
import com.mfs.pricingprofile.dto.PricingProfileSearch;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class RequestValidator {
    public static <T> boolean isNullOrEmpty(T input) {
        return input == null || (input instanceof String && ((String) input).isEmpty()) ||
                (input instanceof List && ((List<?>) input).isEmpty()) ||
                (input instanceof Map && ((Map<?, ?>) input).isEmpty()) ||
                (input instanceof BigDecimal && ((BigDecimal) input).compareTo(BigDecimal.ZERO) <= 0);
    }

    public static void getAllPricingProfileJsonValidate(PricingProfileSearch pricingProfileSearch) {
        if (isNullOrEmpty(pricingProfileSearch.getFromDate()) && isNullOrEmpty(pricingProfileSearch.getToDate()) && isNullOrEmpty(pricingProfileSearch.getChargesProfileName()) && isNullOrEmpty(pricingProfileSearch.getTransTypeId())) {
            throw new ValidationException("Select Any One Option");
        }
    }

    public static void getAllCommissionJsonValidate(CommissionProfileSearch commissionProfileSearch) {
        if (isNullOrEmpty(commissionProfileSearch.getFromDate()) && isNullOrEmpty(commissionProfileSearch.getToDate()) && isNullOrEmpty(commissionProfileSearch.getChargesProfileName()) && isNullOrEmpty(commissionProfileSearch.getTransTypeId())) {
            throw new ValidationException("Select Any One Option");
        }
    }
}
