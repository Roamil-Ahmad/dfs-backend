package com.wallet.transaction.repo;


import com.wallet.transaction.model.TblRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblRequestRepo extends JpaRepository<TblRequest, Long> {

}
