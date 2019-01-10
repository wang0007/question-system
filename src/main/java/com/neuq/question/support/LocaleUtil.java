package com.neuq.question.support;

import com.neuq.question.data.pojo.common.BasicDO;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author yegk7
 * @date 2018/8/28 14:23
 */
public class LocaleUtil {

    /**
     * 获取默认ID，加上多语信息
     *
     * @return 默认ID
     */
    public static String getDefaultIdWithLocale() {

        String locale = LocaleContextHolder.getLocale().toString();
        return BasicDO.DEFAULT_ID + locale;
    }

    public static String getLocale(){
        return LocaleContextHolder.getLocale().toString();
    }
}
