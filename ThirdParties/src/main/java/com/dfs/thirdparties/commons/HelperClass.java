package com.dfs.thirdparties.commons;

import com.dfs.thirdparties.dto.common.ValidationError;
import com.dfs.thirdparties.util.CustomDataNotFoundException;
import com.dfs.thirdparties.util.GenericResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelperClass  {

    Logger LOG = LoggerFactory.getLogger(HelperClass.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ResponseEntity<HashMap<String, Object>> getCustomizedResponseFormat(HttpStatus httpStatus,
                                                                               String responseCode, String responseMessage, Object payload) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("responsecode", responseCode);
        map.put("messages", responseMessage);
        map.put("data", payload);
         return ResponseEntity.status(httpStatus).body(map);
    }
    public ResponseEntity<HashMap<String, Object>> getCustomizedResponseFormat(HttpStatus httpStatus,
                                                                               HashMap<String, Object> resp) {
          return ResponseEntity.status(httpStatus).body(resp);
    }

    public static <T> T fromJson(String json, Class<T> tClass) throws JsonProcessingException {
        if (json == null || tClass == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.JSON_PARSE_EXCEPTION.getResponseCode());
        }
        return objectMapper.readValue(json, tClass);
    }
    public <T> boolean isNullOrEmpty(T input) {
        return input == null || (input instanceof String && ((String) input).isEmpty()) ||
                (input instanceof List && ((List<?>) input).isEmpty()) ||
                (input instanceof Map && ((Map<?, ?>) input).isEmpty()) ||
                (input instanceof BigDecimal && ((BigDecimal) input).compareTo(BigDecimal.ZERO) <= 0);
    }

    public String convertObjecttoJson(Object object) {

        objectMapper.writerWithDefaultPrettyPrinter();  // Pretty printing
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }



}
