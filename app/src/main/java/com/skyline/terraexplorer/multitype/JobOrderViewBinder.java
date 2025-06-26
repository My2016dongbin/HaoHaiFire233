package com.skyline.terraexplorer.multitype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.db.JobOrder;
import com.skyline.terraexplorer.utils.RxViewAction;

import me.drakeet.multitype.ItemViewBinder;
import rx.functions.Action1;

/**
 * Created by geyang on 2019/12/24.
 */
public class JobOrderViewBinder extends ItemViewBinder<JobOrder, JobOrderViewBinder.ViewHolder> {

    public OnJobOrderItemClick listener;

    public void setListener(OnJobOrderItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_job_order, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final JobOrder jobOrder) {
        holder.nameView.setText(jobOrder.getName());
        holder.addressView.setText(jobOrder.getDistrictName() + jobOrder.getStreetName() + jobOrder.getStreetNo());
        if (jobOrder.getStartTime()!=null){
            String starTime = jobOrder.getStartTime().replace("T", " ");
            holder.startTimeView.setText(starTime.substring(0,starTime.indexOf(".")));
        }
        if (jobOrder.getEndTime()!=null){
            String endTime = jobOrder.getEndTime().replace("T"," ");
            holder.endTimeView.setText(endTime.substring(0,endTime.indexOf(".")));
        }
        if (jobOrder.getDeadline()!=null){
            String deadLine = jobOrder.getDeadline().replace("T"," ");
            holder.dead_time_view.setText(deadLine.substring(0,deadLine.indexOf(".")));
        }


        RxViewAction.clickNoDouble(holder.orderLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onJonOrderItemClickLisiener(jobOrder.getId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameView;
        private final TextView addressView;
        private final TextView startTimeView;
        private final TextView endTimeView;
        private final TextView dead_time_view;
        private final LinearLayout orderLayout;

        ViewHolder(View itemView) {
            super(itemView);
            nameView = ((TextView) itemView.findViewById(R.id.order_name_view));
            addressView = ((TextView) itemView.findViewById(R.id.order_address_view));
            startTimeView = ((TextView) itemView.findViewById(R.id.start_time_view));
            endTimeView = ((TextView) itemView.findViewById(R.id.end_time_view));
            dead_time_view = ((TextView) itemView.findViewById(R.id.dead_time_view));
            orderLayout = (LinearLayout) itemView.findViewById(R.id.order_layout);
        }
    }

    public interface OnJobOrderItemClick{
        void  onJonOrderItemClickLisiener(String id);
    }
}
