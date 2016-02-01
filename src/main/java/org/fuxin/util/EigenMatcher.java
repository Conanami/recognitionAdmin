package org.fuxin.util;

import org.fuxin.caller.Coordi;
import org.fuxin.caller.Eigenvalue;

public class EigenMatcher {

    public static double isMatch(Eigenvalue ev, Eigenvalue sta_ev) {
        // TODO Auto-generated method stub
        int lowCnt = sta_ev.lowpoints.size();
        int highCnt = sta_ev.highpoints.size();
        int totalCnt = lowCnt+highCnt;
        double meetCnt = 0;        //有几个点
        double meetRate = 0;       //符合率是多少


        for(int i=0;i<sta_ev.highpoints.size();++i)
        {
            for(int j=0;j<ev.highpoints.size();++j)
            {
                if(IsMeet(sta_ev.highpoints.get(i),ev.highpoints.get(j)))
                    meetCnt++;
            }
        }
        meetRate = meetCnt/highCnt;
        return meetRate;
    }

    private static boolean IsMeet(Coordi coordi, Coordi coordi2) {
        // TODO Auto-generated method stub
        if(Math.abs(coordi.x-coordi2.x)<240
                &&	Math.abs(coordi.y-coordi2.y)<10000) return true;
        else return false;
    }

}
