package com.web.controller;

import com.common.exception.ExceptionConst;
import com.common.exception.ExceptionFormatter;
import com.common.exception.WException;
import com.common.util.*;
import com.web.dto.*;
import com.web.service.IBankService;
import mybatis.one.mapper.CRecogsMapper;
import mybatis.one.mapper.DBBatchLogMapper;
import mybatis.one.mapper.DBDeviceLogMapper;
import mybatis.one.mapper.DBRecogsMapper;
import mybatis.one.po.*;
import net.sf.json.JSONObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by boshu on 2016/1/1.
 */
@RestController
public class BatchRestController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${merchmanager.xml}")
    private String merchManagerUrl = "";    //商户管理信息的xml配置文件地址

    @Value("${computer.sign}")
    private String computer_sign = "";      //计算机环境签名

    @Resource
    IBankService bankService;

    @Resource
    DBRecogsMapper recogsMapper;

    @Resource
    CRecogsMapper cRecogsMapper;

    @Resource
    DBBatchLogMapper batchLogMapper;

    @Resource
    DBDeviceLogMapper deviceLogMapper;

    @ExceptionHandler(Exception.class)
    public WSResponse<Boolean> exceptionHandler(Exception ex, HttpSession httpSession) {
        WSResponse<Boolean> response = new WSResponse<>();
        log.error("Exception：" + ex);
        ExceptionFormatter.setResponse(response, ex);
        return response;
    }

    /**
     * 批次导入记录查询
     * @param merchid
     * @param batchid
     * @param sort
     * @param order
     * @param page
     * @param pagesize
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.batchlog.query")
    public WSResponse<DBRecogs> api_batchlog_query(
            @RequestParam(value = "merchid", required = false) String merchid,
            @RequestParam(value = "batchid", required = false) String batchid,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer pagesize,
            HttpSession httpSession) throws Exception{
        WSResponse<DBRecogs> response = new WSResponse<>();
        response.setRows(bankService.selectBatchLogs(merchid, batchid, (page-1)*pagesize, pagesize));
        response.setTotal(bankService.totalBatchLogs(merchid, batchid));
        response.setRespDescription("批次记录查询成功");
        return response;
    }

    @RequestMapping("api.batchlog.update")
    public DBBatchLog api_batchlog_edit(BatchEditDto batchLogParam, HttpSession httpSession){
        DBBatchLog batchLog = new DBBatchLog();
        batchLog.setSeqid(batchLogParam.getSeqid());
        batchLog.setPickuptime(batchLogParam.getPickuptime());
        batchLog.setMark(batchLogParam.getMark());
        batchLogMapper.updateByPrimaryKeySelective(batchLog);
        return  batchLogMapper.selectByPrimaryKey(batchLog.getSeqid());
    }


    /**
     * 批次记录删除
     * @param seqid
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.batchlog.remove")
    public DestroyResDto api_batchlog_remove(
            @RequestParam(value = "id", required = false) Long seqid,
            HttpSession httpSession) throws Exception{

        DBBatchLog batchLog = batchLogMapper.selectByPrimaryKey(seqid);
        DBRecogsExample recogsExample = new DBRecogsExample();
        recogsExample.createCriteria().andBatchidEqualTo(batchLog.getBatchid());
        recogsMapper.deleteByExample(recogsExample);

        batchLogMapper.deleteByPrimaryKey(seqid);


        DestroyResDto res = new DestroyResDto();
        res.setSuccess(true);
        return res;
    }

    /**
     * 批次查询
     * @param batchid
     * @param httpSession
     * @return
     */
    @RequestMapping("api.batch.statistic.query")
    public WSResponse<BatchStatisticForm> api_batch_statistic(
            @RequestParam(value = "batchid", required = true) String batchid,
            HttpSession httpSession){
        WSResponse<BatchStatisticForm> response = new WSResponse<>();
        List<BatchStatisticDto> list = cRecogsMapper.selectResultStatistic(batchid);

        Integer zcSize = 0;
        Integer tjSize = 0;
        Integer gjSize = 0;
        Integer khSize = 0;
        Integer wsSize = 0;
        Integer otherSize = 0;

        for (BatchStatisticDto statisticDto : list) {
            Integer result = statisticDto.getResult();
            if (result==null) continue;
            switch (statisticDto.getResult()){
                case 1:
                    zcSize = statisticDto.getRowscount();
                    break;
                case 2:
                    tjSize = statisticDto.getRowscount();
                    break;
                case 3:
                    khSize = statisticDto.getRowscount();
                    break;
                case 4:
                    gjSize = statisticDto.getRowscount();
                    break;
                case -2:
                    wsSize = statisticDto.getRowscount();
                    break;
                default:
                    otherSize += statisticDto.getRowscount();;
                    break;
            }
        }
        BatchStatisticForm form = new BatchStatisticForm();
        form.setZcSize(zcSize);
        form.setGjSize(gjSize);
        form.setKhSize(khSize);
        form.setOtherSize(otherSize);
        form.setTjSize(tjSize);
        form.setWsSize(wsSize);

        response.add(form);
        response.setRespDescription("批次统计记录查询成功");
        return response;
    }

    /**
     * 查询最新的领取时间
     * @return
     */
    @RequestMapping("api.batchlog.pickuptime.query")
    public WSResponse<String> api_batchlog_pickuptime_query(){
        WSResponse<String> response = new WSResponse<>();
        DBBatchLogExample example = new DBBatchLogExample();
        example.createCriteria().andPickuptimeIsNotNull();
        example.setOrderByClause(" seqid desc ");
        List<DBBatchLog> list = batchLogMapper.selectByExample(example);
        if (list.size()>0){
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String pickuptime = sdf.format(list.get(0).getPickuptime());
            String nowstr = sdf.format(new Date());
            String result = nowstr.substring(0, 11)+pickuptime.substring(11);
            response.add(result);
        }else{
            response.add("");
        }
        return response;
    }

    /**
     * 离线时间查询
     * @param request
     * @return
     */
    @RequestMapping("api.offline.status.query")
    public WSResponse<Long> api_offline_time(
            HttpServletRequest request) throws Exception{

        Long interval = 1000l;
        DBDeviceLogExample example = new DBDeviceLogExample();
        example.createCriteria().andLasttimeIsNotNull();
        example.setOrderByClause(" lasttime desc ");
        List<DBDeviceLog> list = deviceLogMapper.selectByExample(example);
        if (list.size()>0){
            DBDeviceLog deviceLog = list.get(0);
            interval = (new Date().getTime() - deviceLog.getLasttime().getTime())/ 1000;
        }
        WSResponse<Long> response = new WSResponse<>();
        response.add(interval);
        response.setRespDescription("查询时间成功");
        return response;
    }

    // 查询 设备运行状态
    @RequestMapping("api.devicelog.query")
    public WSResponse<DeviceLogDto> api_devicelog_query(
            HttpServletRequest request) throws Exception{
        WSResponse<DeviceLogDto> response = new WSResponse<>();

        List<DBDeviceLog> list = new ArrayList<DBDeviceLog>(){
            {
                DBDeviceLogExample example = new DBDeviceLogExample();
                example.createCriteria().andLasttimeIsNotNull();
                addAll(deviceLogMapper.selectByExample(example));
            }
        };

        long nowtime = new Date().getTime();
        for (DBDeviceLog dbDeviceLog : list) {
            long diff = nowtime - dbDeviceLog.getLasttime().getTime();
            DeviceLogDto dto = new DeviceLogDto();
            dto.setLastcallmobile(dbDeviceLog.getLastcallmobile());
            dto.setLasttime(dbDeviceLog.getLasttime());
            dto.setMobile(dbDeviceLog.getMobile());
            dto.setUniqueid(dbDeviceLog.getUniqueid());
            dto.setDifference(diff);
            response.add(dto);
        }
        response.setTotal(list.size());
        response.setRespDescription("查询终端日志成功");
        return response;
    }

    /**
     * 查询手机号识别记录
     * @param sort
     * @param order
     * @param page
     * @param pagesize
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.recogs.query")
    public WSResponse<DBRecogs> api_bankflowlog_query(
            @RequestParam(value = "batchid", required = false) String batchid,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "result", required = false) Integer result,
            @RequestParam(value = "manualresult", required = false) Integer manualresult,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer pagesize,
            HttpSession httpSession) throws Exception{
        WSResponse<DBRecogs> response = new WSResponse<>();
        DBRecogs queryParams = new DBRecogs();
        queryParams.setBatchid(batchid);
        queryParams.setStatus(status);
        queryParams.setResult(result);
        queryParams.setManualresult(manualresult);
        queryParams.setMobile(mobile);
        if (queryParams.getStatus()==99){
            queryParams.setStatus(null);
        }
        if (queryParams.getResult()==99){
            queryParams.setResult(null);
        }
        if (queryParams.getManualresult()==99){
            queryParams.setManualresult(null);
        }
        if (IopUtils.isEmpty(queryParams.getMobile())){
            queryParams.setMobile(null);
        }
        response.setRows(bankService.selectRecogs(queryParams, (page-1)*pagesize, pagesize));
        response.setTotal(bankService.totalRecogs(queryParams));
        response.setRespDescription("手机号识别记录查询成功");
        return response;
    }

    /**
     * 新增 识别记录
     * @param mobile
     * @param httpSession
     * @return
     */
    @RequestMapping("api.recogs.save")
    public DBRecogs api_course_add(
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "merchid", required = false) String merchid,
            HttpSession httpSession){

        if (merchid==null) {
            // 如果没有传 teacher，默认为 当前用户自己
            String username = (String) httpSession.getAttribute("username");
            merchid = username;
        }
        DBRecogs recogs = new DBRecogs();
        recogs.setMobile(mobile);
        recogs.setMerchid(merchid);
        recogs.setCreatetime(new Date());
        recogsMapper.insert(recogs);

        DBRecogsExample ex = new DBRecogsExample();
        ex.createCriteria().andMobileEqualTo(mobile);
        ex.setOrderByClause(" seqid desc ");
        recogs = recogsMapper.selectByExample(ex).get(0);

        return recogs;
    }

    /**
     * 更新
     * @param seqid
     * @param mobile
     * @param merchid
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.recogs.update")
    public DBRecogs api_course_update(
            @RequestParam(value = "seqid", required = false) Long seqid,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "merchid", required = false) String merchid,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "manualresult", required = false) Integer manualresult,
            HttpSession httpSession) throws Exception{
        DBRecogsExample ex = new DBRecogsExample();
        ex.createCriteria().andSeqidEqualTo(seqid);
        List<DBRecogs> list = recogsMapper.selectByExample(ex);
        if (list.size()==0) {
            throw new WException(500).setMessage("序号不存在");
        }
        DBRecogs recogs = list.get(0);
        recogs.setMobile(mobile);
        recogs.setMerchid(merchid);
        recogs.setManualresult(manualresult);
        recogsMapper.updateByPrimaryKeySelective(recogs);

        return recogsMapper.selectByPrimaryKey(seqid);
    }


    /**
     * 删除
     * @param seqid
     * @param httpSession
     * @return
     */
    @RequestMapping("api.recogs.remove")
    public DestroyResDto api_course_remove(
            @RequestParam(value = "id", required = false) Long seqid,
            HttpServletRequest request,
            HttpSession httpSession){

        recogsMapper.deleteByPrimaryKey(seqid);

        DestroyResDto res = new DestroyResDto();
        res.setSuccess(true);
        return res;
    }

    private String formatResult(int result){
        String resultName = "";
        if (result==0){
            resultName = "未知";
        }else if(result==1){
            resultName = "正常";
        }else if(result==2){
            resultName = "欠费停机";
        }else if(result==3){
            resultName = "空号";
        }else if(result==4){
            resultName = "关机";
        }else if(result==-1){
            resultName = "尚未处理";
        }
        return  resultName;
    }

    /**
     * 导出某批次的手机号数据
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.recog.batch.exportExcel")
    public WSResponse<String> exportExcelRecog(
            @RequestParam(value = "batchid", required = true) String batchid,
                                               HttpSession httpSession) throws Exception
    {
//        httpSession.setAttribute("importtotal", 100);
//        httpSession.setAttribute("importprogress", 0);
        log.info("开始查询整个主表");
        DBRecogsExample example = new DBRecogsExample();
        example.createCriteria().andBatchidEqualTo(batchid);
        example.setOrderByClause("seqid asc");
        List<DBRecogs> list = recogsMapper.selectByExample(example);

        DataZBStructure dt = new DataZBStructure();
        dt.read("zb_structure.xml");
        List<String> titles = dt.getTitles();
        List<String> fields = dt.getFields();

        log.info("cvs文件开始创建");
        File file = IopUtils.createDownloadFile("mobile","csv");
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter ow = new OutputStreamWriter(fos, "gb2312");
        Writer writer = ow;

        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
        CSVPrinter cp = new CSVPrinter(writer, csvFileFormat);;
        cp.printRecord(titles);
        for (int i = 0; i < list.size(); i++)
        {
            JSONObject jobj = JSONObject.fromObject(IopUtils.parseJson(list.get(i), "yyyy-MM-dd"));
            List<String> record = new ArrayList<>();
            for (int j = 0; j < fields.size(); j++) {
                String value = "";
                if (jobj.containsKey(fields.get(j))) {
                    value = jobj.getString(fields.get(j));
                }else if(fields.get(j).equals("resultName")){
                    if (jobj.containsKey("result")){
                        value = formatResult(jobj.getInt("result"));
                    }
                }
                record.add(value);
            }
            cp.printRecord(record);
            if (i%100==0) {
//                httpSession.setAttribute("importprogress", i);
            }
        }
        log.info("cvs文件创建成功: filename:"+file.getName());
        writer.flush();
        writer.close();
        cp.close();
        log.info("cvs文件资源释放");
//        httpSession.setAttribute("importprogress", list.size());

        WSResponse<String> response = new WSResponse<>();
        response.add(file.getName());
        response.setRespDescription("导出文件成功");
        return response;
    }
}
