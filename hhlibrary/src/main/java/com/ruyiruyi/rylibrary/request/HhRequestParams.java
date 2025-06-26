package com.ruyiruyi.rylibrary.request;

import android.util.Base64;
import android.util.Log;

import org.xutils.http.RequestParams;
import org.xutils.http.app.ParamsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.io.UnsupportedEncodingException;

/**
 * Created by geyang on 2020/12/21.
 */

public class HhRequestParams extends RequestParams {
    public HhRequestParams() {
    }

    public HhRequestParams(String uri) {
        super(uri);
    }

    public HhRequestParams(String uri, ParamsBuilder builder, String[] signs, String[] cacheKeys) {
        super(uri, builder, signs, cacheKeys);
    }

    @Override
    public void setBodyContent(String content) {

        String base64Str = Base64.encodeToString(content.getBytes(), Base64.DEFAULT);
        /*String base64Str = "";
        String base64Stragain="";
        try {
            base64Str = URLEncoder.encode(content,"UTF-8");
            base64Stragain=URLEncoder.encode(base64Str,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        super.setBodyContent(base64Str);
        Log.e("", "setBodyContent: "+base64Str);
    }


}
