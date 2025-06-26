package com.skyline.terraexplorer.multitype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.utils.RxViewAction;

import java.text.DecimalFormat;

import me.drakeet.multitype.ItemViewBinder;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/1/19.
 */
public class ResourceViewBinder extends ItemViewBinder<Resource, ResourceViewBinder.ViewHolder> {

    public OnResourceItemClick listener;

    public void setListener(OnResourceItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_resource, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Resource resource) {

        if (resource.getResourceName().equals("专业队")) {
            holder.checkButton.setVisibility(View.VISIBLE);
        }else if (resource.getResourceName().equals("护林检查站")) {
            holder.checkButton.setVisibility(View.VISIBLE);
        }else if (resource.getResourceName().equals("指挥部")) {
            holder.checkButton.setVisibility(View.VISIBLE);
        }else if (resource.getResourceName().equals("物资库")) {
            holder.checkButton.setVisibility(View.VISIBLE);
        }else  {
            holder.checkButton.setVisibility(View.GONE);
        }
        RxViewAction.clickNoDouble(holder.checkButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onItemCheckClickListener(resource);
                    }
                });
        if (resource.isChoose){
            holder.checkView.setImageResource(R.drawable.ic_checked);
        }else {
            holder.checkView.setImageResource(R.drawable.ic_uncheck);
        }
        holder.nameView.setText(resource.getName());
        DecimalFormat df   = new DecimalFormat("######0.00");
        holder.postiion_view.setText("位置:" + df.format(resource.getLat()) + "," +  df.format(resource.getLng()));
        RxViewAction.clickNoDouble(holder.daohangButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onDaoHangClickListener(resource);
                    }
                });
        RxViewAction.clickNoDouble(holder.orderLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (resource.isChoose){
                            listener.onItemChooseClickListener(resource,false);
                        }else {
                            listener.onItemChooseClickListener(resource,true);
                        }

                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView postiion_view;
        private final TextView daohangButton;
        private final ImageView checkView;
        private final FrameLayout orderLayout;
        private final TextView checkButton;

        ViewHolder(View itemView) {
            super(itemView);
            nameView = ((TextView) itemView.findViewById(R.id.name_view));
            postiion_view = ((TextView) itemView.findViewById(R.id.postiion_view));
            daohangButton = ((TextView) itemView.findViewById(R.id.daohang_button));
            checkView = ((ImageView) itemView.findViewById(R.id.check_view));
            orderLayout = ((FrameLayout) itemView.findViewById(R.id.order_layout));
            checkButton = ((TextView) itemView.findViewById(R.id.check_button));

        }
    }

    public interface OnResourceItemClick{
        void onDaoHangClickListener(Resource resource);
        void onItemChooseClickListener(Resource resource,boolean isChoose);
        void onItemCheckClickListener(Resource resource);
    }
}
