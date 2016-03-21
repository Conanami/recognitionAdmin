package com.web.task;

import mybatis.one.mapper.DBImgZBMapper;
import mybatis.one.mapper.DBRecogsMapper;
import mybatis.one.po.DBImgZB;
import mybatis.one.po.DBImgZBExample;
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
 * Created by boshu on 2016/2/4.
 */
@Service
public class RecogTaskService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String imageserverUrl;

    private String sampleFolder = "";    //样本目录

    @Resource
    DBRecogsMapper recogsMapper;

    @Resource
    DBImgZBMapper imgZBMapper;


    /**
     * 扫描领取超过30分钟没有拨打成功的电话,  扫描频率 30分钟一次。
     */
    @Scheduled(fixedRate = 30*60*1000)   //间隔单位毫秒
    public void runSacanWaitCalling(){
        DBRecogsExample example = new DBRecogsExample();
        example.createCriteria().andStatusEqualTo(2);  // 1 表示 尚未领取 ，2  表示已经领取， 3 表示 已经拨打， 4 表示 已经识别。
        List<DBRecogs> list = recogsMapper.selectByExample(example);
        int count = 0;
        for (DBRecogs recogs : list){
            if (recogs.getReceivetime().getTime() + 30*60*1000 < new Date().getTime()){
                count += 1;
                recogs.setStatus(1);
                Integer dialcount = recogs.getDialcount();
                if (dialcount==null) dialcount=0;
                recogs.setDialcount(dialcount+1);
                recogsMapper.updateByPrimaryKey(recogs);
//                log.info(recogs.getMobile()+" 状态由 已经领取 变更为 尚未领取");
            }
        }
    }


    /**
     * 扫描需要 重新识别的号码  9 表示需要重新识别
     */
    @Scheduled(fixedRate = 5*1000)   //间隔单位毫秒
    public void runSacan2(){
        DBRecogsExample example = new DBRecogsExample();
        example.createCriteria().andManualresultEqualTo(9).andCalltimeIsNotNull();
        List<DBRecogs> list = recogsMapper.selectByExample(example);
        for (DBRecogs recogs : list){
            recogs.setStatus(3);  //状态变更为 已经拨打
            recogs.setResult(null);
            recogs.setManualresult(null);
            recogs.setRecogtime(null);
            recogsMapper.updateByPrimaryKey(recogs);
        }
    }
}
