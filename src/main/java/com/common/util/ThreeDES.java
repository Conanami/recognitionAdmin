package com.common.util;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class ThreeDES {

	/**
	 * 字符串加密
	 * @param source	原文
	 * @param key		32位密钥(必须是base64编码后的字符串)
	 * @return
	 * @throws Exception
	 */
	public static String encode(String source, String key) throws Exception {

		DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey securekey = keyFactory.generateSecret(dks);

		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, securekey);
		byte[] encoded = cipher.doFinal(source.getBytes("UTF-8"));

		return Base64Utils.encodeToString(encoded);
	}
	
	/**
	 * 字符串解密
	 * @param source	密文(必须是base64编码后的字符串)
	 * @param key		32位密钥(必须是base64编码后的字符串)
	 * @return
	 * @throws Exception
	 */
	public static String decode(String source, String key) throws Exception {
		// --通过base64,将字符串转成byte数组
		byte[] bytesrc = Base64Utils.decodeFromString(source);
		// --解密的key
		DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey securekey = keyFactory.generateSecret(dks);

		// --Chipher对象解密
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, securekey);
		byte[] result = cipher.doFinal(bytesrc);

		return new String(result);
	}
	
	public static void main(String[] args) throws Exception {
		// 添加新安全算法,如果用JCE就要把它添加进去
//		Security.addProvider(new com.sun.crypto.provider.SunJCE());

		String key = "86xkr99e40793e7cw0t30u6rgj50a9pi";
		String szSrc = "This is a 3DES test. 测试";

		System.out.println("加密前的字符串:" + szSrc);
		
		String ciphertext = encode(szSrc, key);
		System.out.println("加密后的字符串:" + ciphertext);

		String result = decode(ciphertext, key);
		System.out.println("解密后的字符串:" + result);
	}

}
