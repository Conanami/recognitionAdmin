package com.web.controller;

import com.common.exception.ExceptionFormatter;
import com.common.exception.WException;
import com.common.util.IopUtils;
import com.common.util.WSResponse;
import com.web.dto.MTMDataDto;
import com.web.service.IBankService;
import mybatis.one.mapper.CRecogsMapper;
import mybatis.one.mapper.DBRecogsMapper;
import mybatis.one.po.DBRecogs;
import mybatis.one.po.DBRecogsExample;
import mybatis.two.mapper.CZNMapper;
import mybatis.two.mapper.DBMTMCaseDataMapper;
import mybatis.two.mapper.DBMTMContactMapper;
import mybatis.two.po.DBMTMContact;
import mybatis.two.po.DBMTMContactExample;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by boshu on 2016/3/7.
 */
@RestController
public class ZNRestController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    IBankService bankService;

    @Resource
    CZNMapper cznMapper;

    @Resource
    DBMTMContactMapper mtmContactMapper;

    @Resource
    CRecogsMapper cRecogsMapper;

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
     * 格式化电话号码
     * 去掉非 数字 和 - 的字符。 长度最多保留20位
     * @param phone
     * @return
     */
    private String formatPhone(String phone){
        String tmpStr = "";
        for(int i=0;i<phone.length();i++){
            String tmp=""+phone.charAt(i);
            if((tmp).matches("[0-9.]") || tmp.equals("-")){
                tmpStr+=tmp;
            }
        }
        if (tmpStr.length()>20){
            tmpStr = tmpStr.substring(0,20);
        }
        return tmpStr;
    }

    /**
     * 查询兆能的待拨打的电话号码入库
     * @param merchid
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.zn.mobile.sync")
    public WSResponse<Boolean> api_batchlog_query(
            @RequestParam(value = "merchid", required = false) String merchid,
            @RequestParam(value = "pickuptime", required = true) String pickuptime,
            @RequestParam(value = "mark", required = false) String mark,
            @RequestParam(value = "casenostart", required = true) String casenostart,
            HttpSession httpSession) throws Exception{

        if (IopUtils.isEmpty(merchid)) {
            // 如果没有传 teacher，默认为 当前用户自己
            String username = (String) httpSession.getAttribute("username");
            if (IopUtils.isEmpty(username)){
                throw new WException(500).setMessage("系统已经退出，请重新登录");
            }
            merchid = username;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date pickupDate = new Date();
        if (IopUtils.isNotEmpty(pickuptime)){
            pickupDate = sdf.parse(pickuptime);
        }
        if (IopUtils.isEmpty(mark)){
            mark = "兆能导入";
        }

        WSResponse<Boolean> response = new WSResponse<>();
        httpSession.setAttribute("api.zn.mobile.sync.message", "开始查询...");

        bankService.importFromZN(merchid, casenostart, pickupDate, mark);

        httpSession.removeAttribute("api.zn.mobile.sync.message");

        response.setRespDescription("取得电话号码成功");
        return response;
    }

    /**
     * 进度消息查询
     * @param key
     * @param httpSession
     * @return
     */
    @RequestMapping("api.progress.message.query")
    public WSResponse<String> api_batchlog_query(
            @RequestParam(value = "key", required = false) String key,HttpSession httpSession){
        WSResponse<String> response = new WSResponse<>();
        if (httpSession.getAttribute(key)!=null){
            response.setRespDescription(httpSession.getAttribute(key).toString());
        }else {
            response.setRespCode(400);
            response.setRespDescription("");
        }
        return response;
    }


    /**
     * 判断 当前号码 是否可以导入， 如果当前用户 导入过该号码，但是还没有拨打，不可以再次导入
     * @param merchid
     * @param mobile
     * @return
     */
    public boolean checkMobileExist(String merchid, String mobile){
        DBRecogsExample example = new DBRecogsExample();
        example.createCriteria().andMobileEqualTo(mobile).andMerchidEqualTo(merchid).andCalltimeIsNull();
        List<DBRecogs> dbRecogsList = recogsMapper.selectByExample(example);
        if (dbRecogsList.size()>0){
           return true;
        }
        return false;
    }
}
