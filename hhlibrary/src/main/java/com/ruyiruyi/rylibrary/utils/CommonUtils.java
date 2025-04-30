package com.ruyiruyi.rylibrary.utils;


public class CommonUtils {
    public String parseSix(String str){
        if(str.length() > 6){
            return str.substring(0,6);
        }else{
            return str;
        }
    }
}
