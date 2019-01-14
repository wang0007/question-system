package com.neuq.question.service.iapi.impl;

/**
 * @author wangshyi
 * @date 2019/1/11  15:31
 */


import com.neuq.question.error.*;
import com.neuq.question.service.iapi.InAPIResponseChecker;
import com.neuq.question.service.iapi.pojo.InAPIRequest;
import com.neuq.question.service.iapi.pojo.InAPIResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangshyi
 */
@Slf4j
@Service
public class InAPIResponseCheckerImpl implements InAPIResponseChecker {

    private static final Map<Integer, ExceptionBuilder> ERROR_CODES = new HashMap<>();

    static {
        ERROR_CODES.put(100000012, new ExceptionBuilder(ECUnauthorizedException.class, "InAPI:无效的认证信息,Token已失效"));
        ERROR_CODES.put(100010001, new ExceptionBuilder(ECUnauthorizedException.class, "InAPI:用户未登录"));
        ERROR_CODES.put(100000003, new ExceptionBuilder(ECIllegalArgumentException.class, "InAPI:操作失败"));
        ERROR_CODES.put(100209119, new ExceptionBuilder(ECIllegalArgumentException.class, "InAPI:获取租户过期时间失败"));
        ERROR_CODES.put(100000005, new ExceptionBuilder(ECIllegalArgumentException.class, "InAPI:参数获取失败"));
        ERROR_CODES.put(100000002, new ExceptionBuilder(ECIllegalArgumentException.class, "请求的资源未找到"));
        ERROR_CODES.put(100201301, new ExceptionBuilder(ECIllegalStateException.class, "请求过于频繁，60秒后重试"));

    }

    @Data
    @AllArgsConstructor
    public static class ExceptionBuilder {

        private Class<? extends ECException> clazz;

        private final String message;

        public ECException build(String errorMessage) {
            Constructor<? extends ECException> constructor;
            try {
                constructor = clazz.getConstructor(String.class);
                return constructor.newInstance(message + " " + errorMessage);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new ECConfigurationException("constructor exception failed", e);
            }
        }
    }

    public static void check(InAPIResponse body, Integer statusCode, String url, Object param) {

        Integer code = body.getCode();

        if (code != null && code != 0) {

            ExceptionBuilder ecException = ERROR_CODES.get(code);

            String message = buildErrorMessage(url, param, statusCode, body.getMsg());
            if (ecException != null) {
                log.warn(message);
                throw ecException.build(message);
            }

            throw new ECRemoteServiceException(message);
        }

    }

    @Override
    public void check(ResponseEntity<? extends InAPIResponse> response, InAPIRequest request) {

        HttpStatus statusCode = response.getStatusCode();
        if (statusCode != HttpStatus.OK) {
            String message = buildErrorMessage(request.getUrl(), request.getParamUrl(), statusCode.value(), "");
            throw new ECRemoteServiceException(message);
        }

        if (!response.hasBody()) {
            return;
        }
        InAPIResponse body = response.getBody();

        check(body, statusCode.value(), request.getUrl(), request.getParamUrl());
    }

    private static String buildErrorMessage(String url, Object param, Integer statusCode, String message) {
        return "Request inapi with URL " + url + " failed, with Params " + param
                + ", Response code" + " is " + statusCode + ", message: " + message;
    }
}
