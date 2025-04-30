package com.haohai.platform.platformmodel.ui.Multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haohai.platform.platformmodel.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/6/18.
 */

/**
 * 第二步  创建条目Item
 */
public class LeaveFlowViewBinder extends ItemViewProvider<LeaveFlow, LeaveFlowViewBinder.ViewHolder> {

    public OnLeaveFlowItemClick listener;
    public Context context;

    public LeaveFlowViewBinder(Context context) {
        this.context = context;
    }

    public void setListener(OnLeaveFlowItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_leave_flow, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final LeaveFlow leaveFlow) {
        if (leaveFlow.getProcessStart().equals("1")) {
            holder.zhuangtaiView.setText("审批中...");
            holder.zhuangtaiView.setTextColor(context.getResources().getColor(R.color.orange));
        }else if (leaveFlow.getProcessStart().equals("2")) {
            holder.zhuangtaiView.setText("驳回");
            holder.zhuangtaiView.setTextColor(context.getResources().getColor(R.color.c20));
        }else if (leaveFlow.getProcessStart().equals("3")) {
            holder.zhuangtaiView.setText("已取消");
        }else if (leaveFlow.getProcessStart().equals("4")) {
            holder.zhuangtaiView.setText("已批准");
            holder.zhuangtaiView.setTextColor(context.getResources().getColor(R.color.msg_green));
        }

        holder.shijianView.setText(leaveFlow.getUpdateTime().substring(0,leaveFlow.getUpdateTime().indexOf(".")).replace("T"," "));

        RxViewAction.clickNoDouble(holder.leaveLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onLeaveFlowItemClickListener(leaveFlow);
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView shijianView;
        private final TextView zhuangtaiView;
        private final FrameLayout leaveLayout;

        ViewHolder(View itemView) {
            super(itemView);
            shijianView = ((TextView) itemView.findViewById(R.id.shijian_view));
            zhuangtaiView = ((TextView) itemView.findViewById(R.id.zhuangtai_view));
            leaveLayout = ((FrameLayout) itemView.findViewById(R.id.leave_layout));
        }
    }

    public interface OnLeaveFlowItemClick{
        void onLeaveFlowItemClickListener(LeaveFlow leaveFlow);
    }
}
