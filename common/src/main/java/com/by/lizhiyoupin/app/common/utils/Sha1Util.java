package com.by.lizhiyoupin.app.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/10/31 17:27
 * Summary:
 */
public class Sha1Util {
    public static String encryptBySHA1(String encryptText, String encryptKey) {
        try {
            String ENCODING = "UTF-8";
            String MAC_NAME = "HmacSHA1";
            byte[] data = encryptKey.getBytes(ENCODING);
            SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);//根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            Mac mac = Mac.getInstance(MAC_NAME);//生成一个指定Mac算法的Mac对象
            mac.init(secretKey);//用给定密钥初始化Mac对象
            byte[] text = encryptText.getBytes(ENCODING);
            byte[] payload = mac.doFinal(text);
            BASE64Encoder base64Encoder = new BASE64Encoder();
            return base64Encoder.encode(payload);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }
}
