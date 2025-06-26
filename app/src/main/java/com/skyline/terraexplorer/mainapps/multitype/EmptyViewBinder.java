package com.skyline.terraexplorer.mainapps.multitype;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;

import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by geyang on 2020/3/28.
 */
public class EmptyViewBinder extends ItemViewBinder<Empty, EmptyViewBinder.ViewHolder> {

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
