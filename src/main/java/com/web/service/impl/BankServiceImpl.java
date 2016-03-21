package com.web.service.impl;

import com.common.exception.WException;
import com.common.util.IopUtils;
import com.common.util.MobileUtil;
import com.web.dto.DtoDBRecogs;
import com.web.dto.MTMDataDto;
import com.web.service.IBankService;
import mybatis.one.mapper.*;
import mybatis.one.po.*;
import mybatis.two.mapper.CZNMapper;
import mybatis.two.po.DBMTMContact;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by boshu on 2016/1/1.
 */
@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class BankServiceImpl implements IBankService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    DBRecogsMapper recogsMapper;

    @Resource
    CRecogsMapper cRecogsMapper;

    @Resource
    CZNMapper cznMapper;

    @Resource
    DBBatchLogMapper batchLogMapper;

    @Resource
    DBTmpPhoneMapper tmpPhoneMapper;

    @Resource
    DBImportBatchMapper importBatchMapper;


    // 同步兆能数据
    public void importFromZN(String merchid, String casenostart, Date pickupDate, String mark) throws WException{
        // 案件的 批次id
        String importBatchId = "zn"+new SimpleDateFormat("MMddHHmmss").format(new Date())+"_"+new RandomUtils().nextInt(10);
        // 查询符合导入条件的案件
        List<DBMTMContact> list = cznMapper.queryCase(casenostart+"%");
        log.info("query case :"+list.size());
        if (list.size()==0){
            //没有案件可以导入
            throw new WException(500).setMessage("没有符合条件的数据可以导入");
        }
        // 批量插入 本地案件表
        int s1 = cRecogsMapper.insertCaseBatch(importBatchId, list);
        log.info("batch insert to zncontact:"+s1);
        int s2 = cRecogsMapper.insertCaseData();
        log.info("batch insert to zncasedata:"+s2);
        //查询刚刚插入的案件的全部电话号码
        List<MTMDataDto> mtmDataDtoList = cRecogsMapper.queryPhone(importBatchId);
        log.info("query zncontact distinct:"+mtmDataDtoList.size());
        //格式化电话号码
        List<String> listPhone = new ArrayList<>();
        for (int i = 0; i < mtmDataDtoList.size(); i++) {
            MTMDataDto dataDto = mtmDataDtoList.get(i);
            String phone = MobileUtil.formatPhone(dataDto.getPhone());
            if (IopUtils.isNotEmpty(phone) && phone.length()>5){
                listPhone.add(phone);
            }
        }
        log.info("format zncontact phone:"+listPhone.size());

        cRecogsMapper.deleteTmpPhoneByMerchId(merchid);
        cRecogsMapper.insertTmpPhoneBatch(merchid, listPhone);
        listPhone = cRecogsMapper.selectTmpPhone(merchid);

        //将得到的电话号码列表，以最多2000为单个批次，写入批次表和 批次详情表
        Date createTime = new Date();
        Queue<String> queuePhone=new LinkedList<>();
        queuePhone.addAll(listPhone);

        // 2000笔数据 定义为一个批次
        int count = (int) Math.ceil(listPhone.size()*1.0f / 2000.0f);
        for (int k=0;k<count;k++){
            String batchid = importBatchId+"_"+k;
            log.info("batchid:"+batchid);
            DBImportBatch importBatch = new DBImportBatch();
            importBatch.setImportbatchid(importBatchId);
            importBatch.setBatchid(batchid);
            importBatch.setCreatetime(new Date());
            importBatchMapper.insert(importBatch);

            DBBatchLog batchLog = new DBBatchLog();
            batchLog.setMerchid(merchid);
            batchLog.setBatchid(batchid);
            batchLog.setCreatetime(createTime);
            batchLog.setMark(mark+"_"+k);
            // 每天只预约2000个，多余的延后到明天这个时间点 继续拨打
            batchLog.setPickuptime(new Date(pickupDate.getTime() + 24*60*60*1000*k));

            List<String> list1 = new ArrayList<>();
            for (int i = 0; i < 2000; i++) {
                if (queuePhone.size()==0){
                    break;
                }
                list1.add(queuePhone.poll());
            }
            batchLog.setTotalcount(list1.size());
            batchLogMapper.insert(batchLog);
            cRecogsMapper.insertTmpPhoneToRecogsBatch(merchid, batchid, list1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        }
    }

    /**
     * 从临时表获取数据 插入批次表，批次详情表
     * @param merchid
     */
    public void insertMobiles(String merchid, String importBatchid, Date pickupDate, String mark){
        List<String> dbTmpPhones = cRecogsMapper.selectTmpPhone(merchid);
        Date createTime = new Date();
        Queue<String> queuePhone=new LinkedList<>();
        queuePhone.addAll(dbTmpPhones);

        // 2000笔数据 定义为一个批次
        int count = (int) Math.ceil(dbTmpPhones.size()*1.0f / 2000.0f);
        for (int k=0;k<count;k++){
            String batchid = importBatchid+"_"+k;
            DBImportBatch importBatch = new DBImportBatch();
            importBatch.setImportbatchid(importBatchid);
            importBatch.setBatchid(batchid);
            importBatch.setCreatetime(new Date());
            importBatchMapper.insert(importBatch);

            DBBatchLog batchLog = new DBBatchLog();
            batchLog.setMerchid(merchid);
            batchLog.setBatchid(batchid);
            batchLog.setCreatetime(createTime);
            batchLog.setMark(mark+"_"+k);
            // 每天只预约2000个，多余的延后到明天这个时间点 继续拨打
            batchLog.setPickuptime(new Date(pickupDate.getTime() + 24*60*60*1000*k));

            List<String> listPhone = new ArrayList<>();
            for (int i = 0; i < 2000; i++) {
                if (queuePhone.size()==0){
                    break;
                }
                listPhone.add(queuePhone.poll());
            }
            batchLog.setTotalcount(listPhone.size());
            batchLogMapper.insert(batchLog);
            cRecogsMapper.insertTmpPhoneToRecogsBatch(merchid, batchid, listPhone, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        }
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
        if (IopUtils.isEmpty(recogs.getMobile())
                || recogs.getMobile().length()>13
                || recogs.getMobile().length()<8){
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
        // 总的呼叫次数
        Integer callcount = recogs.getCallcount();
        if (callcount==null) callcount= 0;
        // 总的识别为无声的次数
        Integer silentcount = recogs.getSilentcount();
        if (silentcount==null) silentcount= 0;
        if (silentcount>=5){
            recogs.setStatus(4); //呼叫次数过多的 ，不再识别
            recogs.setManualresult(-1);
            recogs.setReceivetime(new Date());
            recogsMapper.updateByPrimaryKey(recogs);
            throw new WException(500).setMessage("当前领取到的号码识别为无声的次数过多，不再领取");
        }
        // 已领取变更为未领取的次数
        Integer dialcount = recogs.getDialcount();
        if (dialcount==null) dialcount=0;
        if (dialcount>=5){
            recogs.setStatus(11); //重打的次数太多，设置为 拨打重试失败
            recogs.setReceivetime(new Date());
            recogsMapper.updateByPrimaryKey(recogs);
            throw new WException(500).setMessage("当前领取到的号码重试拨打的次数过多，不再领取");
        }

        recogs.setStatus(2);
        recogs.setResult(-1);
        recogs.setManualresult(-1);
        recogs.setReceivetime(new Date());

        recogs.setCallcount(callcount+1);
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
