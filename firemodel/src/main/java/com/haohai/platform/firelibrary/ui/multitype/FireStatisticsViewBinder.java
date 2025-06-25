package com.haohai.platform.firelibrary.ui.multitype;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haohai.platform.firelibrary.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by geyang on 2021/3/12.
 */
public class FireStatisticsViewBinder extends ItemViewProvider<FireStatistics, FireStatisticsViewBinder.ViewHolder> {
    public OnFireStatisticsItemClick listener;
    public void setListener(OnFireStatisticsItemClick listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_fire_statistics, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull FireStatistics fireStatistics) {
        holder.textView1.setText("街道名称: " + fireStatistics.getCountyName() );
        int num = fireStatistics.getHandleCount() + fireStatistics.getUnHandleCount();
        holder.textView2.setText("报警总数: " + num );
        holder.textView3.setText("已处理数: " + fireStatistics.getHandleCount() );
        holder.textView4.setText("未处理数: " + fireStatistics.getUnHandleCount() );

        holder.textView2_.setText("任务总数: " + fireStatistics.getTaskAll() );
        holder.textView3_.setText("执行中数: " + fireStatistics.getTaskIng() );
        holder.textView4_.setText("已结束数: " + fireStatistics.getTaskEnd() );
        holder.textView5_.setText("未处理数: " + fireStatistics.getTaskWait() );

        RxViewAction.clickNoDouble(holder.textView2_).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.taskAllClick();
            }
        });
        RxViewAction.clickNoDouble(holder.textView3_).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.taskIngClick();
            }
        });
        RxViewAction.clickNoDouble(holder.textView4_).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.taskEndClick();
            }
        });
        RxViewAction.clickNoDouble(holder.textView5_).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.taskWaitClick();
            }
        });
        RxViewAction.clickNoDouble(holder.textView2).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.firealarm();
            }
        });
        RxViewAction.clickNoDouble(holder.textView3).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.firealarm();
            }
        });
        RxViewAction.clickNoDouble(holder.textView4).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.firealarm();
            }
        });



    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView1;
        private final TextView textView2;
        private final TextView textView3;
        private final TextView textView4;
        private final FrameLayout orderLayout;

        private final TextView textView2_;
        private final TextView textView3_;
        private final TextView textView4_;
        private final TextView textView5_;

        ViewHolder(View itemView) {
            super(itemView);
            textView1 = ((TextView) itemView.findViewById(R.id.text_view1));
            textView2 = ((TextView) itemView.findViewById(R.id.text_view2));
            textView3 = ((TextView) itemView.findViewById(R.id.text_view3));
            textView4 = ((TextView) itemView.findViewById(R.id.text_view4));
            orderLayout = ((FrameLayout) itemView.findViewById(R.id.order_layout));

            textView2_ = ((TextView) itemView.findViewById(R.id.text_view2_));
            textView3_ = ((TextView) itemView.findViewById(R.id.text_view3_));
            textView4_ = ((TextView) itemView.findViewById(R.id.text_view4_));
            textView5_ = ((TextView) itemView.findViewById(R.id.text_view5_));
        }
    }
    public interface OnFireStatisticsItemClick{
        void onFireStatisticsItemClickListener(FireMission fireMission);
        void taskAllClick();
        void taskIngClick();
        void taskEndClick();
        void taskWaitClick();
        void firealarm();
    }
}
