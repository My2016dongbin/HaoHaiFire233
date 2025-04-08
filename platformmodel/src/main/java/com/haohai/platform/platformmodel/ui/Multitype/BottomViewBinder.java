package com.haohai.platform.platformmodel.ui.Multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haohai.platform.platformmodel.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/3/27.
 */
public class BottomViewBinder extends ItemViewProvider<Bottom, BottomViewBinder.ViewHolder> {

    public OnBottomItemClick listener;

    public void setListener(OnBottomItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_bottom, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Bottom bottom) {

        holder.bottomView.setText(bottom.getBottomStr());
        RxViewAction.clickNoDouble(holder.bottomView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onBottomItemClickListener();
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView bottomView;

        ViewHolder(View itemView) {
            super(itemView);
            bottomView = ((TextView) itemView.findViewById(R.id.bottom_view));
        }
    }
    public interface OnBottomItemClick{
        void onBottomItemClickListener();
    }
}
