package com.neuq.question.support.oss.start.impl;

/**
 * @author wangshyi
 * @date 2019/1/2  10:42
 */

import com.neuq.question.support.oss.start.FileRemotePathPolicy;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class DatePathPolicy implements FileRemotePathPolicy {


    private static final String DATE_PATTERN = "yyyy/MM/dd/HH/mm/";

    @Override
    public String buildFileName() {

        String dateStr = DateFormatUtils.format(new Date(), DATE_PATTERN);

        return dateStr + UUID.randomUUID().toString();
    }

    @Override
    public String buildFileName(final String extension) {
        if (StringUtils.isBlank(extension)) {
            return buildFileName();
        }

        if (extension.charAt(0) == '.') {
            return buildFileName() + extension;
        }


        return buildFileName() + '.' + extension;
    }

}

