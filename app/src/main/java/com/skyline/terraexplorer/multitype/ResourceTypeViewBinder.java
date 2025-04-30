package com.skyline.terraexplorer.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.utils.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/1/19.
 */
public class ResourceTypeViewBinder extends ItemViewProvider<ResourceType, ResourceTypeViewBinder.ViewHolder> {

    public OnResourceTypeItemClick listener;

    public void setListener(OnResourceTypeItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_resource_type, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ResourceType resourceType) {
        if (resourceType.isOpen){
            holder.openImage.setImageResource(R.drawable.ic_open_down);
        }else {
            holder.openImage.setImageResource(R.drawable.ic_close_up);
        }

        holder.resourceTypeView.setText(resourceType.getResourceTypeName());
        RxViewAction.clickNoDouble(holder.resourceTypeLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (resourceType.isOpen){
                            listener.onResourceTypeItemClickListener(resourceType.getResourceTypeName(),false);
                        }else {
                            listener.onResourceTypeItemClickListener(resourceType.getResourceTypeName(),true);
                        }

                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView resourceTypeView;
        private final FrameLayout resourceTypeLayout;
        private final ImageView openImage;

        ViewHolder(View itemView) {
            super(itemView);
            resourceTypeView = ((TextView) itemView.findViewById(R.id.resource_type_view));
            resourceTypeLayout = ((FrameLayout) itemView.findViewById(R.id.resource_type_layout));
            openImage = ((ImageView) itemView.findViewById(R.id.open_image));
        }
    }

    public interface OnResourceTypeItemClick{
        void onResourceTypeItemClickListener(String resourceTypeName,boolean isOpen);
    }
}
