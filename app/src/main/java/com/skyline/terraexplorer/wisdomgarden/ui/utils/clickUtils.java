package com.skyline.terraexplorer.wisdomgarden.ui.utils;

/**
 * Created by lenghaoiyuan on 2020/8/21.
 */

public class clickUtils {
        private static long lastClickTime;
        public static boolean isFastDoubleClick() {
            long time = System.currentTimeMillis();
            long timeD = time - lastClickTime;
            if ( 0 < timeD && timeD < 1000) {       //1000毫秒内按钮无效，这样可以控制快速点击，自己调整频率
                return true;
            }
            lastClickTime = time;
            return false;
        }
    }

