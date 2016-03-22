package com.web.dto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by boshu on 2016/3/4.
 */
public enum AppSingle {
    instance;
    public Queue<DtoDBRecogs> phoneList = new LinkedList<>();

    // 无声重试次数
    public Integer silentLimit = 3;

    // 已领取变更为未领取 重试拨打次数
    public Integer dialLimit = 3;
}
