package com.haohai.ledge.videolibrary.cache;


import com.haohai.ledge.videolibrary.utils.Debuger;
import com.haohai.ledge.videolibrary.videocatch.headers.HeaderInjector;

import java.util.HashMap;
import java.util.Map;

/**
 for android video cache header
 */
public class ProxyCacheUserAgentHeadersInjector implements HeaderInjector {

    public final static Map<String, String> mMapHeadData = new HashMap<>();

    @Override
    public Map<String, String> addHeaders(String url) {
        Debuger.printfLog("****** proxy addHeaders ****** " + mMapHeadData.size());
        return mMapHeadData;
    }
}