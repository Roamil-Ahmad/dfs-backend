package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.LkpChannel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpChannelRepo extends JpaRepository<LkpChannel,Long> {
    List<LkpChannel> findByIsActive(String y);
}
