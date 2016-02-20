package org.fuxin.caller;

public class C {
    public static String standfolder = "c:\\standard\\";

    //移动标准文件位置
    public static String ydkhfile = "ydkh_std.wav";
    public static String ydkhfile2 = "ydkh2_std.wav";
    public static String ydtjfile = "ydtj_std.wav";
    public static String ydgjfile = "ydgj_std.wav";
    public static String ydgjfile2 = "ydgj2_std.wav";
    public static String ydgjfile3 = "ydgj3_std.wav";

    //联通标准文件位置
    public static String ltkhfile = "ltkh_std.wav";
    public static String ltkhfile2 = "ltkh2_std.wav";
    public static String ltkhfile3 = "ltkh3_std.wav";
    public static String lttjfile = "lttj_std.wav";
    public static String lttjfile2 = "lttj2_std.wav";
    public static String ltgjfile = "ltgj_std.wav";

    //电信标准文件位置
    public static String dxkhfile = "dxkh_std.wav";
    public static String dxkhfile2 = "dxkh2_std.wav";
    public static String dxtjfile = "dxtj_std.wav";
    public static String dxgjfile = "dxgj_std.wav";
    public static String dxgjfile2= "dxgj2_std.wav";
    public static String dxgjfile3= "dxgj3_std.wav";


    //判断少于多少是噪音
    public static int smallnoise=1000;
    //WAV文件最大值?
    public static int maxwave = 32768;

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

    //顺序
    //0，移动空号
    //1，移动停机
    //2，移动关机
    //3，联通空号
    //4，联通停机
    //5，联通关机
    //6，电信空号
    //7，电信停机
    //8，电信关机
    //9，联通停机2
    //10,联通空号2
    //11,联通空号3
    //12,电信空号2?
    public static int[] value={
            2000,    //0,移动空号
            3600,	 //1,移动停机
            3000,    //2,移动关机
            2600,    //3,联通空号
            1500,	 //4，联通停机
            2000,    //5，联通关机
            3000,    //6,电信空号,3500
            3000,    //7,电信停机,3000
            2250,    //8，电信关机
            1500,    //9，联通停机2
            2000,    //10，联通空号2
            3000,    //11，联通空号3
            4000,	 //12，电信空号2
            2500,     //13, 电信关机2
            2000,     //14, 电信关机3
            2000,      //15, 移动空号2
            5500,       //16, 移动关机2
            2500		//17, 移动关机3
    };

    public enum Operator
    {
        Unknown,Yd,Lt,Dx
    }

    public enum Type
    {
//		Zc,Kh,Tj,Gj,Unknown,Undo

        Zc(1, "正常")
        ,Tj(2, "停机")
        ,Kh(3, "空号")
        ,Gj(4, "关机")
        ,Unknown(0, "未知")
        ,Undo(-1, "尚未处理，需要重新拨打");

        private Integer code;
        private String simpleName;
        private Type(Integer code, String simpleName)
        {
            this.code = code;
            this.simpleName = simpleName;
        }

        public Integer getCode() {
            return code;
        }

        public String getSimpleName() {
            return simpleName;
        }
    }



}
