package com.neuq.question.web.config.resolver;


import com.neuq.question.error.ECException;
import com.neuq.question.error.ECRemoteServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuhaoi
 */
@Slf4j
@ControllerAdvice("com.yonyoucloud.ec.sns.conference.web.rest")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle400Exception(Exception e) {
        logger.warn("Returning HTTP 400 Bad Request", e);
    }


    @ExceptionHandler(ECException.class)
    @ResponseBody
    ResponseEntity<?> handleControllerException(HttpServletRequest request, ECException ex) {

        int httpCode = ex.getHttpCode();
        String errorMessage = "Exception when process rest request";

        String code = String.valueOf(ex.getCode());

        if (ex instanceof ECRemoteServiceException) {
            ECRemoteServiceException remoteServiceException = (ECRemoteServiceException) ex;
            if (StringUtils.isNotBlank(remoteServiceException.getRemoteErrorCode())) {
                code = remoteServiceException.getRemoteErrorCode();
            }
        }

        if (httpCode >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            log.error(errorMessage + " " + code, ex);
        } else {
            log.warn(errorMessage + " " + code, ex);
        }
        return new ResponseEntity<>(new ExceptionResponseEntity(ex.getMessage(), code), HttpStatus.valueOf(httpCode));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity<?> handleOtherException(HttpServletRequest request, Throwable ex) {

        log.warn("Other Exception when process rest request", ex);

        return new ResponseEntity<>(new ExceptionResponseEntity(ex.getMessage(), "500"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


}