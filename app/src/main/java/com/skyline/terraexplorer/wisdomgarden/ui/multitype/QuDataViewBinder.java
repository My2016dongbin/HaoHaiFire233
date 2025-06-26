package com.skyline.terraexplorer.wisdomgarden.ui.multitype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewBinder;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/12/8.
 */
public class QuDataViewBinder extends ItemViewBinder<QuData, QuDataViewBinder.ViewHolder> {

    public OnQuItemClck listener;

    public void setListener(OnQuItemClck listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_qu_data, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull QuData quData) {
        if (quData.isCheck){
            holder.checkImage.setImageResource(R.drawable.ic_radio1);
        }else {
            holder.checkImage.setImageResource(R.drawable.ic_radio);
        }
        holder.quView.setText(quData.getQuName());

        RxViewAction.clickNoDouble(holder.quLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onQuItemClickListener(quData);
                    }
                });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView checkImage;
        private final TextView quView;
        private final LinearLayout quLayout;

        ViewHolder(View itemView) {
            super(itemView);
            checkImage = ((ImageView) itemView.findViewById(R.id.check_image));
            quView = ((TextView) itemView.findViewById(R.id.qu_view));
            quLayout = ((LinearLayout) itemView.findViewById(R.id.qu_layout));
        }
    }
    public interface OnQuItemClck {
        void onQuItemClickListener(QuData quData);
    }
}
