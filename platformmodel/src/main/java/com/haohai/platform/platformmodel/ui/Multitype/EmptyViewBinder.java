package com.haohai.platform.platformmodel.ui.Multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.haohai.platform.platformmodel.R;

import me.drakeet.multitype.ItemViewProvider;


/**
 * Created by geyang on 2020/3/28.
 */
public class EmptyViewBinder extends ItemViewProvider<Empty, EmptyViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_empty, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Empty empty) {
        holder.emptyView.setText(empty.getStr());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView emptyView;

        ViewHolder(View itemView) {
            super(itemView);
            emptyView = ((TextView) itemView.findViewById(R.id.empty_view));
        }
    }
}
