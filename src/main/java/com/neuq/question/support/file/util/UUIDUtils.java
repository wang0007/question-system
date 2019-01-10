package com.neuq.question.support.file.util;

import java.util.UUID;

/**
 * @author wangshyi
 * @date 2019/1/7  15:31
 */
public class UUIDUtils {
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
