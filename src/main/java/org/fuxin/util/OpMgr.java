package org.fuxin.util;

import org.fuxin.caller.C;

public class OpMgr {

	/***
	 * 通过前3位，取得运营商
	 * @param substring
	 * @return 
	 * -1,表示字符串太短，不能识别运营商
	 * 0 错误的前3位，认不出运营商
	 * 1 移动
	 * 2 联通
	 * 3 电信
	 */
	public static int findOp(String substring) {
		if(substring.length()<=2) return -1;
		if(substring.length()>=3) 
		{
			String prefix = substring.substring(0,3);
			System.out.println(prefix);
			if(Contains(C.ydprefix,prefix))
				return 1;
			if(Contains(C.ltprefix,prefix))
				return 2;
			if(Contains(C.dxprefix,prefix))
				return 3;
		}
		return 0;
	}

	/***
	 * 数组中是否包含某元素
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
