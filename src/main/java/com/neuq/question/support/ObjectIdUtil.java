package com.neuq.question.support;

import org.bson.types.ObjectId;

/**
 * @author liuhaoi
 */
public class ObjectIdUtil {

    public static Object getObjectId(String possibleObjectId) {

        if (isHexString(possibleObjectId)) {
            return new ObjectId(possibleObjectId);
        }

        return possibleObjectId;
    }

    public static boolean isHexString(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return false;
        }
        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i);
            if (!isHexChar(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验是否是16进制字符
     *
     * @param c
     * @return
     */
    private static boolean isHexChar(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
    }

}
