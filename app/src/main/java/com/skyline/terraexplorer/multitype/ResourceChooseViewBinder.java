package com.skyline.terraexplorer.multitype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.utils.RxViewAction;

import me.drakeet.multitype.ItemViewBinder;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/3/20.
 */
public class ResourceChooseViewBinder extends ItemViewBinder<Resource, ResourceChooseViewBinder.ViewHolder> {

    public OnResourceChooseItemClick listener;

    public void setListener(OnResourceChooseItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_resource_choose, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Resource resource) {
        if (resource.isChoose()){
            holder.resourceChooseView.setImageResource(R.drawable.ic_choose_resource);
        }else {
            holder.resourceChooseView.setImageResource(R.drawable.ic_choose_resource_no);
        }
        holder.resourceNameView.setText(resource.getName());

        RxViewAction.clickNoDouble(holder.resourceLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onResourceChooseItemClickListener(resource.getId(),resource.isChoose() ? false:true);
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout resourceLayout;
        private final ImageView resourceChooseView;
        private final TextView resourceNameView;

        ViewHolder(View itemView) {
            super(itemView);
            resourceLayout = ((LinearLayout) itemView.findViewById(R.id.resource_layout));
            resourceChooseView = ((ImageView) itemView.findViewById(R.id.resouce_choose_view));
            resourceNameView = ((TextView) itemView.findViewById(R.id.resouce_name_view));
        }
    }
    public interface OnResourceChooseItemClick{
        void  onResourceChooseItemClickListener(String id, boolean isChoose);
    }
}
