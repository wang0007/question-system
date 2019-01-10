package com.neuq.question.web.rest.management.qrcode;

import com.neuq.question.service.ShortUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 二维码相关接口
 *
 * @author yegk7
 * @since 2018/8/3 11:15
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/qrcode")
@RequiredArgsConstructor
@Api(value = "二维码相关接口", description = "二维码相关接口")
public class QrCodeController {

    private final ShortUrlService qrCodeService;

    /**
     * 通过短地址获取原始url
     *
     * @param shortUrl 短地址
     * @return 原始url
     */
    @ApiOperation(value = "通过短地址获取原始url", notes = "通过短地址获取原始url")
    @GetMapping(value = "")
    public String getOriginalUrl(@RequestParam("shortUrl") String shortUrl) {

        return qrCodeService.getOriginalUrl(shortUrl);
    }

}
