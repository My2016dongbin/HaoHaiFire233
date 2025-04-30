package com.ruyiruyi.rylibrary.utils;

import java.math.BigDecimal;

/**
 * Created by 13589 on 2019/8/9.
 */

public class NumberUtils {

    /**
     * 保留double后两位小数
     * @param d
     * @return
     */
    public static Double saveOneBitTwo(Double d){
        BigDecimal bd = new BigDecimal(d);
        Double tem = bd.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue();
        return tem;
    }

    public NumberUtils() {
    }
}
