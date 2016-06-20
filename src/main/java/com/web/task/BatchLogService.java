package com.web.task;

import com.common.util.IopUtils;
import com.web.dto.AppSingle;
import com.web.dto.DtoDBRecogs;
import mybatis.one.mapper.*;
import mybatis.one.po.*;
import mybatis.two.mapper.CZNMapper;
import mybatis.two.mapper.DBMTMContactMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

//    @Value("${recog.setting}")
//    private String recog_setting_url = "";      //识别程序设置属性 url

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

    //将 识别完的数据，更新回 兆能资产的数据库 联系人表
    @Scheduled(fixedRate = 3*60*1000)   //每3分钟执行一次
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void updateZNMappedTable(){
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
            updateZNContact(recogs, result);

            // 更新 案件的状态
            updateCaseData(recogs.getCaseno());

            //更新对应状态记录
            recogs.setStatus(8);  //8代表已经写回数据库
            recogsMapper.updateByPrimaryKey(recogs);

        }
    }

    // 更新映射的 兆能联系人表
    public void updateZNContact(DtoDBRecogs recogs, String result){
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

    // 更新案件的状态
    public void updateCaseData(String caseno){
        DBZNCaseData caseData = caseDataMapper.selectByPrimaryKey(caseno);
        // 如果案件还没有识别状态
        if (IopUtils.isEmpty(caseData.getCategorize2())){
            if (cRecogsMapper.selectNoRecogContactCase(caseno)==0 ){
                int asize = cRecogsMapper.countForStatusA(caseno);
                if (asize>0){
                    caseData.setCategorize2("A");
                }else if(cRecogsMapper.countForStatusB(caseno) >0 ){
                    caseData.setCategorize2("B");
                }else if(cRecogsMapper.countForStatusC(caseno) >0 ){
                    caseData.setCategorize2("C");
                }else{
                    caseData.setCategorize2("D");
                }
            }
            log.info("案件 "+caseData.getCaseno()+" 状态识别为 "+caseData.getCategorize2());
            if (IopUtils.isNotEmpty(caseData.getCategorize2())){
                caseData.setCreatetime(new Date());
                caseData.setStatus("101");
                caseDataMapper.updateByPrimaryKey(caseData);
                log.info("complete recog case "+caseData.getCaseno()+" result:"+caseData.getCategorize2());
            }
        }
    }

    // 识别案件
    @Scheduled(fixedRate = 15*60*1000)   //每15分钟执行一次
    public void queryAllCaseData(){
        DBZNCaseDataExample example = new DBZNCaseDataExample();
        example.createCriteria().andCategorize2EqualTo("");
        List<DBZNCaseData> list = caseDataMapper.selectByExample(example);
        log.info("查询得到"+list.size()+" 笔未识别的案件记录");
        for (DBZNCaseData caseData : list) {
            updateCaseData(caseData.getCaseno());
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
