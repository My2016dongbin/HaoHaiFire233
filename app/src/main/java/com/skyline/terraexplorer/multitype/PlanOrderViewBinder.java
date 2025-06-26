package com.skyline.terraexplorer.multitype;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.utils.RxViewAction;

import me.drakeet.multitype.ItemViewBinder;
import rx.functions.Action1;


/**
 * Created by geyang on 2019/12/26.
 */
public class PlanOrderViewBinder extends ItemViewBinder<PlanOrder, PlanOrderViewBinder.ViewHolder> {

    public OnPlanOrderItemClick listener;

    public void setListener(OnPlanOrderItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_plan_order, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull PlanOrder planOrder) {
        holder.nameView.setText(planOrder.getName());
        holder.addressView.setText(planOrder.getDistrictName() + planOrder.getStreetName() + planOrder.getStreetNo());
        if (planOrder.getStartTime()!=null){
            String starTime = planOrder.getStartTime().replace("T", " ");
            holder.startTimeView.setText(starTime.substring(0,starTime.indexOf(".")));
        }
        if (planOrder.getEndTime()!=null){
            String endTime = planOrder.getEndTime().replace("T"," ");
            holder.endTimeView.setText(endTime.substring(0,endTime.indexOf(".")));
        }
        Log.e("TAG", "onBindViewHolder: planOrder.getDeadLine() " + planOrder.getDeadline() );
        if (planOrder.getDeadline()!=null){
            String deadLine = planOrder.getDeadline().replace("T"," ");
            holder.dead_time_view.setText(deadLine.substring(0,deadLine.indexOf(".")));
        }
        RxViewAction.clickNoDouble(holder.daohangButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onPlanOrderItemClickListener();
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView addressView;
        private final TextView startTimeView;
        private final TextView endTimeView;
        private final TextView dead_time_view;
        private final FrameLayout orderLayout;
        private final TextView daohangButton;

        ViewHolder(View itemView) {
            super(itemView);
            nameView = ((TextView) itemView.findViewById(R.id.order_name_view));
            addressView = ((TextView) itemView.findViewById(R.id.order_address_view));
            startTimeView = ((TextView) itemView.findViewById(R.id.start_time_view));
            endTimeView = ((TextView) itemView.findViewById(R.id.end_time_view));
            dead_time_view = ((TextView) itemView.findViewById(R.id.dead_time_view));
            orderLayout = (FrameLayout) itemView.findViewById(R.id.order_layout);
            daohangButton = ((TextView) itemView.findViewById(R.id.daohang_guihua_button));
        }
    }
    public interface OnPlanOrderItemClick{
        void onPlanOrderItemClickListener();
    }
}
