package com.xiaobaihb.harmonifunc.Utils;

/**
 * Created by xiaobai on 16-5-22.
 */
public class TonalityUtil {

    /**
     * 十二平律音分对照表/简化表
     * 440*2^((index-49)/12) -> Hz
     * 440Hz->49ind->a1
     */
    private final static String[] toneNameMap = {
            "-"     ,"A2"   ,"#A2"  ,"B2"   ,
            "C1"    ,"#C1"  ,"D1"   ,"#D1"  ,"E1"   ,"F1"   ,"#F1"   ,"G1"   ,"#G1"  ,"A1"   ,"#A1"  ,"B1"   ,
            "C"     ,"#C"   ,"D"    ,"#D"   ,"E"    ,"F"    ,"#F"    ,"G"    ,"#G"   ,"A"    ,"#A"   ,"B"    ,
            "c"     ,"#c"   ,"d"    ,"#d"   ,"e"    ,"f"    ,"#f"    ,"g"    ,"#g"   ,"a"    ,"#a"   ,"b"    ,
            "c1"    ,"#c1"  ,"d1"   ,"#d1"  ,"e1"   ,"f1"   ,"#f1"   ,"g1"   ,"#g1"  ,"a1"   ,"#a1"  ,"b1"   ,
            "c2"    ,"#c2"  ,"d2"   ,"#d2"  ,"e2"   ,"f2"   ,"#f2"   ,"g2"   ,"#g2"  ,"a2"   ,"#a2"  ,"b2"   ,
            "c3"    ,"#c3"  ,"d3"   ,"#d3"  ,"e3"   ,"f3"   ,"#f3"   ,"g3"   ,"#g3"  ,"a3"   ,"#a3"  ,"b3"   ,
            "c4"    ,"#c4"  ,"d4"   ,"#d4"  ,"e4"   ,"f4"   ,"#f4"   ,"g4"   ,"#g4"  ,"a4"   ,"#a4"  ,"b4"   ,
            "c5"
    };

    public static String getToneByHz(float Hz) {
        if (Hz > 4200 || Hz <=0)
            return toneNameMap[0];
        return toneNameMap[Math.round((float) (17.312*Math.log(Hz / 27.5)+1))];
    }

}
