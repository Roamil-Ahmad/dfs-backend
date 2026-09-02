package com.dfs.thirdparties.controller.common;

import com.dfs.thirdparties.commons.HelperClass;
import com.dfs.thirdparties.service.CommonService;
import com.dfs.thirdparties.util.GenericResponseCode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommonController extends HelperClass {
    @Autowired
    private CommonService commonService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/reverse-geocode")
    public ResponseEntity<HashMap<String, Object>> getLocationInfo(@RequestParam double lat, @RequestParam double lon) {
        String url = String.format("https://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json&addressdetails=1", lat, lon);

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "SpringBootApp");
        headers.set("Accept-Language", "en");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JSONObject jsonResponse = new JSONObject(response.getBody());
            String city = "N/A";
            String province = "N/A";
            String country = "N/A";

            if (jsonResponse.has("address")) {
                JSONObject address = jsonResponse.optJSONObject("address");

                if (address != null) {
                    if (address.has("city")) {
                        city = address.getString("city");
                    } else if (address.has("town")) {
                        city = address.getString("town");
                    }

                    if (address.has("state")) {
                        province = address.getString("state")
                                .replaceAll("(?i)\\s*province\\s*", "")
                                .trim();
                    }

                    if (address.has("country")) {
                        country = address.getString("country");
                    }
                }
            }


            Map<String, String> locationInfo = new HashMap<>();
            locationInfo.put("city", city);
            locationInfo.put("province", province);
            locationInfo.put("country", country);

            HashMap<String, Object> finalResponse = commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), locationInfo);
            return getCustomizedResponseFormat(HttpStatus.OK, finalResponse);

        } catch (Exception e) {
            HashMap<String, Object> errorResponse = commonService.getResponse(GenericResponseCode.RECORD_NOT_FOUND.getResponseCode(), "Failed to fetch location info.");
            return getCustomizedResponseFormat(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse);
        }
    }

}
