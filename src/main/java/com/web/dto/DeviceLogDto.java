package com.web.dto;

import mybatis.one.po.DBDeviceLog;

/**
 * Created by boshu on 16/4/26.
 */
public class DeviceLogDto extends DBDeviceLog {
    private long difference;

    public long getDifference() {
        return difference;
    }

    public void setDifference(long difference) {
        this.difference = difference;
    }
}
