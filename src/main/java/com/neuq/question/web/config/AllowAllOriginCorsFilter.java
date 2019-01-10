package com.neuq.question.web.config;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * use com.yonyoucloud.ec.sns.starter.core.support.mvc.interceptor.AllowAllOriginCorsInterceptor
 * @author liuhaoi
 */
public class AllowAllOriginCorsFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        AllowAllOriginCorsInterceptor
                .writeAllowCrossDomainHeaders((HttpServletRequest) request, (HttpServletResponse) response);

        chain.doFilter(request, response);
    }

}
