package org.fuxin.extend;

import java.util.Arrays;

/**
 * Created by boshu on 2016/2/4.
 */
public enum TelecomCompany {
    ChinaMobile(1, "中国移动", WaveSample.ChinaMobileEmpty, WaveSample.ChinaMobileStopped, WaveSample.ChinaMobileClosed)
    ,ChinaUnicom(2, "中国联通", WaveSample.ChinaUnicomEmpty, WaveSample.ChinaUnicomStopped, WaveSample.ChinaUnicomClosed)
    ,ChinaTelicom(3, "中国电信", WaveSample.ChinaTelicomEmpty, WaveSample.ChinaTelicomStopped, WaveSample.ChinaTelicomClosed)
    ,UnKnown(999, "", null, null, null)
    ;

    private Integer seqid;

    /**
     * 简称
     */
    private String simpleName;

    /**
     * 空号样本
     */
    private WaveSample emptyWaveSample;

    /**
     * 停机样本
     */
    private WaveSample stoppedWaveSample;

    /**
     * 关机样本
     */
    private WaveSample closedWaveSample;
    /**
     * 号码的状态
     */
    private PhoneStatus status;

    private TelecomCompany(Integer seqid, String simpleName, WaveSample emptyWaveSample, WaveSample stoppedWaveSample, WaveSample closedWaveSample)
    {
        this.seqid = seqid;
        this.simpleName = simpleName;
        this.emptyWaveSample = emptyWaveSample;
        this.stoppedWaveSample = stoppedWaveSample;
        this.closedWaveSample = closedWaveSample;
    }

    public WaveSample getClosedWaveSample() {
        return closedWaveSample;
    }

    public WaveSample getEmptyWaveSample() {
        return emptyWaveSample;
    }

    public WaveSample getStoppedWaveSample() {
        return stoppedWaveSample;
    }

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

    /**
     * 识别手机号所属的运营商
     * @param mobile
     * @return
     */
    public static TelecomCompany identify(String mobile){
        if (mobile==null || mobile.length()<4){
            return TelecomCompany.UnKnown;
        }
        String prefix = mobile.substring(0,3);
        if(Arrays.asList(ydprefix).contains(prefix)){
            return TelecomCompany.ChinaMobile;
        }
        if(Arrays.asList(ltprefix).contains(prefix)){
            return TelecomCompany.ChinaUnicom;
        }
        if(Arrays.asList(dxprefix).contains(prefix)){
            return TelecomCompany.ChinaTelicom;
        }
        //默认按中国移动处理， 处理虚拟运营商， 以及携号转网
        return TelecomCompany.ChinaMobile;
    }


}
