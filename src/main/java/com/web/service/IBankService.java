package com.web.service;

import mybatis.one.po.DBRecogs;

import java.util.List;

public interface IBankService {

    /**
     * 批量插入手机号码
     * @param merchid
     * @param list
     */
    public void insertMobiles(String merchid, String batchid, String mark, List<String> list);

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
            DBRecogs queryParams,
            Integer start,
            Integer pagesize);

    public Integer totalRecogs(
            DBRecogs queryParams
    );

    /**
     * 查询批次
     * @param merchid
     * @param batchid
     * @param start
     * @param pagesize
     * @return
     */
    public List<DBRecogs> selectBatchLogs(
            String merchid,
            String batchid,
            Integer start,
            Integer pagesize);

    public Integer totalBatchLogs(
            String batchid,
            String mobile
    );
}
