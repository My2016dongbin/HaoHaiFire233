package com.skyline.terraexplorer.multitype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.utils.RxViewAction;

import io.reactivex.annotations.NonNull;
import me.drakeet.multitype.ItemViewBinder;
import rx.functions.Action1;


/**
 * Created by geyang on 2019/12/26.
 */
public class PlanViewBinder extends ItemViewBinder<Plan, PlanViewBinder.ViewHolder> {
    private OnPlanCheckItemClick listener;

    public void setListener(OnPlanCheckItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_plan, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Plan plan) {
        holder.nameView.setText(plan.getResourceName());
        holder.jingduView.setText(plan.getLng() + " ");
        holder.weiduView.setText(plan.getLat() + " ");
        holder.addressView.setText(plan.districtName+plan.streetName+plan.streetNo);
        RxViewAction.clickNoDouble(holder.yinhuanView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onPlanCheckItemButtonClick(0,plan);
                    }
                });
        RxViewAction.clickNoDouble(holder.checkView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onPlanCheckItemButtonClick(1,plan);
                    }
                });
        RxViewAction.clickNoDouble(holder.daohangView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onPlanCheckItemButtonClick(2,plan);
                    }
                });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameView;
        private final TextView jingduView;
        private final TextView weiduView;
        private final TextView addressView;
        private final TextView yinhuanView;
        private final TextView checkView;
        private final TextView daohangView;

        ViewHolder(View itemView) {
            super(itemView);
            nameView = ((TextView) itemView.findViewById(R.id.name_view));
            jingduView = ((TextView) itemView.findViewById(R.id.jingdu_view));
            weiduView = ((TextView) itemView.findViewById(R.id.weidu_view));
            addressView = ((TextView) itemView.findViewById(R.id.address_view));
            yinhuanView = ((TextView) itemView.findViewById(R.id.yinhuan_view));
            checkView = ((TextView) itemView.findViewById(R.id.check_button));
            daohangView = ((TextView) itemView.findViewById(R.id.daohang_button));
        }
    }

    public interface OnPlanCheckItemClick{
        void onPlanCheckItemButtonClick(int state,Plan plan);  //state 0隐患排查 1资源检查  2导航
    }
}
