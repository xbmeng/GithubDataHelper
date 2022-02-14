package com.mxb.common.util;

import java.util.Random;

//06c78999cb0b07e20afcbc6cd1f1d5fcdb4e05
//0f
public class AccessTookenUtil {
    private static final String accessTooken1 = "06c78999cb0b07e20afcbc6cd1f1d5fcdb4e05" + "0f";

    private static final String accessTooken2 = "15b5ece7c6abc08a1e61dd0f52aa639c1a6e27" + "ce";

    public static String getAccessTookenUtil()
    {
        Random random = new Random();
        int i = random.nextInt(10);
        if(i % 2 == 0)
            return accessTooken1;
        else
            return accessTooken2;
    }
}
