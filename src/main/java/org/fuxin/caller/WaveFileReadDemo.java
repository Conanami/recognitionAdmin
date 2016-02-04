package org.fuxin.caller;

import java.io.File;
import java.util.ArrayList;

import org.fuxin.util.FuOutput;
import org.fuxin.util.OpMgr;
import org.fuxin.util.WaveFileReader;
import org.fuxin.util.WaveMatcher;

public class WaveFileReadDemo {
	public static void main(String[] args) {  
		FuOutput.sop("Now Starting...");
		long start = System.currentTimeMillis();
		
		String path="c:\\soundwave\\0203";
		String prefix="139";
		
		//比对目录下的所有开头为prefix的文件
		ArrayList<WaveFileResult> resultlist=CompareAllinPath(path,prefix);
		FuOutput.sop("总共："+resultlist.size()+"个号码");
		FuOutput.writeToFile(resultlist, "list");
       
        long end = System.currentTimeMillis();
        FuOutput.sop((end-start)+"ms");
    }

	/***
	 * 比对目录path下的所有前缀为prefix的文件
	 * @param path
	 * @param prefix
	 * @return 
	 */
	private static ArrayList<WaveFileResult> CompareAllinPath(String path, String prefix) {
		// TODO Auto-generated method stub
		File file=new File(path);
		File[] tempList = file.listFiles();
				
		ArrayList<WaveFileResult> resultlist = new ArrayList<WaveFileResult>();
		
		//把标准文件读入内存    
        WaveFileReader[]  standlist = { 
        								new WaveFileReader(C.ydkhfile),
        								new WaveFileReader(C.ydtjfile),
        								new WaveFileReader(C.ydgjfile),
        								new WaveFileReader(C.ltkhfile),
        								new WaveFileReader(C.lttjfile),
        								new WaveFileReader(C.ltgjfile),
        								new WaveFileReader(C.dxkhfile),
        								new WaveFileReader(C.dxtjfile),
        								new WaveFileReader(C.dxgjfile)
        							   };
		
		for (int i = 0; i < tempList.length; i++) {
		   if (tempList[i].isFile() 
				   && tempList[i].getName().endsWith(".wav")
				   && tempList[i].getName().startsWith(prefix)) {
			   int operator = OpMgr.findOp(tempList[i].getName().substring(0, 3));  //这里将来是一个辨别运营商的函数
			   
			   WaveFileResult wfr=WaveFileCompare(tempList[i].getPath(),standlist,operator);
			   resultlist.add(wfr);
		   }
		   if (tempList[i].isDirectory()) {
			   //System.out.println("文件夹："+tempList[i]);
		   }
		}
		return resultlist;
	}

	/***
	 * 分别与三个样本比对，返回结果
	 * @param filename
	 * @param kh
	 * @param tj
	 * @param gj
	 * @param operatorName
	 * @return
	 */
	private static WaveFileResult WaveFileCompare(String filename, WaveFileReader[] standlist , int operatorName) {
		// TODO Auto-generated method stub
		WaveFileReader reader = new WaveFileReader(filename);  
		WaveFileResult rtwfr = new WaveFileResult(filename);
		
        if(reader.isSuccess())
        {
        	switch(operatorName)
        	{
        	case 1:
        		//空号
	        	rtwfr.setKhresult(WaveMatcher.Compare(reader,standlist[0],C.value[0]));
	        	//停机
	        	rtwfr.setTjresult(WaveMatcher.Compare(reader,standlist[1],C.value[1]));
	        	//关机
	        	rtwfr.setGjresult(WaveMatcher.Compare(reader,standlist[2],C.value[2]));
	        	//综合结果
	        	rtwfr.setFinalresult();
	        	
	            return rtwfr;
        	case 2:
        		//空号
	        	rtwfr.setKhresult(WaveMatcher.Compare(reader,standlist[3],C.value[3]));
	        	//停机
	        	rtwfr.setTjresult(WaveMatcher.Compare(reader,standlist[4],C.value[4]));
	        	//关机
	        	rtwfr.setGjresult(WaveMatcher.Compare(reader,standlist[5],C.value[5]));
	        	//综合结果
	        	rtwfr.setFinalresult();
	        	
	            return rtwfr;
        	case 3:
        		//空号
	        	rtwfr.setKhresult(WaveMatcher.Compare(reader,standlist[6],C.value[6]));
	        	//停机
	        	rtwfr.setTjresult(WaveMatcher.Compare(reader,standlist[7],C.value[7]));
	        	//关机
	        	rtwfr.setGjresult(WaveMatcher.Compare(reader,standlist[8],C.value[8]));
	        	//综合结果
	        	rtwfr.setFinalresult();
	        	
	            return rtwfr;
        	}
        }  
        else{  
            System.err.println("打开wav文件失败！");  
        } 
        //读WAV文件失败
        rtwfr.setFinalresult(4);
        
        return rtwfr;
	}  
	
	
}
