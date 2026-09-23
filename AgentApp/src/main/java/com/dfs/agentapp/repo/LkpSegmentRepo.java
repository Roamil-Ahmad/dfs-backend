package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    @Query(value = "SELECT * FROM LKP_SEGMENT \n" +
            "            WHERE UPPER(TRIM(SEGMENT_DESCR))=UPPER(TRIM(:descr)) \n" +
            "            ORDER BY SEGMENT_ID FETCH FIRST 1 ROW ONLY", nativeQuery = true)
    LkpSegment findBySegmentDescrIgnoreCase(@Param("descr") String descr);

    boolean existsBySegmentCode(String segmentCode);

    /**
     * Inserts a segment, allocating SEGMENT_ID from the table itself.
     *
     * <p>The entity maps SEGMENT_ID to the sequence LKP_SEGMENT_SEQ, which does not exist in this
     * database - a JPA save() therefore fails with ORA-02289. Creating the sequence would be a
     * schema change, so the id comes from MAX+1 in the same statement instead.</p>
     *
     * <p>Two callers racing here can pick the same id, and the SEGMENT_ID primary key rejects the
     * loser rather than letting a duplicate through. Onboarding creates segments rarely enough for
     * that to be the right trade against changing the schema.</p>
     *
     * <p>IS_ACTIVE and STATUS_ID match what the rows already in the table carry.</p>
     */
    @Modifying
    @Query(value = "INSERT INTO LKP_SEGMENT \n" +
            "            (SEGMENT_ID, SEGMENT_CODE, SEGMENT_DESCR, IS_ACTIVE, STATUS_ID, CREATEUSER, CREATEDATE) \n" +
            "            VALUES ((SELECT NVL(MAX(SEGMENT_ID),0)+1 FROM LKP_SEGMENT), \n" +
            "                    :code, :descr, 'Y', 2, 1, SYSTIMESTAMP)", nativeQuery = true)
    void insertSegment(@Param("code") String code, @Param("descr") String descr);
}
