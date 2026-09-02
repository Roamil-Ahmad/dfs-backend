/*
Author Name: romail.ahmed

Project Name: transaction

Package Name: com.wallet.transaction.repo

Interface Name: TblSmsMessageRepo

Date and Time:1/4/2025 4:30 PM

Version:1.0
*/

package com.wallet.transaction.repo;

import com.wallet.transaction.model.TblSmsMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblSmsMessageRepo extends JpaRepository<TblSmsMessage,Long> {
}
