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

            {
                WaveSample sample = tel.getClosedWaveSample();
                WaveFileReader sampleReader = new WaveFileReader(sample.getFile().getAbsolutePath());
                int result = WaveMatcher.Compare(reader, sampleReader, sample.getThreshold());
                if (result==0){
                    //匹配成功
                    return PhoneStatus.closed;
                }
            }

            {
                WaveSample sample = tel.getEmptyWaveSample();
                WaveFileReader sampleReader = new WaveFileReader(sample.getFile().getAbsolutePath());
                int result = WaveMatcher.Compare(reader, sampleReader, sample.getThreshold());
                if (result==0){
                    //匹配成功
                    return PhoneStatus.empty;
                }
            }

            {
                WaveSample sample = tel.getStoppedWaveSample();
                WaveFileReader sampleReader = new WaveFileReader(sample.getFile().getAbsolutePath());
                int result = WaveMatcher.Compare(reader, sampleReader, sample.getThreshold());
                if (result==0){
                    //匹配成功
                    return PhoneStatus.stopped;
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return PhoneStatus.work;
    }
}
