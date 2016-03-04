package com.common.util;

/**
 * Created by boshu on 2016/3/4.
 */
public class DESUtils {
    public static String getDecryptString(String cryptString){
        try {
            String deskey = "sK1KYrUxMrq15ZfjvHW6MbCtSmK1MTK6";
            String value = ThreeDES.decode(cryptString, deskey);
            return value;
        }catch (Exception e){
            return "";
        }
    }

}

