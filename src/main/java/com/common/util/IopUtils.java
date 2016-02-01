package com.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IopUtils {
	private static Logger log = LoggerFactory.getLogger(IopUtils.class);
	
	/***
	 * 判断是否是手机号码
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {   
		if (str==null || str.length()==0) {
			return false;
		}
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][3,4,5,7,8,9][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }  
	
	/***
	 * 判断是身份证号码
	 * @param str
	 * @return
	 */
	 public static boolean isIdentifyCode(String str){
		str = str.toLowerCase();
		if (str.length()==18) {
			String str1 = str.substring(0, 18);
			if (str1.matches("[0-9]+")) {
				return true;
			}else if(str1.charAt(17)=='x' && str1.substring(0, 17).matches("[0-9]+")){
				return true;
			}
		}
		return false;
	}
	 
	/***
	 * 判断是否为空 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if (str==null || str.length()==0) {
			return true;
		}
		return false;
	}
	
	/***
	 * 判断是否不为空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		if (str==null || str.length()==0) {
			return false;
		}
		return true;
	}
	 
	public static MultiValueMap<String, String> toMultiValueMap(
			JSONObject jsonparams) {
		HashMap<String, String> map = new HashMap<String, String>();
		for(Object k : jsonparams.keySet()){ 
			Object v = jsonparams.get(k) ;
			String value = v==null? "" : v +"";
			map.put((String)k, value);
		}
		MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			mvm.add(entry.getKey(), entry.getValue());
		}
		return mvm;
	}
	
	/**
	 * 去掉字符串里面的 回车，换行符号, 并替换为 <br>
	 * @param myString
	 * @return
	 */
	public static String emptyString(String myString) {
		if (myString == null) {
			return "";
		}
		String newString = "";
		Pattern CRLF = Pattern.compile("(\r\n|\t|\r|\n|\n\r)");
		Matcher m = CRLF.matcher(myString);
		if (m.find()) {
			newString = m.replaceAll(" ");
//			log.info("newString " + newString);
		}else{
			newString = myString;
		}
		return newString;
	}
	
	public static String parseJson(Object obj) throws Exception{
		ObjectMapper obm = new ObjectMapper();
		obm.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		obm.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 允许忽略 未知属性 
		obm.setSerializationInclusion(Include.NON_NULL);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		obm.writeValue(out, obj);
		return new String(out.toByteArray());
	}
	
	public static String parseJson(Object obj, String dateformat) throws Exception{
		ObjectMapper obm = new ObjectMapper();
		obm.setDateFormat(new SimpleDateFormat(dateformat));
		obm.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 允许忽略 未知属性 
		obm.setSerializationInclusion(Include.NON_NULL);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		obm.writeValue(out, obj);
		return new String(out.toByteArray());
	}
	
	public static <T> T jsonParseToObject(String jsonstr, Class<T> cls) throws Exception{
		ObjectMapper obm = new ObjectMapper();
		obm.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 允许忽略 未知属性 
		obm.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		obm.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		T res = obm.readValue(jsonstr.getBytes("utf-8"), cls);
		return res;
	}
	
	public static <T> T jsonParseToObject(String jsonstr, Class<T> cls, String dateformat) throws Exception{
		ObjectMapper obm = new ObjectMapper();
		obm.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 允许忽略 未知属性 
		obm.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		obm.setDateFormat(new SimpleDateFormat(dateformat));
		T res = obm.readValue(jsonstr.getBytes("utf-8"), cls);
		return res;
	}
	
	/**
	 * 保存上传的文件
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static File saveFile(MultipartFile file) throws Exception{
		String realName = file.getOriginalFilename();
		String type = realName.substring(realName.indexOf("."),
				realName.length());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String name = realName.substring(0, realName.indexOf("."))+"_"+sdf.format(new Date()) +(new Random()).nextInt(1000)+ type;
		
		String fileuploadpath = System.getProperty("webapp.root")+"fileupload";
		{
			//目录不存在就创建目录
			File f = new File(fileuploadpath);
			if (!f.exists() && !f.isDirectory()) {
				f.mkdir();
			}
		}
		System.out.println(fileuploadpath);
		File fl = new File(fileuploadpath, name);
		// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的
		FileUtils.copyInputStreamToFile(file.getInputStream(), new File(
				fileuploadpath, name));
		return fl;
	}
	
	/**
	 * 创建新文件
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static File createDownloadFile(String filename, String type)throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String name = filename+"_"+sdf.format(new Date()) +"_"+(new Random()).nextInt(1000) +"."+type;
		
		String fileuploadpath = System.getProperty("webapp.root")+"download";
		{
			//目录不存在就创建目录
			File f = new File(fileuploadpath);
			if (!f.exists() && !f.isDirectory()) {
				f.mkdir();
			}
		}
		System.out.println(fileuploadpath);
		File fl = new File(fileuploadpath, name);
		return fl;
	}
	
	/**
	 * 获取32位的md5编码后的值
	 * 
	 * @param plainText
	 * @return
	 */
	public static String md5(String plainText) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("UTF-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * jssdk 签名算法
	 * @param params
	 * @param ignoreParams
	 * @return
	 */
	public static HashMap<String, String> jssdksign(String jsapi_ticket, String url) {
		HashMap<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
		String string1;
		String signature = "";

		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
				+ "&timestamp=" + timestamp + "&url=" + url;
		log.info("params "+string1);

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);

		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
	private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
