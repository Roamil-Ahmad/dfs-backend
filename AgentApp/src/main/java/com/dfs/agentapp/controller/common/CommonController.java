package com.dfs.agentapp.controller.common;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.util.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommonController  extends HelperClass {
    @Autowired
    private CommonService commonService;
    @GetMapping(value = "/v1/getappscreendata/{name}/{languageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> getAppScreenData(@PathVariable("name")String name, @PathVariable("languageId")String languageId){

        RequestValidator.validateGetAppScreenDataRequest(name,languageId);
        HashMap<String, Object> response=commonService.getAppScreenData(name,languageId);
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }
}
