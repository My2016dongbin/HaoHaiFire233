package com.skyline.terraexplorer.mainapps.multitype;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.mainapps.utils.RxViewAction;
import com.skyline.terraexplorer.R;

import me.drakeet.multitype.ItemViewBinder;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/12/2.
 */
public class BsfjViewBinder extends ItemViewBinder<Bsfjjd, BsfjViewBinder.ViewHolder> {

    public OnBsxxItemClick listener;

    public void setListener(OnBsxxItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_bsxx_mission, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Bsfjjd bsfjjd) {
        holder.textView1.setText("名称: " + bsfjjd.getSectionName() );
        holder.textView2.setText("长度: " + bsfjjd.getSectionLength() );
        String jcTime = bsfjjd.getSectionTime()==null ? "" : bsfjjd.getSectionTime();
        holder.textView3.setText("分节时间: " + jcTime);
        holder.textView4.setText("分节人: " + bsfjjd.getSectionPerson() );
        holder.textView5.setText("树木位置: " + bsfjjd.getTreeLocation() );
        holder.textView6.setText("分节地点: " + bsfjjd.getSectionLocation() );
        holder.textView7.setText("备注: " + bsfjjd.getRemark());
        holder.textView8.setVisibility(View.GONE);
        holder.textView9.setVisibility(View.GONE);
        RxViewAction.clickNoDouble(holder.orderLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e("TAG", "call: "+bsfjjd.getId() );
                        listener.onBsxxItemClickListener(bsfjjd.getId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView1;
        private final TextView textView2;
        private final TextView textView3;
        private final TextView textView4;
        private final TextView textView5;
        private final TextView textView6;
        private final TextView textView7;
        private final TextView textView8;
        private final TextView textView9;
        private final FrameLayout orderLayout;
        ViewHolder(View itemView) {
            super(itemView);
            textView1 = ((TextView) itemView.findViewById(R.id.text_view1));
            textView2 = ((TextView) itemView.findViewById(R.id.text_view2));
            textView3 = ((TextView) itemView.findViewById(R.id.text_view3));
            textView4 = ((TextView) itemView.findViewById(R.id.text_view4));
            textView5 = ((TextView) itemView.findViewById(R.id.text_view5));
            textView6 = ((TextView) itemView.findViewById(R.id.text_view6));
            textView7 = ((TextView) itemView.findViewById(R.id.text_view7));
            textView8 = ((TextView) itemView.findViewById(R.id.text_view8));
            textView9 = ((TextView) itemView.findViewById(R.id.text_view9));
            orderLayout= itemView.findViewById(R.id.order_layout1);
        }
    }

    public interface OnBsxxItemClick{
        void onBsxxItemClickListener(String bsxxjd);
    }
}
