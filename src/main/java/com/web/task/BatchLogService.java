package com.web.task;

import com.common.util.IopUtils;
import com.web.dto.DtoDBRecogs;
import mybatis.one.mapper.*;
import mybatis.one.po.*;
import mybatis.two.mapper.CZNMapper;
import mybatis.two.mapper.DBMTMContactMapper;
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

    @Resource
    DBZNContactMapper localContactMapper;

    @Resource
    DBZNCaseDataMapper caseDataMapper;

    @Scheduled(fixedRate = 3*60*1000)   //每3分钟执行一次
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

    //将 识别完的数据，更新回 兆能资产的数据库 联系人表
    @Scheduled(fixedRate = 3*60*1000)   //每3分钟执行一次
    public void updateZNContact(){
        List<DtoDBRecogs> list = cRecogsMapper.queryRecogResult();
        for (int i = 0; i < list.size(); i++) {
            DtoDBRecogs recogs = list.get(i);
            String result = "";
            if (recogs.getStatus()==4){// 识别正常的号码，才进行 空停关正常的判断
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
            }
            if (recogs.getStatus()==12){// 无声， 识别重试失败，也表示正常
                result = "019";
            }
            if (recogs.getStatus()==9){// 号码异常，也标记为正常
                result = "019";
            }
            if (recogs.getStatus()==11){// 拨打重试失败， 标记为 正常
                result = "019";
            }
            if (IopUtils.isEmpty(result)){
                log.info("result is empty, "+recogs.getMobile());
                continue;
            }
            // 更新对应号码识别状态到 zncontact 表
            {
                DBZNContactExample example = new DBZNContactExample();
                example.createCriteria().andCasenoEqualTo(recogs.getCaseno()).andSerinoEqualTo(recogs.getSerino());
                List<DBZNContact> list1 = localContactMapper.selectByExample(example);
                for (DBZNContact dbznContact : list1) {
                    if (recogs.getMobile().equals(dbznContact.getPtel())){
                        dbznContact.setTelck(result);
                    }
                    if (recogs.getMobile().equals(dbznContact.getPtel1())) {
                        dbznContact.setTel1ck(result);
                    }
                    boolean pass = false;
                    pass = pass ||  ( IopUtils.isNotEmpty(dbznContact.getPtel()) && IopUtils.isEmpty(dbznContact.getTelck()) );
                    pass = pass ||  ( IopUtils.isNotEmpty(dbznContact.getPtel1()) && IopUtils.isEmpty(dbznContact.getTel1ck()) );
                    if (!pass){
                        dbznContact.setSeqstatus("101");//如果 联系人的号码都被识别过了。状态变成101
                    }
                    localContactMapper.updateByPrimaryKey(dbznContact);

                    //写入日志
                    DBZNUpdateInfo dbznUpdateInfo = new DBZNUpdateInfo();
                    dbznUpdateInfo.setCaseno(dbznContact.getCaseno());
                    dbznUpdateInfo.setPtel(dbznContact.getPtel());
                    dbznUpdateInfo.setPtel1(dbznContact.getPtel1());
                    dbznUpdateInfo.setTelck(dbznContact.getTelck());
                    dbznUpdateInfo.setTel1ck(dbznContact.getTel1ck());
                    dbznUpdateInfo.setPname(dbznContact.getPname());
                    dbznUpdateInfo.setBatchid(recogs.getBatchid());
                    dbznUpdateInfo.setPhone(recogs.getMobile());
                    dbznUpdateInfo.setCreatetime(new Date());
                    znUpdateInfoMapper.insert(dbznUpdateInfo);
                }
            }

            // 识别 案件的状态
            {
                DBZNCaseData caseData = caseDataMapper.selectByPrimaryKey(recogs.getCaseno());
                // 如果案件还没有识别状态
                if (IopUtils.isEmpty(caseData.getCategorize2())){
                    if (cRecogsMapper.selectNoRecogContactCase(recogs.getCaseno())==0 ){
                        int asize = cRecogsMapper.countForStatusA(recogs.getCaseno());
                        if (asize>0){
                            caseData.setCategorize2("A");
                        }else if(cRecogsMapper.countForStatusB(recogs.getCaseno()) >0 ){
                            caseData.setCategorize2("B");
                        }else if(cRecogsMapper.countForStatusC(recogs.getCaseno()) >0 ){
                            caseData.setCategorize2("C");
                        }else{
                            caseData.setCategorize2("D");
                        }
                    }
                    if (IopUtils.isNotEmpty(caseData.getCategorize2())){
                        caseData.setCreatetime(new Date());
                        caseDataMapper.updateByPrimaryKey(caseData);
                        log.info("complete recog case "+caseData.getCaseno()+" result:"+caseData.getCategorize2());
                    }
                }
            }

            //更新对应状态记录
            recogs.setStatus(8);  //8代表已经写回数据库
            recogsMapper.updateByPrimaryKey(recogs);

        }
    }

    // 批次写回 兆能的数据库
    @Scheduled(fixedRate = 15*60*1000)   //每15分钟执行一次
    public void updateZNCaseStatus(){
        List<DBZNContact> dbznContactList = cRecogsMapper.selectZNContactResult();
        if (dbznContactList.size()>0){
            int size = cznMapper.batchUpdateMTMContact(dbznContactList);
            log.info("successed update MTMContact size:"+size);
            for (DBZNContact dbznContact : dbznContactList) {
                dbznContact.setSeqstatus("201");
                localContactMapper.updateByPrimaryKey(dbznContact);
            }
            log.info("successed update zncontact size:"+dbznContactList.size());
        }

        List<DBZNCaseData> dbznCaseDataList = cRecogsMapper.selectZNCaseDataResult();
        if (dbznCaseDataList.size()>0){
            int size = cznMapper.batchUpdateMTMCaseData(dbznCaseDataList);
            log.info("successed update MTMCaseData size:"+size);
            for (DBZNCaseData caseData : dbznCaseDataList) {
                caseData.setStatus("201");
                caseDataMapper.updateByPrimaryKey(caseData);
            }
            log.info("successed update zncasedata size:"+dbznCaseDataList.size());
        }
    }
}
