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
	public double stand_sqr=0;					//标准音频的面积
	public double sample_sqr=0;                 //样本音频的面积
	public Eigenvalue(int[] newsample, int[] instandard) {
		// TODO Auto-generated constructor stub
		this.sample = newsample;
		this.standard = instandard;
		ScanFileToEigen();
		
	}

	/***
	 * 扫描文件，获得各种声音文件特征值
	 */
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
			double total=0;
			int step = 10;
					
			for(int i=0;i<sample.length;i=i+step)
			{
				//这段是计算标准差的
				double s1,s2;
				
				double bigsound = MathUtil.Average(sample,i,step);
				s1= bigsound;
				if(Math.abs(sample[i])<C.smallnoise) s1=0;
				else s1=Math.abs(sample[i]);
				if(Math.abs(standard[i])<C.smallnoise) s2=0;
				else s2=Math.abs(standard[i]);
				total+= Math.abs(s1-s2);
				stand_sqr+=s2;
				sample_sqr+=s1;
				//上面是计算标准差的
				
			
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
						highpoints.add(new Coordi(tmppos,tmpmax));
						tmpmax = 0;
						tmppos = -1;
						lowpoints.add(new Coordi(i,bigsound));
					}
					silencecount=0;
					lasthighplace=i;
					
				}
					
				if(bigsound<C.smallnoise)
					silencecount++;
				
				
			}
			division = (total*step)/sample.length;
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
