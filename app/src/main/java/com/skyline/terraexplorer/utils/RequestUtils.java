package com.skyline.terraexplorer.utils;

import static com.ruyiruyi.rylibrary.request.RequestUtils.REQUEST_URL_BASE;

public class RequestUtils {
/*    public static String REQUEST_URL_TEST = "http://27.223.18.10:10100/";            //主页正式
    public static String REQUEST_URL_RESRCE = "http://27.223.18.10:10100/resource/";            //主页正式*/
    public static String REQUEST_URL_TEST = "http://120.221.95.109:8060/";            //主页正式 华为云
    public static String REQUEST_URL_BASE_ = REQUEST_URL_BASE + "resource";            //主页正式 华为云
    public static String REQUEST_URL_RESRCE_OLD = REQUEST_URL_BASE + "resource/";            //主页正式 华为云
    public static String REQUEST_URL_RESRCE = REQUEST_URL_BASE + "resource/";            //主页正式 华为云
    public static String REQUEST_URL_RESRCE_LOGIN = REQUEST_URL_BASE;            //主页正式 华为云

    //public static String IAMGE_URL = "http://27.223.18.10:9180/" ;            //
    public static String IAMGE_URL = REQUEST_URL_BASE ;            //华为云图片链接
    public static String OTHER_IAMGE_URL = REQUEST_URL_BASE ;            //主页测试




    public static String REQUEST_URL = REQUEST_URL_RESRCE  ;



}