package com.haohai.platform.mapmodel.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haohai.platform.mapmodel.R;
import com.haohai.platform.mapmodel.model.WeixingModel;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/11/20.
 */
public class WeixingModelViewBinder extends ItemViewProvider<WeixingModel, WeixingModelViewBinder.ViewHolder> {
    public OnWeixingInfoItemClick listener;

    public void setListener(OnWeixingInfoItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_weixing_model, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final WeixingModel weixingModel) {
        if (weixingModel.getCity().equals("[]")){
            holder.addressText.setText(weixingModel.getProvince() + " " + weixingModel.getCounty());
        }else {
            holder.addressText.setText(weixingModel.getProvince() + " " + weixingModel.getCity() + " " + weixingModel.getCounty());
        }
        if (holder.addressText.getText().toString().equals(" ") || holder.addressText.getText().toString().equals("  ")){
            holder.addressText.setText("边境热源");
        }

        if (weixingModel.getFireListType() == 1){       //时间分类
            holder.fireTimeText.setText(weixingModel.getObservationDatetime().replace("T"," ").substring(0,weixingModel.getObservationDatetime().indexOf(".")));
        }else {         //编号分类
            holder.fireTimeText.setText(weixingModel.getFireNo());
        }

        if (weixingModel.isShowTime){
            holder.fireTimeLayout.setVisibility(View.VISIBLE);
        }else {
            holder.fireTimeLayout.setVisibility(View.GONE);
        }
        if (weixingModel.isShowLine){
            holder.lineView.setVisibility(View.VISIBLE);
        }else {
            holder.lineView.setVisibility(View.GONE);
        }
        RxViewAction.clickNoDouble(holder.fireLyout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onWeixingInfoClick(weixingModel);
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView addressText;
        private final TextView fireTimeText;
        private final LinearLayout fireTimeLayout;
        private final View lineView;
        private final LinearLayout fireLyout;

        ViewHolder(View itemView) {
            super(itemView);
            addressText = ((TextView) itemView.findViewById(R.id.address_Text));
            fireTimeText = ((TextView) itemView.findViewById(R.id.fire_time_text));
            fireTimeLayout = ((LinearLayout) itemView.findViewById(R.id.fire_time_layout));
            lineView = ((View) itemView.findViewById(R.id.fire_line));
            fireLyout = ((LinearLayout) itemView.findViewById(R.id.fire_layout));
        }
    }
    public interface OnWeixingInfoItemClick{
        void onWeixingInfoClick(WeixingModel weixingModel);
    }
}
