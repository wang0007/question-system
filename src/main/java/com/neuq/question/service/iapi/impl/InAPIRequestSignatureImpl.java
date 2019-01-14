package com.neuq.question.service.iapi.impl;

/**
 * @author wangshyi
 * @date 2019/1/11  15:56
 */

import com.neuq.question.service.iapi.InAPIProperties;
import com.neuq.question.service.iapi.InAPIRequestSignature;

import com.neuq.question.service.iapi.pojo.DigestUtil;
import com.neuq.question.service.iapi.pojo.InAPIRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 调用企业空间服务使用的管理类，封装有企业空空间接口的调用规则，调用时须实现UpesnEntityParser接口
 *
 * @author wangshyi
 */
@Slf4j
@Service
@AllArgsConstructor
public class InAPIRequestSignatureImpl implements InAPIRequestSignature {



    private static final String SIGN = "sign";

    private static final String TS = "timestamp";

    private static final String V = "v";

    private static final char EQUAL = '=';

    private static final String AND = "&";

    private static final String QUESTION_MARK = "?";

    private static final String AGENT_ID = "agent_id";

    private final InAPIProperties properties;

    private final Map<String, String> internalParams;


    @Override
    public InAPIRequest sign(String url, Map<String, String> params) {
        return sign(url, params, false);
    }

    public InAPIRequest sign(String url, Map<String, String> params, boolean encoded) {

        InAPIRequest request = new InAPIRequest();

        String encodedBody = buildUrlByParams(params, encoded);
        request.setParamUrl(encodedBody);

        Map<String, String> defaultMap = buildInternalParams();
        params.putAll(buildInternalParams());

        String rawUrl = url + buildUrlByParams(params, false) + properties.getSalt();
        if (log.isDebugEnabled()) {
            log.debug("be signed url is {}", rawUrl);
        }

        String sign = DigestUtil.signatureByMD5(rawUrl);

        if (log.isDebugEnabled()) {
            log.debug("inapi signature source {} with cipher {}", rawUrl, sign);
        }

        defaultMap.put(SIGN, sign);

        String urlParam = buildUrlByParams(defaultMap, false);

        request.setUrl(properties.concatURL(properties.getHost(), url, QUESTION_MARK, urlParam));

        return request;
    }

    public Map<String, String> buildInternalParams() {
        return new HashMap<>(internalParams);
    }


    public String buildUrlByParams(Map<String, String> paramMap, boolean escape) {
        if (paramMap == null || paramMap.isEmpty()) {
            return "";
        }
        return paramMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .sorted(Comparator.comparing(Map.Entry::getKey, Comparator.naturalOrder()))
                .map(entry -> {

                    String value = entry.getValue();

                    if (escape) {
                        try {
                            value = URLEncoder.encode(value, StandardCharsets.UTF_8.name());
                        } catch (UnsupportedEncodingException e) {
                            log.error("UnsupportedEncodingException when do url encode", e);
                        }
                    }

                    return entry.getKey() + EQUAL + value;
                })
                .collect(Collectors.joining(AND));

    }
}

