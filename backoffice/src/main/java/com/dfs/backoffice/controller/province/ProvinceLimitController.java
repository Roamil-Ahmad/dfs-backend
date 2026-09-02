package com.dfs.backoffice.controller.province;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.TblProvinceLimitRequest;
import com.dfs.backoffice.dto.TblProvinceLimitSearchRequest;
import com.dfs.backoffice.service.impl.TblProvinceLimitService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/provinceLimit")
public class ProvinceLimitController extends HelperClass {

  private final TblProvinceLimitService service;

  public ProvinceLimitController(TblProvinceLimitService service) {
    this.service = service;
  }

  @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> update(@RequestBody TblProvinceLimitRequest req, HttpServletRequest request) {
    Response resp = service.updateProvinceLimit(req, request);
    return castResponseToEntity(resp, resp.getResponseCode());
  }

  @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> search(@RequestBody TblProvinceLimitSearchRequest req) {
    Response resp = service.search(req);
    return castResponseToEntity(resp, resp.getResponseCode());
  }

  @GetMapping(value = "/getById/{id}")
  public ResponseEntity<Response> getById(@PathVariable long id) {
    Response resp = service.getById(id);
    return castResponseToEntity(resp, resp.getResponseCode());
  }
}