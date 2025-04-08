package com.haohai.platform.fireforestplatform.ui.listener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.ruyiruyi.rylibrary.utils.CommonData;

import org.json.JSONException;
import org.json.JSONObject;

public class MyLocationListener extends BDAbstractLocationListener {
    private static final String TAG = MyLocationListener.class.getSimpleName();

    private Context context;

    public MyLocationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onReceiveLocation(BDLocation location){
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取经纬度相关（常用）的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

        double latitude = location.getLatitude();    //获取纬度信息
        double longitude = location.getLongitude();    //获取经度信息
        float radius = location.getRadius();    //获取定位精度，默认值为0.0f

        String coorType = location.getCoorType();
        //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

        int errorCode = location.getLocType();
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

        Log.e(TAG, "onReceiveLocation:经纬度是 " +  latitude +"," +longitude);

        CommonData.lat = latitude;
        CommonData.lng = longitude;


        final JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("userId",user.getId());

            JSONObject posObj = new JSONObject();
            posObj.put("lat",latitude);
            posObj.put("lng",longitude);

            jsonObject.put("position",posObj);
        } catch (JSONException e) {
        }

        //broadcast
        // service 通过广播来更新GUI
        Intent intent=new Intent();
        intent.putExtra("message",jsonObject.toString());
        intent.setAction("zcd.voicerobot");
        context.sendBroadcast(intent);
    }
}
