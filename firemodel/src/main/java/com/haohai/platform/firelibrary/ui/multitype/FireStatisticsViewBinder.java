package com.haohai.platform.firelibrary.ui.multitype;

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



    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView1;
        private final TextView textView2;
        private final TextView textView3;
        private final TextView textView4;
        private final FrameLayout orderLayout;

        ViewHolder(View itemView) {
            super(itemView);
            textView1 = ((TextView) itemView.findViewById(R.id.text_view1));
            textView2 = ((TextView) itemView.findViewById(R.id.text_view2));
            textView3 = ((TextView) itemView.findViewById(R.id.text_view3));
            textView4 = ((TextView) itemView.findViewById(R.id.text_view4));
            orderLayout = ((FrameLayout) itemView.findViewById(R.id.order_layout));
        }
    }
    public interface OnFireStatisticsItemClick{
        void onFireStatisticsItemClickListener(FireMission fireMission);
    }
}
