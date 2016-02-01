package org.fuxin.caller;

import java.util.ArrayList;

import javax.swing.JFrame;

import org.fuxin.util.ArrayUtil;
import org.fuxin.util.FuOutput;
import org.fuxin.util.WaveFileReader;
import org.fuxin.util.WaveMatcher;

public class WaveFileReadDemo {
    public static void main(String[] args) {
        FuOutput.sop("Now Starting...");
        long start = System.currentTimeMillis();

        String dir = "C:\\tools\\audio\\wavefiles\\";
        // TODO Auto-generated method stub
        String filename = dir + "201602010846208227.wav";
        String ydkonghaofile = dir + "ydkhshort.wav";
        String ltkonghaofile = dir + "ydtj01.wav";
        String dxkonghaofile = dir + "ydtj02.wav";

        JFrame frame = new JFrame();
        WaveFileReader reader = new WaveFileReader(filename);
        //ArrayList sample=ArrayUtil.toArrayList(reader.getData()[0]);
        //FuOutput.writeTofile6(sample, "e:\\konghao.csv");
        WaveFileReader ydkh = new WaveFileReader(ydkonghaofile);

        WaveFileReader ltkh = new WaveFileReader(ltkonghaofile);
        WaveFileReader dxkh = new WaveFileReader(dxkonghaofile);
        int operatorName = 1;  //运营商,1是移动，2是联通，3是电信
        int result = -1;      //返回结果
        if(     reader.isSuccess()
                && ydkh.isSuccess()
                && ltkh.isSuccess()
                && dxkh.isSuccess()){
            switch(operatorName)
            {
                case 1:
                    result = WaveMatcher.Compare(reader,ydkh,2000);
                    break;
                case 2:
                    result = WaveMatcher.Compare(reader, ltkh,1850);
                    break;
                case 3:
                    result = WaveMatcher.Compare(reader, dxkh,1500);
                    break;
                default:
                    break;
            }
            System.out.println(result);
        	/*int[] data = reader.getData()[0]; //获取第一声道
            DrawPanel drawPanel = new DrawPanel(data); // 创建一个绘制波形的面板
            frame.add(drawPanel);
            frame.setTitle(filename);
            frame.setSize(800, 400);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true); */
        }
        else{
            System.err.println(filename + "不是一个正常的wav文件");
        }
        long end = System.currentTimeMillis();
        FuOutput.sop((end-start)+"ms");
    }
}
