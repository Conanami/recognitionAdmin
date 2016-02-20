package org.fuxin.caller;

import org.fuxin.caller.C.Operator;
import org.fuxin.caller.C.Type;

/***
 * ���صĽ����
 * @author Administrator
 *
 */
public class WaveFileResult {
	public WaveFileResult(String path) {
		setFilepath(path);
		setType(C.Type.Undo);
	}
	public String getFilepath() {
		return filepath;
	}
	private void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	private String filepath;  //�ļ���
	private Type type;        //����
	private Operator oper;    //��Ӫ��
	@Override
	public String toString() {
		return filepath + "," + oper + "," + type;
	}
	public Operator getOper() {
		return oper;
	}
	public void setOper(Operator oper) {
		this.oper = oper;
	}
	
	
}
