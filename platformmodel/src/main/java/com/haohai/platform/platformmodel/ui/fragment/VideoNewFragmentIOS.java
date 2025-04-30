package com.haohai.platform.platformmodel.ui.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.haohai.ledge.videolibrary.listener.GSYSampleCallBack;
import com.haohai.ledge.videolibrary.utils.OrientationUtils;
import com.haohai.ledge.videolibrary.video.MultiSampleVideo;
import com.haohai.platform.platformmodel.R;
import com.haohai.platform.platformmodel.ui.fragment.base.HhBaseFragment;
import com.haohai.platform.platformmodel.ui.model.GridCamera;
import com.haohai.platform.platformmodel.ui.model.GridPointModel;
import com.haohai.platform.platformmodel.ui.model.GridTrees;
import com.haohai.platform.platformmodel.ui.model.VideoId;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.route.RouteUtils;
import com.ruyiruyi.rylibrary.utils.CommonData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

import static android.view.MotionEvent.AXIS_X;
import static android.view.MotionEvent.AXIS_Y;
import static com.haohai.ledge.videolibrary.video.base.GSYVideoView.CURRENT_STATE_PLAYING;

public class VideoNewFragmentIOS extends HhBaseFragment {
    private static final String TAG = VideoNewFragmentIOS.class.getSimpleName();
    public int currentVideo = 1;
    private ImageView yiView;
    private ImageView siView;
    private ImageView jiuView;
    private LinearLayout yiLayout;
    private LinearLayout siLayout;
    private LinearLayout jiuLayout;
    private FrameLayout video1_layout;
    private FrameLayout video2_layout;
    private FrameLayout video3_layout;
    private FrameLayout video4_layout;
    private FrameLayout video5_layout;
    private FrameLayout video6_layout;
    private FrameLayout video7_layout;
    private FrameLayout video8_layout;
    private FrameLayout video9_layout;
    private MultiSampleVideo video1Player;
    private MultiSampleVideo video2Player;
    private MultiSampleVideo video3Player;
    private MultiSampleVideo video4Player;
    private MultiSampleVideo video5Player;
    private MultiSampleVideo video6Player;
    private MultiSampleVideo video7Player;
    private MultiSampleVideo video8Player;
    private MultiSampleVideo video9Player;
    private Dialog videoListDialog;
    private View videoListInflater;

    public int num = 0;
    private ImageView listButton;
    private FrameLayout video_click1_layout;
    private FrameLayout video_click2_layout;
    private FrameLayout video_click3_layout;
    private FrameLayout video_click4_layout;
    private FrameLayout video_click5_layout;
    private FrameLayout video_click6_layout;
    private FrameLayout video_click7_layout;
    private FrameLayout video_click8_layout;
    private FrameLayout video_click9_layout;
    private int currentChooseVideo = 0;
    private int currentVideo1PlayerState = 0;
    private int currentVideo2PlayerState = 0;
    private int currentVideo3PlayerState = 0;
    private int currentVideo4PlayerState = 0;
    private int currentVideo5PlayerState = 0;
    private int currentVideo6PlayerState = 0;
    private int currentVideo7PlayerState = 0;
    private int currentVideo8PlayerState = 0;
    private int currentVideo9PlayerState = 0;
    private Button yichuButton;
    private ImageView video1AddView;
    private ImageView video2AddView;
    private ImageView video3AddView;
    private ImageView video4AddView;
    private ImageView video5AddView;
    private ImageView video6AddView;
    private ImageView video7AddView;
    private ImageView video8AddView;
    private ImageView video9AddView;
    private boolean isAddVideoViewClick = false;
    private LinearLayout videoListLayout;
    private ImageView listDialogImage;
    private OrientationUtils orientationUtils;
    private ImageView addImageView;



    private ScrollView sv_gridtrees;
    private String currentClickItemName;
    private String currentClickItemId;


    private Dialog addDialog;
    private View addInflate;
    private TextView huoqingView;
    private TextView yinhuanView;
    private Button zuoshangButton;
    private Button shangButton;
    private Button youshangButton;
    private Button zuoButton;
    private Button xunhangButton;
    private Button youButton;
    private Button zuoxiaButton;
    private Button xiaButton;
    private Button youxiaButton;
    private Button lajinButton;
    private Button jiepingButton;
    private Button jujiaoButton;
    private Button layuanButton;
    private Button luxiangButton;
    private User user;
    private List<VideoId> videoIdList;

    private int moveType = 0;//1：上，2：下，3：左，4：右，5：左上，6：左下，7：右上，8：右下
    private int steptype = 0;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private boolean isStop = false; //true false

    private ChangeTabReceiver changeTabReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_new, container, false);
    }

    class ChangeTabReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {

            String videoId = intent.getStringExtra("id");
            String deviceId = intent.getStringExtra("deviceId");
            Log.i("videoonReceive: ", videoId);
            postPlayerUrl(videoId,deviceId,deviceId);//deviceId??mId  ??? channelId
            currentClickItemName="监控点视频";
            currentClickItemId = deviceId;//新版 需替换为videoId
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenHeight = display.getHeight();
        screenWidth = display.getWidth();

        changeTabReceiver=new ChangeTabReceiver();
        IntentFilter resourcefilter = new IntentFilter();
        resourcefilter.addAction("video_play");
        getActivity().registerReceiver(changeTabReceiver, resourcefilter);

        videoIdList = new ArrayList<>();
        user = new DbConfig(getContext()).getUser();

        intiView();

        getTokenFromService();
        getDataFromService();
        bingView();
    }

    /**
     * 获取视频树
     */
    List<GridTrees> gridTreesList = new ArrayList<>();
    private void getDataFromService() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL +"resource/api/grid/listGridNewTrees");
        params.addHeader("Authorization","bearer " + new DbConfig(getContext()).getUser().getToken());

        params.setConnectTimeout(10000);
        Log.e(TAG, "listGridNewTrees: ---" + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: bingo gridNew" + result );
                sv_gridtrees.removeAllViews();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("200")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            gridTreesList.add(new Gson().fromJson(data.get(i).toString(),GridTrees.class));
                        }

                        sv_gridtrees.addView(buildGridTrees(gridTreesList));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: bingo gridNew error" + ex.toString() );
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
     * 构建树框架
     */
    View buildGridTrees(List<GridTrees> treesList){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_gridtrees_out,null);
        LinearLayout ll_big = view.findViewById(R.id.ll_big);
        for (int i = 0; i < treesList.size(); i++) {
            final GridTrees gridTrees = treesList.get(i);
            View item = LayoutInflater.from(getActivity()).inflate(R.layout.item_gridtrees,null);
            LinearLayout ll_out = item.findViewById(R.id.ll_out);
            final LinearLayout ll_in = item.findViewById(R.id.ll_in);//用于监控点-摄像头便于动态加载
            final ImageView iv_status = item.findViewById(R.id.iv_status);
            TextView tv_gridtrees = item.findViewById(R.id.tv_gridtrees);
            tv_gridtrees.setText(gridTrees.getName());
            //有Children子项(递归展示)
            if(gridTrees.getChildren()!=null && gridTrees.getChildren().size()>0){
                List<GridTrees> itemList = new ArrayList<>();
                for (int m = 0; m < gridTrees.getChildren().size(); m++) {
                    itemList.add(gridTrees.getChildren().get(m));
                }
                //递归
                final View childTrees = buildGridTrees(itemList);
                //初始化绑定
                if(gridTrees.isStatus()){
                    iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_open));
                    childTrees.setVisibility(View.VISIBLE);
                }else{
                    iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
                    childTrees.setVisibility(View.GONE);
                }
                RxViewAction.clickNoDouble(ll_out).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(gridTrees.isStatus()){
                            gridTrees.setStatus(false);
                            iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
                            childTrees.setVisibility(View.GONE);
                        }else{
                            gridTrees.setStatus(true);
                            iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_open));
                            childTrees.setVisibility(View.VISIBLE);
                        }
                    }
                });

                ll_out.addView(childTrees);
            }else{
                //无Children子项(点击加载监控点-摄像头)

                if(gridTrees.isStatus()){
                    iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_open));
                    //展示新View(暂无意义)
                    ll_in.setVisibility(View.VISIBLE);
                }else{
                    iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
                    //隐藏新View(暂无意义)
                    ll_in.setVisibility(View.GONE);
                }
                RxViewAction.clickNoDouble(ll_out).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(gridTrees.isStatus()){
                            gridTrees.setStatus(false);
                            iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
                            //隐藏/移除新View
                            ll_in.setVisibility(View.GONE);
                            ll_in.removeAllViews();
                        }else{
                            gridTrees.setStatus(true);
                            iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_open));
                            //展示/添加新View

                            //加载监控点-摄像头数据
                            getGridTreesChildren(gridTrees.getId());

                            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    try {
                                        List<GridPointModel> pointModelList = new ArrayList<>();
                                        String result = intent.getStringExtra("result");
                                        JSONObject jsonObject = new JSONObject(result);
                                        JSONArray data = jsonObject.getJSONArray("data");
                                        Gson gson = new Gson();
                                        for (int x = 0; x < data.length(); x++) {
                                            pointModelList.add(gson.fromJson(data.get(x).toString(),GridPointModel.class));
                                        }

                                        //monitor子View
                                        View monitorView = buildMonitor(pointModelList);
                                        ll_in.addView(monitorView);
                                        ll_in.setVisibility(View.VISIBLE);
                                        getActivity().unregisterReceiver(this);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            IntentFilter intentFilter = new IntentFilter();
                            intentFilter.addAction(gridTrees.getId());
                            getActivity().registerReceiver(broadcastReceiver, intentFilter);
                        }
                    }
                });
            }


            ll_big.addView(item);
        }
        return ll_big;
    }

    /**
     * 构建监控点子View
     */
    View buildMonitor(List<GridPointModel> gridPointModelList){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_gridtrees_out,null);
        LinearLayout ll_big = view.findViewById(R.id.ll_big);//取一个LinearLayout
        for (int i = 0; i < gridPointModelList.size(); i++) {
            final GridPointModel model = gridPointModelList.get(i);
            View item = LayoutInflater.from(getActivity()).inflate(R.layout.item_gridtrees,null);
            LinearLayout ll_out = item.findViewById(R.id.ll_out);
            final LinearLayout ll_in = item.findViewById(R.id.ll_in);
            final ImageView iv_status = item.findViewById(R.id.iv_status);
            TextView tv_gridtrees = item.findViewById(R.id.tv_gridtrees);
            tv_gridtrees.setText(model.getMonitor().getName());
            if(model.isStatus()){
                iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_open));
            }else{
                iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
            }
            RxViewAction.clickNoDouble(ll_out).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    if(model.isStatus()){
                        model.setStatus(false);
                        iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
                        //隐藏/移除新View
                        ll_in.setVisibility(View.GONE);
                        ll_in.removeAllViews();
                    }else{
                        model.setStatus(true);
                        iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_open));
                        //显示/添加新View
                        for (int m = 0; m < model.getCameraList().size(); m++) {
                            final GridCamera gridCamera = model.getCameraList().get(m);
                            //构建摄像头
                            View cameraView = LayoutInflater.from(getActivity()).inflate(R.layout.item_gridcamera,null);
                            LinearLayout ll_camera = cameraView.findViewById(R.id.ll_camera);
                            ImageView iv_camera = cameraView.findViewById(R.id.iv_camera);//isOnLine 0 离线 1 在线
                            TextView tv_camera = cameraView.findViewById(R.id.tv_camera);
                            tv_camera.setText(gridCamera.getName());
                            RxViewAction.clickNoDouble(ll_camera).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void unused) {
                                    postPlayerUrl(gridCamera.getId(),model.getMonitor().getId(),gridCamera.getId());

                                    currentClickItemName = gridCamera.getName();
                                    currentClickItemId = gridCamera.getDeviceId();//新版 需替换为id
                                }
                            });

                            ll_in.addView(cameraView);
                        }

                        ll_in.setVisibility(View.VISIBLE);
                    }
                }
            });
            ll_big.addView(item);
        }

        return ll_big;
    }

    private void postPlayerUrl(final String ids,final String mId,final String channelId) {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/mediaKit/getStreamAndroidByOut");
        params.addHeader("Authorization","bearer " + new DbConfig(getContext()).getUser().getToken());
        params.addBodyParameter("cameraId",ids);
        Log.e(TAG, "postPlayerUrl: bingo ids = " + ids );
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e(TAG, "onSuccess: bingo postPlayerUrl" + result );
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    if(data.length()!=0){
                        JSONObject obj = (JSONObject) data.get(0);
                        String url = obj.getString("url");

                        //跳转播放视频流
                        videoListDialog.dismiss();
                        if (isAddVideoViewClick) {//直接往点击的视频播放机中添加
                            addVideoPlayer(url,mId,channelId);
                        } else {//往列表中排序添加
                            getVideoPlayerState();
                            setVideoPlayer(url,mId,channelId);
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

    /**
     * 往点击的视频播放器中添加视频
     *
     * @param playerUrl url
     */
    private void addVideoPlayer(String playerUrl,String monitorId,String channelId) {
        if (currentChooseVideo == 1) {
            video1AddView.setVisibility(View.GONE);
            video1Player.setVisibility(View.VISIBLE);
            video1Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
            video1Player.startButtonLogic();
            videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,1));
        } else if (currentChooseVideo == 2) {
            video2AddView.setVisibility(View.GONE);
            video2Player.setVisibility(View.VISIBLE);
            video2Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
            video2Player.startButtonLogic();
            videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,2));
        } else if (currentChooseVideo == 3) {
            video3AddView.setVisibility(View.GONE);
            video3Player.setVisibility(View.VISIBLE);
            video3Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
            video3Player.startButtonLogic();
            videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,3));
        } else if (currentChooseVideo == 4) {
            video4AddView.setVisibility(View.GONE);
            video4Player.setVisibility(View.VISIBLE);
            video4Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
            video4Player.startButtonLogic();
            videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,4));
        } else if (currentChooseVideo == 5) {
            video5AddView.setVisibility(View.GONE);
            video5Player.setVisibility(View.VISIBLE);
            video5Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
            video5Player.startButtonLogic();
            videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,5));
        } else if (currentChooseVideo == 6) {
            video6AddView.setVisibility(View.GONE);
            video6Player.setVisibility(View.VISIBLE);
            video6Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
            video6Player.startButtonLogic();
            videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,6));
        } else if (currentChooseVideo == 7) {
            video7AddView.setVisibility(View.GONE);
            video7Player.setVisibility(View.VISIBLE);
            video7Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
            video7Player.startButtonLogic();
            videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,7));
        } else if (currentChooseVideo == 8) {
            video8AddView.setVisibility(View.GONE);
            video8Player.setVisibility(View.VISIBLE);
            video8Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
            video8Player.startButtonLogic();
            videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,8));
        } else if (currentChooseVideo == 9) {
            video9AddView.setVisibility(View.GONE);
            video9Player.setVisibility(View.VISIBLE);
            video9Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
            video9Player.startButtonLogic();
            videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,9));
        }
        initTurnId(currentChooseVideo);
    }

    /**
     * 设置视频播放
     *
     * @param playerUrl url
     */
    private void setVideoPlayer(String playerUrl,String monitorId,String channelId) {
        Log.e(TAG, "setVideoPlayer: " + currentClickItemName);
        if (currentVideo == 1) {
            if (currentVideo1PlayerState == 0) {
                video1Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video1Player.startButtonLogic();
                video1Player.setVisibility(View.VISIBLE);
                video1AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,1));
                initTurnId(1);
            } else {
                Toast.makeText(getContext(), "目前没有闲置播放器", Toast.LENGTH_SHORT).show();
            }
        } else if (currentVideo == 4) {
            if (currentVideo1PlayerState == 0) {
                video1Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video1Player.startButtonLogic();
                video1Player.setVisibility(View.VISIBLE);
                video1AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,1));
                initTurnId(1);
            } else if (currentVideo2PlayerState == 0) {
                video2Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video2Player.startButtonLogic();
                video2Player.setVisibility(View.VISIBLE);
                video2AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,2));
                initTurnId(2);
            } else if (currentVideo4PlayerState == 0) {
                video4Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video4Player.startButtonLogic();
                video4Player.setVisibility(View.VISIBLE);
                video4AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,4));
                initTurnId(4);
            } else if (currentVideo5PlayerState == 0) {
                video5Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video5Player.startButtonLogic();
                video5Player.setVisibility(View.VISIBLE);
                video5AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,5));
                initTurnId(5);
            } else {
                Toast.makeText(getContext(), "目前没有闲置播放器", Toast.LENGTH_SHORT).show();
            }
        } else if (currentVideo == 9) {
            if (currentVideo1PlayerState == 0) {
                video1Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video1Player.startButtonLogic();
                video1Player.setVisibility(View.VISIBLE);
                video1AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,1));
                initTurnId(1);
            } else if (currentVideo2PlayerState == 0) {
                video2Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video2Player.startButtonLogic();
                video2Player.setVisibility(View.VISIBLE);
                video2AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,2));
                initTurnId(2);
            } else if (currentVideo3PlayerState == 0) {
                video3Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video3Player.startButtonLogic();
                video3Player.setVisibility(View.VISIBLE);
                video3AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,3));
                initTurnId(3);
            } else if (currentVideo4PlayerState == 0) {
                video4Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video4Player.startButtonLogic();
                video4Player.setVisibility(View.VISIBLE);
                video4AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,4));
                initTurnId(4);
            } else if (currentVideo5PlayerState == 0) {
                video5Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video5Player.startButtonLogic();
                video5Player.setVisibility(View.VISIBLE);
                video5AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,5));
                initTurnId(5);
            } else if (currentVideo6PlayerState == 0) {
                video6Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video6Player.startButtonLogic();
                video6Player.setVisibility(View.VISIBLE);
                video6AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,6));
                initTurnId(6);
            } else if (currentVideo7PlayerState == 0) {
                video7Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video7Player.startButtonLogic();
                video7Player.setVisibility(View.VISIBLE);
                video7AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,7));
                initTurnId(7);
            } else if (currentVideo8PlayerState == 0) {
                video8Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video8Player.startButtonLogic();
                video8Player.setVisibility(View.VISIBLE);
                video8AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,8));
                initTurnId(8);
            } else if (currentVideo9PlayerState == 0) {
                video9Player.setUpLazy(playerUrl, false, null, null, currentClickItemName);
                video9Player.startButtonLogic();
                video9Player.setVisibility(View.VISIBLE);
                video9AddView.setVisibility(View.GONE);
                videoIdList.add(new VideoId(monitorId,channelId,currentClickItemId,currentClickItemName,9));
                initTurnId(9);
            } else {
                Toast.makeText(getContext(), "目前没有闲置播放器", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //获取监控点-摄像头数据
    void getGridTreesChildren(final String gridId){
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL +"resource/api/monitor/getMonitorDetaisByGridAndType");
        params.addHeader("Authorization","bearer " + new DbConfig(getContext()).getUser().getToken());
        params.addBodyParameter("gridId",gridId);
        //params.addBodyParameter("monitorType","1");

        params.setConnectTimeout(10000);
        Log.e(TAG, "listGridNewTrees: ---" + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: bingo gridNew" + result );
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("200")) {

                        Intent it = new Intent();
                        it.setAction(gridId);
                        it.putExtra("result",result);
                        getActivity().sendBroadcast(it);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: bingo gridNew error" + ex.toString() );
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }


    private void intiView() {
        addImageView = ((ImageView) getView().findViewById(R.id.add_view));
        //按钮布局
        yichuButton = ((Button) getView().findViewById(R.id.yichu_button));
        zuoshangButton = ((Button) getView().findViewById(R.id.zuoshang_button));
        shangButton = ((Button) getView().findViewById(R.id.shang_button));
        youshangButton = ((Button) getView().findViewById(R.id.youshang_button));
        zuoButton = ((Button) getView().findViewById(R.id.zuo_button));
        xunhangButton = ((Button) getView().findViewById(R.id.xunhang_button));
        youButton = ((Button) getView().findViewById(R.id.you_button));
        zuoxiaButton = ((Button) getView().findViewById(R.id.zuoxia_button));
        xiaButton = ((Button) getView().findViewById(R.id.xia_button));
        youxiaButton = ((Button) getView().findViewById(R.id.youxia_button));
        lajinButton = ((Button) getView().findViewById(R.id.lajin_button));
        jiepingButton = ((Button) getView().findViewById(R.id.jieping_button));
        jujiaoButton = ((Button) getView().findViewById(R.id.jujiao_button));
        layuanButton = ((Button) getView().findViewById(R.id.layuan_button));
        luxiangButton = ((Button) getView().findViewById(R.id.luxiang_button));

        listButton = ((ImageView) getView().findViewById(R.id.list_button));
        //视频布局
        yiLayout = ((LinearLayout) getView().findViewById(R.id.yi_layout));
        siLayout = ((LinearLayout) getView().findViewById(R.id.si_layout));
        jiuLayout = ((LinearLayout) getView().findViewById(R.id.jiu_layout));
        video1_layout = ((FrameLayout) getView().findViewById(R.id.video1_layout));
        video2_layout = ((FrameLayout) getView().findViewById(R.id.video2_layout));
        video3_layout = ((FrameLayout) getView().findViewById(R.id.video3_layout));
        video4_layout = ((FrameLayout) getView().findViewById(R.id.video4_layout));
        video5_layout = ((FrameLayout) getView().findViewById(R.id.video5_layout));
        video6_layout = ((FrameLayout) getView().findViewById(R.id.video6_layout));
        video7_layout = ((FrameLayout) getView().findViewById(R.id.video7_layout));
        video8_layout = ((FrameLayout) getView().findViewById(R.id.video8_layout));
        video9_layout = ((FrameLayout) getView().findViewById(R.id.video9_layout));
        video_click1_layout = (FrameLayout) getView().findViewById(R.id.video_click1_layout);
        video_click2_layout = (FrameLayout) getView().findViewById(R.id.video_click2_layout);
        video_click3_layout = (FrameLayout) getView().findViewById(R.id.video_click3_layout);
        video_click4_layout = (FrameLayout) getView().findViewById(R.id.video_click4_layout);
        video_click5_layout = (FrameLayout) getView().findViewById(R.id.video_click5_layout);
        video_click6_layout = (FrameLayout) getView().findViewById(R.id.video_click6_layout);
        video_click7_layout = (FrameLayout) getView().findViewById(R.id.video_click7_layout);
        video_click8_layout = (FrameLayout) getView().findViewById(R.id.video_click8_layout);
        video_click9_layout = (FrameLayout) getView().findViewById(R.id.video_click9_layout);
        video1Player = ((MultiSampleVideo) getView().findViewById(R.id.video1_player));
        video2Player = ((MultiSampleVideo) getView().findViewById(R.id.video2_player));
        video3Player = ((MultiSampleVideo) getView().findViewById(R.id.video3_player));
        video4Player = ((MultiSampleVideo) getView().findViewById(R.id.video4_player));
        video5Player = ((MultiSampleVideo) getView().findViewById(R.id.video5_player));
        video6Player = ((MultiSampleVideo) getView().findViewById(R.id.video6_player));
        video7Player = ((MultiSampleVideo) getView().findViewById(R.id.video7_player));
        video8Player = ((MultiSampleVideo) getView().findViewById(R.id.video8_player));
        video9Player = ((MultiSampleVideo) getView().findViewById(R.id.video9_player));
        video1AddView = (ImageView) getView().findViewById(R.id.video1_add_view);
        video2AddView = (ImageView) getView().findViewById(R.id.video2_add_view);
        video3AddView = (ImageView) getView().findViewById(R.id.video3_add_view);
        video4AddView = (ImageView) getView().findViewById(R.id.video4_add_view);
        video5AddView = (ImageView) getView().findViewById(R.id.video5_add_view);
        video6AddView = (ImageView) getView().findViewById(R.id.video6_add_view);
        video7AddView = (ImageView) getView().findViewById(R.id.video7_add_view);
        video8AddView = (ImageView) getView().findViewById(R.id.video8_add_view);
        video9AddView = (ImageView) getView().findViewById(R.id.video9_add_view);
        initVideoPlayer(video1Player, 1);
        initVideoPlayer(video2Player, 2);
        initVideoPlayer(video3Player, 3);
        initVideoPlayer(video4Player, 4);
        initVideoPlayer(video5Player, 5);
        initVideoPlayer(video6Player, 6);
        initVideoPlayer(video7Player, 7);
        initVideoPlayer(video8Player, 8);
        initVideoPlayer(video9Player, 9);

        yiView = ((ImageView) getView().findViewById(R.id.yi_view));
        siView = ((ImageView) getView().findViewById(R.id.si_view));
        jiuView = ((ImageView) getView().findViewById(R.id.jiu_view));

        /**
         * 视频列表dialog
         */
        videoListDialog = new Dialog(getContext(), R.style.ActionSheetDialogStyleLeft);
        videoListInflater = LayoutInflater.from(getContext()).inflate(R.layout.dialog_video_list_new, null);
        videoListInflater.setMinimumWidth(100000);
        sv_gridtrees = ((ScrollView) videoListInflater.findViewById(R.id.sv_gridtrees));
        listDialogImage = ((ImageView) videoListInflater.findViewById(R.id.list_dialog_button));
        videoListDialog.setContentView(videoListInflater);
        Window videoListDialogWindow = videoListDialog.getWindow();
        videoListDialogWindow.setGravity(Gravity.LEFT);

        WindowManager.LayoutParams lpvideoList = videoListDialogWindow.getAttributes();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int width = wm.getDefaultDisplay().getWidth();
        lpvideoList.width = (int) (width * 0.7);
        lpvideoList.height = height * 1;
        videoListDialogWindow.setAttributes(lpvideoList);
        videoListDialog.setCanceledOnTouchOutside(true);

        /**
         * 火情dialog
         */
        addDialog = new Dialog(getContext(), R.style.ActionSheetDialogStyle);
        addInflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_fire, null);
        addInflate.setMinimumWidth(10000);

        huoqingView = ((TextView) addInflate.findViewById(R.id.huoqing_view));
        yinhuanView = ((TextView) addInflate.findViewById(R.id.yinhuan_view));
        addDialog.setContentView(addInflate);

        Window addWindow = addDialog.getWindow();
        addWindow.setWindowAnimations(R.style.ActionSheetDialogTopScaleAnimation);
        addWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        addWindow.setGravity(Gravity.TOP | Gravity.RIGHT);
        WindowManager.LayoutParams addListLp = addWindow.getAttributes();

        WindowManager addwm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int addheight = addwm.getDefaultDisplay().getHeight();
        int addwidth = addwm.getDefaultDisplay().getWidth();

        addListLp.width = (int) (addwidth * 0.4);
        addListLp.height = 330;
        addListLp.y = 90;
        addWindow.setAttributes(addListLp);
        addDialog.setCanceledOnTouchOutside(true);
    }

    private void initVideoPlayer(final MultiSampleVideo videoPlayer, int position) {
        String url = "rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101";
        videoPlayer.setPlayTag(TAG);
        videoPlayer.setPlayPosition(position);

        boolean isPlaying = videoPlayer.getCurrentPlayer().isInPlayingState();
        Log.e(TAG, "onBindViewHolder:isPlaying " + isPlaying);
        if (!isPlaying) {
            videoPlayer.setUpLazy(url, false, null, null, "这是title");
        }

        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);

        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置旋转
        orientationUtils = new OrientationUtils(getActivity(), videoPlayer);

        //设置全屏按键功能
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // resolveFullBtn(videoPlayer);
                videoPlayer.startWindowFullscreen(getContext(), false, true);
            }
        });
        videoPlayer.setRotateViewAuto(true);
        videoPlayer.setLockLand(true);
        videoPlayer.setReleaseWhenLossAudio(false);  //长时间失去音频焦点，暂停播放器
        videoPlayer.setVideoTypeErrorIsPlayer(true);  //视频播放格式出现错误 是否重新进行播放
        videoPlayer.setShowFullAnimation(true);
        if(position == 1){
            videoPlayer.setIsTouchWiget(true);
        }else{
            videoPlayer.setIsTouchWiget(false);
        }
        videoPlayer.setNeedLockFull(true);

        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            private String fullKey;

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                fullKey = "null";
                Log.e(TAG, "onQuitFullscreen: ");
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                videoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                fullKey = videoPlayer.getKey();
                Log.e(TAG, "onEnterFullscreen: ");
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
                Log.e(TAG, "onAutoComplete: ");
            }
        });

        videoPlayer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e(TAG, "onFocusChange: " + hasFocus );
            }
        });

    }


    private void bingView() {
        /**
         * 添加火情上报 跟隐患排查
         */
        RxViewAction.clickNoDouble(addImageView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        addDialog.show();
                    }
                });
        RxViewAction.clickNoDouble(huoqingView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        addDialog.dismiss();
                        ARouter.getInstance().build(RouteUtils.FireAdd)
                                .withString("token",new DbConfig(getContext()).getUser().getToken())
                                .navigation();

                    }
                });

        RxViewAction.clickNoDouble(yinhuanView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        addDialog.dismiss();
                        ARouter.getInstance().build(RouteUtils.HiddenDangerr)
                                .withString("token",new DbConfig(getContext()).getUser().getToken())
                                .navigation();
                    }
                });


        RxViewAction.clickNoDouble(listDialogImage)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        videoListDialog.dismiss();
                    }
                });
        yichuButton.setOnTouchListener(buttonListener);
        zuoshangButton.setOnTouchListener(buttonListener);
        shangButton.setOnTouchListener(buttonListener);
        youshangButton.setOnTouchListener(buttonListener);
        zuoButton.setOnTouchListener(buttonListener);
        xunhangButton.setOnTouchListener(buttonListener);
        youButton.setOnTouchListener(buttonListener);
        zuoxiaButton.setOnTouchListener(buttonListener);
        xiaButton.setOnTouchListener(buttonListener);
        youxiaButton.setOnTouchListener(buttonListener);
        lajinButton.setOnTouchListener(buttonListener);
        jiepingButton.setOnTouchListener(buttonListener);
        jujiaoButton.setOnTouchListener(buttonListener);
        layuanButton.setOnTouchListener(buttonListener);
        luxiangButton.setOnTouchListener(buttonListener);

        /**
         * 顶部listdialog弹出的点击事件
         */
        RxViewAction.clickNoDouble(listButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isAddVideoViewClick = false;
                        videoListDialog.show();
                    }
                });

        RxViewAction.clickNoDouble(yiView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        currentVideo = 1;
                        video1Player.setIsTouchWiget(true);
                        currentChooseVideo = 0;
                        initVideoChooseView();
                        yiView.setImageResource(R.drawable.ic_one_selected);
                        siView.setImageResource(R.drawable.ic_four);
                        jiuView.setImageResource(R.drawable.ic_sixteen);

                        initVideoView();
                    }
                });

        RxViewAction.clickNoDouble(siView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentVideo = 4;
                        video1Player.setIsTouchWiget(false);
                        currentChooseVideo = 0;
                        initVideoChooseView();
                        yiView.setImageResource(R.drawable.ic_one);
                        siView.setImageResource(R.drawable.ic_four_selected);
                        jiuView.setImageResource(R.drawable.ic_sixteen);
                        initVideoView();
                    }
                });
        RxViewAction.clickNoDouble(jiuView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentVideo = 9;
                        video1Player.setIsTouchWiget(false);
                        currentChooseVideo = 0;
                        initVideoChooseView();
                        yiView.setImageResource(R.drawable.ic_one);
                        siView.setImageResource(R.drawable.ic_four);
                        jiuView.setImageResource(R.drawable.ic_sixteen_selected);
                        initVideoView();
                    }
                });


        /**
         * 视频选中的9个点击
         */

        RxViewAction.clickNoDouble(video_click1_layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseVideo = 1;
                        initVideoChooseView();
                        initTurnId(1);
                    }
                });
        RxViewAction.clickNoDouble(video_click2_layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseVideo = 2;
                        initVideoChooseView();
                        initTurnId(2);
                    }
                });
        RxViewAction.clickNoDouble(video_click3_layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseVideo = 3;
                        initVideoChooseView();
                        initTurnId(3);
                    }
                });
        RxViewAction.clickNoDouble(video_click4_layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseVideo = 4;
                        initVideoChooseView();
                        initTurnId(4);
                    }
                });
        RxViewAction.clickNoDouble(video_click5_layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseVideo = 5;
                        initVideoChooseView();
                        initTurnId(5);
                    }
                });
        RxViewAction.clickNoDouble(video_click6_layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseVideo = 6;
                        initVideoChooseView();
                        initTurnId(6);
                    }
                });
        RxViewAction.clickNoDouble(video_click7_layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseVideo = 7;
                        initVideoChooseView();
                        initTurnId(7);
                    }
                });
        RxViewAction.clickNoDouble(video_click8_layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseVideo = 8;
                        initVideoChooseView();
                        initTurnId(8);
                    }
                });
        RxViewAction.clickNoDouble(video_click9_layout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseVideo = 9;
                        initVideoChooseView();
                        initTurnId(9);
                    }
                });

        /**
         * 点击添加按钮添加视频
         */

        RxViewAction.clickNoDouble(video1AddView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isAddVideoViewClick = true;
                        videoListDialog.show();
                    }
                });

        RxViewAction.clickNoDouble(video2AddView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isAddVideoViewClick = true;
                        videoListDialog.show();
                    }
                });

        RxViewAction.clickNoDouble(video3AddView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isAddVideoViewClick = true;
                        videoListDialog.show();
                    }
                });

        RxViewAction.clickNoDouble(video4AddView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isAddVideoViewClick = true;
                        videoListDialog.show();
                    }
                });

        RxViewAction.clickNoDouble(video5AddView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isAddVideoViewClick = true;
                        videoListDialog.show();
                    }
                });
        RxViewAction.clickNoDouble(video6AddView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isAddVideoViewClick = true;
                        videoListDialog.show();
                    }
                });
        RxViewAction.clickNoDouble(video7AddView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isAddVideoViewClick = true;
                        videoListDialog.show();
                    }
                });
        RxViewAction.clickNoDouble(video8AddView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isAddVideoViewClick = true;
                        videoListDialog.show();
                    }
                });

        RxViewAction.clickNoDouble(video9AddView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isAddVideoViewClick = true;
                        videoListDialog.show();
                    }
                });

    }

    private void initTurnId(int index) {
        Log.e(TAG, "initTurnId: index = " + index );
        for (int i = 0; i < videoIdList.size(); i++) {
            VideoId videoId = videoIdList.get(i);
            if(index == videoId.getVideoPlayer()){
                Log.e(TAG, "initTurnId: ==? = " );
                CommonData.turnId = videoId.getVideoId();
                Log.e(TAG, "initTurnId: CommonData.turnId = " + CommonData.turnId );
            }
        }
    }


    private View.OnTouchListener buttonListenerNew = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            //1：上，2：下，3：左，4：右，5：左上，6：左下，7：右上，8：右下
            if (v.getId() == R.id.yichu_button) {
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    yichuButton.setBackgroundResource(R.drawable.ic_button_hover);
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    yichuButton.setBackgroundResource(R.drawable.ic_button);
                    yichuVideo();
                }
            } else if (v.getId() == R.id.zuoshang_button) {       //左上  5
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    moveType = 7;
                    isStop = false;
                    moveShexiangtou();
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    moveType = 7;
                    isStop = true;
                    moveShexiangtou();
                }
            } else if (v.getId() == R.id.shang_button) {      //上 1
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    moveType = 3;
                    isStop = false;
                    moveShexiangtou();
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    moveType = 3;
                    isStop = true;
                    moveShexiangtou();
                }
            } else if (v.getId() == R.id.youshang_button) {       //右上  7
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    moveType = 9;
                    isStop = false;
                    moveShexiangtou();
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    moveType = 9;
                    isStop = true;
                    moveShexiangtou();
                }
            } else if (v.getId() == R.id.zuo_button) {        //左  3
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    moveType = 1;
                    isStop = false;
                    moveShexiangtou();
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    moveType = 1;
                    isStop = true;
                    moveShexiangtou();
                }
            } else if (v.getId() == R.id.xunhang_button) {
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    Toast.makeText(getContext(), "暂无控制权限", Toast.LENGTH_SHORT).show();
                } else if (action == MotionEvent.ACTION_UP) { // 松开

                }
            } else if (v.getId() == R.id.you_button) {        //右  4
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    moveType = 2;
                    isStop = false;
                    moveShexiangtou();
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    moveType = 2;
                    isStop = true;
                    moveShexiangtou();
                }
            } else if (v.getId() == R.id.zuoxia_button) {    //左下  6
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    moveType = 8;
                    isStop = false;
                    moveShexiangtou();
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    moveType = 8;
                    isStop = true;
                    moveShexiangtou();
                }
            } else if (v.getId() == R.id.xia_button) {        //下 2
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    moveType = 4;
                    isStop = false;
                    moveShexiangtou();
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    moveType = 4;
                    isStop = true;
                    moveShexiangtou();
                }
            } else if (v.getId() == R.id.youxia_button) {        //右下  8
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    moveType = 10;
                    isStop = false;
                    moveShexiangtou();
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    moveType = 10;
                    isStop = true;
                    moveShexiangtou();
                }
            }  else if (v.getId() == R.id.jieping_button) {
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    Toast.makeText(getContext(), "暂无控制权限", Toast.LENGTH_SHORT).show();
                } else if (action == MotionEvent.ACTION_UP) { // 松开

                }
            } else if (v.getId() == R.id.jujiao_button) {
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    Toast.makeText(getContext(), "暂无控制权限", Toast.LENGTH_SHORT).show();
                } else if (action == MotionEvent.ACTION_UP) { // 松开

                }
            } else if (v.getId() == R.id.luxiang_button) {
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    Toast.makeText(getContext(), "暂无控制权限", Toast.LENGTH_SHORT).show();
                } else if (action == MotionEvent.ACTION_UP) { // 松开

                }
            }
            if (v.getId() == R.id.lajin_button) {
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    steptype = 5;
                    isStop = false;
                    moveShexiangtou();
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    steptype = 5;
                    isStop = true;
                    moveShexiangtou();
                }
            } else if (v.getId() == R.id.layuan_button) {
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    steptype = 6;
                    isStop = false;
                    moveShexiangtou();
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    steptype = 6;
                    isStop = true;
                    moveShexiangtou();
                }
            }
            return false;
        }
    };

    private void moveShexiangtouNew() {
        String id = "";
        boolean isHasVideo = false;
        for (int i = 0; i < videoIdList.size(); i++) {
            if (videoIdList.get(i).getVideoPlayer() == currentChooseVideo) {
                isHasVideo = true;
                id = videoIdList.get(i).getVideoId();
            }
        }
        if (!isHasVideo){
            Toast.makeText(getContext(), "当前控制器暂无摄像头", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("enumCode",11001);
            jsonObject.put("stop",isStop);
            if (steptype == 0){
                jsonObject.put("direction",moveType);   //转动
            }else {
                jsonObject.put("step",steptype);        //拉进拉远
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/resource/api/guide/yunTaiReverse");
        params.setBodyContent(jsonObject.toString());
        params.setConnectTimeout(10000);
        params.addHeader("Authorization","bearer " + new DbConfig(getContext()).getUser().getToken());

        Log.e(TAG, "moveShexiangtou: " + params);
        Log.e(TAG, "moveShexiangtou: " + jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "moveShexiangtou: onSuccess" + result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "moveShexiangtou: onError" + ex.toString());

            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG, "moveShexiangtou: onCancelled" + cex.toString());

            }

            @Override
            public void onFinished() {
            }
        });
        steptype=0;
    }



    private String aqishiToken;
    /**
     * 从服务器获取阿启视的token
     */
    private void getTokenFromService() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/aqishi/authentication");
        params.setConnectTimeout(10000);
        Log.e(TAG, "getTokenFromService: " + params);
        params.addHeader("Authorization","bearer " + new DbConfig(getContext()).getUser().getToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "getTokenFromService: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    aqishiToken = jsonObject.getJSONArray("data").getJSONObject(0).getString("data");
                    CommonData.aqsToken = aqishiToken;
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

    private View.OnTouchListener buttonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            //1：上，2：下，3：左，4：右，5：左上，6：左下，7：右上，8：右下
            if (v.getId() == R.id.yichu_button) {
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    yichuButton.setBackgroundResource(R.drawable.ic_button_hover);
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    yichuButton.setBackgroundResource(R.drawable.ic_button);
                    yichuVideo();
                }
            }else if (v.getId() == R.id.zuoshang_button){       //左上  5
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    zuoshangButton.setBackgroundResource(R.drawable.ic_button_hover);
                    moveType = 25;
                    isStop = false;
                    moveShexiangtou();
                }else if (action == MotionEvent.ACTION_UP) { // 松开
                    zuoshangButton.setBackgroundResource(R.drawable.ic_button);
                    moveType = 25;
                    isStop = true;
                    moveShexiangtou();
                }
            }else if (v.getId() == R.id.shang_button){      //上 1
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    shangButton.setBackgroundResource(R.drawable.ic_button_hover);
                    moveType = 21;
                    isStop = false;
                    moveShexiangtou();
                }else if (action == MotionEvent.ACTION_UP) { // 松开
                    shangButton.setBackgroundResource(R.drawable.ic_button);
                    moveType = 21;
                    isStop = true;
                    moveShexiangtou();
                }
            }else if (v.getId() == R.id.youshang_button){       //右上  7
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    youshangButton.setBackgroundResource(R.drawable.ic_button_hover);
                    moveType = 26;
                    isStop = false;
                    moveShexiangtou();
                }else if (action == MotionEvent.ACTION_UP) { // 松开
                    youshangButton.setBackgroundResource(R.drawable.ic_button);
                    moveType = 26;
                    isStop = true;
                    moveShexiangtou();
                }
            }else if (v.getId() == R.id.zuo_button){        //左  3
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    zuoButton.setBackgroundResource(R.drawable.ic_button_hover);
                    moveType = 23;
                    isStop = false;
                    moveShexiangtou();
                }else if (action == MotionEvent.ACTION_UP) { // 松开
                    zuoButton.setBackgroundResource(R.drawable.ic_button);
                    moveType = 23;
                    isStop = true;
                    moveShexiangtou();
                }
            }else if (v.getId() == R.id.xunhang_button){

            }else if (v.getId() == R.id.you_button){        //右  4
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    youButton.setBackgroundResource(R.drawable.ic_button_hover);
                    moveType = 24;
                    isStop = false;
                    moveShexiangtou();
                }else if (action == MotionEvent.ACTION_UP) { // 松开
                    youButton.setBackgroundResource(R.drawable.ic_button);
                    moveType = 24;
                    isStop = true;
                    moveShexiangtou();
                }
            }else if (v.getId() == R.id.zuoxia_button){    //左下  6
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    zuoxiaButton.setBackgroundResource(R.drawable.ic_button_hover);
                    moveType = 27;
                    isStop = false;
                    moveShexiangtou();
                }else if (action == MotionEvent.ACTION_UP) { // 松开
                    zuoxiaButton.setBackgroundResource(R.drawable.ic_button);
                    moveType = 27;
                    isStop = true;
                    moveShexiangtou();
                }
            }else if (v.getId() == R.id.xia_button){        //下 2
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    xiaButton.setBackgroundResource(R.drawable.ic_button_hover);
                    moveType = 22;
                    isStop = false;
                    moveShexiangtou();
                }else if (action == MotionEvent.ACTION_UP) { // 松开
                    xiaButton.setBackgroundResource(R.drawable.ic_button);
                    moveType = 22;
                    isStop = true;
                    moveShexiangtou();
                }
            }else if (v.getId() == R.id.youxia_button){        //右下  8
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    youxiaButton.setBackgroundResource(R.drawable.ic_button_hover);
                    moveType = 28;
                    isStop = false;
                    moveShexiangtou();
                }else if (action == MotionEvent.ACTION_UP) { // 松开
                    youxiaButton.setBackgroundResource(R.drawable.ic_button);
                    moveType = 28;
                    isStop = true;
                    moveShexiangtou();
                }
            }else if (v.getId() == R.id.lajin_button){      //拉近
                if (action == MotionEvent.ACTION_DOWN) {
                    lajinButton.setBackgroundResource(R.drawable.ic_button_hover);
                    moveType = 11;
                    isStop = false;
                    moveShexiangtou();
                }else if (action == MotionEvent.ACTION_UP) {
                    lajinButton.setBackgroundResource(R.drawable.ic_button);
                    moveType = 11;
                    isStop = true;
                    moveShexiangtou();
                }
            }else if (v.getId() == R.id.jieping_button){    //截屏

            }else if (v.getId() == R.id.jujiao_button){ //聚焦
                if (action == MotionEvent.ACTION_DOWN) {
                    jujiaoButton.setBackgroundResource(R.drawable.ic_button_hover);
                    moveType = 101;
                    moveShexiangtou();
                }else if (action == MotionEvent.ACTION_UP) {
                    jujiaoButton.setBackgroundResource(R.drawable.ic_button);
                }
            }else if (v.getId() == R.id.layuan_button){ //拉远
                if (action == MotionEvent.ACTION_DOWN) {
                    layuanButton.setBackgroundResource(R.drawable.ic_button_hover);
                    moveType = 12;
                    isStop = false;
                    moveShexiangtou();
                }else if (action == MotionEvent.ACTION_UP) {
                    layuanButton.setBackgroundResource(R.drawable.ic_button);
                    moveType = 12;
                    isStop = true;
                    moveShexiangtou();
                }
            }else if (v.getId() == R.id.luxiang_button){

            }


            return false;
        }
    };


    private void moveShexiangtou() {
        String channelId = "";
        String monitorId = "";
        boolean isHasVideo = false;
        for (int i = 0; i < videoIdList.size(); i++) {
            Log.e(TAG, String.valueOf(videoIdList.get(i).getVideoPlayer()));
            Log.e(TAG, String.valueOf(currentChooseVideo));
            if (videoIdList.get(i).getVideoPlayer() == currentChooseVideo) {
                isHasVideo = true;
                channelId = videoIdList.get(i).getChannelId();
                Log.e(TAG, "moveShexiangtou: "+videoIdList.get(i) );
                monitorId = videoIdList.get(i).getMonitorId();
            }
        }
        if (!isHasVideo){
            Toast.makeText(getContext(), "当前控制器暂无摄像头，请点击视频后继续控制", Toast.LENGTH_SHORT).show();
            return;
        }

/*        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("groupId","001011");
            jsonObject.put("monitorId",monitorId);
            jsonObject.put("channelId",id);
            jsonObject.put("controlType",moveType);
            jsonObject.put("step",5);
            jsonObject.put("stop",isStop);
            jsonObject.put("speed",5);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        //  RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/aqishi/ptzCmd"); //阿奇视平台控制
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/liveVideo/control");
        params.addParameter("token",aqishiToken);
        params.addParameter("groupId","001");//TODO 特克斯林草
//        params.addParameter("groupId",new CommonUtils().parseSix(new DbConfig(getActivity()).getUser().getGroupId()));//TODO 四师
        params.addParameter("monitorId",monitorId);
        params.addParameter("channelId",channelId);
        params.addParameter("controlType",moveType+"");
        params.addParameter("step","3");
        if (moveType!=101){
            params.addParameter("stop",isStop?1:0);
        }else {
            params.addParameter("stop",0);
        }
        params.addParameter("speed",4);
//        params.setBodyContent(jsonObject.toString());

        params.setConnectTimeout(10000);
        params.addHeader("Authorization","bearer " + new DbConfig(getContext()).getUser().getToken());

        Log.e(TAG, "moveShexiangtou: " + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "moveShexiangtou: " + result);

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

    private void lajilayuanShexiangtou() {
        String id = "";
        boolean isHasVideo = false;
        for (int i = 0; i < videoIdList.size(); i++) {
            if (videoIdList.get(i).getVideoPlayer() == currentChooseVideo) {
                isHasVideo = true;
                id = videoIdList.get(i).getVideoId();
            }
        }
        if (!isHasVideo){
            Toast.makeText(getContext(), "当前控制器暂无摄像头", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("camera_id",id);
            jsonObject.put("operation",moveType);
            jsonObject.put("step",5);
            jsonObject.put("stop",isStop);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "resource/api/aqishi/zoomCmd");
        params.addParameter("token",aqishiToken);

        params.setBodyContent(jsonObject.toString());

        params.setConnectTimeout(10000);
        params.addHeader("Authorization","bearer " + new DbConfig(getContext()).getUser().getToken());

        Log.e(TAG, "moveShexiangtou: " + params);
        Log.e(TAG, "moveShexiangtou: " + jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "moveShexiangtou: " + result);

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
     * 移除当前选中视频
     */
    private void yichuVideo() {
        for (int i = 0; i < videoIdList.size(); i++) {
            if (videoIdList.get(i).getVideoPlayer()==currentChooseVideo) {
                videoIdList.remove(i);
                break;
            }
        }
        if (currentChooseVideo == 1) {
            //   video1Player.startPrepare();
            video1Player.onVideoReset();
            video1Player.setVisibility(View.GONE);
            video1AddView.setVisibility(View.VISIBLE);
        } else if (currentChooseVideo == 2) {
            video2Player.onVideoReset();
            video2Player.setVisibility(View.GONE);
            video2AddView.setVisibility(View.VISIBLE);
        } else if (currentChooseVideo == 3) {
            video3Player.onVideoReset();
            video3Player.setVisibility(View.GONE);
            video3AddView.setVisibility(View.VISIBLE);
        } else if (currentChooseVideo == 4) {
            video4Player.onVideoReset();
            video4Player.setVisibility(View.GONE);
            video4AddView.setVisibility(View.VISIBLE);
        } else if (currentChooseVideo == 5) {
            video5Player.onVideoReset();
            video5Player.setVisibility(View.GONE);
            video5AddView.setVisibility(View.VISIBLE);
        } else if (currentChooseVideo == 6) {
            video6Player.onVideoReset();
            video6Player.setVisibility(View.GONE);
            video6AddView.setVisibility(View.VISIBLE);
        } else if (currentChooseVideo == 7) {
            video7Player.onVideoReset();
            video7Player.setVisibility(View.GONE);
            video7AddView.setVisibility(View.VISIBLE);
        } else if (currentChooseVideo == 8) {
            video8Player.onVideoReset();
            video8Player.setVisibility(View.GONE);
            video8AddView.setVisibility(View.VISIBLE);
        } else if (currentChooseVideo == 9) {
            video9Player.onVideoReset();
            video9Player.setVisibility(View.GONE);
            video9AddView.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(getActivity(), "请选择要移除的视频", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 点击视频 选中视频带篮筐
     */
    private void initVideoChooseView() {
        video1_layout.setBackgroundResource(R.drawable.bg_video_choose_no);
        video2_layout.setBackgroundResource(R.drawable.bg_video_choose_no);
        video3_layout.setBackgroundResource(R.drawable.bg_video_choose_no);
        video4_layout.setBackgroundResource(R.drawable.bg_video_choose_no);
        video5_layout.setBackgroundResource(R.drawable.bg_video_choose_no);
        video6_layout.setBackgroundResource(R.drawable.bg_video_choose_no);
        video7_layout.setBackgroundResource(R.drawable.bg_video_choose_no);
        video8_layout.setBackgroundResource(R.drawable.bg_video_choose_no);
        video9_layout.setBackgroundResource(R.drawable.bg_video_choose_no);
        video_click1_layout.setVisibility(View.VISIBLE);
        video_click2_layout.setVisibility(View.VISIBLE);
        video_click3_layout.setVisibility(View.VISIBLE);
        video_click4_layout.setVisibility(View.VISIBLE);
        video_click5_layout.setVisibility(View.VISIBLE);
        video_click6_layout.setVisibility(View.VISIBLE);
        video_click7_layout.setVisibility(View.VISIBLE);
        video_click8_layout.setVisibility(View.VISIBLE);
        video_click9_layout.setVisibility(View.VISIBLE);
        if (currentChooseVideo == 1) {
            video1_layout.setBackgroundResource(R.drawable.bg_video_choose);
            video_click1_layout.setVisibility(View.GONE);
        } else if (currentChooseVideo == 2) {
            video2_layout.setBackgroundResource(R.drawable.bg_video_choose);
            video_click2_layout.setVisibility(View.GONE);
        } else if (currentChooseVideo == 3) {
            video3_layout.setBackgroundResource(R.drawable.bg_video_choose);
            video_click3_layout.setVisibility(View.GONE);
        } else if (currentChooseVideo == 4) {
            video4_layout.setBackgroundResource(R.drawable.bg_video_choose);
            video_click4_layout.setVisibility(View.GONE);
        } else if (currentChooseVideo == 5) {
            video5_layout.setBackgroundResource(R.drawable.bg_video_choose);
            video_click5_layout.setVisibility(View.GONE);
        } else if (currentChooseVideo == 6) {
            video6_layout.setBackgroundResource(R.drawable.bg_video_choose);
            video_click6_layout.setVisibility(View.GONE);
        } else if (currentChooseVideo == 7) {
            video7_layout.setBackgroundResource(R.drawable.bg_video_choose);
            video_click7_layout.setVisibility(View.GONE);
        } else if (currentChooseVideo == 8) {
            video8_layout.setBackgroundResource(R.drawable.bg_video_choose);
            video_click8_layout.setVisibility(View.GONE);
        } else if (currentChooseVideo == 9) {
            video9_layout.setBackgroundResource(R.drawable.bg_video_choose);
            video_click9_layout.setVisibility(View.GONE);
        }
    }

    private void initVideoView() {

        if (currentVideo == 1) {
            Log.e(TAG, "initVideoView: 1");
            yiLayout.setVisibility(View.VISIBLE);
            siLayout.setVisibility(View.GONE);
            jiuLayout.setVisibility(View.GONE);
            video1_layout.setVisibility(View.VISIBLE);
            video2_layout.setVisibility(View.GONE);
            video3_layout.setVisibility(View.GONE);
            video4_layout.setVisibility(View.GONE);
            video5_layout.setVisibility(View.GONE);
            video6_layout.setVisibility(View.GONE);
            video7_layout.setVisibility(View.GONE);
            video8_layout.setVisibility(View.GONE);
            video9_layout.setVisibility(View.GONE);
            closeVideoPlayer(video2Player);
            closeVideoPlayer(video3Player);
            closeVideoPlayer(video4Player);
            closeVideoPlayer(video5Player);
            closeVideoPlayer(video6Player);
            closeVideoPlayer(video7Player);
            closeVideoPlayer(video8Player);
            closeVideoPlayer(video9Player);
            video2AddView.setVisibility(View.VISIBLE);
            video3AddView.setVisibility(View.VISIBLE);
            video4AddView.setVisibility(View.VISIBLE);
            video5AddView.setVisibility(View.VISIBLE);
            video6AddView.setVisibility(View.VISIBLE);
            video7AddView.setVisibility(View.VISIBLE);
            video8AddView.setVisibility(View.VISIBLE);
            video9AddView.setVisibility(View.VISIBLE);
        } else if (currentVideo == 4) {
            Log.e(TAG, "initVideoView: 4");
            yiLayout.setVisibility(View.VISIBLE);
            siLayout.setVisibility(View.VISIBLE);
            jiuLayout.setVisibility(View.GONE);
            video1_layout.setVisibility(View.VISIBLE);
            video2_layout.setVisibility(View.VISIBLE);
            video3_layout.setVisibility(View.GONE);
            video4_layout.setVisibility(View.VISIBLE);
            video5_layout.setVisibility(View.VISIBLE);
            video6_layout.setVisibility(View.GONE);
            video7_layout.setVisibility(View.GONE);
            video8_layout.setVisibility(View.GONE);
            video9_layout.setVisibility(View.GONE);
            closeVideoPlayer(video3Player);
            closeVideoPlayer(video6Player);
            closeVideoPlayer(video7Player);
            closeVideoPlayer(video8Player);
            closeVideoPlayer(video9Player);
            video3AddView.setVisibility(View.VISIBLE);
            video6AddView.setVisibility(View.VISIBLE);
            video7AddView.setVisibility(View.VISIBLE);
            video8AddView.setVisibility(View.VISIBLE);
            video9AddView.setVisibility(View.VISIBLE);
        } else if (currentVideo == 9) {
            Log.e(TAG, "initVideoView: 9");
            yiLayout.setVisibility(View.VISIBLE);
            siLayout.setVisibility(View.VISIBLE);
            jiuLayout.setVisibility(View.VISIBLE);
            video1_layout.setVisibility(View.VISIBLE);
            video2_layout.setVisibility(View.VISIBLE);
            video3_layout.setVisibility(View.VISIBLE);
            video4_layout.setVisibility(View.VISIBLE);
            video5_layout.setVisibility(View.VISIBLE);
            video6_layout.setVisibility(View.VISIBLE);
            video7_layout.setVisibility(View.VISIBLE);
            video8_layout.setVisibility(View.VISIBLE);
            video9_layout.setVisibility(View.VISIBLE);
        }

    }

    private void closeVideoPlayer(MultiSampleVideo videoPlayer) {
        videoPlayer.setVisibility(View.GONE);
        videoPlayer.onVideoReset();


    }

    /**
     * 获取视频状态
     */
    private void getVideoPlayerState() {
        currentVideo1PlayerState = video1Player.getCurrentState();
        currentVideo2PlayerState = video2Player.getCurrentState();
        currentVideo3PlayerState = video3Player.getCurrentState();
        currentVideo4PlayerState = video4Player.getCurrentState();
        currentVideo5PlayerState = video5Player.getCurrentState();
        currentVideo6PlayerState = video6Player.getCurrentState();
        currentVideo7PlayerState = video7Player.getCurrentState();
        currentVideo8PlayerState = video8Player.getCurrentState();
        currentVideo9PlayerState = video9Player.getCurrentState();
    }

    @Override
    public void onResume() {
        super.onResume();
        getVideoPlayerState();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK){
                    if (video1Player.isIfCurrentIsFullscreen()){        //判断是否是全屏显示

                        video1Player.clearFullscreenLayout();
                        return true;
                    }
                    if (video1Player.isIfCurrentIsFullscreen()){        //判断是否是全屏显示
                        video1Player.clearFullscreenLayout();
                        return true;
                    }
                    if (video2Player.isIfCurrentIsFullscreen()){        //判断是否是全屏显示
                        video2Player.clearFullscreenLayout();
                        return true;
                    }
                    if (video3Player.isIfCurrentIsFullscreen()){        //判断是否是全屏显示
                        video3Player.clearFullscreenLayout();
                        return true;
                    }
                    if (video4Player.isIfCurrentIsFullscreen()){        //判断是否是全屏显示
                        video4Player.clearFullscreenLayout();
                        return true;
                    }
                    if (video5Player.isIfCurrentIsFullscreen()){        //判断是否是全屏显示
                        video5Player.clearFullscreenLayout();
                        return true;
                    }
                    if (video6Player.isIfCurrentIsFullscreen()){        //判断是否是全屏显示
                        video6Player.clearFullscreenLayout();
                        return true;
                    }
                    if (video7Player.isIfCurrentIsFullscreen()){        //判断是否是全屏显示
                        video7Player.clearFullscreenLayout();
                        return true;
                    }
                    if (video8Player.isIfCurrentIsFullscreen()){        //判断是否是全屏显示
                        video8Player.clearFullscreenLayout();
                        return true;
                    }
                    if (video9Player.isIfCurrentIsFullscreen()){        //判断是否是全屏显示
                        video9Player.clearFullscreenLayout();
                        return true;
                    }

                    return false;
                }
                return false;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        getVideoPlayerState();
        videoPause();
    }

    /**
     * 视频不在前台的时候播放中的视频暂停
     */
    private void videoPause() {
        if (currentVideo1PlayerState == CURRENT_STATE_PLAYING) {
            video1Player.onVideoPause();
        }
        if (currentVideo2PlayerState == CURRENT_STATE_PLAYING) {
            video2Player.onVideoPause();
        }
        if (currentVideo3PlayerState == CURRENT_STATE_PLAYING) {
            video3Player.onVideoPause();
        }
        if (currentVideo4PlayerState == CURRENT_STATE_PLAYING) {
            video4Player.onVideoPause();
        }
        if (currentVideo5PlayerState == CURRENT_STATE_PLAYING) {
            video5Player.onVideoPause();
        }
        if (currentVideo6PlayerState == CURRENT_STATE_PLAYING) {
            video6Player.onVideoPause();
        }
        if (currentVideo7PlayerState == CURRENT_STATE_PLAYING) {
            video7Player.onVideoPause();
        }
        if (currentVideo8PlayerState == CURRENT_STATE_PLAYING) {
            video8Player.onVideoPause();
        }
        if (currentVideo9PlayerState == CURRENT_STATE_PLAYING) {
            video9Player.onVideoPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(changeTabReceiver);
    }
}