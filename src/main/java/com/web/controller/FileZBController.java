package com.web.controller;

import com.common.exception.ExceptionFormatter;
import com.common.exception.WException;
import com.common.util.IopUtils;
import com.common.util.WSResponse;
import mybatis.one.mapper.DBImgZBMapper;
import mybatis.one.po.DBImgZB;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by boshu on 2015/12/30.
 */
@RestController
public class FileZBController {
    private static Logger log = LoggerFactory.getLogger(FileZBController.class);

    @Resource
    private DBImgZBMapper zbmapper;

    @Value("${imageserver.url}")
    String imageserverUrl;

    @ExceptionHandler(Exception.class)
    public WSResponse<Boolean> exceptionHandler(Exception ex, HttpSession httpSession) {
        WSResponse<Boolean> response = new WSResponse<>();
        log.error("Exception异常：" + ex);
        ExceptionFormatter.setResponse(response, ex);
        return response;
    }

    /**
     * 读取文件 为 字节流
     * @param imagename
     * @param suffix
     * @return
     * @throws Exception
     */
    byte[] readFile(String project, String imagename, String suffix) throws Exception{
        String imagePath = imageserverUrl+project+File.separator+imagename+"."+suffix;
        byte[] buffer = null;
        File file = new File(imagePath);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1)
        {
            bos.write(b, 0, n);
        }
        fis.close();
        bos.close();
        buffer = bos.toByteArray();
        return buffer;
    }

    /**
     * 读取文件 为 字节流
     * @param imagename
     * @param suffix
     * @return
     * @throws Exception
     */
    byte[] readFile(String project, String merchid, String batchid, String imagename, String suffix) throws Exception{
        String imagePath = imageserverUrl+project+File.separator+merchid+File.separator+batchid+File.separator+imagename+"."+suffix;
        byte[] buffer = null;
        File file = new File(imagePath);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1)
        {
            bos.write(b, 0, n);
        }
        fis.close();
        bos.close();
        buffer = bos.toByteArray();
        return buffer;
    }

    /**
     * 下载服务器的文档
     * @param project
     * @param imagename
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping(value="api.file.get", produces = {"application/octet-stream"})
    public byte[] txt_query(
            @RequestParam(value = "project", required = false) String project,
            @RequestParam(value = "filename", required = false) String imagename,
            @RequestParam(value = "merchid", required = false) String merchid,
            @RequestParam(value = "batchid", required = false) String batchid,
            HttpSession httpSession, HttpServletResponse response) throws Exception{
        String suffix = "txt";
        if (imagename.indexOf(".")!=-1) {
            //自带有图片后缀名，需要提取出来
            suffix = imagename.substring(imagename.indexOf(".")+1);
            imagename = imagename.replace("."+suffix, "");
        }
        if(project==null){
            project = "";
        }
        byte[] buffer = null;
        try
        {
            //默认直接从图片库 读取图片
            if (IopUtils.isNotEmpty(merchid) && IopUtils.isNotEmpty(batchid)){
                buffer = readFile(project, merchid, batchid, imagename, suffix);
            }else{
                buffer = readFile(project, imagename, suffix);
            }
            response.addHeader("Content-Disposition","attachment;filename="+imagename+"."+suffix);
        }
        catch (FileNotFoundException e)
        {
            //读取不到的时候，从数据库寻找
            DBImgZB img = zbmapper.selectByPrimaryKey(imagename);
            if (img==null) {
                log.info("数据库里面也找不到图片，返回默认图片");
                buffer = readFile("", "default_not_found", "png");
                response.addHeader("Content-Disposition","attachment;filename=default_not_found.png");
            }else{
                log.info("数据库里面找到图片，返回");
                if (IopUtils.isNotEmpty(img.getMerchid()) && IopUtils.isNotEmpty(img.getBatchid())){
                    buffer = readFile(img.getGroupname(), img.getMerchid(), img.getBatchid(), img.getImagename(), img.getSuffix());
                }else {
                    buffer = readFile(img.getGroupname(), img.getImagename(), img.getSuffix());
                }
                response.addHeader("Content-Disposition","attachment;filename="+img.getImagename()+"."+img.getSuffix());
            }
        }
        catch (IOException e)
        {
            log.info(""+e.getMessage());
//            e.printStackTrace();
        }

        return buffer;
    }


    /**
     * 其他文件上传
     * @param multifile
     * @param project
     * @param request
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.file.upload")
    public WSResponse<String> uploadFile(
            @RequestParam(value = "file", required = false) MultipartFile multifile,
            @RequestParam(value = "project", required = false) String project,
            @RequestParam(value = "merchid", required = false) String merchid,
            @RequestParam(value = "batchid", required = false) String batchid,
            HttpServletRequest request, HttpSession httpSession) throws Exception{
        WSResponse<String> response = new WSResponse<>();
        if (multifile.isEmpty()) {
            throw new WException(500).setMessage("文件未上传");
        } else {
            log.info("文件上传开始===================");
            log.info("文件长度: " + multifile.getSize());
            log.info("文件类型: " + multifile.getContentType());
            log.info("文件名称: " + multifile.getName());
            log.info("文件原名: " + multifile.getOriginalFilename());

            //保存文件
            String realName = multifile.getOriginalFilename();
            String suffix = realName.substring(realName.indexOf(".")+1,
                    realName.length()).toLowerCase();

            String filename = realName.substring(0, realName.indexOf("."))
                    +new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ RandomUtils.nextInt(10000);
            FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(
                    imageserverUrl+project+File.separator+merchid+File.separator+batchid+File.separator, filename+"."+suffix));

            DBImgZB img = new DBImgZB();
            img.setMerchid(merchid);
            img.setBatchid(batchid);
            img.setGroupname(project);
            img.setImagename(filename);
            img.setSuffix(suffix);
            img.setOldname(realName);
            img.setRemark("");
            img.setFilesize(multifile.getSize());
            img.setCreatetime(new Date());
            zbmapper.insert(img);

            String url = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + request.getContextPath()
                    + "/api.file.get?filename=" + filename + "." + suffix
                    ;
            response.add(url);

            log.info("文件上传结束===================");
        }
        response.setRespDescription("文件上传成功");
        return response;
    }

    /**
     * 录音文件上传
     * @param multifile
     * @param project
     * @param request
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.audio.file.upload")
    public WSResponse<String> uploadAudioFile(
            @RequestParam(value = "file", required = true) MultipartFile multifile,
            @RequestParam(value = "project", required = true) String project,
            @RequestParam(value = "merchid", required = true) String merchid,
            @RequestParam(value = "batchid", required = true) String batchid,
            HttpServletRequest request, HttpSession httpSession) throws Exception{
        WSResponse<String> response = new WSResponse<>();
        if (multifile.isEmpty()) {
            throw new WException(500).setMessage("文件未上传");
        } else {
//            log.info("文件上传开始===================");
            log.info("文件长度: " + multifile.getSize());
//            log.info("文件类型: " + multifile.getContentType());
//            log.info("文件名称: " + multifile.getName());
            log.info("文件原名: " + multifile.getOriginalFilename());
            //判断 文件大小
            if (multifile.getSize() < 100*1024){
                throw new WException(500).setMessage("录音文件大小不够，需要重新拨打");
            }

            //保存文件
            String realName = multifile.getOriginalFilename();
            String suffix = realName.substring(realName.indexOf(".")+1,
                    realName.length()).toLowerCase();

            String filename = realName.substring(0, realName.indexOf("."))
                    +new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ RandomUtils.nextInt(10000);
            FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(
                    imageserverUrl+project+File.separator+merchid+File.separator+batchid+File.separator, filename+"."+suffix));

            DBImgZB img = new DBImgZB();
            img.setMerchid(merchid);
            img.setBatchid(batchid);
            img.setGroupname(project);
            img.setImagename(filename);
            img.setSuffix(suffix);
            img.setOldname(realName);
            img.setRemark("");
            img.setFilesize(multifile.getSize());
            img.setCreatetime(new Date());
            zbmapper.insert(img);

            String url = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + request.getContextPath()
                    + "/api.file.get?filename=" + filename + "." + suffix
                    ;
            response.add(url);

//            log.info("文件上传结束===================");
        }
        response.setRespDescription("文件上传成功");
        return response;
    }

    /**
     * 字符串上传
     * @param file
     * @param project
     * @param request
     * @param httpSession
     * @return
     * @throws Exception
     */
    @RequestMapping("api.txt.upload")
    public WSResponse<String> uploadTxt(
            @RequestParam(value = "string", required = false) String file,
            @RequestParam(value = "project", required = false) String project,
            HttpServletRequest request, HttpSession httpSession) throws Exception{
        WSResponse<String> response = new WSResponse<>();
        if (file.isEmpty()) {
            throw new WException(500).setMessage("文件未上传");
        } else {
            log.info("文件上传开始===================");
            log.info("文件长度: " + file.length());
            log.info("文件类型: " + "txt");

            //保存文件
            String suffix = "txt";

            String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ RandomUtils.nextInt(10000);
            FileUtils.copyInputStreamToFile(new ByteArrayInputStream(file.getBytes("UTF-8")), new File(
                    imageserverUrl+project+File.separator, filename+"."+suffix));

            DBImgZB img = new DBImgZB();
            img.setGroupname(project);
            img.setImagename(filename);
            img.setSuffix(suffix);
            img.setOldname("");
            img.setRemark("");
            img.setCreatetime(new Date());
            zbmapper.insert(img);

            String url = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + request.getContextPath()
                    + "/api.file.get?filename=" + filename + "." + suffix;
            response.add(url);

            log.info("文件上传结束===================");
        }
        response.setRespDescription("文件上传成功");
        return response;
    }
}
