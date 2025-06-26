package com.skyline.terraexplorer.mainapps.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.google.gson.Gson;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.mainapps.utils.JsApi;
import com.skyline.terraexplorer.mainapps.utils.LatLngChange;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import rx.functions.Action1;
import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;


public class JianguanFragment extends Fragment implements JsApi.OnJsClickListener {
    private DWebView dWebView;
    private TextView daohangView;
    private TextView dingweiView;
    private static final String TAG = "JianguanFragment";
    private myReceiver Receiver;
    private TextView cejuView;
    private TextView cemianView;
    private TextView qingchuView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jianguan, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("JavascriptInterface")
    private void initView() {
        daohangView = getView().findViewById(R.id.daohang_view);
        dingweiView = getView().findViewById(R.id.dingwei_view);
        cejuView = getView().findViewById(R.id.ceju_view);
        cemianView = getView().findViewById(R.id.cemian_view);
        qingchuView=getView().findViewById(R.id.qingchu_view);
        Receiver = new myReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("GPSinfo");
        getActivity().registerReceiver(Receiver, filter);
        dWebView = ((DWebView) getView().findViewById(R.id.dwebview));
        dWebView.getSettings().setJavaScriptEnabled(true);

        String url = "file:///android_asset/map/mars_demo.html";
        dWebView.loadUrl(url);
        dWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //加载逃生出口资源点
                // getDataFromService();
                //加载道路
                // getRoadDataFromService();
            }
        });
        dWebView.addJavascriptInterface(this, "jk");
        JsApi jsApi = new JsApi(getContext());
        jsApi.setListener(this);
        dWebView.addJavascriptObject(jsApi, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dWebView.setWebContentsDebuggingEnabled(true);
        }
        //添加跨域支持
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            dWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            dWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        } else {
            try {
                Class<?> clazz = dWebView.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(dWebView.getSettings(), true);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String mapUrl = "assets/qds_2005261619/{z}/{x}/{y}.png"; // 瓦片路径
        dWebView.callHandler("initMap", new Object[]{mapUrl}, new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Log.d("jsbridge", "call succeed,return value is " + retValue);
            }
        });

        RxViewAction.clickNoDouble(daohangView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        AmapNaviParams params = new AmapNaviParams(null, null, null, AmapNaviType.DRIVER, AmapPageType.ROUTE);
                        params.setUseInnerVoice(true);
                        AmapNaviPage.getInstance().showRouteActivity(getContext(), params, null);
                    }
                });
        RxViewAction.clickNoDouble(dingweiView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String jingweiStr = getLocation();
                        String starweidu = "";
                        String starjingdu = "";
                        Log.e(TAG, "jingweiStr: "+jingweiStr );
                        if (!jingweiStr.isEmpty()) {
                            List<String> jingweiList = Arrays.asList(jingweiStr.split(","));
                            starweidu = jingweiList.get(1);
                            starjingdu = jingweiList.get(0);
                        }
                        JSONObject jsonObject = new JSONObject();
                        JSONObject posObj = new JSONObject();
                        try {
                            posObj.put("lat", Double.parseDouble(starweidu));
                            posObj.put("lng", Double.parseDouble(starjingdu));
                            jsonObject.put("position", posObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dWebView.callHandler("GPSflyto", new Object[]{new Gson().toJson(jsonObject.toString())}, new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.d("jsbridge", "call succeed,return value is " + retValue);
                            }
                        });
                    }
                });
        RxViewAction.clickNoDouble(cejuView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dWebView.callHandler("ceju", new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.d("jsbridge", "call succeed,return value is " + retValue);
                            }
                        });
                    }
                });
        RxViewAction.clickNoDouble(cemianView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dWebView.callHandler("ceju", new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.d("jsbridge", "call succeed,return value is " + retValue);
                            }
                        });
                    }
                });
        RxViewAction.clickNoDouble(qingchuView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dWebView.callHandler("cedelete", new OnReturnValue<String>() {
                            @Override
                            public void onValue(String retValue) {
                                Log.d("jsbridge", "call succeed,return value is " + retValue);
                            }
                        });
                    }
                });
    }

    @Override
    public void onJsEscapeDetailsClickListener(String json) {

    }
    /**
     * 获取当前位置经纬度
     *
     * @return
     */
    @JavascriptInterface
    public String getLocation() {
        //获得位置服务
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(getContext(), "请打开GPS和使用网络定位以提高精度", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        String provider = judgeProvider(locationManager);
        //有位置提供器的情况
        List<String> providerList = locationManager.getProviders(true);
        // 测试一般都在室内，这里颠倒了书上的判断顺序
        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            // 当没有可用的位置提供器时，弹出Toast提示用户
            Toast.makeText(getContext(), "Please Open Your GPS or Location Service", Toast.LENGTH_SHORT).show();

        }
        if (provider != null) {
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            try {
                return location.getLongitude() + "," + location.getLatitude();
            } catch (Exception e) {
                return "0.00,0.00";
            }

        }
        return null;
    }
    /**
     * 定位器provider
     *
     * @param locationManager
     * @return
     */
    private String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if (prodiverlist.contains(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;//网络定位
        } else if (prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;//GPS定位
        } else {
            Toast.makeText(getContext(), "未开启本应用地理位置信息，请先开启！", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    class myReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {

            String msg = intent.getStringExtra("message");
            //Toast.makeText(context, "广播已经接收", Toast.LENGTH_SHORT).show();
            //Log.e("onReceive: ", msg);
            dWebView.callHandler("GPSpoint", new Object[]{new Gson().toJson(msg)}, new OnReturnValue<String>() {
                @Override
                public void onValue(String retValue) {
                    Log.d("jsbridge", "call succeed,return value is " + retValue);
                }
            });
        }
    }
}
