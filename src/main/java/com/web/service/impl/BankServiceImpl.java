package com.web.service.impl;

import com.common.exception.WException;
import com.common.util.IopUtils;
import com.web.dto.DtoDBRecogs;
import com.web.service.IBankService;
import mybatis.one.mapper.CRecogsMapper;
import mybatis.one.mapper.DBBatchLogMapper;
import mybatis.one.mapper.DBRecogsMapper;
import mybatis.one.po.DBBatchLog;
import mybatis.one.po.DBRecogs;
import mybatis.one.po.DBRecogsExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by boshu on 2016/1/1.
 */
@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class BankServiceImpl implements IBankService {
    @Resource
    DBRecogsMapper recogsMapper;

    @Resource
    CRecogsMapper cRecogsMapper;

    @Resource
    DBBatchLogMapper batchLogMapper;

    /**
     * 批量插入手机号码
     * @param merchid
     * @param list
     */
    public void insertMobiles(String merchid, String batchid, Date pickupDate, String mark,  List<String> list){
        Date createTime = new Date();

        DBBatchLog batchLog = new DBBatchLog();
        batchLog.setMerchid(merchid);
        batchLog.setBatchid(batchid);
        batchLog.setCreatetime(createTime);
        batchLog.setMark(mark);
        batchLog.setPickuptime(pickupDate);

        int k=0;
        for (int i=0;i<list.size();i++){
            DBRecogs recogs = new DBRecogs();
            recogs.setMerchid(merchid);
            recogs.setBatchid(batchid);
            String mobile = list.get(i);
            if (mobile.length()>40){
                mobile = mobile.substring(0,40);
            }
            recogs.setMobile(mobile);
            recogs.setCreatetime(createTime);
            recogsMapper.insert(recogs);
            k++;
        }

        batchLog.setTotalcount(k);
        batchLogMapper.insert(batchLog);

    }
    /**
     * 领取手机号 用于拨打电话
     * @param merchid
     * @throws Exception
     */
    public DtoDBRecogs pickup(String merchid) throws Exception{
        //status 状态 空 或者 1 表示 尚未 领取 ，2  表示已经领取， 3 表示 已经拨打， 4 表示 已经识别。
        DtoDBRecogs recogs = cRecogsMapper.pickup(merchid);
        if (recogs==null){
            throw new WException(500).setMessage("没有待识别的记录");
        }
        {
            //过滤手机号码中的非数字字符
            String str = recogs.getMobile();
            String tmpStr="";
            if(str.length()>0){
                for(int i=0;i<str.length();i++){
                    String tmp=""+str.charAt(i);
                    if((tmp).matches("[0-9.]") || tmp.equals("-")){
                        tmpStr+=tmp;
                    }
                }
            }
            recogs.setMobile(tmpStr);
        }
        if (IopUtils.isEmpty(recogs.getMobile()) || recogs.getMobile().length()>11){
            recogs.setStatus(9); // 9 为 状态 号码异常
            recogs.setResult(-1);
            recogs.setManualresult(-1);
            recogs.setReceivetime(new Date());
            recogs.setCalltime(new Date());
            recogs.setRecogtime(new Date());
            recogs.setDataurl(null);
            updateRecogs(recogs);

            throw new WException(500).setMessage("当前记录手机号为空");
        }
        recogs.setStatus(2);
        recogs.setResult(-1);
        recogs.setManualresult(-1);
        recogs.setReceivetime(new Date());
        recogsMapper.updateByPrimaryKey(recogs);

        {
            //查询当前记录 在批次里面的顺序号，从 1开始
            DBRecogsExample example = new DBRecogsExample();
            example.createCriteria().andBatchidEqualTo(recogs.getBatchid());
            example.setOrderByClause("seqid asc ");
            List<DBRecogs> list = recogsMapper.selectByExample(example);
            if (list.size()>0){
                Long startid = list.get(0).getSeqid();
                recogs.setOrderid(recogs.getSeqid()-startid+1);
            }
        }
        return recogs;
    }

    private void updateRecogs(DBRecogs recogs){
        recogsMapper.updateByPrimaryKey(recogs);
    }

    /**
     * 保存录音文件
     * @param seqid
     * @param dataurl
     * @throws Exception
     */
    public void saveAudioInfo(Long seqid, String zjmobile, String dataurl) throws Exception{
        DBRecogs recogs = recogsMapper.selectByPrimaryKey(seqid);
        if (recogs==null){
            throw new WException(500).setMessage("没有对应记录");
        }
        //status 状态 空 或者 1 表示 尚未 领取 ，2  表示已经领取， 3 表示 已经拨打， 4 表示 已经识别。
        recogs.setStatus(3);
        recogs.setZjmobile(zjmobile);
        recogs.setDataurl(dataurl);
        recogs.setCalltime(new Date());
        recogsMapper.updateByPrimaryKey(recogs);
    }

    /**
     * 查询 识别记录
     * @param start
     * @param pagesize
     * @return
     */
    public List<DBRecogs> selectRecogs(
            DBRecogs queryParams,
            Integer start,
            Integer pagesize){
        return cRecogsMapper.selectRecogs(queryParams.getBatchid(), queryParams.getStatus(), queryParams.getResult(),
                queryParams.getManualresult(), queryParams.getMobile(), start, pagesize);
    }

    public Integer totalRecogs(
            DBRecogs queryParams
    ){
        return cRecogsMapper.totalRecogs(queryParams.getBatchid(), queryParams.getStatus(),
                queryParams.getResult(), queryParams.getManualresult(), queryParams.getMobile());
    }

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
            Integer pagesize){
        return cRecogsMapper.selectBatchLogs(merchid, batchid, start, pagesize);
    }

    public Integer totalBatchLogs(
            String merchid,
            String batchid
    ){
        return cRecogsMapper.totalBatchLogs(merchid, batchid);
    }
}
