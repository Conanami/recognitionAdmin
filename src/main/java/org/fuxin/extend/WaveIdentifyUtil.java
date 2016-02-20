package org.fuxin.extend;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.fuxin.caller.C;
import org.fuxin.caller.ClassifyWave;
import org.fuxin.caller.StandFile;
import org.fuxin.caller.WaveFileResult;
import org.fuxin.util.WaveFileReader;
import org.fuxin.util.WaveMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by boshu on 2016/2/4.
 */
public class WaveIdentifyUtil {
    private static Logger log = LoggerFactory.getLogger(WaveIdentifyUtil.class);

    private static ArrayList<StandFile> stanlist = new ArrayList<>();

    public static WaveFileResult indentify(String urlstr) throws Exception{
        if (stanlist.size()==0){
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ydkhfile),2000, C.Operator.Yd, C.Type.Kh));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ydtjfile), 3600, C.Operator.Yd, C.Type.Tj));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ydgjfile), 3000, C.Operator.Yd, C.Type.Gj));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ltkhfile),2600, C.Operator.Lt, C.Type.Kh));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.lttjfile), 1500, C.Operator.Lt, C.Type.Tj));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ltgjfile), 2000, C.Operator.Lt, C.Type.Gj));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.dxkhfile),3000, C.Operator.Dx, C.Type.Kh));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.dxtjfile), 3000, C.Operator.Dx, C.Type.Tj));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.dxgjfile), 2250, C.Operator.Dx, C.Type.Gj));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.lttjfile2),1500, C.Operator.Lt, C.Type.Tj));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ltkhfile2), 2000, C.Operator.Lt, C.Type.Kh));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ltkhfile3), 3000, C.Operator.Lt, C.Type.Kh));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.dxkhfile2),4000, C.Operator.Dx, C.Type.Kh));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.dxgjfile2), 2500, C.Operator.Dx, C.Type.Gj));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.dxgjfile3), 2000, C.Operator.Dx, C.Type.Gj));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ydkhfile2),2000, C.Operator.Yd, C.Type.Kh));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ydgjfile2), 2600, C.Operator.Yd, C.Type.Gj));
            stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ydgjfile3), 2500, C.Operator.Yd, C.Type.Gj));
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet post = new HttpGet(urlstr);

        HttpResponse httpresponse = client.execute(post);
        HttpEntity resEntity = httpresponse.getEntity();
        byte[] message = EntityUtils.toByteArray(resEntity);
        File file = File.createTempFile("test", "wav");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(message);
        fos.flush();

        //对文件进行分类
        ClassifyWave cw= new ClassifyWave();
        WaveFileResult wfr = cw.Filter(file, stanlist);

        return wfr;
    }





    public static void setWaveSampleResourceDir(String dir){
        WaveSample.resoureDir = dir;
        C.standfolder = dir;
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
