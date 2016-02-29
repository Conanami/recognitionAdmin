package com.web.dto;

import mybatis.one.po.DBRecogs;

import java.util.Date;

/**
 * Created by boshu on 2016/2/26.
 */
public class DtoDBRecogs extends DBRecogs {
    private Date pickuptime;
    private String mark;

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
}
