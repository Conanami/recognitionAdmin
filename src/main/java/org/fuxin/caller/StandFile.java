package org.fuxin.caller;

import org.fuxin.caller.C.Operator;
import org.fuxin.caller.C.Type;
import org.fuxin.util.WaveFileReader;

/***
 * 样本文件类
 * @author Administrator
 *
 */
public class StandFile {
	private WaveFileReader wavfile;
	private double threshold;
	private Operator operator;
	private Type type;
	
	public StandFile(WaveFileReader wavfile, double threshold, Operator yd,
                     Type kh) {
		super();
		this.wavfile = wavfile;
		this.threshold = threshold;
		this.setOperator(yd);
		this.setType(kh);
	}
	
	public WaveFileReader getWavfile() {
		return wavfile;
	}
	public void setWavfile(WaveFileReader wavfile) {
		this.wavfile = wavfile;
	}
	public double getThreshold() {
		return threshold;
	}
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	
}
