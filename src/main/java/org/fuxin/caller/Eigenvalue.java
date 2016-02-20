package org.fuxin.caller;

import org.fuxin.util.MathUtil;

/***
 * 获取特征值
 * @author Administrator
 *
 */
public class Eigenvalue {
	private int[] sample;       //样本文件
	private int[] standard;     //标准文件
	public double division;    //差值
	
	public double stand_sqr=0;					//标准面积
	public double sample_sqr=0;                 //样本面积
	
	public Eigenvalue(int[] newsample, int[] instandard) {
		// TODO Auto-generated constructor stub
		this.sample = newsample;
		this.standard = instandard;
		ScanFileToEigen();
		
	}

	
	private void ScanFileToEigen() {
			
		if(sample.length==standard.length)
		{
			double total=0;
			int step = 20;
					
			for(int i=0;i<sample.length;i=i+step)
			{
						
				double s1 = MathUtil.getMax(sample,i,step);
				double s2 = MathUtil.getMax(standard,i,step);
								
				if(s1<C.smallnoise) s1=0;
				if(s2<C.smallnoise) s2=0;
				
				total+= Math.abs(s1-s2);
				stand_sqr+=s2;
				sample_sqr+=s1;
			}
			division = total*step/sample.length;
		}
	}

	public Eigenvalue(int[] regu_sta) {
		this.sample = regu_sta;
		this.standard = regu_sta;
		ScanFileToEigen();
	}

	
	
}
