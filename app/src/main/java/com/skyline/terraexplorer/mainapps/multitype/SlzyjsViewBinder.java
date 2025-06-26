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
public class SlzyjsViewBinder extends ItemViewBinder<Slzyjsjd, SlzyjsViewBinder.ViewHolder> {

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
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Slzyjsjd slzyjsjd) {
        holder.textView1.setText("县: " + slzyjsjd.getCounty() );
        holder.textView2.setText("乡镇: " + slzyjsjd.getTown() );
        holder.textView3.setText("村: " + slzyjsjd.getVillage() );
        holder.textView4.setText("面积: " + String.valueOf(slzyjsjd.getArea()) );
        holder.textView5.setText("林种: " + slzyjsjd.getForestCategory() );
        holder.textView6.setText("森林类别: " + slzyjsjd.getForestType());
        holder.textView7.setVisibility(View.GONE);
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
