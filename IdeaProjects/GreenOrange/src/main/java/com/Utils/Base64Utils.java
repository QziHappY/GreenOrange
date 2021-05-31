package com.Utils;

import org.apache.tomcat.util.codec.binary.Base64;

public class Base64Utils {
    //base64 解码
    public static String decode(String s) {
        byte[] bytes=s.getBytes();
        return new String(Base64.decodeBase64(bytes));
    }

    //base64 编码
    public static String encode(String s) {
        byte[] bytes=s.getBytes();
        return new String(Base64.encodeBase64(bytes));
    }
}
