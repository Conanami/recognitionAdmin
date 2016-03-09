package com.web.controller;

import com.common.exception.ExceptionFormatter;
import com.common.util.IopUtils;
import com.common.util.MobileUtil;
import com.common.util.WSResponse;
import com.web.dto.DataZBStructure;
import mybatis.one.mapper.DBRecogsMapper;
import mybatis.one.po.DBRecogs;
import mybatis.one.po.DBRecogsExample;
import net.sf.json.JSONObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by boshu on 2016/3/8.
 */
@RestController
public class LearnController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    DBRecogsMapper recogsMapper;

    @ExceptionHandler(Exception.class)
    public WSResponse<Boolean> exceptionHandler(Exception ex, HttpSession httpSession) {
        WSResponse<Boolean> response = new WSResponse<>();
        log.error("Exception异常：" + ex);
        ExceptionFormatter.setResponse(response, ex);
        return response;
    }

    /**
     * 导出自动学习的样本excel
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.auto.learn.exportExcel")
    public WSResponse<String> exportExcelRecog(
            @RequestParam(value = "batchid", required = true) String batchid,
            HttpSession httpSession) throws Exception
    {
        DBRecogsExample example = new DBRecogsExample();
        example.createCriteria().andBatchidEqualTo(batchid);
        example.setOrderByClause("seqid asc");
        List<DBRecogs> list = recogsMapper.selectByExample(example);

        DataZBStructure dt = new DataZBStructure();
        dt.read("learn_structure.xml");

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
                String field = fields.get(j);
                if (jobj.containsKey(field)) {
                    value = jobj.getString(fields.get(j));
                }else if(field.equals("filename")){
                    if (jobj.containsKey("dataurl")){
                        String dataurl = jobj.getString("dataurl");
                        value = dataurl.substring(dataurl.indexOf("filename=")+9);
                    }
                }else if(field.equals("operate")){
                    value = MobileUtil.findOp(jobj.getString("mobile"));
                }else if(field.equals("type")){
                    if(jobj.containsKey("result")){
                        int result = jobj.getInt("result");
                        if (result==2){
                            value = "tj";
                        }else if(result==3){
                            value = "kh";
                        }else if(result==4){
                            value = "gj";
                        }else if(result==1){
                            value = "zc";
                        }
                    }
                }
                record.add(value);
            }
            cp.printRecord(record);
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
