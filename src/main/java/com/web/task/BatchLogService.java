package com.web.task;

import com.web.dto.DtoDBRecogs;
import mybatis.one.mapper.CRecogsMapper;
import mybatis.one.mapper.DBBatchLogMapper;
import mybatis.one.mapper.DBRecogsMapper;
import mybatis.one.mapper.DBZNUpdateInfoMapper;
import mybatis.one.po.*;
import mybatis.two.mapper.CZNMapper;
import mybatis.two.mapper.DBMTMContactMapper;
import mybatis.two.po.DBMTMCaseData;
import mybatis.two.po.DBMTMCaseDataExample;
import mybatis.two.po.DBMTMContact;
import mybatis.two.po.DBMTMContactExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Resource
    CRecogsMapper cRecogsMapper;

    @Resource
    CZNMapper cznMapper;

    @Resource
    DBMTMContactMapper contactMapper;

    @Resource
    DBZNUpdateInfoMapper znUpdateInfoMapper;

    @Scheduled(fixedRate = 3*60*1000)   //每5分钟执行一次
    public void run(){
//        log.info("---------start batchlog analysis---------");
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
//                log.info("batchid："+batchLog.getBatchid()+"  call complete count:"+batchLog.getCallcount());
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
//                log.info("batchid："+batchLog.getBatchid()+"  recog complete count:"+batchLog.getRecogcount());
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
//        log.info("---------complete batchlog analysis---------");
    }

    //将 识别完的数据，更新回 兆能资产的数据库
    @Scheduled(fixedRate = 3*60*1000)   //每3分钟执行一次
    public void updateZNDB(){
        List<DtoDBRecogs> list = cRecogsMapper.queryRecogResult();
        for (int i = 0; i < list.size(); i++) {
            DtoDBRecogs recogs = list.get(i);
            String result = "";
            switch (recogs.getResult()){
                case 1:         //表示正常
                    result = "019";
                    break;
                case 2:         //欠费停机
                    result = "006";
                    break;
                case 3:         //空号
                    result = "008";
                    break;
                case 4:         //关机
                    result = "012";
                    break;
            }
            //更新到联系人表 对应电话的 对应状态
            if (recogs.getMobile().equals(recogs.getPtel())){
                cznMapper.updateMTMContactTelCk(recogs.getCaseno(), recogs.getSerino(), result);
            }
            if (recogs.getMobile().equals(recogs.getPtel1())){
                cznMapper.updateMTMContactTel1Ck(recogs.getCaseno(), recogs.getSerino(), result);
            }

            //更新对应状态记录
            recogs.setStatus(8);  //8代表已经写回数据库
            recogsMapper.updateByPrimaryKey(recogs);

            // 更新到 日志库
            Map<String, String> contact = cznMapper.queryContactByCaseNo(recogs.getCaseno(), recogs.getSerino());
            DBZNUpdateInfo dbznUpdateInfo = new DBZNUpdateInfo();
            dbznUpdateInfo.setCaseno(contact.get("Case_No"));
            dbznUpdateInfo.setPtel(contact.get("PTel"));
            dbznUpdateInfo.setPtel1(contact.get("PTel1"));
            dbznUpdateInfo.setTelck(contact.get("TelCK"));
            dbznUpdateInfo.setTel1ck(contact.get("Tel1CK"));
            dbznUpdateInfo.setPname(contact.get("PName"));
            dbznUpdateInfo.setBatchid(recogs.getBatchid());
            dbznUpdateInfo.setPhone(recogs.getMobile());
            dbznUpdateInfo.setCreatetime(new Date());
            znUpdateInfoMapper.insert(dbznUpdateInfo);
        }
    }
}
