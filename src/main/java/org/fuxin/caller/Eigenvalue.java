package org.fuxin.caller;

import java.util.ArrayList;

import org.fuxin.util.ArrayUtil;
import org.fuxin.util.MathUtil;

/***
 * 记录两段相同的音频的各种特征值信息
 * @author Administrator
 *
 */
public class Eigenvalue {
    private int[] sample;       //比对样本
    private int[] standard;     //标准样本
    public double division;    //声音震动的标准差
    public int hanziCount;     //这句话有几个字
    public int  highPlace;	//最高点出现的大致位置
    public ArrayList<Coordi> lowpoints;			//每个字声音开始的位置
    public ArrayList<Coordi> highpoints;        //高点的位置
    public Eigenvalue(int[] newsample, int[] instandard) {
        // TODO Auto-generated constructor stub
        this.sample = newsample;
        this.standard = instandard;
        ScanFileToEigen();

    }

    private void ScanFileToEigen() {
        // TODO Auto-generated method stub
        this.hanziCount = 0;
        this.highPlace = -1;
        double tmpmax= 0;
        int  tmppos= -1;
        int silencecount = 0;
        int lasthighplace = -1;
        lowpoints = new ArrayList<Coordi>();
        highpoints = new ArrayList<Coordi>();
        if(sample.length==standard.length)
        {
            double lasthigh=0;
            double total=0;
            for(int i=0;i<sample.length;i=i+10)
            {
                //这段是计算标准差的
                double s1,s2;

                double bigsound = MathUtil.Average(sample,i,20);
                s1= bigsound;
                if(Math.abs(sample[i])<1000) s1=0;
                else s1=Math.abs(sample[i]);
                if(Math.abs(standard[i])<1000) s2=0;
                else s2=Math.abs(standard[i]);
                total+= Math.pow(s1-s2,2);
                //上面是计算标准差的

                //下面这段是计算有多少个字
                //if(tmpmax<Math.abs(sample[i]))
                //{
                //	tmpmax = Math.abs(sample[i]);
                //	if(tmpmax==32768) highPlace = i;
                //}
                //如果发现新的大声音
                //System.out.println(bigsound);
                if(bigsound>3000)
                {
                    if(tmpmax<Math.abs(sample[i]))
                    {
                        tmpmax = Math.abs(sample[i]);
                        tmppos = i;
                    }
                    if(lasthighplace>0 && silencecount>15)
                    {
                        hanziCount++;
                        //if(tmpmax>10000)
                        highpoints.add(new Coordi(tmppos,tmpmax));
                        tmpmax = 0;
                        tmppos = -1;
                        //if(bigsound<10000)
                        lowpoints.add(new Coordi(i,bigsound));
                    }
                    silencecount=0;
                    lasthighplace=i;

                }

                if(bigsound<1000)
                    silencecount++;


            }
            division = Math.sqrt(total/sample.length);
        }
    }

    public Eigenvalue(int[] regu_sta) {
        this.sample = regu_sta;
        this.standard = regu_sta;
        ScanFileToEigen();
    }

    @Override
    public String toString() {
        return "Eigenvalue [division=" + division + ", hanziCount="
                + hanziCount + ", highPlace=" + highPlace + "]"
                + ArrayUtil.print(lowpoints)
                + ArrayUtil.print(highpoints);
    }

}
