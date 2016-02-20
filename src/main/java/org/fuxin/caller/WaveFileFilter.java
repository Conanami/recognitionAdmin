package org.fuxin.caller;

import org.fuxin.caller.C.Operator;
import org.fuxin.caller.C.Type;
import org.fuxin.util.WaveFileReader;

import java.io.File;
import java.util.ArrayList;

/***
 * 这是个如何使用接口
 * IClassifyWave
 * 的示例类
 * @author Administrator
 *
 */
public class WaveFileFilter {
	/***
	 * 处理目录下所有的文件
	 * @param path
	 * @param prefix
	 * @return
	 */
	public static ArrayList<WaveFileResult> CompareAllinPath(String path, String prefix) {
		// TODO Auto-generated method stub
		File file=new File(path);
		File[] tempList = file.listFiles();

		ArrayList<WaveFileResult> resultlist = new ArrayList<>();

		ArrayList<StandFile> stanlist = new ArrayList<>();

        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ydkhfile),2000,Operator.Yd,Type.Kh));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ydtjfile), 3600,Operator.Yd,Type.Tj));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ydgjfile), 3000,Operator.Yd,Type.Gj));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ltkhfile),2600,Operator.Lt,Type.Kh));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.lttjfile), 1500,Operator.Lt,Type.Tj));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ltgjfile), 2000,Operator.Lt,Type.Gj));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.dxkhfile),3000,Operator.Dx,Type.Kh));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.dxtjfile), 3000,Operator.Dx,Type.Tj));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.dxgjfile), 2250,Operator.Dx,Type.Gj));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.lttjfile2),1500,Operator.Lt,Type.Tj));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ltkhfile2), 2000,Operator.Lt,Type.Kh));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ltkhfile3), 3000,Operator.Lt,Type.Kh));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.dxkhfile2),4000,Operator.Dx,Type.Kh));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.dxgjfile2), 2500,Operator.Dx,Type.Gj));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.dxgjfile3), 2000,Operator.Dx,Type.Gj));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ydkhfile2),2000,Operator.Yd,Type.Kh));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ydgjfile2), 2600,Operator.Yd,Type.Gj));
        stanlist.add(new StandFile(new WaveFileReader(C.standfolder+C.ydgjfile3), 2500,Operator.Yd,Type.Gj));


		for (int i = 0; i < tempList.length; i++) {
		   if (tempList[i].isFile()
				   && tempList[i].getName().endsWith(".wav")
				   && tempList[i].getName().startsWith(prefix)) {


			   //对文件进行分类
			   ClassifyWave cw= new ClassifyWave();
			   WaveFileResult wfr = cw.Filter(tempList[i], stanlist);
			   resultlist.add(wfr);
		   }
		   if (tempList[i].isDirectory()) {
			   //System.out.println("????У?"+tempList[i]);
		   }
		}
		return resultlist;
	}
	
	
}
