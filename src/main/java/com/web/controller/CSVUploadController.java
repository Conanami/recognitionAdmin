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
import mybatis.one.po.DBTmpPhoneExample;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

            if (file.getSize()>1000*1000){
                throw new WException(500).setMessage("文件大小不能超过1M");
            }

            //保存文件
            String realName = file.getOriginalFilename();
            type = realName.substring(realName.lastIndexOf("."),
                    realName.length());
            if (!type.equals(".txt")
                    && !type.equals(".csv")
                    && !type.equals(".xls")
                    && !type.equals(".xlsx")) {
                throw new WException(500).setMessage("文件必须是制表符分隔txt格式,csv格式,excel格式");
            }
        }

        if (colnum<1){
            throw  new WException(500).setMessage("请输入有效的 表格中手机号所在的列数，从1开始");
        }
        if (rowstart<1){
            throw  new WException(500).setMessage("请输入有效的 读取手机号开始的行数，从1开始");
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


        List<String> listPhone = new ArrayList<>();
        if (type.equals(".txt") || type.equals(".csv")){
            listPhone =  readCsv(file.getInputStream(), type, rowstart, colnum);
        }else if(type.equals(".xlsx") || type.equals(".xls")){
            listPhone = readExcel(file.getInputStream(), type, rowstart, colnum);
        }else{
            throw new WException(500).setMessage("文件必须是制表符分隔txt格式,csv格式,excel格式");
        }

        // 案件的 批次id
        String importBatchId = "up"+new SimpleDateFormat("MMddHHmmss").format(new Date())+"_"+new RandomUtils().nextInt(10);
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
        int insertsize = bankService.insertMobiles(merchid, importBatchId, pickupDate, mark);
        WSResponse<Boolean> response = new WSResponse<>();
        response.setRespDescription("批量提交手机号码 "+insertsize+" 条 ");
        return response;
    }

    //读取 cvs格式 文件  传入的 rowstart, colstart 默认从1开始， 实际使用的从0开始
    public List<String> readCsv(InputStream fis, String type, int rowstart, int colstart) throws Exception{
        CSVFormat format = CSVFormat.DEFAULT;
        if (type.equals(".txt")){
            format = CSVFormat.TDF;
        }
        if (type.equals(".csv")){
            format = CSVFormat.EXCEL;
        }

        List<String> listPhone = new ArrayList<>();
        List<List<String>> lists = readListFromCsvFile(fis, format);
        for (int i=rowstart-1;i<lists.size();i++){
            List<String> list = lists.get(i);
            String mobile = MobileUtil.formatPhone(list.get(colstart-1));
            if (IopUtils.isNotEmpty(mobile)){
                listPhone.add(mobile);
            }
        }
        return listPhone;
    }

    //读取excel文件, 读取第一页的手机号, 支持 xls,xlsx  传入的 rowstart, colstart 默认从1开始， 实际使用的从0开始
    public List<String> readExcel(InputStream fis, String type, int rowstart, int colstart) throws Exception{
        List<String> listPhone = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        int start = Math.max(firstRowNum, rowstart-1);
        for (int i=start;i<=lastRowNum;i++){
            Cell cell = sheet.getRow(i).getCell(colstart-1);
            if (cell==null){
                throw new WException(500).setMessage("excel文件第"+i+"行 手机号为空,请检查文件");
            }
            cell.setCellType(Cell.CELL_TYPE_STRING);
            listPhone.add(cell.getStringCellValue());
        }
        return listPhone;
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
