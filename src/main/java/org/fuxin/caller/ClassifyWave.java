package org.fuxin.caller;

import org.fuxin.caller.C.Operator;
import org.fuxin.interf.IClassifyWave;
import org.fuxin.util.OpMgr;
import org.fuxin.util.WaveFileReader;
import org.fuxin.util.WaveMatcher;

import java.io.File;
import java.util.ArrayList;

public class ClassifyWave implements IClassifyWave{

	@Override
	public WaveFileResult Filter(File file, ArrayList<StandFile> standlist) {
		
		WaveFileReader reader = new WaveFileReader(file.getPath());
		WaveFileResult rtwfr = new WaveFileResult(file.getPath());
		//根据文件名决定运营商
		Operator operator = OpMgr.findOp(file.getName().substring(0, 3));
        rtwfr.setOper(operator);

		if(reader.isSuccess())
        {
        	for(int i=0;i<standlist.size();++i)
        	{
        		StandFile stand=standlist.get(i);
        		if(operator==stand.getOperator()
        		    && rtwfr.getType()!=stand.getType())
        		{
        			int result=WaveMatcher.Compare(reader,stand.getWavfile(),stand.getThreshold());
        			if(result==0 && rtwfr.getType()==C.Type.Undo)
        				rtwfr.setType(stand.getType());
        			else if(result==0)
        				rtwfr.setType(C.Type.Unknown);
        		}

        	}
        	//循环执行完毕，一个都没有对上，则认为是正常
        	if(rtwfr.getType()==C.Type.Undo)
        		rtwfr.setType(C.Type.Zc);


        }
        else{
            System.err.println("打开录音文件失败");
            rtwfr.setType(C.Type.Unknown);
        } 
               
        return rtwfr;
	}

}
