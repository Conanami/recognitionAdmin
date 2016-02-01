package com.common.util;


/**
 * md5加密算法类
 * 
 */
public abstract class MD5 {

        
        /**
         * 加密为大写的MD5字符串.
         * 
         * @param src
         *                      要加密的字符串.
         * @return  String
         *                      加密完毕的字符串,其中字母为大写.
         */
        public static String encrypt(String src) {
                return encryptLowerCase(src).toUpperCase();
        }
        
        /**
         * 加密为小写的MD5字符串.
         * 
         * @param src
         *                      要加密的字符串.
         * @return  String
         *                      加密完毕的字符串,其中字母为小写.
         */
        public static String encryptLowerCase(String src) {
                src = src.trim();

                StringBuilder digestBuffer = new StringBuilder(32);
                try {
                        byte[] mess =src.getBytes("UTF-8");
                        byte[] hash ;
                        synchronized(currentAlgorithm){
                                currentAlgorithm.reset();
                                hash = currentAlgorithm.digest(mess);
                        }
                        int legth = hash.length;
                        for (int i = 0; i < legth; i++) {
                                int v = hash[i];
                                if (v < 0) v += 256;
                                if (v < 16) digestBuffer.append('0');
                                digestBuffer.append(Integer.toHexString(v));
                        }
                } catch (Exception e) {
                }
                return digestBuffer.toString();
        }
        
        
        private static  java.security.MessageDigest currentAlgorithm ;
        static{
                try{
                        currentAlgorithm =java.security.MessageDigest.getInstance("MD5");
                }catch (Exception e) {
                        System.err.println("无法获取信息摘要加密算法");
                }
        }     
        
      
}
