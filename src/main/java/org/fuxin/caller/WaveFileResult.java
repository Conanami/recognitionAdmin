package org.fuxin.caller;

/***
 * 记录音频文件的文件名，类别，及其它参数
 * @author Administrator
 *
 */
public class WaveFileResult {
	public String filepath;
	private int khresult;
	private int tjresult;
	private int gjresult;
	private int finalresult=0;   
	//0,表示正常电话
	//1,表示空号
	//2,表示停机
	//3,表示关机
	
	public int getFinalresult() {
		return finalresult;
	}

	public void setFinalresult(int finalresult) {
		this.finalresult = finalresult;
	}

	public WaveFileResult(String filename) {
		filepath=filename;
	}

	public int getKhresult() {
		return khresult;
	}

	public void setKhresult(int khresult) {
		this.khresult = khresult;
	}

	public int getTjresult() {
		return tjresult;
	}

	public void setTjresult(int tjresult) {
		this.tjresult = tjresult;
	}

	public int getGjresult() {
		return gjresult;
	}

	public void setGjresult(int gjresult) {
		this.gjresult = gjresult;
	}

	@Override
	public String toString() {
		return filepath + ", " + khresult + ", " + tjresult + ", " + gjresult
				+ ", " + finalresult;
	}

	//根据各种匹配结果计算最终结果
	public void setFinalresult() {
		if(khresult==0 && tjresult==2 && gjresult==2)
		{
			setFinalresult(1);
		}
		if(khresult==2 && tjresult==0 && gjresult==2)
		{	
			setFinalresult(2);
		}
		if(khresult==2 && tjresult==2 && gjresult==0)
		{
			setFinalresult(3);
		}
		if(khresult+tjresult+gjresult<3)
			setFinalresult(4);
		if(khresult+tjresult+gjresult>5)
			setFinalresult(0);
	
		
	}
	
	
}
