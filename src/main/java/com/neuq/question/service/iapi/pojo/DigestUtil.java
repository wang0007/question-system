package com.neuq.question.service.iapi.pojo;

/**
 * @author wangshyi
 * @date 2019/1/11  16:12
 */

import com.neuq.question.error.ECIOException;
import com.neuq.question.error.ECIllegalArgumentException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 简单的摘要计算工具类
 *
 * @author liuhaoi
 */
@Slf4j
public class DigestUtil {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final String ALGORITHM_MD5 = "MD5";

    private static final String ALGORITHM_SHA1 = "SHA-1";

    private static final String ALGORITHM_SHA256 = "SHA-256";

    private static final String ALGORITHM_SHA512 = "SHA-512";

    private DigestUtil() {
    }

    public static String signatureBySHA1(String source) {
        return getFormattedText(encode(ALGORITHM_SHA1, source));
    }

    public static String signatureByMD5(String source) {
        return getFormattedText(encode(ALGORITHM_MD5, source));
    }

    public static String signatureBySHA256(String source) {
        return getFormattedText(encode(ALGORITHM_SHA256, source));
    }

    public static String signatureBySHA512(String source) {
        return getFormattedText(encode(ALGORITHM_SHA512, source));
    }

    public static byte[] signBySHA1(String source) {
        return encode(ALGORITHM_SHA1, source);
    }

    public static byte[] signByMD5(String source) {
        return encode(ALGORITHM_MD5, source);
    }

    public static byte[] signBySHA256(String source) {
        return encode(ALGORITHM_SHA256, source);
    }

    public static byte[] signBySHA512(String source) {
        return encode(ALGORITHM_SHA512, source);
    }

    public static byte[] signBySHA1(byte[] source) {
        return encode(ALGORITHM_SHA1, source);
    }

    public static byte[] signByMD5(byte[] source) {
        return encode(ALGORITHM_MD5, source);
    }

    public static byte[] signBySHA256(byte[] source) {
        return encode(ALGORITHM_SHA256, source);
    }

    public static byte[] signBySHA512(byte[] source) {
        return encode(ALGORITHM_SHA512, source);
    }

    public static String signatureFileByMD5(File file) {
        MessageDigest md = getMessageDigest(ALGORITHM_MD5);
        try (InputStream is = new FileInputStream(file);
             DigestInputStream dis = new DigestInputStream(is, md)) {

            int length = 16 * 1024;
            int offset = 0;

            while (dis.read(new byte[length]) != -1) {
                offset += length;
            }

            log.info("calculate file md5 with file size {} ", offset);

        } catch (IOException e) {
            throw new ECIOException("IOException when calculate MD5 of file " + file.getAbsolutePath(), e);
        }
        byte[] digest = md.digest();

        return getFormattedText(digest);
    }

    /**
     * encode string
     *
     * @param algorithm 算法
     * @param source    原字符串
     * @return String 加密后的字符串
     */
    private static byte[] encode(String algorithm, String source) {
        if (source == null) {
            return null;
        }
        return encode(algorithm, source.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] encode(String algorithm, byte[] source) {
        if (source == null) {
            return null;
        }
        MessageDigest messageDigest = getMessageDigest(algorithm);
        messageDigest.update(source);
        return messageDigest.digest();
    }

    private static MessageDigest getMessageDigest(String algorithm) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new ECIllegalArgumentException("NoSuchAlgorithm " + algorithm, e);
        }
        return messageDigest;
    }

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    public static String getFormattedText(byte[] bytes) {

        if (bytes == null || bytes.length == 0) {
            return null;
        }

        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (byte aByte : bytes) {
            buf.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return buf.toString();
    }

}
