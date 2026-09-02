package com.dfs.thirdparties.repo;

import com.dfs.thirdparties.model.TblOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TblOtpRepo extends JpaRepository<TblOtp, Long> {
    @Query(value = "SELECT *  FROM TBL_OTPS  WHERE OTPIN = :otPin AND VERIFIED = 'N' "
            + "  AND CUSTOMER_ID = :customerId AND OTP_TYPE =  :otpType "
            + " --AND SYSDATE + INTERVAL '5' MINUTE BETWEEN (case when effective_from < createdate then effective_from \r\n"
            + "     --  when createdate < effective_from then createdate else effective_from end) AND EFFECTIVE_TO", nativeQuery = true)
    List<TblOtp> getOTPByOtPinCustomerIdAndOtpType(@Param("otPin") String otPin,
                                                   @Param("customerId") BigDecimal customerId, @Param("otpType") String otpType);

    @Query(value = "SELECT *  FROM TBL_OTPS WHERE OTPIN = :otPin AND VERIFIED = 'N' "
            + "  AND OTP_TYPE = :otpType  AND SYSDATE BETWEEN EFFECTIVE_FROM AND EFFECTIVE_TO", nativeQuery = true)
    List<TblOtp> getOTPByOtPinAndOtpType(@Param("otPin") String otPin, @Param("otpType") String otpType);

    @Query(value = "SELECT O.* \n" +
            "FROM TBL_OTPS O \n" +
            "INNER JOIN LKP_OTP_TYPE T \n" +
            "    ON O.OTP_TYPE = T.OTP_TYPE \n" +
            "WHERE O.CUSTOMER_ID = :customerId \n" +
            "  AND O.VERIFIED = 'A' \n" +
            "  AND T.OTP_TYPE=:otpType \n" +
            "  AND O.OTP_TRIES <= T.OTP_ATTEMPTS", nativeQuery = true)
    TblOtp findOtpByCustomerIdAndOtpType(String otpType, Long customerId);

    @Query(value = "SELECT O.* \n" +
            "FROM TBL_OTPS O \n" +
            "INNER JOIN LKP_OTP_TYPE T \n" +
            "    ON O.OTP_TYPE = T.OTP_TYPE \n" +
            "WHERE O.CUSTOMER_ALL_ID = :customerAllId \n" +
            "  AND O.VERIFIED = 'A' \n" +
            "  AND T.OTP_TYPE=:otpType \n" +
            "  AND O.OTP_TRIES <= T.OTP_ATTEMPTS", nativeQuery = true)
    TblOtp findOtpByCustomerAllIdAndOtpType(String otpType, Long customerAllId);


    @Query(value = "SELECT O.* \n" +
            "FROM TBL_OTPS O \n" +
            "INNER JOIN LKP_OTP_TYPE T \n" +
            "    ON O.OTP_TYPE = T.OTP_TYPE \n" +
            "    AND O.OTP_TYPE=:otpType AND (CUSTOMER_ID=:customerId OR CUSTOMER_ALL_ID=:customerAllId)\n" +
            "WHERE O.OTPS_ID=:otpId", nativeQuery = true)
    TblOtp findOtpByIdAndType(String otpType, String otpId, Long customerId, Long customerAllId);

    @Query(value = "SELECT O.* \n" +
            "FROM TBL_OTPS O \n" +
            "INNER JOIN LKP_OTP_TYPE T ON O.OTP_TYPE = T.OTP_TYPE \n" +
            "WHERE O.APP_USER_ID = :appUserId \n" +
            "  AND O.VERIFIED = 'A' \n" +
            "  AND T.OTP_TYPE = :otpType \n" +
            "  AND O.OTP_TRIES <= T.OTP_ATTEMPTS", nativeQuery = true)
    TblOtp findOtpByOtpTypeAndAppUserId(String otpType, long appUserId);

    @Query(value = "SELECT O.* \n" +
            "FROM TBL_OTPS O \n" +
            "INNER JOIN LKP_OTP_TYPE T \n" +
            "    ON O.OTP_TYPE = T.OTP_TYPE \n" +
            "    AND O.OTP_TYPE=:otpType AND (APP_USER_ID=:appUserId)\n" +
            "WHERE O.OTPS_ID=:otpId", nativeQuery = true)
    TblOtp findOtpByAppUserIdAndType(String otpType, String otpId, long appUserId);

    @Query(value = "SELECT O.* \n" +
            "FROM TBL_OTPS O \n" +
            "INNER JOIN LKP_OTP_TYPE T \n" +
            "    ON O.OTP_TYPE = T.OTP_TYPE \n" +
            "WHERE O.AGENT_ID = :agentId \n" +
            "  AND O.VERIFIED = 'A' \n" +
            "  AND T.OTP_TYPE=:otpType \n" +
            "  AND O.OTP_TRIES <= T.OTP_ATTEMPTS", nativeQuery = true)
    TblOtp findOtpByAgentIdAndOtpType(String otpType, Long agentId);

    @Query(value = "SELECT O.* \n" +
            "FROM TBL_OTPS O \n" +
            "INNER JOIN LKP_OTP_TYPE T \n" +
            "    ON O.OTP_TYPE = T.OTP_TYPE \n" +
            "    AND O.OTP_TYPE=:otpType AND (AGENT_ID=:agentId OR CUSTOMER_ALL_ID=:customerAllId)\n" +
            "WHERE O.OTPS_ID=:otpId", nativeQuery = true)
    TblOtp findOtpByIdAndTypeAgent(String otpType, String otpId, Long agentId, Long customerAllId);
}
