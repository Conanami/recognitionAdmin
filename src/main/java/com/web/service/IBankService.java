package com.web.service;

import com.common.exception.WException;
import com.web.dto.DtoDBRecogs;
import mybatis.one.po.DBRecogs;
import mybatis.two.po.DBMTMContact;

import java.util.Date;
import java.util.List;

public interface IBankService {

    // 从兆能资产读取数据写入本地
    public void importFromZN(String merchid, String casenostart, Date pickupDate, String mark) throws WException;
    /**
     * 批量插入手机号码
     * @param merchid
     */
    public void insertMobiles(String merchid, String batchid, Date pickupDate, String mark);

    /**
     * 领取手机号 用于拨打电话
     * @param merchid
     * @throws Exception
     */
	public DtoDBRecogs pickup(String merchid) throws Exception;

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
