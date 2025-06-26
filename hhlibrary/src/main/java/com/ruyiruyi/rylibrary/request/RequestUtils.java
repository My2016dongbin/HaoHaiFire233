package com.ruyiruyi.rylibrary.request;

/**
 * Created by geyang on 2020/6/2.
 */

public class RequestUtils {
//    public static String REQUEST_URL_BASE = "http://172.16.10.69:8083/";//姜
    public static String REQUEST_URL_BASE = "http://117.132.5.139:18012/";//Release
    public static String REQUEST_LOGIN_URL_TEST = "http://192.168.0.144:8083/";            //开发地址
    //public static String REQUEST_URL_HUAWEI = "http://api.ehaohai.com:10090/";            //华为云ip地址
     public static String REQUEST_URL_HUAWEI = "http://121.36.103.6:10090/";            //华为云ip地址11
    public static String REQUEST_URL_HXY = "http://172.16.50.59:10100/park/";            //海信云ip地址11
    public static String REQUEST_URL_HXY_LOGIN = "http://172.16.50.59:10100/auth/";            //海信云ip地址11
//    public static String REQUEST_URL_HXY2 = "http://121.36.68.43:10100/park/";
    public static String REQUEST_URL_HXY2 = REQUEST_URL_BASE + "park/";
//    public static String REQUEST_URL_HXY_LOGIN2 = "http://121.36.68.43:10100/auth/";
    public static String REQUEST_URL_HXY_LOGIN2 = REQUEST_URL_BASE +"auth/";

    public static String REQUEST_URL = REQUEST_URL_HXY2 ;
    public static String LOGIN_URL = REQUEST_URL_HXY_LOGIN2  ;
}
