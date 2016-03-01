package mybatis.one.po;

import java.io.Serializable;
import java.util.Date;

public class DBDeviceLog implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devicelog.UniqueID
     *
     * @mbggenerated
     */
    private String uniqueid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devicelog.lasttime
     *
     * @mbggenerated
     */
    private Date lasttime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devicelog.lastcallmobile
     *
     * @mbggenerated
     */
    private String lastcallmobile;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column devicelog.mobile
     *
     * @mbggenerated
     */
    private String mobile;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table devicelog
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devicelog.UniqueID
     *
     * @return the value of devicelog.UniqueID
     *
     * @mbggenerated
     */
    public String getUniqueid() {
        return uniqueid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devicelog.UniqueID
     *
     * @param uniqueid the value for devicelog.UniqueID
     *
     * @mbggenerated
     */
    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devicelog.lasttime
     *
     * @return the value of devicelog.lasttime
     *
     * @mbggenerated
     */
    public Date getLasttime() {
        return lasttime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devicelog.lasttime
     *
     * @param lasttime the value for devicelog.lasttime
     *
     * @mbggenerated
     */
    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devicelog.lastcallmobile
     *
     * @return the value of devicelog.lastcallmobile
     *
     * @mbggenerated
     */
    public String getLastcallmobile() {
        return lastcallmobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devicelog.lastcallmobile
     *
     * @param lastcallmobile the value for devicelog.lastcallmobile
     *
     * @mbggenerated
     */
    public void setLastcallmobile(String lastcallmobile) {
        this.lastcallmobile = lastcallmobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column devicelog.mobile
     *
     * @return the value of devicelog.mobile
     *
     * @mbggenerated
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column devicelog.mobile
     *
     * @param mobile the value for devicelog.mobile
     *
     * @mbggenerated
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}