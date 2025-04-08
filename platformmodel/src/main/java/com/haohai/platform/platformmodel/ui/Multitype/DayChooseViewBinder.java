package com.haohai.platform.platformmodel.ui.Multitype;

import android.content.Context;
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
 * Created by geyang on 2020/6/5.
 */
public class DayChooseViewBinder extends ItemViewProvider<DayChoose, DayChooseViewBinder.ViewHolder> {

    private Context context;
    public OnDateChooseClick listener;

    public DayChooseViewBinder(Context context) {
        this.context = context;
    }


    public void setListener(OnDateChooseClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_day_choose, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final DayChoose dayChoose) {

        holder.dateView.setText(dayChoose.getMonthDay() + " " + dayChoose.getWeek());
        if (dayChoose.isChoose){
            holder.dateView.setTextColor(context.getResources().getColor(R.color.c24));
        }else {
            holder.dateView.setTextColor(context.getResources().getColor(R.color.c71));
        }
        RxViewAction.clickNoDouble(holder.dateView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onDateChooseClickListener(dayChoose.getId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dateView;

        ViewHolder(View itemView) {
            super(itemView);
            dateView = ((TextView) itemView.findViewById(R.id.date_view));
        }
    }
    public interface OnDateChooseClick{
        void onDateChooseClickListener(int id);
    }
}
