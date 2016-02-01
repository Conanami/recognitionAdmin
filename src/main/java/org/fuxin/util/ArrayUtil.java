package org.fuxin.util;

import java.util.ArrayList;

import org.fuxin.caller.Coordi;

/***
 * 数组工具类
 *  @author Administrator
 *
 */
public class ArrayUtil {
    /***割一段跟标准样本一样长的音频
     *
     * @param sample
     * @param start     起始点
     * @param length    长度
     * @return
     */
    public static int[] Cut(int[] sample, int start, int length) {
        int[] rt=new int[length];
        for(int i=0;i<length;++i)
        {
            if(start+i<sample.length)
                rt[i]=sample[start+i];
            else
                rt[i]=0;

        }
        return rt;
    }

    public static ArrayList<?> toArrayList(int[] is) {
        ArrayList<String> rtlist = new ArrayList<String>();
        for(int i=0;i<is.length;++i)
        {
            rtlist.add(String.format("%.5f", (double)i/8000)+","+is[i]);
        }
        return rtlist;
    }

    public static String print(ArrayList<Coordi> keypoints) {
        // TODO Auto-generated method stub
        String rtStr = "";
        for(int i=0;i<keypoints.size();++i)
        {
            rtStr+="\r\n"+String.format("%.4f",keypoints.get(i).x/8000)
                    +","+keypoints.get(i).y;
        }
        return rtStr;
    }

}
