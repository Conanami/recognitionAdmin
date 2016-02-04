package org.fuxin.caller;

public class C {
	//移动空号文件
	public static String ydkhfile = "c:\\standard\\ydkh_std.wav";
	//移动停机文件
	public static String ydtjfile = "c:\\standard\\ydtj_std.wav";
	//移动关机文件
	public static String ydgjfile = "c:\\standard\\ydgj_std.wav";
	//联通
	public static String ltkhfile = "c:\\standard\\ltkh_std.wav";
	public static String lttjfile = "c:\\standard\\lttj_std.wav";
	public static String ltgjfile = "c:\\standard\\ltgj_std.wav";
	
	//电信
	public static String dxkhfile = "c:\\standard\\dxkh_std.wav";
	public static String dxtjfile = "c:\\standard\\ydtj_std.wav";
	public static String dxgjfile = "c:\\standard\\ydgj_std.wav";
	
	
	//多少以下算作小噪音
	public static int smallnoise=1000;
	//wav震动最大值
	public static int maxwave = 32768;
	
	//移动号段
	public static String[] ydprefix={"134","135","136","137",
									"138","139","150","151","152",
									"157","158","159","182","183",
									"184","187","178","188","147"};
	//联通号段
	public static String[] ltprefix={"130","131","132","145",
									"155","156","176","185","186"};
		
	//电信号段
	public static String[] dxprefix={"133","153","177","180","181","189"};
	
	//各种阈值
	public static int[] value={3000,3000,2250,2000,2000,2000,3000,3000,2250};
	
}
