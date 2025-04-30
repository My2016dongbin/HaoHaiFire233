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
 * Created by geyang on 2020/12/2.
 */
public class FireMissionViewBinder extends ItemViewProvider<FireMission, FireMissionViewBinder.ViewHolder> {

    public OnFireMissionItemClick listener;

    public void setListener(OnFireMissionItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_fire_mission, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull FireMission fireMission) {
        holder.textView1.setText("任务内容: " + fireMission.getTaskContent() );
        holder.textView2.setText("开始时间: " + fireMission.getTaskStartTime() );
        holder.textView3.setText("截止时间: " + fireMission.getTaskEndTime() );
     //  0 holder.textView4.setText("执行人: " + fireMission.getOperatorName() );
        //任务状态，0未开始，1执行中，2已结束
        if (fireMission.getStatus() == 0) {
            holder.orderStateView.setText("未开始");
        }else if (fireMission.getStatus() == 1){
            holder.orderStateView.setText("执行中");
        }else {
            holder.orderStateView.setText("已结束");
        }


        RxViewAction.clickNoDouble(holder.orderLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onFireMissionItemClickListener(fireMission);
                    }
                });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView1;
        private final TextView textView2;
        private final TextView textView3;
        private final TextView textView4;
        private final TextView orderStateView;
        private final FrameLayout orderLayout;

        ViewHolder(View itemView) {
            super(itemView);
            textView1 = ((TextView) itemView.findViewById(R.id.text_view1));
            textView2 = ((TextView) itemView.findViewById(R.id.text_view2));
            textView3 = ((TextView) itemView.findViewById(R.id.text_view3));
            textView4 = ((TextView) itemView.findViewById(R.id.text_view4));
            orderStateView = ((TextView) itemView.findViewById(R.id.order_state_view));
            orderLayout = ((FrameLayout) itemView.findViewById(R.id.order_layout));
        }
    }

    public interface OnFireMissionItemClick{
        void onFireMissionItemClickListener(FireMission fireMission);
    }
}
