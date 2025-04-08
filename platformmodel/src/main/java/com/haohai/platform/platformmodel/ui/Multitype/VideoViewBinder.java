package com.haohai.platform.platformmodel.ui.Multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.haohai.ledge.videolibrary.listener.GSYSampleCallBack;
import com.haohai.ledge.videolibrary.video.MultiSampleVideo;
import com.haohai.platform.platformmodel.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

import static com.haohai.ledge.videolibrary.video.base.GSYVideoView.CURRENT_STATE_PAUSE;


/**
 * Created by geyang on 2020/6/20.
 */
public class VideoViewBinder extends ItemViewProvider<Video, VideoViewBinder.ViewHolder> {
    public static final String TAG = "VideoViewBinder";
    public OnVideoItemClick listener;

    public void setListener(OnVideoItemClick listener) {
        this.listener = listener;
    }

    private String fullKey = "null";
    public Context context;


    public VideoViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_video, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Video video) {

        final String url = video.videoUrl;
        if (video.isChoose){
            holder.videoFrameLayout.setBackgroundColor(context.getResources().getColor(R.color.theme_primary));
            holder.videoClickLayout.setVisibility(View.GONE);
        }else {
            holder.videoFrameLayout.setBackgroundColor(context.getResources().getColor(R.color.c12));
            holder.videoClickLayout.setVisibility(View.VISIBLE);
        }

        //多个播放时必须在setUpLazy、setUp和getGSYVideoManager()等前面设置
        holder.videoPlayer.setPlayTag(TAG);
        holder.videoPlayer.setPlayPosition(video.position);

        boolean isPlaying = holder.videoPlayer.getCurrentPlayer().isInPlayingState();
        Log.e(TAG, "onBindViewHolder:isPlaying " +  isPlaying);
        if (!isPlaying) {
            holder.videoPlayer.setUpLazy(url, false, null, null, "这是title");
        }

        //videoPlayer
        holder.videoPlayer.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        holder.videoPlayer.getBackButton().setVisibility(View.GONE);


        //设置全屏按键功能
        holder.videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // resolveFullBtn(holder.videoPlayer);
                holder.videoPlayer.startWindowFullscreen(context, false, true);
            }
        });
        holder.videoPlayer.setRotateViewAuto(true);
        holder.videoPlayer.setLockLand(true);
        holder.videoPlayer.setReleaseWhenLossAudio(false);
        holder.videoPlayer.setShowFullAnimation(true);
        holder.videoPlayer.setIsTouchWiget(false);

        holder.videoPlayer.setNeedLockFull(true);

        if (video.position % 2 == 0) {
            holder.videoPlayer.loadCoverImage(url, R.mipmap.image_loading);
        } else {
            holder.videoPlayer.loadCoverImage(url, R.mipmap.image_loading);
        }

        holder.videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {


            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                fullKey = "null";
                Log.e(TAG, "onQuitFullscreen: " );
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                holder.videoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                fullKey = holder.videoPlayer.getKey();
                Log.e(TAG, "onEnterFullscreen: " );
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
                Log.e(TAG, "onAutoComplete: ");
            }
        });





        RxViewAction.clickNoDouble(holder.videoClickLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onVideoItemClickListener(video.getPosition());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final MultiSampleVideo videoPlayer;
        private final FrameLayout videoFrameLayout;
        private final FrameLayout videoClickLayout;

        ViewHolder(View itemView) {
            super(itemView);
            videoPlayer = ((MultiSampleVideo) itemView.findViewById(R.id.video_player));
            videoFrameLayout = ((FrameLayout) itemView.findViewById(R.id.video_layout));
            videoClickLayout = ((FrameLayout) itemView.findViewById(R.id.video_click_layout));
        }
    }

    public interface OnVideoItemClick{
        void onVideoItemClickListener(int id);
    }
}
