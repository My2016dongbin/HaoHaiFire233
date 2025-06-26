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
 * Created by geyang on 2019/11/25.
 */
public class FamousViewBinder extends ItemViewBinder<YllhBarList.ChildrenDataFirejd, FamousViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_famous_bar_list, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final YllhBarList.ChildrenDataFirejd childrenDataFirejd) {
        //holder.numView.setText(String.valueOf(childrenDataFirejd.getEachCount()));
        holder.dqView.setText(childrenDataFirejd.getEachAreaName());
        holder.mjView.setText(String.valueOf(childrenDataFirejd.getOldCount()));
        holder.mmView.setText(String.valueOf(childrenDataFirejd.getFamousCount()));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView numView;
        private final TextView dqView;
        private final TextView mjView;
        private final TextView mmView;
        ViewHolder(View itemView) {
            super(itemView);
            numView = ((TextView) itemView.findViewById(R.id.num_txt));
            dqView = ((TextView) itemView.findViewById(R.id.dq_txt));
            mjView = ((TextView) itemView.findViewById(R.id.mj_txt));
            mmView = ((TextView) itemView.findViewById(R.id.mm_txt));
        }
    }
}
