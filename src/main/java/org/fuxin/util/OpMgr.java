package org.fuxin.util;

import org.fuxin.caller.C;

public class OpMgr {

	/***
	 * 根据电话号码前3位决定运营商
	 */
	public static C.Operator findOp(String substring) {
		if(substring.length()<=2) return C.Operator.Unknown;
		if(substring.length()>=3) 
		{
			String prefix = substring.substring(0,3);
			System.out.println(prefix);
			if(Contains(C.ydprefix,prefix))
				return C.Operator.Yd;
			if(Contains(C.ltprefix,prefix))
				return C.Operator.Lt;
			if(Contains(C.dxprefix,prefix))
				return C.Operator.Dx;
		}
		return C.Operator.Unknown;
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
