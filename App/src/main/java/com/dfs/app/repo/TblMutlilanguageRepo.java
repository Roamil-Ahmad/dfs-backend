package com.dfs.app.repo;

import com.dfs.app.model.TblMultilanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface TblMutlilanguageRepo extends JpaRepository<TblMultilanguage,Long> {
    @Query(value = "SELECT * FROM TBL_MULTILANGUAGE tm WHERE LANGUAGE_ID =:languageId  AND APP_SCREEN_NAME =:appScreenName",nativeQuery = true)
    List<TblMultilanguage> findAllByLanguageIdAndAppScreenName(String languageId,String appScreenName);

    TblMultilanguage findByColumnNameAndAndRefId(String languageId, BigDecimal refId);
}
