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

    /**
     * 格式化电话号码
     * 去掉非 数字 和 - 的字符。 长度最多保留20位
     * @param phone
     * @return
     */
    public static String formatPhone(String phone){
        String tmpStr = "";
        for(int i=0;i<phone.length();i++){
            String tmp=""+phone.charAt(i);
            if((tmp).matches("[0-9.]") || tmp.equals("-")){
                tmpStr+=tmp;
            }
        }
        if (tmpStr.length()>20){
            tmpStr = tmpStr.substring(0,20);
        }
        return tmpStr;
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

    //移动的号段
    public static String[] ydprefix={"134","135","136","137",
            "138","139","150","151","152",
            "157","158","159","182","183",
            "184","187","178","188","147"};
    //联通的号段
    public static String[] ltprefix={"130","131","132","145",
            "155","156","176","185","186"};

    //电信的号段
    public static String[] dxprefix={"133","153","177","180","181","189"};

    /**
     * 判断运营商
     * @param substring
     * @return
     */
    public static String findOp(String substring) {
        if (substring.length()<4) return "";
        substring = substring.substring(0,3);

        String prefix = substring.substring(0,3);
        //System.out.println(prefix);
        if(Contains(ydprefix,prefix))
            return "yd";
        if(Contains(ltprefix,prefix))
            return "lt";
        if(Contains(dxprefix,prefix))
            return "dx";
        else
            return "dx";
    }

    /***
     * 是否包含
     * @param list
     * @param prefix
     * @return
     */
    private static boolean Contains(Object[] list, Object prefix) {
        // TODO Auto-generated method stub
        for(int i=0;i<list.length;++i)
            if(list[i].equals(prefix)) return true;
        return false;
    }
	
}
