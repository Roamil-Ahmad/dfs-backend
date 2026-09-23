package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LkpSegmentRepo extends JpaRepository<LkpSegment, Long> {

    List<LkpSegment> findAllByIsActive(String isActive);

    /**
     * Finds a segment by its description, ignoring case and surrounding spaces.
     *
     * <p>SEGMENT_DESCR carries no unique index, so the table can already hold duplicates. Pinned to
     * the lowest SEGMENT_ID so repeated onboardings settle on the same row rather than picking a
     * different duplicate each time, and so a single-valued return can never raise
     * NonUniqueResultException.</p>
     */
    @Query("SELECT s FROM LkpSegment s WHERE UPPER(TRIM(s.segmentDescr)) = UPPER(:descr) "
            + "ORDER BY s.segmentId")
    List<LkpSegment> findBySegmentDescrIgnoreCase(@Param("descr") String descr);

    boolean existsBySegmentCode(String segmentCode);

    /**
     * The segment holding the highest SEGMENT_ID, or null when the table is empty.
     *
     * <p>SEGMENT_ID is assigned by the application because the sequence the entity used to map to
     * does not exist, so the next id is this one plus one.</p>
     */
    LkpSegment findTopByOrderBySegmentIdDesc();
}
