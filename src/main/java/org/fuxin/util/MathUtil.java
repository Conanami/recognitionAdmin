package org.fuxin.util;

public class MathUtil {

	//求平均数
	public static double Average(int[] sample, int start, int cnt) {
		// TODO Auto-generated method stub
		int end = start+cnt-1;
		double total=0;
		double count=0;
		if(end>=sample.length) end=sample.length -1;
		for(int i=start;i<=end;++i)
		{
			total+=Math.abs(sample[i]);
			count++;
		}
		return (double)(total/count);
	}

}
