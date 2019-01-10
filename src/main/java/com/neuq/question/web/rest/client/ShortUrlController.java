package com.neuq.question.web.rest.client;



import com.neuq.question.service.ShortUrlService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.neuq.question.service.impl.ShortUrlServiceImpl.SHORT_URL_PREFIX;


/**
 * 用户信息相关接口
 *
 * @author yaoshw@yonyou.com
 */
@Api(value = "短连接服务", description = "短连接服务")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    @RequestMapping(SHORT_URL_PREFIX + "{urlId}")
    public void redirect(@PathVariable String urlId, HttpServletResponse response) throws IOException {

        String originalUrl = shortUrlService.getOriginalUrl(urlId);

        response.sendRedirect(originalUrl);
    }
}
