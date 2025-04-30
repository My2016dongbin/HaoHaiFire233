package com.skyline.test.plan;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyline.test.net.DownloadService;
import com.skyline.test.net.HttpInfo;
import com.skyline.test.net.HttpUtils;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.models.UI;
import com.skyline.test.others.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by miao on 2017/5/13 15:54.
 */

public class OnlineFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "OnlineFragment";
    private ListView lv;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mNoData;
    private LinearLayout mNoInternet;
    private ProgressBar mPb;
    private DownloadService.DownloadBinder downloadBinder;
    private static OnDownloadListener monDownloadListener;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online, null);
        lv = (ListView) view.findViewById(R.id.online_lv);
        mNoData = (LinearLayout) view.findViewById(R.id.online_ll_nodata);
        mNoInternet = (LinearLayout) view.findViewById(R.id.online_ll_noInternet);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.online_srl);
        mPb = (ProgressBar) view.findViewById(R.id.online_pb);
        initView();
        initmSRL();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initData();
        super.onActivityCreated(savedInstanceState);
    }

    public void initView() {
        boolean isNetConnected = HttpUtils.isNetworkConnected(getContext());
        if (isNetConnected) {
            setViewVisible(lv);
            mPb.setVisibility(View.VISIBLE);
        } else {
            setViewVisible(mNoInternet);
            mPb.setVisibility(View.INVISIBLE);
        }
    }

    private void initmSRL() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setDistanceToTriggerSync(150);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
    }

    public void initData() {
        //绑定下载服务
        Intent intent = new Intent(getContext(), DownloadService.class);
        getContext().startService(intent); // 启动服务
        getContext().bindService(intent, connection, BIND_AUTO_CREATE); // 绑定服务
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        //请求可下载的word数据
        HttpUtils.sendOkHttpRequest(HttpInfo.DOWNLOAD_LIST_URL, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                UI.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(getActivity() != null) {
                            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                        mPb.setVisibility(View.INVISIBLE);
                        setViewVisible(mNoData);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
//                String dataUTF_8 = HttpUtils.toUTF_8(responseData);
                LogUtils.d(TAG, "responseData = " + responseData);

                Gson gson = new Gson();
                List<OnlineInfo> mOnLineInfos = gson.fromJson(responseData, new TypeToken<List<OnlineInfo>>() {
                }.getType());
                final ArrayList<OnlineInfo1> mOnlineInfo1s = new ArrayList<OnlineInfo1>();

                //判断local有没有数据
                if (mOnLineInfos != null) {
                    if (HttpInfo.localWord.size() == 0) {
                        for (OnlineInfo onlineInfo : mOnLineInfos) {
                            mOnlineInfo1s.add(new OnlineInfo1(onlineInfo.getName(), onlineInfo.getDownloadUrl(), false));
                        }
                    } else {
                        for (OnlineInfo onlineInfo : mOnLineInfos) {
                            if(isExist(onlineInfo.getName())) {
                                mOnlineInfo1s.add(new OnlineInfo1(onlineInfo.getName(), onlineInfo.getDownloadUrl(), true));
                            } else {
                                mOnlineInfo1s.add(new OnlineInfo1(onlineInfo.getName(), onlineInfo.getDownloadUrl(), false));
                            }
                        }
                    }
                }

                UI.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mOnlineInfo1s != null) {
                            OnlineAdapter onlineAdapter = new OnlineAdapter(mOnlineInfo1s, getContext(), R.layout.fragment_online_item);
                            lv.setAdapter(onlineAdapter);

                            //下载事件
                            onlineAdapter.setOnDownloadListener(new OnlineAdapter.OnDownloadListener() {
                                @Override
                                public void downLoad(String downloadUrl, View download, View progressbar, View downfinish) {
                                    Toast.makeText(getContext(), "你点击了下载按钮", Toast.LENGTH_SHORT).show();
                                    download.setVisibility(View.INVISIBLE);
                                    progressbar.setVisibility(View.VISIBLE);
                                    String url = downloadUrl;
                                    LogUtils.d(TAG,"下载地址"+downloadUrl);
//                                    Log.d(TAG, "downLoad: downloadUrl = " + downloadUrl);
//                                    String test = "http://192.168.56.1:8080/haohaigis/word/a.apk";
                                    downloadBinder.startDownload(url);
                                    HttpInfo.download = download;
                                    HttpInfo.pb = progressbar;
                                    HttpInfo.finish = downfinish;
                                }
                            });
                            mPb.setVisibility(View.INVISIBLE);
                        } else {
                            setViewVisible(mNoData);
                            mPb.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            default:
        }
    }

    @Override
    public void onRefresh() {
        initView();
        initData();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setViewVisible(View view) {
        lv.setVisibility(View.INVISIBLE);
        mNoInternet.setVisibility(View.INVISIBLE);
        mNoData.setVisibility(View.INVISIBLE);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unbindService(connection);
    }

    private boolean isExist(String name) {
        for (WordInfo wordInfo : HttpInfo.localWord) {
            if (name.equals(wordInfo.getName())) {
                return true;
            }
        }
        return false;
    }

    public static class MyBrocaseRecevicer extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            monDownloadListener.download();
        }
    }

    public void setonDownloadListener(OnDownloadListener monDownloadListener) {
        this.monDownloadListener = monDownloadListener;
    }

    public interface OnDownloadListener {
        void download();
    }

}
