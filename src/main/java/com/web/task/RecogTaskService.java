package com.web.task;

import mybatis.one.mapper.DBImgZBMapper;
import mybatis.one.mapper.DBRecogsMapper;
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
     * 扫描领取超过1分钟没有拨打成功的电话
     */
    @Scheduled(cron="0 0/3 * * * ?")   //每5分钟执行一次
    public void runSacanWaitCalling(){
        log.info("---------开始扫描领取超过1分钟没有拨打成功的电话---------");
        DBRecogsExample example = new DBRecogsExample();
        example.createCriteria().andStatusEqualTo(2);  // 1 表示 尚未领取 ，2  表示已经领取， 3 表示 已经拨打， 4 表示 已经识别。
        List<DBRecogs> list = recogsMapper.selectByExample(example);
        int count = 0;
        for (DBRecogs recogs : list){
            if (recogs.getReceivetime().getTime() + 3*60*1000 < new Date().getTime()){
                count += 1;
                recogs.setStatus(1);
                recogsMapper.updateByPrimaryKey(recogs);
                log.info(recogs.getMobile()+" 状态由 已经领取 变更为 尚未领取");
            }
        }
        log.info("扫描到已经领取的电话数量:"+list.size()+"  状态变更为尚未领取的数量:"+count);
        log.info("---------完成扫描---------");
    }
}
