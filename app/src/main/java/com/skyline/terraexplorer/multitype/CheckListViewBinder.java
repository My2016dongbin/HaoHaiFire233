package com.skyline.terraexplorer.multitype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.utils.RxViewAction;

import me.drakeet.multitype.ItemViewBinder;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/4/7.
 */
public class CheckListViewBinder extends ItemViewBinder<CheckList, CheckListViewBinder.ViewHolder> {

    public OnCheckListItemClick listener;

    public void setListener(OnCheckListItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_check_list, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final CheckList checkList) {
        holder.checkNameView.setText(checkList.getCheckName());
        holder.checkTimeView.setText(checkList.getCheckTime().substring(0,checkList.getCheckTime().indexOf(".")).replace("T"," "));
        holder.checkMenView.setText(checkList.getCheckMen());
        RxViewAction.clickNoDouble(holder.checkLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onCheckListItemClickListener(checkList.getId());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView checkNameView;
        private final TextView checkTimeView;
        private final TextView checkMenView;
        private final FrameLayout checkLayout;

        ViewHolder(View itemView) {
            super(itemView);
            checkNameView = ((TextView) itemView.findViewById(R.id.check_name_view));
            checkTimeView = ((TextView) itemView.findViewById(R.id.check_time_view));
            checkMenView = ((TextView) itemView.findViewById(R.id.check_men_view));
            checkLayout = ((FrameLayout) itemView.findViewById(R.id.check_layout));

        }
    }

    public interface OnCheckListItemClick{
        void onCheckListItemClickListener(String id);
    }
}
