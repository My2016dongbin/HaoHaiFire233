package com.haohai.platform.mapmodel.multitype;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haohai.platform.mapmodel.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/12/3.
 */
public class ResourceListViewBinder extends ItemViewProvider<ResourceList, ResourceListViewBinder.ViewHolder> {

    public OnResourceLsitItemClick listener;

    public void setListener(OnResourceLsitItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_resourcelist, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ResourceList resourceList) {
        holder.nameView.setText(resourceList.getName());
        if (resourceList.isCheck()){
            holder.checkView.setChecked(true);
        }else {
            holder.checkView.setChecked(false);
        }

        RxViewAction.clickNoDouble(holder.resourceLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onResourceListItemClickListener(resourceList);
                    }
                });
        RxViewAction.clickNoDouble(holder.checkView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onResourceListItemClickListener(resourceList);
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        private final CheckBox checkView;
        private final TextView nameView;
        private final LinearLayout resourceLayout;

        ViewHolder(View itemView) {
            super(itemView);
            checkView = ((CheckBox) itemView.findViewById(R.id.check_view));
            nameView = ((TextView) itemView.findViewById(R.id.tv_name));
            resourceLayout = ((LinearLayout) itemView.findViewById(R.id.resource_layout));
        }
    }
    public interface OnResourceLsitItemClick{
        void onResourceListItemClickListener(ResourceList resourceList);
    }

}
