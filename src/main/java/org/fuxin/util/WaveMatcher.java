package org.fuxin.util;

import org.fuxin.caller.C;
import org.fuxin.caller.Eigenvalue;

import java.util.ArrayList;

/***
 * ????????????????��??
 * @author Administrator
 *
 */
public class WaveMatcher {


	/***
	 *
	 * @param reader
	 * @param standard
	 * @param
	 * @return
	 * 0 ƥ����
	 * 1 ̫��
	 * 2 û��ƥ����
	 * 3 �ļ���ʽ��ƥ��
	 */
	public static int Compare(WaveFileReader reader, WaveFileReader standard, double d) {
		// TODO Auto-generated method stub
		if(reader.getSampleRate()==standard.getSampleRate()
			&& reader.getBitPerSample()==standard.getBitPerSample()
			&& reader.getNumChannels()==standard.getNumChannels()
			&& reader.getNumChannels()==1 )
		{
			return SoundMatch(reader.getData()[0],standard.getData()[0],d,reader.getFilename(),standard.getFilename());
		}
		else
			return 3;

	}

	/***
	 *
	 * @param sample
	 * @param standard
	 * @param d
	 *
	 */
	private static int SoundMatch(int[] sample, int[] standard, double d,String filename,String standfile) {
		// TODO Auto-generated method stub
		if(sample.length<standard.length)
		{
			//��������ȱ�׼�Ķ̣����޷��жϣ�����1
			return 1;
		}
		else
		{
			int meetcnt = 0;
			int[] regu_sta=Regulize(standard);



			ArrayList<String> tofile = new ArrayList<>();
			int step = 5;
			if(sample.length<24000) step = 2;
			for(int i=0;i<=sample.length-standard.length;i=i+step)
			{
				String output=String.format("%.2f", ((float)i/8000))+",";
				int[] newsample=ArrayUtil.Cut(sample,i,standard.length);
				int[] regu_new=Regulize(newsample);


				Eigenvalue ev =SameMatch(regu_new,regu_sta);

				output=output+ev.division+","+ev.sample_sqr+","+ev.stand_sqr;
				if(i%100==0)
				{
					System.out.println(output);
				}
				tofile.add(output);


				if(ev.division < d*3 &&
						Math.abs(ev.sample_sqr/ev.stand_sqr-1)<=0.8)
					meetcnt++;
				else
				//ר������ر�������
				/*if(ev.sample_sqr>ev.stand_sqr
						&& ev.division<d*(ev.sample_sqr/ev.stand_sqr)
						&& ev.sample_sqr/ev.stand_sqr<1.7)
					meetcnt++;

				else*/
					meetcnt=0;


				if(meetcnt>0)
				{
					return 0;
				}
			}
			//���û��ƥ���ϣ����¼���ļ�
			FuOutput.writeToFile(tofile, CutDotWave(filename)+CutDotWave(standfile));
		}
		//û��ƥ���ϣ��ͷ���2
		return 2;
	}

	private static String CutDotWave(String filename){
	    String tmp = filename.substring(0,filename.lastIndexOf("."));
		return tmp.substring(tmp.lastIndexOf("\\")+1,tmp.length());
	}
	/***
	 * ���򻯣�ͳһ���ֵ
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
			if(max>C.smallnoise)
				temp = sound/max;
			else
				temp = 0;
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
	 * ȡ�������ļ�����ֵ
	 * @param newsample
	 * @param standard
	 * @return?
	 */
	private static Eigenvalue SameMatch(int[] newsample, int[] standard) {
		Eigenvalue rtev = new Eigenvalue(newsample,standard);
		return rtev;
	}
	
}
