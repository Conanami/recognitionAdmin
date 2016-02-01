package com.common.util;

import com.common.exception.ExceptionConst;
import com.common.exception.WException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HttpUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 发送post请求
     * @param urlstr url
     * @param map   post 请求键值对集合
     * @return 字符串
     * @throws Exception 异常
     */
    public static String post(String urlstr, HashMap<String,String> map) throws  Exception{
        ArrayList<NameValuePair> formparams = new ArrayList<>();

        if(map!=null){
            for (Map.Entry<String, String> entry : map.entrySet()) {
                formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        HttpEntity reqEntity = new UrlEncodedFormEntity(formparams, "utf-8");

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .setConnectionRequestTimeout(30000)
                .build();

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(urlstr);
        post.setEntity(reqEntity);
        post.setConfig(requestConfig);
        HttpResponse httpresponse = client.execute(post);

        if (httpresponse.getStatusLine().getStatusCode() == 200) {
            HttpEntity resEntity = httpresponse.getEntity();
            String message = EntityUtils.toString(resEntity, "utf-8");
            log.info("message:" + message);
            return  message;
        } else {
            HttpEntity resEntity = httpresponse.getEntity();
            String message = EntityUtils.toString(resEntity, "utf-8");
            log.info("message:" + message);
            throw new WException(ExceptionConst.HTTPRequestError.intValue()).setMessage(message);
        }
    }
}
