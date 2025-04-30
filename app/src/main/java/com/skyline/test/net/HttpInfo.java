package com.skyline.test.net;

import android.view.View;

import com.skyline.test.plan.WordInfo;

import java.util.List;

/**
 * Created by miao on 2017/5/13 10:09.
 */

public class HttpInfo {

    //预案管理列表地址
//    public static String DOWNLOAD_LIST_URL = "http://192.168.56.1:8080/haohaigis/downloadList.json";
    public static String DOWNLOAD_LIST_URL = "http://192.168.1.122:8080/haohaigis/downloadList.json";


    public static boolean IS_DOWNLOAD = false;
    public static View download;
    public static View pb;
    public static View finish;

    public static List<WordInfo> localWord = null;



}
