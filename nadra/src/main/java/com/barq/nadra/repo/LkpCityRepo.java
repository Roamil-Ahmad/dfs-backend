package com.barq.nadra.repo;

import com.barq.nadra.model.LkpCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface LkpCityRepo extends JpaRepository<LkpCity,Long> {

    @Query(value = "SELECT UPPER(CITY_DESCR) FROM LKP_CITY WHERE IS_ACTIVE='Y'",nativeQuery = true)
    ArrayList<String> getAllCitiesIsActiveYes();
}
