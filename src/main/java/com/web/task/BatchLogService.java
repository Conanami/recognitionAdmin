package com.web.task;

import mybatis.one.mapper.DBBatchLogMapper;
import mybatis.one.mapper.DBRecogsMapper;
import mybatis.one.po.DBBatchLog;
import mybatis.one.po.DBBatchLogExample;
import mybatis.one.po.DBRecogs;
import mybatis.one.po.DBRecogsExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 处理批次的拨打时间，识别时间的统计
 * Created by boshu on 2016/2/18.
 */
@Service
public class BatchLogService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    DBBatchLogMapper batchLogMapper;

    @Resource
    DBRecogsMapper recogsMapper;

    @Scheduled(fixedRate = 3*60*1000)   //每5分钟执行一次
    public void run(){
        log.info("---------start batchlog analysis---------");
        DBBatchLogExample example = new DBBatchLogExample();
        example.createCriteria().andBatchidIsNotNull();
        List<DBBatchLog> dbBatchLogs = batchLogMapper.selectByExample(example);
        for (DBBatchLog batchLog : dbBatchLogs ) {
            {
                //分析拨打时间
                DBRecogsExample ex = new DBRecogsExample();
                ex.createCriteria().andBatchidEqualTo(batchLog.getBatchid()).andCalltimeIsNotNull();
                ex.setOrderByClause("calltime asc");
                List<DBRecogs> dbRecogsList = recogsMapper.selectByExample(ex);
                batchLog.setCallcount(dbRecogsList.size());
                log.info("batchid："+batchLog.getBatchid()+"  call complete count:"+batchLog.getCallcount());
                Date start = null;
                Date end = null;
                if (dbRecogsList.size()>0){
                    start = dbRecogsList.get(0).getCalltime();
                    end = dbRecogsList.get(dbRecogsList.size()-1).getCalltime();
                }
                batchLog.setCallstarttime(start);
                if (dbRecogsList.size()== batchLog.getTotalcount()){
                    batchLog.setCallendtime(end);
                }else{
                    batchLog.setCallendtime(null);
                }
                if (start!=null && batchLog.getCallendtime()!=null){
                    long dd = (end.getTime() - start.getTime()) /1000;
                    batchLog.setTotalcalltime(dd);
                }
                batchLogMapper.updateByPrimaryKey(batchLog);
            }
            {
                //分析识别时间
                DBRecogsExample ex = new DBRecogsExample();
                ex.createCriteria().andBatchidEqualTo(batchLog.getBatchid()).andRecogtimeIsNotNull();
                ex.setOrderByClause("recogtime asc");
                List<DBRecogs> dbRecogsList = recogsMapper.selectByExample(ex);
                batchLog.setRecogcount(dbRecogsList.size());
                log.info("batchid："+batchLog.getBatchid()+"  recog complete count:"+batchLog.getRecogcount());
                Date start = null;
                Date end = null;
                if (dbRecogsList.size()>0){
                    start = dbRecogsList.get(0).getRecogtime();
                    end = dbRecogsList.get(dbRecogsList.size()-1).getRecogtime();
                }
                batchLog.setRecogstarttime(start);
                if (dbRecogsList.size()== batchLog.getTotalcount()){
                    batchLog.setRecogendtime(end);
                }else{
                    batchLog.setRecogendtime(null);
                }
                if (start!=null && batchLog.getRecogendtime()!=null){
                    long dd = (end.getTime() - start.getTime()) /1000;
                    batchLog.setTotalrecogtime(dd);
                }
                batchLogMapper.updateByPrimaryKey(batchLog);
            }
        }
        log.info("---------complete batchlog analysis---------");
    }
}
