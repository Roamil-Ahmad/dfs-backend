package com.dfs.backoffice.config;


import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.utils.CustomDataNotFoundException;
import com.dfs.backoffice.utils.CustomException;
import com.dfs.backoffice.utils.ValidationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLDataException;

@RestControllerAdvice
public class ExceptionController extends HelperClass {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response> handleRuntimeException(RuntimeException ex) {
        ResponseEntity<Response> responseEntity;
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof CustomDataNotFoundException) {
            responseEntity = handleException(ex, HttpStatus.OK, true, "/customDataNotFoundException");
        } else if (rootCause instanceof CustomException) {
            responseEntity = handleException(ex, HttpStatus.OK, false, "/customException");
        } else if (rootCause instanceof ValidationException) {
            responseEntity = handleValidationException(ex, "/customException");
        } else {
            responseEntity = handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR, true, "/runTimeException");
        }
        return responseEntity;
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex) {
        return handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR, true, "/arrayIndexOutOfBoundsException");
    }

    @ExceptionHandler(SQLDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response> handleSQLDataException(SQLDataException ex) {
        return handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR, true, "/sqlDataException");
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response> handleNullPointerException(NullPointerException ex) {
        return handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR, true, "/nullPointerException");
    }

    @ExceptionHandler(InvocationTargetException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response> handleInvocationTargetException(InvocationTargetException ex) {
        return handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR, true, "/invocationTargetException");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleIllegalArgumentException(IllegalArgumentException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, true, "/illegalArgumentException");
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, true, "/indexOutOfBoundsException");
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleNumberFormatException(NumberFormatException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, true, "/numberFormatException");
    }

    @ExceptionHandler(ClassNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response> handleClassNotFoundException(ClassNotFoundException ex) {
        return handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR, true, "/classNotFoundException");
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<Response> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        return handleException(ex, HttpStatus.METHOD_NOT_ALLOWED, true, "/unsupportedOperationException");
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Response> handleSecurityException(SecurityException ex) {
        return handleException(ex, HttpStatus.FORBIDDEN, true, "/securityException");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response> handleGenericException(Exception ex) {
        return handleException(ex, HttpStatus.INTERNAL_SERVER_ERROR, true, "/genericException");
    }


    @ExceptionHandler(CustomDataNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleCustomDataNotFoundException(RuntimeException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, true, "/customDataNotFoundException");
    }

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleCustomException(RuntimeException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, false, "/customException");
    }

    private ResponseEntity<Response> handleValidationException(Exception ex, String exceptionName) {
        return getResponseFormat(HttpStatus.BAD_REQUEST, ex.getMessage(), null, true);

    }


    private ResponseEntity<Response> handleException(Exception ex, HttpStatus httpStatus, boolean isCustom, String exceptionName) {
        return getResponseFormat(HttpStatus.BAD_REQUEST, ex.getMessage(), null, isCustom);
    }

}
