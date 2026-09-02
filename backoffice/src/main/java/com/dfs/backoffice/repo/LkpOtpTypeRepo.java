package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.LkpOtpType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LkpOtpTypeRepo extends JpaRepository<LkpOtpType, Long> {
    @Query(value = "SELECT * FROM LKP_OTP_TYPE WHERE OTP_TYPE=:otpTypeCode",nativeQuery = true)
    LkpOtpType findOtpTypeByOtpCode(String otpTypeCode);
}
