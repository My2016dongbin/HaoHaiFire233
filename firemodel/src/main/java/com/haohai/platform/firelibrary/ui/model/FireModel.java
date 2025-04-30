package com.haohai.platform.firelibrary.ui.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.haohai.platform.firelibrary.ui.back.CallBack;
import com.haohai.platform.firelibrary.ui.back.CallBack2;
import com.haohai.platform.firelibrary.ui.model.imodel.IFireModel;
import com.ruyiruyi.rylibrary.db.Area;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.request.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.config.DbConfigs;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by geyang on 2020/7/8.
 */

public class FireModel implements IFireModel {
    private static final String TAG = FireModel.class.getSimpleName();
    private Context context;
    private String token;

    public FireModel(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    private List<Area> allAreaList;
    private Handler handler=new Handler(Looper.getMainLooper());

    /**
     * 获取本地数据库数据
     * 数据为空的时候从服务器获取
     * @param callBack2
     */
    @Override
    public void getAreaDate(CallBack2<Area> callBack2) {
        Log.e(TAG, "getAreaDate: 3" );
        //new DbConfig(context)
        if (new DbConfig(context).getAreaList() == null) {
            Log.e(TAG, "getAreaDate: 31" );
            getAreFromService(callBack2);
        }else {
            Log.e(TAG, "getAreaDate: 32" );
            allAreaList = new DbConfig(context).getAreaList();
            callBack2.onSuccess(allAreaList);
        }

    }

    /**
     * 从服务器上获取数据
     * @param callBack2
     */
    private void getAreFromService(final CallBack2<Area> callBack2) {
        JSONObject jsonObject = new JSONObject();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_QUANXIAN + "api/sysArea/list");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + token);
        Log.e(TAG, "getAreFromService: " + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess:diqu -- " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    allAreaList.clear();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        String id = object.getString("id");
                        String name = object.getString("name");
                        String parentId = object.getString("parentId");
                        String level = object.getString("level");
                        String createTime = object.getString("createTime");
                        Area area = new Area(id, name, parentId, createTime, level);
                        allAreaList.add(area);
                    }
                    Log.e(TAG, "onSuccess:allAreaList.size== "+  allAreaList.size() );
                    DbConfig dbConfig = new DbConfig(context);
                    DbManager db = dbConfig.getDbManager();
                    try {
                        db.saveOrUpdate(allAreaList);
                    } catch (DbException e) {

                    }
                    callBack2.onSuccess(allAreaList);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString() );
                callBack2.onFilure("请求失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public void postData(String address,String beizhu,CallBack callBack) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              /*  if (user.equals("123456") && pwd.equals("123456")){
                    callBack.onSuccess();
                }else {
                    callBack.onFilure("账号或密码错误");
                }*/
            }
        },2000 );
    }
}
