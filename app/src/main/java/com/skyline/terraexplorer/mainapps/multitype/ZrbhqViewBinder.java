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
 * Created by geyang on 2020/12/2.
 */
public class ZrbhqViewBinder extends ItemViewBinder<Zrbhqjd, ZrbhqViewBinder.ViewHolder> {

    public OnFireMissionItemClick listener;

    public void setListener(OnFireMissionItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_fire_mission, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Zrbhqjd zrbhqjd) {
        holder.textView1.setText("地区: " + zrbhqjd.getRegion() );
        holder.textView2.setText("检查人: " + zrbhqjd.getSignatureMonitor() );
        holder.textView3.setText("负责人: " + zrbhqjd.getHead() );
        holder.textView4.setText("电话: " + zrbhqjd.getPhone() );
        holder.textView5.setText("自然保护区: " + zrbhqjd.getSignatureNatureReserveUnits() );
        holder.textView6.setText("地址: " + zrbhqjd.getAddress() );
        String jcTime = zrbhqjd.getInspectionTime()==null ? "" : zrbhqjd.getInspectionTime();
        holder.textView7.setText("检查时间: " + jcTime);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView1;
        private final TextView textView2;
        private final TextView textView3;
        private final TextView textView4;
        private final TextView textView5;
        private final TextView textView6;
        private final TextView textView7;
        ViewHolder(View itemView) {
            super(itemView);
            textView1 = ((TextView) itemView.findViewById(R.id.text_view1));
            textView2 = ((TextView) itemView.findViewById(R.id.text_view2));
            textView3 = ((TextView) itemView.findViewById(R.id.text_view3));
            textView4 = ((TextView) itemView.findViewById(R.id.text_view4));
            textView5 = ((TextView) itemView.findViewById(R.id.text_view5));
            textView6 = ((TextView) itemView.findViewById(R.id.text_view6));
            textView7 = ((TextView) itemView.findViewById(R.id.text_view7));
        }
    }

    public interface OnFireMissionItemClick{
        void onFireMissionItemClickListener(Firejd firejd);
    }
}
