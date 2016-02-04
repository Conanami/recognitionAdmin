package com.web.task.impl;

import com.web.task.IRecogTaskService;
import mybatis.one.mapper.DBRecogsMapper;
import mybatis.one.po.DBRecogs;
import mybatis.one.po.DBRecogsExample;
import org.fuxin.extend.TelePhone;
import org.fuxin.extend.WaveIdentifyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by boshu on 2016/2/4.
 */
@Service
public class RecogTaskServiceImpl implements IRecogTaskService{
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String sampleFolder = "";    //样本目录

    @Resource
    DBRecogsMapper recogsMapper;

    @Scheduled(cron="0 0/5 * * * ?")   //每5分钟执行一次
    @Override
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
