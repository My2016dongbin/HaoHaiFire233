package com.hht.hsatellitemobile.ui.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.hht.hsatellitemobile.R;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by 13589 on 2019/8/7.
 */
public class FireInfoViewBinder extends ItemViewProvider<FireInfo, FireInfoViewBinder.ViewHolder> {
    public OnFireInfoItemClick listener;

    public void setListener(OnFireInfoItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_fire_info, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final FireInfo fireInfo) {
        if (fireInfo.getCity().equals("[]")){
            holder.addressText.setText(fireInfo.getProvince() + " " + fireInfo.getCounty());
        }else {
            holder.addressText.setText(fireInfo.getProvince() + " " + fireInfo.getCity() + " " + fireInfo.getCounty());
        }
        if (holder.addressText.getText().toString().equals(" ") || holder.addressText.getText().toString().equals("  ")){
            holder.addressText.setText("边境热源");
        }

        if (fireInfo.getFireListType() == 1){       //时间分类
            holder.fireTimeText.setText(fireInfo.getObservationDateTime().replace("T"," "));
        }else {         //编号分类
            holder.fireTimeText.setText(fireInfo.getFireNo());
        }

        if (fireInfo.isShowTime){
            holder.fireTimeLayout.setVisibility(View.VISIBLE);
        }else {
            holder.fireTimeLayout.setVisibility(View.GONE);
        }
        if (fireInfo.isShowLine){
            holder.lineView.setVisibility(View.VISIBLE);
        }else {
            holder.lineView.setVisibility(View.GONE);
        }
        RxViewAction.clickNoDouble(holder.fireLyout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onFireInfoClick(fireInfo);
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

    public interface OnFireInfoItemClick{
        void onFireInfoClick(FireInfo fireInfo);
    }
}
