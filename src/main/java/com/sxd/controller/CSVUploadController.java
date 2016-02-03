package com.sxd.controller;

import com.common.exception.ExceptionFormatter;
import com.common.exception.WException;
import com.common.util.IopUtils;
import com.common.util.WSResponse;
import com.sxd.service.IBankService;
import mybatis.one.mapper.DBRecogsMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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
     * @param request
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.mobile.upload")
    public WSResponse<Boolean> uploadmobile(
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpServletRequest request, HttpSession httpSession) throws Exception {
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
            String type = realName.substring(realName.indexOf("."),
                    realName.length());
            if (!type.equals(".txt")) {
                throw new WException(500).setMessage("文件必须是制表符分隔txt格式");
            }
        }

        String merchid = "";
        if (IopUtils.isEmpty(merchid)) {
            // 如果没有传 teacher，默认为 当前用户自己
            String username = (String) httpSession.getAttribute("username");
            merchid = username;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddHHmm");
        String batchid = simpleDateFormat.format(new Date());
        List<String> listMobile = new ArrayList<>();
        List<List<String>> lists = readListFromCsvFile(file.getInputStream());
        for (int i=0;i<lists.size();i++){
            List<String> list = lists.get(i);
            String mobile = list.get(0);
            if (isNumeric(mobile)){
                listMobile.add(mobile);
            }
        }
        bankService.insertMobiles(merchid, batchid, listMobile);
        WSResponse<Boolean> response = new WSResponse<>();
        response.setRespDescription("批量提交手机号成功");
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

    public static List<List<String>> readListFromCsvFile(InputStream is) throws Exception, IOException {
        List<List<String>> result = new ArrayList<>();

        String encoding = "GBK";
        CSVParser parser = new CSVParser(new InputStreamReader(
                is, encoding), CSVFormat.TDF);
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
