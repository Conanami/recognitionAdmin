package com.web.dto;

/**
 * Created by boshu on 2016/3/25.
 */
public class BatchStatisticForm {
    private Integer zcSize = 0;
    Integer tjSize = 0;
    Integer gjSize = 0;
    Integer khSize = 0;
    Integer wsSize = 0;
    Integer otherSize = 0;

    public Integer getGjSize() {
        return gjSize;
    }

    public void setGjSize(Integer gjSize) {
        this.gjSize = gjSize;
    }

    public Integer getKhSize() {
        return khSize;
    }

    public void setKhSize(Integer khSize) {
        this.khSize = khSize;
    }

    public Integer getOtherSize() {
        return otherSize;
    }

    public void setOtherSize(Integer otherSize) {
        this.otherSize = otherSize;
    }

    public Integer getTjSize() {
        return tjSize;
    }

    public void setTjSize(Integer tjSize) {
        this.tjSize = tjSize;
    }

    public Integer getWsSize() {
        return wsSize;
    }

    public void setWsSize(Integer wsSize) {
        this.wsSize = wsSize;
    }

    public Integer getZcSize() {
        return zcSize;
    }

    public void setZcSize(Integer zcSize) {
        this.zcSize = zcSize;
    }
}
