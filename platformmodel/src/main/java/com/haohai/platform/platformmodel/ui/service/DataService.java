package com.haohai.platform.platformmodel.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.Department;
import com.ruyiruyi.rylibrary.db.UserModel;
import com.ruyiruyi.rylibrary.request.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class DataService extends Service {
    private static final String TAG = DataService.class.getSimpleName();
    private List<Department> departmentList;
    public DataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        departmentList = new ArrayList<>();
        getBumenDataFromService();
        geUserDataFromService();
    }

    private void getBumenDataFromService() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_QUANXIAN + "api/department/list");
        params.setAsJsonContent(true);
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());

        JSONObject jsonObject = new JSONObject();
        params.setBodyContent(jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    departmentList.clear();
                    Gson gson = new Gson();
                    List<Department> list = gson.fromJson(String.valueOf(data), new TypeToken<List<Department>>() {
                    }.getType());
                    departmentList.addAll(list);
                    DbConfig dbConfig = new DbConfig(getApplicationContext());
                    DbManager db = dbConfig.getDbManager();
                    try {
                        Log.e(TAG, "onSuccess: " + departmentList.size() );
                        db.saveOrUpdate(departmentList);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void geUserDataFromService() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_QUANXIAN + "api/auth/user/list");
        params.setAsJsonContent(true);
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());

        JSONObject jsonObject = new JSONObject();
        params.setBodyContent(jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: user" + result );
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    Gson gson = new Gson();
                    List<UserModel> userList = gson.fromJson(String.valueOf(data), new TypeToken<List<UserModel>>() {
                    }.getType());
                    DbConfig dbConfig = new DbConfig(getApplicationContext());
                    DbManager db = dbConfig.getDbManager();
                    try {
                        Log.e(TAG, "onSuccess: " + userList.size() );
                        db.saveOrUpdate(userList);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }
}
