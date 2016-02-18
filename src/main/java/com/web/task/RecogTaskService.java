package com.web.task;

import mybatis.one.mapper.DBRecogsMapper;
import mybatis.one.po.DBRecogs;
import mybatis.one.po.DBRecogsExample;
import org.fuxin.extend.TelePhone;
import org.fuxin.extend.WaveIdentifyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by boshu on 2016/2/4.
 */
@Service
public class RecogTaskService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String sampleFolder = "";    //样本目录

    @Resource
    DBRecogsMapper recogsMapper;


    /**
     * 扫描领取超过5分钟没有拨打成功的电话
     */
    @Scheduled(cron="0 0/5 * * * ?")   //每5分钟执行一次
    public void runSacanWaitCalling(){
        log.info("---------开始扫描领取超过5分钟没有拨打成功的电话---------");
        DBRecogsExample example = new DBRecogsExample();
        example.createCriteria().andStatusEqualTo(2);  // 1 表示 尚未领取 ，2  表示已经领取， 3 表示 已经拨打， 4 表示 已经识别。
        List<DBRecogs> list = recogsMapper.selectByExample(example);
        int count = 0;
        for (DBRecogs recogs : list){
            if (recogs.getReceivetime().getTime() + 5*60*1000 < new Date().getTime()){
                count += 1;
                recogs.setStatus(1);
                recogsMapper.updateByPrimaryKey(recogs);
                log.info(recogs.getMobile()+" 状态由 已经领取 变更为 尚未领取");
            }
        }
        log.info("扫描到已经领取的电话数量:"+list.size()+"  状态变更为尚未领取的数量:"+count);
        log.info("---------完成扫描---------");
    }

    /**
     * 语音识别
     */
    @Scheduled(cron="0 0/5 * * * ?")   //每5分钟执行一次
    public void runIndentify(){

        Properties properties =  new Properties();
        try {
            properties.load(new ClassPathResource("config.properties").getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
        sampleFolder = properties.getProperty("wave.sample.resource.folder");

        WaveIdentifyUtil.setWaveSampleResourceDir(sampleFolder);

        List<DBRecogs> dbRecogsList = queryLast();
        while (dbRecogsList.size()>0){
            ExecutorService executor = Executors.newFixedThreadPool(5);
            for (int i=0;i<dbRecogsList.size();i++){
                DBRecogs dto = dbRecogsList.get(i);
                Runnable worker = new WorkerThread("线程："+dto.getMobile(), dto);
                executor.execute(worker);
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
                // Wait until all threads are finish,and also you can use "executor.awaitTermination();" to wait
            }
            log.info("识别完成");
            dbRecogsList = queryLast();
        }

    }

    public List<DBRecogs> queryLast(){
        DBRecogsExample example = new DBRecogsExample();
        example.createCriteria().andStatusEqualTo(3);//已经拨打的电话记录
        example.setOrderByClause("seqid asc");
        List<DBRecogs> dbRecogsList = recogsMapper.selectByExample(example);
        if (dbRecogsList.size()>20){
            return dbRecogsList.subList(0,20);
        }
        return dbRecogsList;
    }

    class WorkerThread implements Runnable {

        private String command;
        private DBRecogs dbRecogs;

        public WorkerThread(String s, DBRecogs dbRecogs){
            this.command=s;
            this.dbRecogs = dbRecogs;
        }

        @Override
        public void run() {
            log.info(Thread.currentThread().getName()+" Start. Command = "+command);
            processCommand();
            log.info(Thread.currentThread().getName()+" End.");
        }

        private void processCommand() {

            TelePhone phone = new TelePhone(this.dbRecogs.getMobile());
            phone.identifyWave(this.dbRecogs.getDataurl());

            dbRecogs.setStatus(4);//4 表示 已经识别
            dbRecogs.setResult(phone.getStatus().getCode());
            dbRecogs.setRecogtime(new Date());
            recogsMapper.updateByPrimaryKey(dbRecogs);
        }

        @Override
        public String toString(){
            return this.command;
        }
    }
}
