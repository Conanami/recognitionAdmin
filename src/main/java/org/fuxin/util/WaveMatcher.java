package org.fuxin.util;

import java.util.ArrayList;

import org.fuxin.caller.Eigenvalue;

public class WaveMatcher {

    private static double stand_max;    //标准最大震动
    private static double sample_max;   //样本最大震动

    /***
     *
     * @param reader
     * @param standard
     * @param i
     * @return
     * 0 是匹配上
     * 1 是样本时长太短
     * 2 是没有匹配上
     * 3 是样本与标准参数不同，参数包括取样频率，声道数，BIT数
     */
    public static int Compare(WaveFileReader reader, WaveFileReader standard, int thresholdValue) {
        // TODO Auto-generated method stub
        if(reader.getSampleRate()==standard.getSampleRate()
                && reader.getBitPerSample()==standard.getBitPerSample()
                && reader.getNumChannels()==standard.getNumChannels()
                && reader.getNumChannels()==1 )
        {
            return SoundMatch(reader.getData()[0],standard.getData()[0],thresholdValue);
        }
        else
            return 3;

    }

    /***
     *
     * @param sample
     * @param standard
     * @param thresholdValue
     * @return 返回0代表匹配成功，
     * 返回1代表样本时长比标准样本短，无法比较
     * 返回2代表匹配不成功
     */
    private static int SoundMatch(int[] sample, int[] standard, int thresholdValue) {
        // TODO Auto-generated method stub

        if(sample.length<standard.length)
        {
            //1,代表样本时长比标准样本短，无法比较
            return 1;
        }
        else
        {
            int meetcnt = 0;
            int[] regu_sta=Regulize(standard,1);
            Eigenvalue sta_ev=new Eigenvalue(regu_sta);
            //ArrayList outfile=ArrayUtil.toArrayList(regu_sta);
            // FuOutput.writeTofile6(outfile, "e:\\outfile1.csv");
            ArrayList<String> tofile = new ArrayList<String>();
            //取样样本的间隔进行自动选择
            int step = 11;
            if(sample.length<24000) step = 2;
            for(int i=0;i<=sample.length-standard.length;i=i+step)
            {
                String output="now|"+String.format("%.2f", ((float)i/8000))+"|";
                int[] newsample=ArrayUtil.Cut(sample,i,standard.length);
                //如果切下来的一段，与standard的误差在一个范围之内，则返回匹配上
                //正则化standard和newsample，然后再计算
                int[] regu_new=Regulize(newsample,2);


                Eigenvalue ev =SameMatch(regu_new,regu_sta);

                //这个是输出到文件检查
                output=output+ev.division;
                System.out.println(output);
                tofile.add(output);

                double matchrate = EigenMatcher.isMatch(ev,sta_ev);
                if(ev.division < thresholdValue
                        && matchrate>=0.6
                        && Math.abs(ev.hanziCount-sta_ev.hanziCount)<=1)
                    meetcnt++;
                else
                    meetcnt=0;

                //连续2次小于一个值，认为匹配上
                if(meetcnt>0)
                {
                    System.out.println(sta_ev);
                    System.out.println(ev);
                    System.out.println("matchrate:"+matchrate);
                    FuOutput.showSecond(i);
                    //FuOutput.writeTofile6(tofile, "e:\\SoundResult\\01.txt");
                    return 0;
                }
            }
            FuOutput.writeTofile6(tofile, "C:\\tools\\audio\\wavefiles\\01.txt");
        }
        //正常没匹配上；
        return 2;
    }

    /***
     *
     * @param sample
     * @param type 1,代表是标准录音，2，代表是样本录音
     * @return
     */
    private static int[] Regulize(int[] sample, int type) {
        double max=GetMax(sample);
        if(type==1) stand_max=max;
        else if(type==2) sample_max=max;


        int[] rt=new int[sample.length];
        for(int i=0;i<sample.length;++i)
        {
            double temp;
            temp = ((double)sample[i])/max;
            rt[i]= (int) (temp * 32768);
        }
        return rt;
    }

    private static double GetMax(int[] sample) {
        // TODO Auto-generated method stub
        double max=0;
        for(int i=0;i<sample.length;++i)
        {
            if(Math.abs(sample[i])>max)
                max=Math.abs(sample[i]);
        }
        return max;
    }

    /***
     * 两段一样的比较是否一样
     * @param newsample
     * @param standard
     * @return 返回一个特征值类，
     * 里面记录模式识别需要的所有特征值
     */
    private static Eigenvalue SameMatch(int[] newsample, int[] standard) {
        Eigenvalue rtev = new Eigenvalue(newsample,standard);
        return rtev;
    }

}
