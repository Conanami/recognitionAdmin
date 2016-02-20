package org.fuxin.caller;

import org.fuxin.util.AudioPlay;
import org.fuxin.util.FuOutput;

import java.util.ArrayList;

public class WaveFileReadDemo {
	public static void main(String[] args) {  
		FuOutput.sop("PhoneFilter Starting...");
		long start = System.currentTimeMillis();
		
		String path="c:\\soundwave\\lt\\ltgj";
		String prefix="1";
		
		
		ArrayList<WaveFileResult> resultlist=WaveFileFilter.CompareAllinPath(path,prefix);
		FuOutput.sop("共"+resultlist.size()+"个电话");
		FuOutput.writeToFile(resultlist, "list");
       
        long end = System.currentTimeMillis();
        FuOutput.sop((end-start)+"ms");
        new AudioPlay("e:\\work\\alarm07.wav");
    }

	

	
	
	
}
