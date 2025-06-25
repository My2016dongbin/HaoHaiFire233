package com.haohai.platform.platformmodel.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haohai.platform.platformmodel.R;
import com.haohai.platform.platformmodel.ui.Multitype.Video;
import com.haohai.platform.platformmodel.ui.Multitype.VideoViewBinder;
import com.haohai.platform.platformmodel.ui.fragment.base.HhBaseFragment;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

/**
 * Created by geyang on 2020/6/20.
 */

public class VideoFragment extends HhBaseFragment implements VideoViewBinder.OnVideoItemClick {
    private static final String TAG = VideoFragment.class.getSimpleName();
    private RecyclerView videoListView;
    private List<Object> videoItems = new ArrayList<>();
    private MultiTypeAdapter videoAdapter;
    public int currentVideoNum = 1;
    private TextView yiView;
    private TextView siView;
    private TextView jiuView;
    private FrameLayout videoLayout;
    public List<Video> videoList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        videoList = new ArrayList<>();


        videoList.clear();
        videoList.add(new Video(0,false,false,"rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101"));

        initView();
        bindView();

        getDataFromService();
    }

    private void getDataFromService() {

    }

    private void bindView() {
        RxViewAction.clickNoDouble(yiView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentVideoNum = 1;
                        yiView.setBackgroundResource(R.drawable.bg_button_lan);
                        siView.setBackgroundResource(R.drawable.bg_button_hui);
                        jiuView.setBackgroundResource(R.drawable.bg_button_hui);

                        videoList.clear();
                        videoList.add(new Video(0,false,false,"rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101"));
                        initListView();
                    }
                });
        RxViewAction.clickNoDouble(siView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentVideoNum = 4;
                        yiView.setBackgroundResource(R.drawable.bg_button_hui);
                        siView.setBackgroundResource(R.drawable.bg_button_lan);
                        jiuView.setBackgroundResource(R.drawable.bg_button_hui);
                        videoList.clear();
                        videoList.add(new Video(0,false,false,"rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101"));
                        videoList.add(new Video(1,false,false,"http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"));
                        videoList.add(new Video(2,false,false,"rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101"));
                        videoList.add(new Video(3,false,false,"http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"));
                        initListView();
                    }
                });
        RxViewAction.clickNoDouble(jiuView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        initData();
                      /*  currentVideoNum = 9;
                        yiView.setBackgroundResource(R.drawable.bg_button_hui);
                        siView.setBackgroundResource(R.drawable.bg_button_hui);
                        jiuView.setBackgroundResource(R.drawable.bg_button_lan);
                        videoList.clear();
                        videoList.add(new Video(0,false,false,"rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101"));
                        videoList.add(new Video(1,false,false,"rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101"));
                        videoList.add(new Video(2,false,false,"rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101"));
                        videoList.add(new Video(3,false,false,"rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101"));
                        videoList.add(new Video(4,false,false,"rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101"));
                        videoList.add(new Video(5,false,false,"rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101"));
                        videoList.add(new Video(6,false,false,"rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101"));
                        videoList.add(new Video(7,false,false,"rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101"));
                        videoList.add(new Video(8,false,false,"rtsp://admin:hh123456@192.168.1.12:554/Streaming/Channels/101"));
                        initListView();*/
                    }
                });
    }

    private void initData() {
        videoItems.clear();
        for (int i = 0; i < videoList.size(); i++) {
            videoItems.add(videoList.get(i));
        }

        assertAllRegistered(videoAdapter,videoItems);
        videoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
      /*  int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        videoLayout.measure(h,0);
        int height =videoLayout.getMeasuredHeight();*/

        Log.e(TAG, "initView:height " + videoLayout.getHeight());
    }

    private void initView() {


        yiView = ((TextView) getView().findViewById(R.id.yi_view));
        siView = ((TextView) getView().findViewById(R.id.si_view));
        jiuView = ((TextView) getView().findViewById(R.id.jiu_view));
        videoLayout = ((FrameLayout) getView().findViewById(R.id.video_layout));


        initListView();
    }

    private void initListView() {
        videoListView = ((RecyclerView) getView().findViewById(R.id.video_listview));
        GridLayoutManager gridLayoutManager = null;
        if (currentVideoNum == 1){
            gridLayoutManager = new GridLayoutManager(getContext(), 1);
        }else if (currentVideoNum == 4){
            gridLayoutManager = new GridLayoutManager(getContext(), 2);
        }else if (currentVideoNum == 9){
            gridLayoutManager = new GridLayoutManager(getContext(), 3);
        }

        videoListView.setLayoutManager(gridLayoutManager);
        videoAdapter = new MultiTypeAdapter(videoItems);
        register();
        videoListView.setAdapter(videoAdapter);
        assertHasTheSameAdapter(videoListView, videoAdapter);

        initData();

    }

    private void register() {
        VideoViewBinder videoViewBinder = new VideoViewBinder(getContext());
        videoViewBinder.setListener(this);
        videoAdapter.register(Video.class, videoViewBinder);
    }

    /**
     * video的条目点击
     * @param id
     */
    @Override
    public void onVideoItemClickListener(int id) {
        Log.e(TAG, "onVideoItemClickListener: " + id );
        for (int i = 0; i < videoList.size(); i++) {
            if (videoList.get(i).getPosition() == id) {
                Log.e(TAG, "onVideoItemClickListener:111 " );
                videoList.get(i).setChoose(true);
            }else {
                videoList.get(i).setChoose(false);
            }
        }
        initData();
    }
}
