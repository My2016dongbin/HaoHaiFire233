package com.haohai.haohai.address.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haohai.haohai.address.R;
import com.haohai.haohai.address.modle.AddressModel;

import java.util.Arrays;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by geyang on 2019/9/16.
 */
public class AddressViewBinder extends ItemViewProvider<AddressMul, AddressViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_address, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull AddressMul address) {

        holder.timeView.setText(address.getInsertTime().replace("T", " "));
        holder.jingWeiView.setText(address.getLongitude()+ "  " +address.getLatitude());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView timeView;
        private final TextView jingWeiView;

        ViewHolder(View itemView) {
            super(itemView);
            timeView =  ((TextView)itemView.findViewById(R.id.time_view));
            jingWeiView = ((TextView) itemView.findViewById(R.id.jingwei_view));
        }
    }
}
