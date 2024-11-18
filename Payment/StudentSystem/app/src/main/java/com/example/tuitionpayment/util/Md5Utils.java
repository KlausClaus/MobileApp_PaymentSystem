package com.example.tuitionpayment.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for MD5 encryption with a predefined salt.
 */
public class Md5Utils {
    private static final String SALT = "$9900^^^Super&(+)&@!$";

    /**
     * Encrypts the given input string using the MD5 hashing algorithm with a predefined salt.
     *
     * @param input the string to be encrypted
     * @return the MD5 hashed string of the input, or an empty string if an exception occurs
     */
    public static String encryptMD5(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            String saltedInput = input + SALT;
            digest.update(saltedInput.getBytes()); // Update the MD5 input
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2) h = "0" + h; // Ensure each byte is represented as two characters
                hexString.append(h);
            }
            return hexString.toString(); // Return the encrypted hash value
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
