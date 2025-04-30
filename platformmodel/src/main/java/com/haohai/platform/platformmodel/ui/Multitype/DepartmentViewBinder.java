package com.haohai.platform.platformmodel.ui.Multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haohai.platform.platformmodel.R;
import com.ruyiruyi.rylibrary.db.Department;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/7/13.
 */
public class DepartmentViewBinder extends ItemViewProvider<Department, DepartmentViewBinder.ViewHolder> {

    public OnBumenItemClick listener;

    public void setListener(OnBumenItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_department, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Department department) {
        holder.bumenView.setText(department.getName());
        RxViewAction.clickNoDouble(holder.bumenLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onBumenItemClick(department);
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView bumenView;
        private final LinearLayout bumenLayout;

        ViewHolder(View itemView) {
            super(itemView);
            bumenView = ((TextView) itemView.findViewById(R.id.bumen_view));
            bumenLayout = ((LinearLayout) itemView.findViewById(R.id.bumen_layout));
        }
    }
    public interface OnBumenItemClick{
        void onBumenItemClick(Department department);
    }
}
