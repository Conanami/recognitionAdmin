package com.web.dto;

import mybatis.one.po.DBRecogs;

import java.util.Date;

/**
 * Created by boshu on 2016/2/26.
 */
public class DtoDBRecogs extends DBRecogs {
    private Date pickuptime;
    private String mark;
    private Long orderid;
    private String caseno;
    private String serino;
    private String ptel;
    private String ptel1;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Date getPickuptime() {
        return pickuptime;
    }

    public void setPickuptime(Date pickuptime) {
        this.pickuptime = pickuptime;
    }

    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public String getCaseno() {
        return caseno;
    }

    public void setCaseno(String caseno) {
        this.caseno = caseno;
    }

    public String getSerino() {
        return serino;
    }

    public void setSerino(String serino) {
        this.serino = serino;
    }

    public String getPtel1() {
        return ptel1;
    }

    public void setPtel1(String ptel1) {
        this.ptel1 = ptel1;
    }

    public String getPtel() {
        return ptel;
    }

    public void setPtel(String ptel) {
        this.ptel = ptel;
    }
}
