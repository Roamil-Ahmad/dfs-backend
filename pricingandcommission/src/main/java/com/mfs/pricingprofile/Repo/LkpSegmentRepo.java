package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.LkpSegment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpSegmentRepo extends JpaRepository<LkpSegment,Long> {
    List<LkpSegment> findByIsActive(String y);
}
