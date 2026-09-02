package com.dfs.backoffice.controller.district;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.TblDistrictLimitRequest;
import com.dfs.backoffice.dto.TblDistrictLimitSearchRequest;
import com.dfs.backoffice.service.impl.TblDistrictLimitService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/districtLimit")
public class DistrictLimitController extends HelperClass {

  private final TblDistrictLimitService service;

  public DistrictLimitController(TblDistrictLimitService service) {
    this.service = service;
  }

  @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> update(@RequestBody TblDistrictLimitRequest req, HttpServletRequest request) {
    Response resp = service.updateDistrictLimit(req, request);
    return castResponseToEntity(resp, resp.getResponseCode());
  }

  @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> search(@RequestBody TblDistrictLimitSearchRequest req) {
    Response resp = service.search(req);
    return castResponseToEntity(resp, resp.getResponseCode());
  }

  @GetMapping(value = "/getById/{id}")
  public ResponseEntity<Response> getById(@PathVariable long id) {
    Response resp = service.getById(id);
    return castResponseToEntity(resp, resp.getResponseCode());
  }
}