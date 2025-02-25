package com.hht.hsatellitemobile.utils;

import org.xutils.http.RequestParams;

/**
 * Created by 13589 on 2019/8/13.
 */

public class NetParams extends RequestParams{


    public NetParams(String url, int timeOut){

        setConnectTimeout(timeOut==0?30*1000:timeOut);
        addHeader("Type","android");
        long timestamp = System.currentTimeMillis()/1000;
        addHeader("Timestamp", timestamp+"");


    }
}
