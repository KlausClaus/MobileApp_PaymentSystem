package com.example.tuitionpayment.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
    private static final String SALT = "Myapp123";

    public static String encryptMD5(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            String saltedInput = input + SALT;
            digest.update(saltedInput.getBytes()); // 更新 MD5 输入
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2) h = "0" + h; // 保证每个字节长度为 2
                hexString.append(h);
            }
            return hexString.toString(); // 返回加密后的哈希值
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
