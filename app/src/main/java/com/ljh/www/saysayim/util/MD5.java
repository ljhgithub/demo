package com.ljh.www.saysayim.util;

import android.provider.MediaStore;

import com.ljh.www.imkit.util.log.LogUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Created by ljh on 2016/5/27.
 */
public class MD5 {
    public static String getStringMD5(String value) {
        if (value == null || value.trim().length() < 1) {
            return null;
        }
        try {
            return getMD5(value.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    public static String getMD5(byte[] source) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return HexDump.toHex(md5.digest(source));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
