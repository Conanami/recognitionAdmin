package org.fuxin.util;

public class MathUtil {

	//???????
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

	public static double getMax(int[] sample, int start, int cnt) {
		int end = start+cnt-1;
		double tmpmax=0;
	
		if(end>=sample.length) end=sample.length -1;
		for(int i=start;i<=end;++i)
		{
			if(tmpmax<=Math.abs(sample[i]))
				tmpmax=Math.abs(sample[i]);
		}
		return tmpmax;
	}

}
