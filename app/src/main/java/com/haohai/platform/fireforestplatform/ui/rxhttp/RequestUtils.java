package com.haohai.platform.fireforestplatform.ui.rxhttp;

import rxhttp.wrapper.annotation.DefaultDomain;

/**
 * Created by geyang on 2020/10/22.
 */

public class RequestUtils {
    @DefaultDomain() //设置为默认域名
    public static String baseUrl = "http://api.ehaohai.com:10100/";
}
