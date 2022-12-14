package mybatis.one.po;

import java.io.Serializable;
import java.util.Date;

public class DBZNCaseData implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zncasedata.caseno
     *
     * @mbggenerated
     */
    private String caseno;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zncasedata.categorize2
     *
     * @mbggenerated
     */
    private String categorize2;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zncasedata.createtime
     *
     * @mbggenerated
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column zncasedata.status
     *
     * @mbggenerated
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table zncasedata
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zncasedata.caseno
     *
     * @return the value of zncasedata.caseno
     *
     * @mbggenerated
     */
    public String getCaseno() {
        return caseno;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zncasedata.caseno
     *
     * @param caseno the value for zncasedata.caseno
     *
     * @mbggenerated
     */
    public void setCaseno(String caseno) {
        this.caseno = caseno;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zncasedata.categorize2
     *
     * @return the value of zncasedata.categorize2
     *
     * @mbggenerated
     */
    public String getCategorize2() {
        return categorize2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zncasedata.categorize2
     *
     * @param categorize2 the value for zncasedata.categorize2
     *
     * @mbggenerated
     */
    public void setCategorize2(String categorize2) {
        this.categorize2 = categorize2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zncasedata.createtime
     *
     * @return the value of zncasedata.createtime
     *
     * @mbggenerated
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zncasedata.createtime
     *
     * @param createtime the value for zncasedata.createtime
     *
     * @mbggenerated
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column zncasedata.status
     *
     * @return the value of zncasedata.status
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column zncasedata.status
     *
     * @param status the value for zncasedata.status
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status;
    }
}