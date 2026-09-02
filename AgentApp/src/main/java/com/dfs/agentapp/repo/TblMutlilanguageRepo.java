package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblMultilanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblMutlilanguageRepo extends JpaRepository<TblMultilanguage,Long> {
    @Query(value = "SELECT * FROM TBL_MULTILANGUAGE tm WHERE LANGUAGE_ID =:languageId  AND APP_SCREEN_NAME =:appScreenName",nativeQuery = true)
    List<TblMultilanguage> findAllByLanguageIdAndAppScreenName(String languageId, String appScreenName);
}
