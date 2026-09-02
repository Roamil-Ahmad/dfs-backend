package com.dfs.agentapp.config;


import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.util.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLDataException;
import java.util.HashMap;

@RestControllerAdvice
public class ExceptionController extends HelperClass {
    @Autowired
    private CommonService commonService;
    Logger LOG = LoggerFactory.getLogger(ExceptionController.class);
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<HashMap<String, Object>> handleRuntimeException(RuntimeException ex) {
        ResponseEntity<HashMap<String, Object>> responseEntity;
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof CustomDataNotFoundException) {
            responseEntity = handleException(ex,HttpStatus.OK,true,"/customDataNotFoundException");
        } else if (rootCause instanceof CustomException) {
            responseEntity = handleException(ex,HttpStatus.OK,false,"/customException");
        } else if (rootCause instanceof ValidationException) {
            responseEntity = handleValidationException(ex,"/customException");
        } else {
            responseEntity = handleException(ex,HttpStatus.INTERNAL_SERVER_ERROR,false,"/runTimeException");
        }
        return responseEntity;
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<HashMap<String, Object>> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex) {
         return handleException(ex,HttpStatus.INTERNAL_SERVER_ERROR,false,"/arrayIndexOutOfBoundsException");
    }

    @ExceptionHandler(SQLDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<HashMap<String, Object>> handleSQLDataException(SQLDataException ex) {
        return handleException(ex,HttpStatus.INTERNAL_SERVER_ERROR,false,"/sqlDataException");
    }
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<HashMap<String, Object>> handleNullPointerException(NullPointerException ex) {
        return handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR, false, "/nullPointerException");
    }
    @ExceptionHandler(InvocationTargetException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<HashMap<String, Object>> handleInvocationTargetException(InvocationTargetException ex) {
        return handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR, false, "/invocationTargetException");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<HashMap<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, false, "/illegalArgumentException");
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<HashMap<String, Object>> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, false, "/indexOutOfBoundsException");
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<HashMap<String, Object>> handleNumberFormatException(NumberFormatException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, false, "/numberFormatException");
    }

    @ExceptionHandler(ClassNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<HashMap<String, Object>> handleClassNotFoundException(ClassNotFoundException ex) {
        return handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR, false, "/classNotFoundException");
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<HashMap<String, Object>> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        return handleException(ex, HttpStatus.METHOD_NOT_ALLOWED, false, "/unsupportedOperationException");
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<HashMap<String, Object>> handleSecurityException(SecurityException ex) {
        return handleException(ex, HttpStatus.FORBIDDEN, false, "/securityException");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<HashMap<String, Object>> handleGenericException(Exception ex) {
        return handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR, false, "/genericException");
    }


    @ExceptionHandler(CustomDataNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HashMap<String, Object>> handleCustomDataNotFoundException(RuntimeException ex) {
        return handleException(ex,HttpStatus.OK,true,"/customDataNotFoundException");
    }

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HashMap<String, Object>> handleCustomException(RuntimeException ex) {
        return handleException(ex,HttpStatus.OK,false,"/customException");
    }
    private ResponseEntity<HashMap<String, Object>> handleValidationException(Exception ex,String exceptionName){

            return getCustomizedResponseFormat(HttpStatus.OK, GenericResponseCode.VALIDATION_ERROR.getResponseCode(),ex.getMessage(),null, exceptionName);

    }


    private ResponseEntity<HashMap<String, Object>> handleException(Exception ex, HttpStatus httpStatus,boolean isCustom,String exceptionName){
        if(!isCustom){
            return getCustomizedResponseFormat(HttpStatus.OK, GenericResponseCode.CUSTOM_MESSAGE.getResponseCode(),ex.toString(),null, exceptionName);

        }else {

            return getCustomizedResponseFormat(HttpStatus.OK,ex.getMessage(), commonService.getResponseMessageByCode(ex.getMessage()),null, exceptionName);
        }

    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<HashMap<String, Object>> handleAuthenticationException(RuntimeException ex) {
        return getCustomizedResponseFormat(HttpStatus.UNAUTHORIZED, ex.getMessage(), commonService.getResponseMessageByCode(ex.getMessage()),null, "/authenticationException");
    }

}
