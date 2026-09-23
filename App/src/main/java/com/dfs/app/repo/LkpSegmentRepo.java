package com.dfs.app.repo;

import com.dfs.app.model.LkpSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LkpSegmentRepo extends JpaRepository<LkpSegment, Long> {

    /**
     * Finds a segment by its description, ignoring case and surrounding spaces.
     *
     * <p>SEGMENT_DESCR carries no unique index, so the table can hold duplicates. Ordered by id and
     * returned as a list so the caller takes the lowest without risking
     * NonUniqueResultException.</p>
     */
    @Query("SELECT s FROM LkpSegment s WHERE UPPER(TRIM(s.segmentDescr)) = UPPER(:descr) "
            + "ORDER BY s.segmentId")
    List<LkpSegment> findBySegmentDescrIgnoreCase(@Param("descr") String descr);
}
