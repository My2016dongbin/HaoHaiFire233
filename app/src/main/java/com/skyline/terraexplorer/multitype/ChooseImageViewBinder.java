package com.skyline.terraexplorer.multitype;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.utils.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/2/6.
 */
public class ChooseImageViewBinder extends ItemViewProvider<ChooseImage, ChooseImageViewBinder.ViewHolder> {
    public Context context;
    public OnChooseImageClickListener listener;

    public ChooseImageViewBinder(Context context) {
        this.context = context;
    }

    public void setListener(OnChooseImageClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_choose_image, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ChooseImage chooseImage) {

        if (chooseImage.isAdd.booleanValue()){
            holder.imageView.setImageResource(R.drawable.ic_bigphoto);
            holder.imageDelete.setVisibility(View.GONE);
        }else {
            if (chooseImage.isNetPic){  //加载网络图片
                holder.imageDelete.setVisibility(View.GONE);
                Glide.with(context).load(chooseImage.getPicStr()).error(R.drawable.ic_no_pic)
                        .placeholder(R.drawable.ic_jaizai).into(holder.imageView);
            }else {
                holder.imageDelete.setVisibility(View.VISIBLE);
                Glide.with(context).load(chooseImage.getUri()).error(R.drawable.ic_no_pic)
                        .placeholder(R.drawable.ic_jaizai).into(holder.imageView);
            }
        }

        RxViewAction.clickNoDouble(holder.imageView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onImageAddClickListener(chooseImage.isAdd.booleanValue(),chooseImage.getUri(),chooseImage.getuCheckId(),chooseImage);
                    }
                });
        RxViewAction.clickNoDouble(holder.imageDelete)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onImageDelete(chooseImage.getUri(),chooseImage.getuCheckId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final ImageView imageDelete;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = ((ImageView) itemView.findViewById(R.id.evaluate_item_image));
            imageDelete = ((ImageView) itemView.findViewById(R.id.evaluate_item_delete));

        }
    }
    public interface OnChooseImageClickListener {
        void onImageAddClickListener(boolean var1, Uri var2,String id,ChooseImage chooseImage);
        void onImageDelete(Uri var1,String id);
    }
}
