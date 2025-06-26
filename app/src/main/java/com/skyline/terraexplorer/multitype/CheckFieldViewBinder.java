package com.skyline.terraexplorer.multitype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.db.CheckField;

import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by geyang on 2019/11/25.
 */
public class CheckFieldViewBinder extends ItemViewBinder<CheckField, CheckFieldViewBinder.ViewHolder> {

    public OnCheckFieldItemClick listener;

    public void setListener(OnCheckFieldItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_check_field, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final CheckField checkField) {
        holder.nameView.setText(checkField.getName());
        if (checkField.state == 0){
            holder.switchButton.setChecked(false);
        }else {
            holder.switchButton.setChecked(true);
        }
        holder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onCheckFieldItemClickLinstener(checkField.getId(),holder.switchButton.isChecked() ? 1 : 0);
            }
        });


    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final Switch switchButton;
        private final TextView nameView;

        ViewHolder(View itemView) {
            super(itemView);
            switchButton = ((Switch) itemView.findViewById(R.id.swith_button));
            nameView = ((TextView) itemView.findViewById(R.id.check_field_name_view));
        }
    }

    public interface OnCheckFieldItemClick{
        void onCheckFieldItemClickLinstener(String id,int state);
    }
}
