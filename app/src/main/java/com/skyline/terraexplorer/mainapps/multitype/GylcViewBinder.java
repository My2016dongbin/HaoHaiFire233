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
public class GylcViewBinder extends ItemViewBinder<Gylcjd, GylcViewBinder.ViewHolder> {

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
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Gylcjd gylcjd) {
        holder.textView1.setText("地区: " + gylcjd.getRegion() );
        holder.textView2.setText("检查人: " + gylcjd.getSignatureMonitor() );
        holder.textView3.setText("负责人: " + gylcjd.getHead() );
        holder.textView4.setText("电话: " + gylcjd.getPhone() );
        holder.textView5.setText("厂方人员签字: " + gylcjd.getSignatureHead() );
        holder.textView6.setText("地址: " + gylcjd.getAddress() );
        String jcTime = gylcjd.getInspectionTime()==null ? "" : gylcjd.getInspectionTime();
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
