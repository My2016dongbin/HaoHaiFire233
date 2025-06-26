package com.skyline.terraexplorer.wisdomgarden.ui.multitype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewBinder;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/10/23.
 */
public class ParkViewBinder extends ItemViewBinder<Park, ParkViewBinder.ViewHolder> {
    public OnPatkItemClck listener;

    public void setListener(OnPatkItemClck listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_park, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Park park) {
        holder.parkNameView.setText("公园名称:" + park.getName());
        String area = park.getArea()==null?" ": park.getArea()+"(公顷)";
        holder.parkQuView.setText("公园面积:" +area);
        String address = park.getAddress()==null ? "" : park.getAddress();
        holder.parkWeizhiView.setText("公园位置:" + park.getAddress());

        RxViewAction.clickNoDouble(holder.parkButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onParkItemClickListener(park.getId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView parkNameView;
        private final TextView parkQuView;
        private final TextView parkWeizhiView;
        private final TextView parkButton;

        ViewHolder(View itemView) {
            super(itemView);
            parkNameView = ((TextView) itemView.findViewById(R.id.park_name_view));
            parkQuView = ((TextView) itemView.findViewById(R.id.park_qu_view));
            parkWeizhiView = ((TextView) itemView.findViewById(R.id.park_weizhi_view));
            parkButton = ((TextView) itemView.findViewById(R.id.park_button));
        }
    }

    public interface OnPatkItemClck{
        void onParkItemClickListener(String id);
    }
}
