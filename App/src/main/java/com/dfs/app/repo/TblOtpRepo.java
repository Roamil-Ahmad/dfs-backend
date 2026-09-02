package com.dfs.app.repo;

import com.dfs.app.model.TblOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblOtpRepo  extends JpaRepository<TblOtp,Long> {
    TblOtp findByOtpsIdAndVerified(long otpId,String status);
}
