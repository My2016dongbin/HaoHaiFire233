package com.haohai.platform.mapmodel.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haohai.platform.mapmodel.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/12/3.
 */
public class OneBodyFireViewBinder extends ItemViewProvider<OneBodyFire, OneBodyFireViewBinder.ViewHolder> {

    public OnOneBodyItemClick listener;

    public void setListener(OnOneBodyItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_one_body_fire, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final OneBodyFire oneBodyFire) {
        holder.textView1.setText("监控点名称 : " + oneBodyFire.getName());
        holder.textView2.setText("发现时间 : " + oneBodyFire.getAlarmDatetime().replace("T"," ").substring(0,oneBodyFire.getAlarmDatetime().indexOf(".")));
        holder.textView3.setText("经度、纬度 : " + oneBodyFire.getAlarmLongitude() +"、" + oneBodyFire.getAlarmLatitude());
        if (oneBodyFire.getAddress()==null) {
            holder.textView4.setVisibility(View.GONE);
        }else {
            holder.textView4.setVisibility(View.VISIBLE);
            holder.textView4.setText("详细地址 : " +  oneBodyFire.getAddress() );
        }


        RxViewAction.clickNoDouble(holder.oneBodyLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onOneBodyItemClickListener(oneBodyFire);
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView1;
        private final TextView textView2;
        private final TextView textView3;
        private final TextView textView4;
        private final FrameLayout oneBodyLayout;

        ViewHolder(View itemView) {
            super(itemView);
            textView1 = ((TextView) itemView.findViewById(R.id.text_view1));
            textView2 = ((TextView) itemView.findViewById(R.id.text_view2));
            textView3 = ((TextView) itemView.findViewById(R.id.text_view3));
            textView4 = ((TextView) itemView.findViewById(R.id.text_view4));
            oneBodyLayout = ((FrameLayout) itemView.findViewById(R.id.one_body_layout));
        }
    }
    public interface OnOneBodyItemClick{
        void onOneBodyItemClickListener(OneBodyFire oneBodyFire);
    }

}
