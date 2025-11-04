package com.hht.hsatellitemobile.ui.multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hht.hsatellitemobile.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import java.util.Objects;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class FeedBackViewBinder extends ItemViewProvider<FeedBack, FeedBackViewBinder.ViewHolder> {
    public OnFeedBackItemClick listener;
    public Context context;

    public void setListener(OnFeedBackItemClick listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_feed_back, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final FeedBack feedBack) {
        holder.address.setText(feedBack.getAddress());
        holder.lat_lng.setText(feedBack.getLongitude()+","+feedBack.getLatitude());
        holder.info.setText(feedBack.getPic_path3());
        holder.address.setText(feedBack.getAddress());
        if(feedBack.getPic_path1()!=null && !Objects.equals(feedBack.getPic_path1(), "null")){
            Glide.with(context).load(feedBack.getPic_path1()).into(holder.picture1);
            holder.picture1.setVisibility(View.VISIBLE);
            RxViewAction.clickNoDouble(holder.picture1).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    listener.onFeedBackPicClick(feedBack.getPic_path1());
                }
            });
        }else {
            holder.picture1.setVisibility(View.GONE);
        }
        if(feedBack.getPic_path2()!=null && !Objects.equals(feedBack.getPic_path2(), "null")){
            Glide.with(context).load(feedBack.getPic_path2()).placeholder(R.drawable.ic_jaizai).into(holder.picture2);
            holder.picture2.setVisibility(View.VISIBLE);
            RxViewAction.clickNoDouble(holder.picture2).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    listener.onFeedBackPicClick(feedBack.getPic_path2());
                }
            });
        }else {
            holder.picture2.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView address;
        private final TextView lat_lng;
        private final TextView info;
        private final ImageView picture1;
        private final ImageView picture2;

        ViewHolder(View itemView) {
            super(itemView);
            address = ((TextView) itemView.findViewById(R.id.address));
            lat_lng = ((TextView) itemView.findViewById(R.id.lat_lng));
            info = ((TextView) itemView.findViewById(R.id.info));
            picture1 = ((ImageView) itemView.findViewById(R.id.picture1));
            picture2 = ((ImageView) itemView.findViewById(R.id.picture2));
        }
    }

    public interface OnFeedBackItemClick{
        void onFeedBackPicClick(String url);
    }
}
