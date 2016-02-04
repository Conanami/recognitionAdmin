package org.fuxin.extend;

/**
 * Created by boshu on 2016/2/4.
 */
public enum PhoneStatus {
    work(1, "正常")
    ,stopped(2, "停机")
    ,closed(3, "关机")
    ,empty(4, "空号")
    ;

    private Integer seqid;
    private String simpleName;
    private PhoneStatus(Integer seqid, String simpleName)
    {
        this.seqid = seqid;
        this.simpleName = simpleName;
    }
}
