package org.fuxin.extend;

/**
 * Created by boshu on 2016/2/4.
 */
public class TelePhone {
    /**
     *  电话号码
     */
    private String number;
    private TelecomCompany company;
    /**
     * 号码状态
     */
    private PhoneStatus status;

    public PhoneStatus getStatus() {
        return status;
    }

    public TelePhone(String mobile){
        this.setNumber(mobile);
    }

    public void setNumber(String number){
        this.number = number;
        this.company = TelecomCompany.identify(number);
    }

    /**
     * 录音文件识别
     * @param urlstr
     */
    public void identifyWave(String urlstr){
        this.status = WaveIdentifyUtil.indentify(number, urlstr);
    }
}
