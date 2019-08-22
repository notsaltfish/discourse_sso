package com.chen;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 该类是用来负责处理SSO过程
 * 需要用到的一些编码和签名验证
 */
public class EncryptUtils {

    public static final Base64.Decoder decoder = Base64.getDecoder();
    public static final Base64.Encoder encoder = Base64.getEncoder();

    public static String HMACSHA256(String data, String key) throws Exception {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");

        sha256_HMAC.init(secret_key);

        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();

        for (byte item : array) {

            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));

        }

        return sb.toString().toUpperCase();

    }

    public static String base64Encode(String text) throws UnsupportedEncodingException {

        return encoder.encodeToString(text.getBytes("UTF-8"));
    }

    public static String base64Decode(String text) throws UnsupportedEncodingException {

        return new String (decoder.decode(text),"UTF-8");
    }

    public static void main(String[] args) throws Exception {
        System.out.println(HMACSHA256("bm9uY2U9Y2I2ODI1MWVlZmI1MjExZTU4YzAwZmYxMzk1ZjBjMGI=\n","d836444a9e"));
    }
}
