package com.dfs.app.repo;

import com.dfs.app.model.TblBioverisy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblBioverisyRepo extends JpaRepository<TblBioverisy,Long> {
    @Query(value = "SELECT * FROM TBL_BIOVERISYS tb  WHERE tb.NID_NO =:nidNo AND FINGER_INDEX =:fingerIndex AND IS_ACTIVE ='Y'",nativeQuery = true)
    TblBioverisy findByNidNoAndFingerIndex(String nidNo,int fingerIndex);

}
