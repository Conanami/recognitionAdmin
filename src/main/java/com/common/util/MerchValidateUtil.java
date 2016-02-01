package com.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MerchValidateUtil {
	private static Logger log = LoggerFactory.getLogger(MerchValidateUtil.class);
	
	/**
	 * 签名校验方法
	 * @param param
	 * @param merchId
	 * @param appSecret
	 * @return true 表示校验通过，false 校验失败
	 */
	public static boolean validate(Map<String, String[]> param, String merchId, String appSecret){
		Map<String, String> requestParams = formatMap(param);
		String requestSignInfo = requestParams.get("signinfo");
		requestParams.remove("signinfo"); 
		String signInfo = sign(requestParams, null, merchId, appSecret);
		if (signInfo.equals(requestSignInfo)) {
			return true;
		}
		log.error("signinfo expect="+signInfo +" get="+requestSignInfo);
		return false;
	}
	
	/**
	 * 签名接口
	 * @param requestParams
	 * @param merchId
	 * @return
	 */
	public static String sign(Map<String, String> requestParams, String merchId, String appSecret){
		String signInfo = sign(requestParams, null, merchId, appSecret);
		return signInfo;
	}
	
	private static Map<String, String> formatMap(Map<String, String[]> param) {
		Map<String, String> res = new HashMap<String, String>();
		Iterator<String> keys = param.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String[] value = param.get(key);
			res.put(key, value[0]);
		}
		return res;
	}
	
	private static String sign(Map<String, String> params, List<String> ignoreParams, String merchId, String secret) {
		StringBuffer buffer = new StringBuffer();
		List<String> signParams = new ArrayList<String>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (null == ignoreParams || !ignoreParams.contains(entry.getKey())) {
				signParams.add(entry.getKey());
			}
		}
		Collections.sort(signParams);
		buffer.append(merchId);
		for (String param : signParams) {
			buffer.append(param).append(params.get(param));
		}
		buffer.append(secret);
		log.info("sign:"+buffer.toString());
		String sign=IopUtils.md5(buffer.toString());
		return sign;
	}
}
