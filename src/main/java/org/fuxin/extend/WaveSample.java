package org.fuxin.extend;

/**
 * Created by boshu on 2016/2/4.
 */

import java.io.File;

/**
 * 标准样本文件
 */
public enum WaveSample {
    ChinaMobileEmpty(1001, "中国移动空号样本", "ydkh_std.wav", 3000)
    ,ChinaMobileStopped(1002, "中国移动停机样本", "ydtj_std.wav", 3000)
    ,ChinaMobileClosed(1003, "中国移动关机样本", "ydgj_std.wav", 2250)

    ,ChinaUnicomEmpty(2001, "中国联通空号样本", "ltkh_std.wav", 2000)
    ,ChinaUnicomStopped(2002, "中国联通停机样本", "lttj_std.wav", 2000)
    ,ChinaUnicomClosed(2003, "中国联通关机样本", "ltgj_std.wav", 2000)

    ,ChinaTelicomEmpty(3001, "中国电信空号样本", "ydkh_std.wav", 3000)
    ,ChinaTelicomStopped(3002, "中国电信停机样本", "ydtj_std.wav", 3000)   //电信 用的移动的样本
    ,ChinaTelicomClosed(3003, "中国电信关机样本", "ydgj_std.wav", 2250)
    ;
    private Integer seqid;
    private String simpleName;
    private String fileName;
    private int threshold;

    /**
     * 样本资源文件 所在目录
     */
    public static String resoureDir = "";

    /**
     * 样本初始化函数
     * @param seqid id
     * @param simpleName  中文简称
     * @param fileName 文件名
     * @param threshold  阀值
     */
    private WaveSample(Integer seqid, String simpleName, String fileName, int threshold)
    {
        this.seqid = seqid;
        this.simpleName = simpleName;
        this.fileName = fileName;
        this.threshold = threshold;
    }

    /**
     * 获取样本的文件资源
     * @return
     * @throws Exception
     */
    public File getFile() throws Exception{
        if (resoureDir==null || resoureDir.length()==0){
            throw new Exception("尚未配置样本资源目录");
        }
        return new File(resoureDir, this.fileName);
    }

    /**
     * 获取阀值
     * @return
     */
    public int getThreshold() {
        return threshold;
    }
}
