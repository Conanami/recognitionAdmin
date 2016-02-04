package org.fuxin.util;

import java.util.ArrayList;

import org.fuxin.caller.C;
import org.fuxin.caller.Eigenvalue;

/***
 * 对两段声音文件进行比较
 * @author Administrator
 *
 */
public class WaveMatcher {
	
	
	/***
	 * 
	 * @param reader
	 * @param standard
	 * @param threshold是阈值，小于这个值算匹配成功 
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
			int[] regu_sta=Regulize(standard);
			
			//Eigenvalue sta_ev=new Eigenvalue(regu_sta);
			
			ArrayList<String> tofile = new ArrayList<String>();
			//取样样本的间隔进行自动选择
			int step = 5;
			if(sample.length<24000) step = 2; 
			for(int i=0;i<=sample.length-standard.length;i=i+step)
			{
				String output=String.format("%.2f", ((float)i/8000))+"|";
				int[] newsample=ArrayUtil.Cut(sample,i,standard.length);
				//如果切下来的一段，与standard的误差在一个范围之内，则返回匹配上
				//正则化standard和newsample，然后再计算
				int[] regu_new=Regulize(newsample);
				
				
				Eigenvalue ev =SameMatch(regu_new,regu_sta);
				
				//这个是输出的检查，test用的
				if(i%100==0)
				{
					output=output+ev.division+"|"+ev.sample_sqr+"|"+ev.stand_sqr;
					System.out.println(output);
					tofile.add(output);
				}
				
				
				//double matchrate = EigenMatcher.isMatch(ev,sta_ev);
				if(ev.division < thresholdValue &&
						Math.abs(ev.sample_sqr/ev.stand_sqr-1)<=0.5)
					meetcnt++;
				else
					meetcnt=0;
				
				//连续2次小于一个值，认为匹配上
				if(meetcnt>0) 
				{
					//这里全部是输出检查用的代码test
					//System.out.println(sta_ev);
					//System.out.println(ev);
					//System.out.println("matchrate:"+matchrate);
					//FuOutput.showSecond(i);
					return 0;
				}
			}
			//如果没有匹配上，则输出全部的标准差，看看问题在哪里
			//这里是test
			FuOutput.writeToFile(tofile, "wav");
		}
		//正常没匹配上；
		return 2;
	}

	/***
	 * 统一音量大小
	 * @param sample
	 * @return
	 */
	private static int[] Regulize(int[] sample) {
		
		double max=GetMax(sample);
		int[] rt=new int[sample.length];
		for(int i=0;i<sample.length;++i)
		{
			double temp;
			double sound;
			sound = sample[i];
			temp = sound/max;
			rt[i]= (int) (temp * C.maxwave);
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
