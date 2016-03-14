package com.web.controller;

import com.common.exception.ExceptionFormatter;
import com.common.exception.WException;
import com.common.util.IopUtils;
import com.common.util.MobileUtil;
import com.common.util.WSResponse;
import com.web.service.IBankService;
import mybatis.one.mapper.CRecogsMapper;
import mybatis.one.mapper.DBRecogsMapper;
import mybatis.one.mapper.DBTmpPhoneMapper;
import mybatis.one.po.DBRecogsExample;
import mybatis.one.po.DBTmpPhoneExample;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by boshu on 2016/2/3.
 */
@RestController
public class CSVUploadController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    IBankService bankService;

    @Resource
    DBRecogsMapper recogsMapper;

    @Resource
    CRecogsMapper cRecogsMapper;

    @Resource
    DBTmpPhoneMapper tmpPhoneMapper;

    @ExceptionHandler(Exception.class)
    public WSResponse<Boolean> exceptionHandler(Exception ex, HttpSession httpSession) {
        WSResponse<Boolean> response = new WSResponse<Boolean>();
        log.error("Exception异常：" + ex);
        ExceptionFormatter.setResponse(response, ex);
        return response;
    }

    /**
     * 手机号批量上传
     * @param file
     * @param colnum
     * @param rowstart
     * @param mark
     * @param request
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.mobile.upload")
    public WSResponse<Boolean> uploadmobile(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "colnum", required = true) Integer colnum,
            @RequestParam(value = "rowstart", required = true) Integer rowstart,
            @RequestParam(value = "pickuptime", required = true) String pickuptime,
            @RequestParam(value = "mark", required = true) String mark,
            HttpServletRequest request, HttpSession httpSession) throws Exception {
        String type = "";
        if (file.isEmpty()) {
            System.out.println("文件未上传");
            throw new WException(500).setMessage("请选择文件");
        } else {
            System.out.println("文件长度: " + file.getSize());
            System.out.println("文件类型: " + file.getContentType());
            System.out.println("文件名称: " + file.getName());
            System.out.println("文件原名: " + file.getOriginalFilename());
            System.out.println("========================================");

            //保存文件
            String realName = file.getOriginalFilename();
            type = realName.substring(realName.indexOf("."),
                    realName.length());
            if (!type.equals(".txt") && !type.equals(".csv")) {
                throw new WException(500).setMessage("文件必须是制表符分隔txt格式,或者csv格式");
            }
        }

        if (colnum<1){
            throw  new WException(500).setMessage("请输入有效的 表格中手机号所在的列数，从1开始");
        }
        if (rowstart<1){
            throw  new WException(500).setMessage("请输入有效的 读取手机号开始的行数，从1开始");
        }

        CSVFormat format = CSVFormat.DEFAULT;
        if (type.equals(".txt")){
            format = CSVFormat.TDF;
        }
        if (type.equals(".csv")){
            format = CSVFormat.EXCEL;
        }

        String merchid = "";
        if (IopUtils.isEmpty(merchid)) {
            // 如果没有传 teacher，默认为 当前用户自己
            String username = (String) httpSession.getAttribute("username");
            if (IopUtils.isEmpty(username)){
                throw new WException(500).setMessage("系统已经退出，请重新登录");
            }
            merchid = username;
        }

        colnum = colnum -1 ;//传入的默认从1开始， 实际使用的从0开始

        List<String> listPhone = new ArrayList<>();
        List<List<String>> lists = readListFromCsvFile(file.getInputStream(), format);
        for (int i=rowstart-1;i<lists.size();i++){
            List<String> list = lists.get(i);
            String mobile = MobileUtil.formatPhone(list.get(colnum));
            if (IopUtils.isNotEmpty(mobile)){
                listPhone.add(mobile);
            }
        }
        {
            //先清空 临时表里面 当前商户的 数据
            DBTmpPhoneExample example = new DBTmpPhoneExample();
            example.createCriteria().andMerchidEqualTo(merchid);
            tmpPhoneMapper.deleteByExample(example);
        }
        //将数据插入临时表
        cRecogsMapper.insertTmpPhoneBatch(merchid, listPhone);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date pickupDate = new Date();
        if (IopUtils.isNotEmpty(pickuptime)){
            pickupDate = sdf.parse(pickuptime);
        }
        bankService.insertMobiles(merchid, pickupDate, mark);
        WSResponse<Boolean> response = new WSResponse<>();
        response.setRespDescription("批量提交手机号码 "+listPhone.size()+" 条 成功");
        return response;
    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static List<List<String>> readListFromCsvFile(InputStream is, CSVFormat format) throws Exception, IOException {
        List<List<String>> result = new ArrayList<>();

        String encoding = "GBK";
        CSVParser parser = new CSVParser(new InputStreamReader(
                is, encoding), format);
        List<CSVRecord> list = parser.getRecords();
        for (int j = 0; j < list.size(); j++) {
            CSVRecord item = list.get(j);
            List<String> l = new ArrayList<>();
            for (int i = 0; i < item.size(); i++) {
                l.add(IopUtils.emptyString(item.get(i)));
            }
            result.add(l);
        }
        parser.close();

        return result;
    }
}
