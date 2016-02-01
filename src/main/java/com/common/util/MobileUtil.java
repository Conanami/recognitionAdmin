package com.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileUtil {
	/***
	 * 判断是否是手机号码
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {   
		if (str==null) {
			return false;
		}
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号  
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
	
}
