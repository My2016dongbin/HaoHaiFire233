package com.haohai.platform.platformmodel.ui.Multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haohai.platform.platformmodel.R;

import me.drakeet.multitype.ItemViewProvider;


/**
 * Created by geyang on 2020/7/15.
 */
public class FlowApproveTopViewBinder extends ItemViewProvider<FlowApproveTop, FlowApproveTopViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_flow_approve_top, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull FlowApproveTop flowApproveTop) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
