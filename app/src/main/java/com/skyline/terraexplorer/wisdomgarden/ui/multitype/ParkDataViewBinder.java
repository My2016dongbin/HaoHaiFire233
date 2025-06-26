package com.skyline.terraexplorer.wisdomgarden.ui.multitype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewBinder;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/12/8.
 */
public class ParkDataViewBinder extends ItemViewBinder<ParkData, ParkDataViewBinder.ViewHolder> {

    public OnDataItemClck listener;

    public void setListener(OnDataItemClck listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_park_data, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ParkData parkData) {
        holder.dataView.setText(parkData.getDataName());
        if (parkData.isCheck){
            holder.checkImage.setImageResource(R.drawable.ic_checked);
        }else {
            holder.checkImage.setImageResource(R.drawable.ic_check);
        }

        RxViewAction.clickNoDouble(holder.dataLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onDataItemClickListener(parkData);
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView checkImage;
        private final TextView dataView;
        private final LinearLayout dataLayout;

        ViewHolder(View itemView) {
            super(itemView);
            checkImage = ((ImageView) itemView.findViewById(R.id.check_image));
            dataView = ((TextView) itemView.findViewById(R.id.data_view));
            dataLayout = ((LinearLayout) itemView.findViewById(R.id.data_layout));
        }
    }
    public interface OnDataItemClck {
        void onDataItemClickListener(ParkData parkData);
    }
}
