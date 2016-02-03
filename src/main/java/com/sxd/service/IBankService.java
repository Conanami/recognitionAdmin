package com.sxd.service;

import mybatis.one.po.DBRecogs;

import java.util.List;

public interface IBankService {

    /**
     * 批量插入手机号码
     * @param merchid
     * @param list
     */
    public void insertMobiles(String merchid, String batchid, List<String> list);

    /**
     * 领取手机号 用于拨打电话
     * @param merchid
     * @throws Exception
     */
	public DBRecogs pickup(String merchid) throws Exception;

    /**
     * 保存录音文件
     * @param seqid
     * @param dataurl
     * @throws Exception
     */
    public void saveAudioInfo(Long seqid, String zjmobile, String dataurl) throws Exception;

    /**
     * 查询 识别记录
     * @param start
     * @param pagesize
     * @return
     */
    public List<DBRecogs> selectRecogs(
            String merchid,
            String mobile,
            Integer start,
            Integer pagesize);

    public Integer totalRecogs(
            String merchid,
            String mobile
    );
}
