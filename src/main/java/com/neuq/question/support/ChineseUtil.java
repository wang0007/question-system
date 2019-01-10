package com.neuq.question.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yegk7
 */
public class ChineseUtil {

    private final static String REG_EX = "[\u4e00-\u9fa5]";
    private final static Pattern PAT = Pattern.compile(REG_EX);

    public static boolean isContainsChinese(String str) {
        Matcher matcher = PAT.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }
}
