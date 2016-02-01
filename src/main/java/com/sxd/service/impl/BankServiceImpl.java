package com.sxd.service.impl;

import com.common.exception.WException;
import com.sxd.service.IBankService;
import mybatis.one.mapper.CRecogsMapper;
import mybatis.one.mapper.DBRecogsMapper;
import mybatis.one.po.DBRecogs;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by boshu on 2016/1/1.
 */
@Service
public class BankServiceImpl implements IBankService {
    @Resource
    DBRecogsMapper recogsMapper;

    @Resource
    CRecogsMapper cRecogsMapper;


    /**
     * 领取手机号 用于拨打电话
     * @param merchid
     * @throws Exception
     */
    public DBRecogs pickup(String merchid) throws Exception{
        //status 状态 空 或者 1 表示 尚未 领取 ，2  表示已经领取， 3 表示 已经拨打， 4 表示 已经识别。
        DBRecogs recogs = cRecogsMapper.pickup(merchid);
        if (recogs==null){
            throw new WException(500).setMessage("没有待识别的记录");
        }
        recogs.setStatus(2);
        recogs.setReceivetime(new Date());
        recogsMapper.updateByPrimaryKey(recogs);
        return recogs;
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
            String merchid,
            String mobile,
            Integer start,
            Integer pagesize){
        return cRecogsMapper.selectRecogs(merchid, mobile, start, pagesize);
    }

    public Integer totalRecogs(
            String merchid,
            String mobile
    ){
        return cRecogsMapper.totalRecogs(merchid, mobile);
    }
}
