package com.haohai.platform.firelibrary.ui.multitype;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haohai.platform.firelibrary.R;
import com.haohai.platform.firelibrary.ui.activity.FireMissionInfoActivity;
import com.haohai.platform.firelibrary.ui.activity.Pic1Activity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import java.util.List;
import java.util.Objects;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class SenceFooterViewBinder extends ItemViewProvider<SenceFooter, SenceFooterViewBinder.ViewHolder> {
    public OnSenceFooterItemClick listener;
    private Context mContext;

    public SenceFooterViewBinder(Context mContext) {
        this.mContext = mContext;
    }

    public void setListener(OnSenceFooterItemClick listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_footer_sence, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull SenceFooter senceFooter) {
        holder.tv_time.setText("上报时间: " + senceFooter.getTime() );
        holder.tv_descs.setText("现场情况: " + senceFooter.getDescs() );
        holder.tv_other.setText("其他信息: " + senceFooter.getOther() );
        if(senceFooter.getLongitude()!=null && senceFooter.getLatitude()!=null){
            holder.tv_longitude.setText("经度: " + senceFooter.getLongitude() );
            holder.tv_latitude.setText("纬度: " + senceFooter.getLatitude() );
            holder.tv_longitude.setVisibility(View.VISIBLE);
            holder.tv_latitude.setVisibility(View.VISIBLE);
        }
        String[] split = senceFooter.getImages().split(",");
        for (int i = 0; i < split.length; i++) {
            String value = split[i];
            if(Objects.equals(split[i], "")){
                continue;
            }
            ImageView imageView = new ImageView(mContext);
            imageView.setMaxHeight(50);
            Glide.with(mContext).load(value).into(imageView);
            RxViewAction.clickNoDouble(imageView).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    Intent intent = new Intent(mContext,Pic1Activity.class);
                    intent.putExtra("pic",value);
                    mContext.startActivity(intent);
                }
            });
            holder.ll_images.addView(imageView);
        }

        if(senceFooter.getVideos()==null || Objects.equals(senceFooter.getVideos(), "")){
            holder.ll_video.setVisibility(View.GONE);
        }
        RxViewAction.clickNoDouble(holder.iv_video).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.videoClick(senceFooter.getVideos());
            }
        });



    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_time;
        private final TextView tv_descs;
        private final TextView tv_other;
        private final TextView tv_longitude;
        private final TextView tv_latitude;
        private final LinearLayout ll_images;
        private final TextView iv_video;
        private final LinearLayout ll_video;

        ViewHolder(View itemView) {
            super(itemView);
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));
            tv_descs = ((TextView) itemView.findViewById(R.id.tv_descs));
            tv_other = ((TextView) itemView.findViewById(R.id.tv_other));
            tv_longitude = ((TextView) itemView.findViewById(R.id.tv_longitude));
            tv_latitude = ((TextView) itemView.findViewById(R.id.tv_latitude));
            ll_images = ((LinearLayout) itemView.findViewById(R.id.ll_images));
            iv_video = ((TextView) itemView.findViewById(R.id.iv_video));
            ll_video = ((LinearLayout) itemView.findViewById(R.id.ll_video));
        }
    }
    public interface OnSenceFooterItemClick{
        void videoClick(String video);
    }
}
