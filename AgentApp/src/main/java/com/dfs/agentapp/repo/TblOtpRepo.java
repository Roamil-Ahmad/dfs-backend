package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblOtpRepo extends JpaRepository<TblOtp,Long> {
    TblOtp findByOtpsIdAndVerified(long otpId,String status);
}
