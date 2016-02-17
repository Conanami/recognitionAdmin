package org.fuxin.extend;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.fuxin.util.WaveFileReader;
import org.fuxin.util.WaveMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by boshu on 2016/2/4.
 */
public class WaveIdentifyUtil {
    private static Logger log = LoggerFactory.getLogger(WaveIdentifyUtil.class);

    public static void setWaveSampleResourceDir(String dir){
        WaveSample.resoureDir = dir;
    }

    public static PhoneStatus indentify(String mobile, String urlstr){
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet post = new HttpGet(urlstr);
        try {
            HttpResponse httpresponse = client.execute(post);
            HttpEntity resEntity = httpresponse.getEntity();
            byte[] message = EntityUtils.toByteArray(resEntity);
            File file = File.createTempFile("test", "wav");
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(message);
            fos.flush();

            TelecomCompany tel = TelecomCompany.identify(mobile);
            WaveFileReader reader = new WaveFileReader(file.getAbsolutePath());

            int emptyResult = 0;
            int stopResult = 0;
            int closedResult = 0;
            {
                WaveSample sample = tel.getClosedWaveSample();
                WaveFileReader sampleReader = new WaveFileReader(sample.getFile().getAbsolutePath());
                closedResult = WaveMatcher.Compare(reader, sampleReader, sample.getThreshold());
//                if (result==0){
//                    //匹配成功
//                    return PhoneStatus.closed;
//                }
            }

            {
                WaveSample sample = tel.getEmptyWaveSample();
                WaveFileReader sampleReader = new WaveFileReader(sample.getFile().getAbsolutePath());
                emptyResult = WaveMatcher.Compare(reader, sampleReader, sample.getThreshold());
//                if (result==0){
//                    //匹配成功
//                    return PhoneStatus.empty;
//                }
            }

            {
                WaveSample sample = tel.getStoppedWaveSample();
                WaveFileReader sampleReader = new WaveFileReader(sample.getFile().getAbsolutePath());
                stopResult = WaveMatcher.Compare(reader, sampleReader, sample.getThreshold());
//                if (result==0){
//                    //匹配成功
//                    return PhoneStatus.stopped;
//                }
            }

            int result = calcFinalresult(emptyResult, stopResult, closedResult);
            if (result==1)
                return PhoneStatus.empty;
            if (result==2)
                return PhoneStatus.stopped;
            if (result==3)
                return PhoneStatus.closed;
            return PhoneStatus.work;

        }catch (Exception e){
            e.printStackTrace();
        }
        return PhoneStatus.work;
    }

    public static int calcFinalresult(int khresult, int tjresult, int gjresult) {
        if(khresult==0 && tjresult==2 && gjresult==2)
        {
            return 1;
        }
        if(khresult==2 && tjresult==0 && gjresult==2)
        {
            return 2;
        }
        if(khresult==2 && tjresult==2 && gjresult==0)
        {
            return  3;
        }
        if(khresult+tjresult+gjresult<3)
            return 4;
        if(khresult+tjresult+gjresult>5)
            return 0;

        return 0;
    }
}
