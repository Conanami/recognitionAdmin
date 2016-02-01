package com.sxd.controller;

import com.common.exception.ExceptionConst;
import com.common.exception.ExceptionFormatter;
import com.common.exception.WException;
import com.common.util.IopUtils;
import com.common.util.MerchManagerUtil;
import com.common.util.MerchValidateUtil;
import com.common.util.WSResponse;
import com.sxd.dto.DestroyResDto;
import com.sxd.service.IBankService;
import mybatis.one.mapper.DBRecogsMapper;
import mybatis.one.po.DBRecogs;
import mybatis.one.po.DBRecogsExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * Created by boshu on 2016/1/1.
 */
@RestController
public class BankRestController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${merchmanager.xml}")
    private String merchManagerUrl = "";    //商户管理信息的xml配置文件地址

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
     * 获取手机号
     * @param merchId
     * @param signInfo
     * @param request
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.pickup.mobile")
    public WSResponse<DBRecogs> api_pickup_mobile(
            @RequestParam(value = "merchid", required = true) String merchId,
            @RequestParam(value = "signinfo", required = true) String signInfo,
            HttpServletRequest request,
            HttpSession httpSession) throws Exception{
         /* 商户号检查 */
        String appSecret = new MerchManagerUtil(merchManagerUrl).getSecret("recog", merchId);
        if (IopUtils.isEmpty(appSecret)){
            throw new WException(ExceptionConst.MerchIdNotFound.intValue());
        }

        /* 签名校验 */
        if (!MerchValidateUtil.validate(request.getParameterMap(), merchId, appSecret)) {
            throw new WException(ExceptionConst.SIGN_VALID_FAIL.intValue());
        }

        WSResponse<DBRecogs> response = new WSResponse<>();

        DBRecogs recogs = bankService.pickup(null);
        response.add(recogs);
        response.setRespDescription("领取手机号成功");
        return response;
    }

    /**
     * 保存 录音文件到对应手机号
     * @param merchId
     * @param signInfo
     * @param reqid
     * @param dataurl
     * @param request
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.mobile.audio.save")
    public WSResponse<DBRecogs> api_mobile_audio_save(
            @RequestParam(value = "merchid", required = true) String merchId,
            @RequestParam(value = "signinfo", required = true) String signInfo,
            @RequestParam(value = "reqid", required = true) Long reqid,
            @RequestParam(value = "zjmobile", required = true) String zjmobile,
            @RequestParam(value = "dataurl", required = true) String dataurl,
            HttpServletRequest request,
            HttpSession httpSession) throws Exception{
         /* 商户号检查 */
        String appSecret = new MerchManagerUtil(merchManagerUrl).getSecret("recog", merchId);
        if (IopUtils.isEmpty(appSecret)){
            throw new WException(ExceptionConst.MerchIdNotFound.intValue());
        }

        /* 签名校验 */
        if (!MerchValidateUtil.validate(request.getParameterMap(), merchId, appSecret)) {
            throw new WException(ExceptionConst.SIGN_VALID_FAIL.intValue());
        }

        WSResponse<DBRecogs> response = new WSResponse<>();
        bankService.saveAudioInfo(reqid, zjmobile, dataurl);
        response.setRespDescription("保存录音文件信息成功");
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
            @RequestParam(value = "merchid", required = false) String merchid,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer pagesize,
            HttpSession httpSession) throws Exception{
        WSResponse<DBRecogs> response = new WSResponse<>();
        response.setRows(bankService.selectRecogs(merchid, mobile, (page-1)*pagesize, pagesize));
        response.setTotal(bankService.totalRecogs(merchid, mobile));
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
}
