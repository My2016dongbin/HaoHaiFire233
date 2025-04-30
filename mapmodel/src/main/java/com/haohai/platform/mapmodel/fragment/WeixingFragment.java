package com.haohai.platform.mapmodel.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.haohai.platform.mapmodel.R;
import com.haohai.platform.mapmodel.Utils.JsApi;
import com.haohai.platform.mapmodel.fragment.base.HhBaseFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;

/**
 * Created by geyang on 2020/11/9.
 */

public class WeixingFragment extends HhBaseFragment implements JsApi.OnJsClickListener{
    private static final String TAG = WeixingFragment.class.getSimpleName();
    private DWebView dWebView;
    private Button jiaButton;
    private Button jianButton;
    private String token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weixing_map, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setUserVisibleHint(true);
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        token = bundle.getString("TOKEN");
        Log.e(TAG, "onActivityCreated: " + token );

        initView();


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        dWebView = ((DWebView) getView().findViewById(R.id.weixing_dwebview));

        dWebView.getSettings().setJavaScriptEnabled(true);
          String url = "file:///android_asset/map/mars_demo.html";
        //url：网页地址；name/pwd:cookie信息
        // String isUser = SPUtils.getInstance().getString("userContent");
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
        dWebView.addJavascriptObject(jsApi,null);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            dWebView.setWebContentsDebuggingEnabled(true);
        }
        //添加跨域支持
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            dWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            dWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        }else{
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

        // String mapUrl = "http://www.google.cn/maps/vt?lyrs=y@189&gl=cn&x={x}&y={y}&z={z}http://www.google.cn/maps/vt?lyrs=y@189&gl=cn&x={x}&y={y}&z={z}"; // 瓦片路径
        String mapUrl = "assets/qds_2005261619/{z}/{x}/{y}.png"; // 瓦片路径
        dWebView.callHandler("initMap", new Object[]{mapUrl}, new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                Log.d("jsbridge", "call succeed,return value is " + retValue);
            }
        });
    }

    @Override
    public void onJsEscapeDetailsClickListener(String json) {

    }


}
