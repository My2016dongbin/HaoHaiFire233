package com.skyline.terraexplorer.mainapps.multitype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.multitype.Lcpbar;

import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by geyang on 2019/11/25.
 */
public class LcpbarViewBinder extends ItemViewBinder<Lcpbar, LcpbarViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_bar_list, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Lcpbar lcpbar) {
        holder.dqView.setText(String.valueOf(lcpbar.getRegion()));
        holder.numView.setText(String.valueOf(lcpbar.getAnnualOutput()));
        holder.mjView.setText(String.valueOf(lcpbar.getArea()));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView numView;
        private final TextView dqView;
        private final TextView mjView;
        ViewHolder(View itemView) {
            super(itemView);
            numView = ((TextView) itemView.findViewById(R.id.num_txt));
            dqView = ((TextView) itemView.findViewById(R.id.dq_txt));
            mjView = ((TextView) itemView.findViewById(R.id.mj_txt));
        }
    }
}
