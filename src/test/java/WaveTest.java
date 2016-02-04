import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.fuxin.extend.TelePhone;
import org.fuxin.extend.TelecomCompany;
import org.fuxin.extend.WaveIdentifyUtil;
import org.fuxin.extend.WaveSample;
import org.fuxin.util.WaveFileReader;
import org.fuxin.util.WaveMatcher;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by boshu on 2016/2/4.
 */
public class WaveTest {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void runtest(){
        WaveIdentifyUtil.setWaveSampleResourceDir("C:\\Users\\boshu\\Downloads\\11公开接口\\9录音\\standard");

        String urlstr = "http://sxddev.zhangguijr.com:80/imageserver/api.file.get?filename=18221822405_201602031630347027.wav";
        String mobile = "";
        Pattern p = Pattern.compile("filename=(.*)_");
        Matcher m = p.matcher(urlstr);
        if (m.find()){
            mobile = m.group(1);
        }
        log.info("mobile:"+mobile);
        TelePhone phone = new TelePhone(mobile);
        phone.identifyWave(urlstr);

       log.info(""+phone.getStatus().getSimpleName());
    }
}
