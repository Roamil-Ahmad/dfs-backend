package com.dfs.backoffice.controller.province;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.ProvinceRequest;
import com.dfs.backoffice.dto.ProvinceSearchRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.service.impl.ProvinceService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/province")
public class ProvinceController extends HelperClass {

  private final ProvinceService provinceService;

  public ProvinceController(ProvinceService provinceService) {
    this.provinceService = provinceService;
  }

  @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> saveProvince(@RequestBody ProvinceRequest req, HttpServletRequest request) {
    Response resp = provinceService.saveProvince(req, request);
    return castResponseToEntity(resp, resp.getResponseCode());
  }

  @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> updateProvince(@RequestBody ProvinceRequest req, HttpServletRequest request) {
    Response resp = provinceService.updateProvince(req, request);
    return castResponseToEntity(resp, resp.getResponseCode());
  }

  @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> search(@RequestBody ProvinceSearchRequest request) {
    Response resp = provinceService.searchProvinces(request);
    return castResponseToEntity(resp, resp.getResponseCode());
  }

  @GetMapping(value = "/getById/{id}")
  public ResponseEntity<Response> getById(@PathVariable long id) {
    Response resp = provinceService.getById(id);
    return castResponseToEntity(resp, resp.getResponseCode());
  }
}
