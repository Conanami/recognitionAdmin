package org.fuxin.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FuOutput {
	public static String Date2String(java.util.Date date,String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}


	public static void writeToFile(ArrayList<?> resultListNew, String prefix) {
		// TODO Auto-generated method stub
		if(resultListNew==null || resultListNew.size()==0) return;
		SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyyMMdd-HHmmss");
		String tstamp=sdf.format(new Date());
//		writeTofile6(resultListNew, "d:\\tmp\\"+prefix+"-"+tstamp+".txt");
	}



	public static void writeTofile6(ArrayList<?> insp,
			String filename) {
		// TODO Auto-generated method stub
		if(insp==null || insp.size()==0) return;
		FileOutputStream fos = null;
		try {
			// String str=System.currentTimeMillis();
			fos = new FileOutputStream(filename, true);// 第二个参数设定是否追加文件
			@SuppressWarnings("resource")
			PrintWriter pw = new PrintWriter(fos);
			for (int i=0;i<insp.size();i++) {
				if(insp.get(i)==null)
				{
					pw.write("null");
				}
				else
				{
					pw.write(insp.get(i).toString());
				}
				pw.write("\r\n");
			}
			pw.flush(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public static void printCnt(Integer cnt, int i) {
		// TODO Auto-generated method stub
		if(cnt%i==0)
			System.out.println(cnt);
		
	}


	public static void sop(Object obj) {
		// TODO Auto-generated method stub
		System.out.println(obj);
	}


	public static void showSecond(int i) {
		// TODO Auto-generated method stub
		System.out.println(String.format("%.4f",(double)i/8000)+"s");
	}

}
