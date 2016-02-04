package org.fuxin.extend;

/**
 * Created by boshu on 2016/2/4.
 */
public enum PhoneStatus {
    work(1, "正常")
    ,stopped(2, "停机")
    ,empty(3, "空号")
    ,closed(4, "关机")
    ;

    private Integer code;
    private String simpleName;
    private PhoneStatus(Integer code, String simpleName)
    {
        this.code = code;
        this.simpleName = simpleName;
    }

    public Integer getCode() {
        return code;
    }

    public String getSimpleName() {
        return simpleName;
    }
}
