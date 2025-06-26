package com.skyline.terraexplorer.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyline.terraexplorer.db.CheckField;
import com.skyline.terraexplorer.db.CheckRecord;
import com.skyline.terraexplorer.db.DangerRecord;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.db.ImagesUrl;
import com.skyline.terraexplorer.mainapps.db.User;
import com.skyline.terraexplorer.utils.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by geyang on 2019/11/27.
 * 检测 网络好的情况下 将本地数据库数据上传
 */


public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    private String token;
    private DbConfig dbConfig;
    public Context context;
    private DbManager db;
    public int currentPostNum = 0;
    public List<String> fullList;
    private  List<ImagesUrl> imagesUrlList;
    private List<CheckRecord> checkRecordList;
    private List<DangerRecord> dangerRecordList;

    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "3G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        context = context;
        fullList = new ArrayList<>();
        dbConfig = new DbConfig(context);
        db = dbConfig.getDbManager();
        User user = dbConfig.getUser();
        imagesUrlList = new ArrayList<>();
        checkRecordList = new ArrayList<>();
        dangerRecordList = new ArrayList<>();
        if (user!=null){
            token = user.getToken();
        }

        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            Log.e("TAG", "wifiState:" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
            }
        }
        // 监听wifi的连接状态即是否连上了一个有效无线路由
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                // 获取联网状态的NetWorkInfo对象
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                //获取的State对象则代表着连接成功与否等状态
                NetworkInfo.State state = networkInfo.getState();
                //判断网络是否已经连接
                boolean isConnected = state == NetworkInfo.State.CONNECTED;
                Log.e("TAG", "isConnected:" + isConnected);
                if (isConnected) {
                } else {

                }
            }
        }
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent
                    .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI){//连上wifi走这个

                        Log.e(TAG, "onReceive: 连上 wifi"  );
                    //    Log.e("TAG", getConnectionType(info.getType()) + "连上（wifi）"+host[0][0]);

                        checkRecordList.clear();
                        dangerRecordList.clear();
                        imagesUrlList.clear();


                        checkRecordList = new DbConfig(context).getCheckRecordList();
                        dangerRecordList = new DbConfig(context).getDangerRecordList();

                        imagesUrlList = new DbConfig(context).getImageUrlList();

                        if (imagesUrlList!=null) {
                            Log.e(TAG, "imagesUrlList:本地还有图片-- " + imagesUrlList.size());
                            for (int i = 0; i < imagesUrlList.size(); i++) {
                                if (imagesUrlList.get(i).getFullImagePath() == null){
                                    postImagesToService(i);
                                }
                            }
                        }else {
                            Log.e(TAG, "imagesUrlList:没有图片" );
                        }


                    }else if (info.getType() == ConnectivityManager.TYPE_MOBILE){//4G走这个
                        Log.e(TAG, "onReceive: 4G"  );
                        checkRecordList.clear();
                        dangerRecordList.clear();
                        imagesUrlList.clear();

                        checkRecordList = new DbConfig(context).getCheckRecordList();
                        dangerRecordList = new DbConfig(context).getDangerRecordList();
                        try {
                            imagesUrlList = db.selector(ImagesUrl.class).findAll();
                            if (imagesUrlList.size() > 0){
                                for (int i = 0; i < imagesUrlList.size(); i++) {
                                    if (imagesUrlList.get(i).getFullImagePath() != null){
                                        postImagesToService(i);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                      //  Log.e("TAG", getConnectionType(info.getType()) + "连上（3g）"+host[0][0]);
                      /*  List<CheckRecord> checkRecordList = new DbConfig(context).getCheckRecordList();
                        List<DangerRecord> dangerRecordList = new DbConfig(context).getDangerRecordList();
                        if (checkRecordList != null){
                            Log.e(TAG, "onReceive:checkRecordList- " +  checkRecordList.size());
                            postCheckRecordToService(checkRecordList);
                        }
                        if (dangerRecordList!=null){
                            Log.e(TAG, "onReceive: dangerRecordList-" +  dangerRecordList.size());
                            postDangerRedordToService(dangerRecordList);
                        }*/
                    }
                } else {
                    Log.e(TAG, "onReceive: 断开连接"  );
                   // Log.e("TAG", getConnectionType(info.getType()) + "断开");
                }
            }
        }
    }

    private void postImagesToService(final int index) {
        Log.e(TAG, "postimage: index="+index );
        ImagesUrl imagesUrl = imagesUrlList.get(index);
        String pic = imagesUrl.getPic();

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(pic),null,pic);
        params.addHeader("Authorization","bearer " + token);
        Log.e(TAG, "postimage: " + params );
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "postimage: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        String fullImagePath = data.getJSONObject(0).getString("fullPath");
                        imagesUrlList.get(index).setFullImagePath(fullImagePath);
                        try {
                            db.saveOrUpdate(imagesUrlList);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "imagesUrlList:已经上传成功 " + index );
                        currentPostNum++;

                        if (currentPostNum >= imagesUrlList.size()){        //图片上传成功后 开始上传数据
                            Log.e(TAG, "imagesUrlList:全部上传成功 " + checkRecordList.size() );
                            if (checkRecordList.size() > 0){
                                //  Log.e(TAG, "onReceive:checkRecordList- " +  checkRecordList.size());
                                postCheckRecordNewToService();
                            }
                            if (dangerRecordList.size() > 0){
                                //   Log.e(TAG, "onReceive: dangerRecordList-" +  checkRecordList.size());
                                postDangerRedordToService(dangerRecordList);
                            }
                        }


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


    private void postDangerRedordToService(List<DangerRecord> dangerRecordList) {
        for (int i = 0; i < dangerRecordList.size(); i++) {
            DangerRecord dangerRecord = dangerRecordList.get(i);

            if (dangerRecord.getPic1().isEmpty()){
                postDangerDataService(dangerRecord,"","");
            }else {
                postDangerImageService(dangerRecord);
            }
        }
    }

    private void postDangerImageService(final DangerRecord dangerRecord) {
        final String pic1 = dangerRecord.getPic1();
        final String pic2 = dangerRecord.getPic2();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(pic1),null,pic1);
        params.addHeader("Authorization","bearer " + token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        String fullImagePath1 = data.getJSONObject(0).getString("fullPath");
                        if (!pic2.isEmpty()){
                            postDangerPic2Service(dangerRecord,fullImagePath1);
                        }else {
                            postDangerDataService(dangerRecord,fullImagePath1,"");
                        }

                    }else {

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

    private void postDangerPic2Service(final DangerRecord dangerRecord, final String fullImagePath1) {
        String pic2 = dangerRecord.getPic2();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(pic2),null,pic2);
        Log.e(TAG, "postDataService: ---" +"bearer " + token  );
        Log.e(TAG, "反馈---" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        String fullImagePath2 = data.getJSONObject(0).getString("fullPath");
                        postDangerDataService(dangerRecord,fullImagePath1,fullImagePath2);


                    }else {

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

    private void postDangerDataService(final DangerRecord dangerRecord, String fullImagePath1, String fullImagePath2) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("resourceId",dangerRecord.getResourceId());
            jsonObject.put("resourceName",dangerRecord.getResourceName());
            jsonObject.put("resourceType",dangerRecord.getResourceType());
            jsonObject.put("dangerDescription",dangerRecord.getDangerDescription()); //隐患描述
            jsonObject.put("dangerType",dangerRecord.getDangerType()); // 隐患类型 ,
            jsonObject.put("remark",dangerRecord.getRemark()); // 整治描述 ,
            jsonObject.put("status",dangerRecord.getStatus()); // 整治描述 ,
            JSONObject postsion = new JSONObject();
            postsion.put("lat",Double.parseDouble(dangerRecord.getLat()));
            postsion.put("lng",Double.parseDouble(dangerRecord.getLng()));
            jsonObject.put("position",postsion); // 整治描述 ,

            jsonObject.put("pic1",fullImagePath1);
            jsonObject.put("pic2",fullImagePath2);
            jsonObject.put("gridId",dangerRecord.getGridId());
            jsonObject.put("gridName",dangerRecord.getGridName());
            jsonObject.put("gridNo",dangerRecord.getGridNo());

        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/dangerCheck");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + token);
        Log.e(TAG, "postData:-- jsonObject.toString()--" + jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    if (code.equals("200")){
                        Log.e(TAG, "onSuccess:111 " );
                       /* if (!dangerRecord.getPic1().isEmpty()){
                            deleteImage(dangerRecord.getPic1(),context);
                        }
                        if (!dangerRecord.getPic2().isEmpty()){
                            deleteImage(dangerRecord.getPic2(),context);
                        }*/
                        Log.e(TAG, "onSuccess:222 " );
                        try {
                            Log.e(TAG, "onSuccess: 本地数据删除");
                            db.delete(dangerRecord);

                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }else {

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



    /**
     * 新方法 9tu
     * 提交资源检查离线数据
     */
    private void postCheckRecordNewToService() {
        for (int i = 0; i < checkRecordList.size(); i++) {
            CheckRecord checkRecord = checkRecordList.get(i);
            postQianmingService(checkRecord, "", "");//上传签名
        }
    }

    /**
     * 单张图
     * 提交资源检查离线数据
     */
    private void postCheckRecordToService(List<CheckRecord> checkRecordList) {
        for (int i = 0; i < checkRecordList.size(); i++) {
            CheckRecord checkRecord = checkRecordList.get(i);
            String id = checkRecord.getId();
            try {
                List<ImagesUrl> imagesUrls = new DbConfig(context).getDbManager().selector(ImagesUrl.class)
                        .where("resourceid", "=", id)
                        .findAll();

                if (imagesUrls != null){
                    for (int j = 0; j < imagesUrls.size(); j++) {
                        postMoreImagesService(imagesUrls.get(i).getPic(),j < imagesUrls.size() ? false : true);
                    }
                }else {
                    postQianmingService(checkRecord, "", "");//上传签名
                }
            } catch (DbException e) {
                e.printStackTrace();
            }


          /*  if (checkRecord.getPic1().isEmpty()){
                postQianmingService(checkRecord, "", "");//上传签名
            }else {
                postImageService(checkRecord);
            }*/

        }
    }

    /**
     * 上传图片 9图循环上传
     * @param pic
     */
    private void postMoreImagesService(String pic, final boolean lastPic) {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(pic),null,pic);
        params.addHeader("Authorization","bearer " + token);
        Log.e(TAG, "postimage: " + params );
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "postimage: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        String fullImagePath = data.getJSONObject(0).getString("fullPath");
                        fullList.add(fullImagePath);
                        currentPostNum++;
                        if (lastPic){   //最后一张图
                            //postMoreImagesService();
                        }else {
                         //   postQianmingService();//上传签名
                        }


                    }else {
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

    private void postImageService(final CheckRecord checkRecord) {
        final String pic1 = checkRecord.getPic1();
        final String pic2 = checkRecord.getPic2();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(pic1),null,pic1);
        params.addHeader("Authorization","bearer " + token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        String fullImagePath1 = data.getJSONObject(0).getString("fullPath");
                        if (!pic2.isEmpty()){
                            postPic2Service(checkRecord,fullImagePath1);
                        }else {
                            postQianmingService(checkRecord, fullImagePath1, "");//上传签名
                        }

                    }else {

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

    private void postPic2Service(final CheckRecord checkRecord, final String fullImagePath1) {
        String pic2 = checkRecord.getPic2();
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(pic2),null,pic2);
        Log.e(TAG, "postDataService: ---" +"bearer " + token  );
        Log.e(TAG, "反馈---" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        String fullImagePath2 = data.getJSONObject(0).getString("fullPath");
                        postQianmingService(checkRecord,fullImagePath1,fullImagePath2);//上传签名


                    }else {

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

    /**
     * 上传第一个人签名
     * @param checkRecord
     * @param fullImagePath1
     * @param fullImagePath2
     */
    private void postQianmingService(final CheckRecord checkRecord, final String fullImagePath1, final String fullImagePath2) {
        String qianMingPic = checkRecord.getQianMingPic();

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(qianMingPic),null,qianMingPic);
        params.addHeader("Authorization","bearer " + token);
        Log.e(TAG, "反馈---" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "imagesUrlList-onSuccess第一签名: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        String fullPath = data.getJSONObject(0).getString("fullPath");
                        Log.e(TAG, "onSuccess:imagesUrlList ----"  +checkRecord.getQianMingOnePic());
                        //网络获取数据
                        // postDataToService();
                        //本地数据
                        if (checkRecord.getQianMingOnePic() != null) {
                            Log.e(TAG, "imagesUrlList-上传第二签名: " + result);
                            postQianmingOneService(checkRecord,fullImagePath1,fullImagePath2,fullPath);
                        }else {
                            Log.e(TAG, "imagesUrlList: 直接上传数据" );
                            postDataToServiceFromDb(checkRecord,fullImagePath1,fullImagePath2,fullPath,"");
                        }

                    }else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " );
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });

    }

    /**
     * 上传第二个人签名
     * @param checkRecord
     * @param fullImagePath1
     * @param fullImagePath2
     */
    private void postQianmingOneService(final CheckRecord checkRecord, final String fullImagePath1, final String fullImagePath2,final String fullPath) {
        String qianMingOnePic = checkRecord.getQianMingOnePic();

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/file/upload");
        params.setAsJsonContent(true);
        params.setMultipart(true);    //以表单得形式上传  文件上传必须要
        // params.setBodyContent(jsonObject.toString());
        params.addBodyParameter("file", new File(qianMingOnePic),null,qianMingOnePic);
        params.addHeader("Authorization","bearer " + token);
        Log.e(TAG, "反馈---" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "imagesUrlList-第二签名上传成功: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        String fullOnePath = data.getJSONObject(0).getString("fullPath");
                        Log.e(TAG, "onSuccess: ----"  );
                        //网络获取数据
                        // postDataToService();
                        //本地数据
                        postDataToServiceFromDb(checkRecord,fullImagePath1,fullImagePath2,fullPath,fullOnePath);
                    }else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " );
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });

    }

    private void postDataToServiceFromDb(final CheckRecord checkRecord, String fullImagePath1, String fullImagePath2, String fullPath,String fullPathOne) {

        Log.e(TAG, "postDataToServiceFromDb: imagesUrlList 开始上传数据");
        JSONObject jsonObject = new JSONObject();
        List<ImagesUrl>  imagesUrlList = new ArrayList<>();

        try {

            jsonObject.put("resourceId",checkRecord.getResourceId());
            jsonObject.put("name",checkRecord.getName());

            Gson gson = new Gson();

             List<CheckField> list = gson.fromJson(checkRecord.getCheckStr(), new TypeToken<List<CheckField>>(){}.getType());
            for (int i = 0; i < list.size(); i++) {
                jsonObject.put(list.get(i).getCode(), list.get(i).state+"");
            }
            Log.e(TAG, "imagesUrlList-imagesize: " + imagesUrlList.size() );
            Log.e(TAG, "imagesUrlList-id: " + checkRecord.getId() );
            imagesUrlList = db.selector(ImagesUrl.class)
                    .where("resourceid", "=", checkRecord.getId())
                    .findAll();



            if (imagesUrlList != null){
                for (int i = 0; i < imagesUrlList.size(); i++) {
                    int num = i + 1;
                    Log.e(TAG, "imagesUrlList: 已选择图片=" + num );

                    jsonObject.put("pic" + num, imagesUrlList.get(i).getFullImagePath());
                }

            }


            // jsonObject.put("type",type);
            jsonObject.put("signaturePic",fullPath);
            jsonObject.put("signaturePic1",fullPathOne);
           /* jsonObject.put("pic1",fullImagePath1);
            if (!fullImagePath2.isEmpty()){
                jsonObject.put("pic2",fullImagePath2);
            }*/
            jsonObject.put("checkTime",checkRecord.getCheckTime());
            jsonObject.put("description",checkRecord.getDescription());
            jsonObject.put("gridId",checkRecord.getGridId());
            jsonObject.put("gridName",checkRecord.getGridName());
            jsonObject.put("gridNo",checkRecord.getGridNo());


        } catch (Exception e) {
        }


        Log.e(TAG, "postDataToService: ---" + jsonObject.toString());
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource" + checkRecord.getApiUrl() );
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + token);
        params.setConnectTimeout(10000);

        final List<ImagesUrl> finalImagesUrlList = imagesUrlList;
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "imagesUrlList-onSuccess: --数据上传成功--" + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    String message = jsonObject1.getString("message");
                    if (code.equals("200")){
                        Log.e(TAG, "resource--上传成功" );

                        try {
                            Log.e(TAG, "imagesUrlList-onSuccess: 本地数据删除" +  finalImagesUrlList.size());
                            db.delete(checkRecord);
                            db.delete(finalImagesUrlList);
                            for (int i = 0; i < finalImagesUrlList.size(); i++) {
                               // deleteImage(finalImagesUrlList.get(i).getPic(),context);
                            }

                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: 请求失败1" +ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    /*
   * 根据String Path 删除图片
   * */
    public static void deleteImage(String imgPath, Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=?",
                new String[]{imgPath}, null);
        boolean result = false;
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uri = ContentUris.withAppendedId(contentUri, id);
            int count = context.getContentResolver().delete(uri, null, null);
            result = count == 1;
        } else {
            File file = new File(imgPath);
            result = file.delete();
        }

        if (result) {
         /*   imageList.remove(imgPath);
            adapter.notifyDataSetChanged();
            Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show();*/
            Log.e("UtilsRY ", "imagesUrlList-DeleteImage: 图片删除成功");
        } else {
            Log.e("UtilsRY ", "imagesUrlList-DeleteImage: 图片删除失败");
        }
    }

}
