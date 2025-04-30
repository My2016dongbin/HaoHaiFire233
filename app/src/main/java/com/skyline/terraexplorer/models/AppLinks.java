package com.skyline.terraexplorer.models;

import android.os.Handler;

import java.io.InputStream;
import java.net.URL;

public class AppLinks {
    private static String tutorialLink;
    private static String searchLinkPostfix;
    private static String defaultFlyFile;

    public static String getTutorialUrl() {
        if (tutorialLink == null) {
            tutorialLink = resolveAppUrl("tutorial");   //跟下面的内容是一样的，从网络上获取不到，就用下面的
        }
        //设置默认的跳转路径公司官网
        if (tutorialLink == null)
            tutorialLink = "http://www.ehaohai.com/index.php?c=product&a=detail&id=93";
        return tutorialLink;
    }

    public static String getSearchUrlPostfix() {
        if (searchLinkPostfix == null) {
            searchLinkPostfix = resolveAppUrl("search_postfix");
        }
        if (searchLinkPostfix == null)
            searchLinkPostfix = "AddressSearch/AddressSearch.ashx";
        return searchLinkPostfix;
    }

    /**
     * 获取本地的地图
     * @return
     */
    public static String getLocalDefaultFlyFile()
    {
        String filePath="/storage/sdcard1/手机地图/DefaultMobile.fly";
        return filePath;

    }
    public static String getDefaultFlyFile() {
        if (defaultFlyFile == null) {
            defaultFlyFile = resolveAppUrl("default_fly");  //http://www.skylineglobe.com/SkylineGlobeLayers/SkylineGlobe Mobile/SkylineGlobe Mobile.fly
        }
        if (defaultFlyFile == null)
            defaultFlyFile = "http://www.skylineglobe.com/SkylineGlobeLayers/SkylineGlobe Mobile/SkylineGlobe Mobile.fly";
        return defaultFlyFile;
    }

    public static String getDefaultSearchServer() {
        return "http://www.skylineglobe.com";
    }

    private static String resolveAppUrl(String id) {

        try {
            URL url = new URL("http://www.skylineglobe.com/mobilelinks.ashx?linkid=" + id);
            InputStream in = url.openStream();  //打开到此URL的链接并返回一个从该链接读入的InputStream
            return new java.util.Scanner(in).useDelimiter("\\A").next();    //将流转换为字符串
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void initializeAsync() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
               // AppLinks.getDefaultFlyFile();
//                AppLinks.getTutorialUrl();
//                AppLinks.getSearchUrlPostfix();
            }
        });
    }
}
