package com.dfs.backoffice.controller.district;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.DistrictRequest;
import com.dfs.backoffice.dto.DistrictSearchRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.service.impl.DistrictService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/district")
public class DistrictController extends HelperClass {

  private final DistrictService districtService;

  public DistrictController(DistrictService districtService) {
    this.districtService = districtService;
  }

  @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> saveDistrict(@RequestBody DistrictRequest req, HttpServletRequest request) {
    Response resp = districtService.saveDistrict(req, request);
    return castResponseToEntity(resp, resp.getResponseCode());
  }

  @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> updateDistrict(@RequestBody DistrictRequest req, HttpServletRequest request) {
    Response resp = districtService.updateDistrict(req, request);
    return castResponseToEntity(resp, resp.getResponseCode());
  }

  @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> search(@RequestBody DistrictSearchRequest request) {
    Response resp = districtService.searchDistricts(request);
    return castResponseToEntity(resp, resp.getResponseCode());
  }

  @GetMapping(value = "/getById/{id}")
  public ResponseEntity<Response> getById(@PathVariable long id) {
    Response resp = districtService.getById(id);
    return castResponseToEntity(resp, resp.getResponseCode());
  }
}