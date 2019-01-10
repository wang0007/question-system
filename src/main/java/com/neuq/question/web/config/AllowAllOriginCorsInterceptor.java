package com.neuq.question.web.config;

/**
 * @author wangshyi
 * @date 2019/1/3  21:06
 */

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liuhaoi
 */
public class AllowAllOriginCorsInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        writeAllowCrossDomainHeaders(request, response);
        return true;
    }

    public static void writeAllowCrossDomainHeaders(HttpServletRequest httpRequest,
                                                    HttpServletResponse httpServletResponse) {

        String origin = httpRequest.getHeader("Origin");

        String name = "Access-Control-Allow-Origin";

        if (httpServletResponse.getHeader(name) != null) {
            return;
        }

        if (origin != null && origin.trim().length() > 0 && !"null".equalsIgnoreCase(origin)) {
            httpServletResponse.addHeader(name, origin);
        } else {
            httpServletResponse.addHeader(name, "*");
        }

        // you probably don't need to change this one, it's indicating what headers you will use. There is no wildcard for this one
        httpServletResponse
                .addHeader("Access-Control-Allow-Headers", "X-Requested-With, X-HTTP-Method-Override, origin, " +
                        "content-type, accept, authorization, access-control-request-method");
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");

        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD");

        // the max age policy to renew CORS check. Here it's 14 days long
        httpServletResponse.addHeader("Access-Control-Max-Age", "1209600");
    }


}
