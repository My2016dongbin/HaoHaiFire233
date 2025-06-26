package com.skyline.terraexplorer.wisdomgarden.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyline.terraexplorer.wisdomgarden.models.point;
import com.skyline.terraexplorer.wisdomgarden.models.qushiinfo;
import com.skyline.terraexplorer.wisdomgarden.models.userGuid;

import org.xutils.x;
import java.lang.reflect.Type;

/**
 * SharedPreferences 管理类
 */
public class PrefsManager {

    public static final String SP_NAME = "newsapplication";

    public static final String point = "point";
    public static final String qsinfo = "qsinfo";
    public static final String userGuid = "userGuid";

    private static SharedPreferences getSharedPreference() {
        return x.app().getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);
    }

    /**
     * 设置经纬度
     *
     * @param data
     */
    public static void setpoint(point data) {
        SharedPreferences sp = getSharedPreference();
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(point);
        editor.apply();
        editor.putString(point, new Gson().toJson(data));
        editor.apply();
    }

    /**
     * 获取经纬度
     *
     * @return
     */
    public static com.skyline.terraexplorer.wisdomgarden.models.point getpoint() {
        SharedPreferences sp = getSharedPreference();
        Type listType = new TypeToken<point>() {
        }.getType();
        return new Gson().fromJson(sp.getString(point, ""),
                listType);
    }
    /**
     * 设置区市详情
     *
     * @param data
     */
    public static void setqushinfo(qushiinfo data) {
        SharedPreferences sp1 = getSharedPreference();
        SharedPreferences.Editor editor1 = sp1.edit();
        editor1.remove(point);
        editor1.apply();
        editor1.putString(point, new Gson().toJson(data));
        editor1.apply();
    }
    /**
     * 获取区市详情
     *
     * @return
     */
    public static point getqushinfo() {
        SharedPreferences sp1 = getSharedPreference();
        Type listType = new TypeToken<point>() {
        }.getType();
        return new Gson().fromJson(sp1.getString(qsinfo, ""),
                listType);
    }
    /**
     * 设置经纬度
     *
     * @param data
     */
    public static void setGuid(userGuid data) {
        SharedPreferences sp = getSharedPreference();
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(userGuid);
        editor.apply();
        editor.putString(userGuid, new Gson().toJson(data));
        editor.apply();
    }

    /**
     * 获取经纬度
     *
     * @return
     */
    public static com.skyline.terraexplorer.wisdomgarden.models.userGuid getGuid() {
        SharedPreferences sp = getSharedPreference();
        Type listType = new TypeToken<point>() {
        }.getType();
        return new Gson().fromJson(sp.getString(userGuid, ""),
                listType);
    }
}
