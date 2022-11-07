package com.d205.sdutyplus.util;

import com.d205.sdutyplus.global.error.exception.NotSupportedImageTypeException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Generator {
    private String result;

    public MD5Generator(String input) {
        try {
            MessageDigest mdMD5 = MessageDigest.getInstance("MD5");
            mdMD5.update(input.getBytes("UTF-8"));
            byte[] md5Hash = mdMD5.digest();
            StringBuilder hexMD5hash = new StringBuilder();
            for (byte b : md5Hash) {
                String hexString = String.format("%02x", b);
                hexMD5hash.append(hexString);
            }
            result = hexMD5hash.toString();
        }
        catch(UnsupportedEncodingException e){
            throw new NotSupportedImageTypeException();
        }
        catch(NoSuchAlgorithmException e2){
            throw new NotSupportedImageTypeException();
        }
    }

    public String toString() {
        return result;
    }
}
