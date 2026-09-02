package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TblMenuRepo extends JpaRepository<TblMenu, Long> {

    @Query(value = "SELECT M.* \n" +
            "  FROM TBL_MENU M \n" +
            " INNER JOIN TBL_ROLE_RIGHTS R ON M.MENU_ID = R.MENU_ID \n" +
            " WHERE R.ROLE_ID = :roleId\n" +
            "   AND R.STATUS_ID = 2 \n" +
            "   AND R.IS_ACTIVE = 'Y' AND R.VIEW_ALLOWED = 'Y'\n" +
            "   AND M.STATUS_ID = 2 \n" +
            "   AND M.IS_ACTIVE = 'Y'\n" +
            " ORDER BY M.SORT_SEQ", nativeQuery = true)
    List<TblMenu> userMenu(@Param("roleId") long roleId);

    @Query(value = "select * from tbl_menu where is_active = 'Y' and status_id = '2' and menu_type = 'P'", nativeQuery = true)
    List<TblMenu> getAllParentMenus();

    @Query(value = "SELECT * FROM TBL_MENU WHERE MENU_ID= :menuId", nativeQuery = true)
    TblMenu getMenuById(@Param("menuId") long menuId);

    @Query(value = "select * from tbl_menu where is_active = 'Y' and status_id = '2'", nativeQuery = true)
    List<TblMenu> getAllMenus(String status);

    List<TblMenu> findByIsActive(String yes);
}
