package com.dfs.app.controller.lookups;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.LovService;
import com.dfs.app.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LookUpsGetApis extends HelperClass {
    @Autowired
    private CommonService commonService;
    @Autowired
    private LovService lovService;


    @GetMapping("/v1/getAllProvince")
    public ResponseEntity<HashMap<String, Object>> getAllProvince(HttpServletRequest httpServletRequest){
        HashMap<String, Object> response=lovService.getAllProvince();
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }
    @GetMapping("/v1/getAccountUpgradeLovs")
    public ResponseEntity<HashMap<String, Object>> getAccountUpgradeLovs(HttpServletRequest httpServletRequest){
        HashMap<String, Object> response=lovService.getAccountUpgradeLovs();
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }
    @GetMapping("/v1/getAllDistrictByProviceId/{id}")
    public ResponseEntity<HashMap<String, Object>> getAllDistrictByProviceId(@PathVariable(value = "id")Long id ,HttpServletRequest httpServletRequest){
        HashMap<String, Object> response=lovService.getAllDistrictByProviceId(id);
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }

    @GetMapping("/v1/getIssueTypeLov")
    public ResponseEntity<HashMap<String, Object>> getIssueTypeLov(HttpServletRequest httpServletRequest){
        HashMap<String, Object> response=lovService.getIssueTypeLov();
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }

    @GetMapping("/v1/getCategories")
    public ResponseEntity<HashMap<String, Object>> getCategories(HttpServletRequest httpServletRequest){
        HashMap<String, Object> response=lovService.getCategories();
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }

}
