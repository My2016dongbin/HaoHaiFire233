package com.hht.hsatellitemobile.ui.multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hht.hsatellitemobile.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

/**
 * Created by geyang on 2019/12/16.
 */
public class GroundFireViewBinder extends ItemViewProvider<GroundFire, GroundFireViewBinder.ViewHolder> {

    public Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public OnGroundFireInfoItemClick listener;

    public void setListener(OnGroundFireInfoItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_ground_fire, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GroundFire groundFire) {
        if (groundFire.getIsDispose().equals("1")){     //已处理
            holder.stateView.setText("已处理");
        }else {
            holder.stateView.setText("待处理");
        }
        holder.lngView.setText(groundFire.getAlarmLongitude());
        holder.latVIEW.setText(groundFire.getAlarmLatitude());
        holder.dataView.setText(groundFire.getAlarmDateTime());
        RxViewAction.clickNoDouble(holder.firelayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onGroundFireInfoClick(groundFire);
                    }
                });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView lngView;
        private final TextView latVIEW;
        private final TextView dataView;
        private final LinearLayout firelayout;
        private final TextView stateView;

        ViewHolder(View itemView) {
            super(itemView);
            lngView = ((TextView) itemView.findViewById(R.id.lng_view));
            latVIEW = ((TextView) itemView.findViewById(R.id.lat_view));
            dataView = ((TextView) itemView.findViewById(R.id.date_view));
            firelayout = ((LinearLayout) itemView.findViewById(R.id.fire_layout));
            stateView = ((TextView) itemView.findViewById(R.id.state_view));
        }
    }

    public interface OnGroundFireInfoItemClick{
        void onGroundFireInfoClick(GroundFire groundFire);
    }
}
