package com.skyline.terraexplorer.mainapps.multitype;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;

import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.multitype.ItemViewBinder;


/**
 * Created by geyang on 2019/11/25.
 */
public class CheckslyeditViewBinder extends ItemViewBinder<Checksyledit, CheckslyeditViewBinder.ViewHolder> {

    public OnChecksylItemClick listener;
    private Timer timer = new Timer();
    private final long DELAY = 1000;
    public void setListener(OnChecksylItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_check_syledit, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Checksyledit checksyledit) {
        holder.nameView.setText(checksyledit.getName());
        holder.sylEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            //输入时的调用
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(timer != null)
                    timer.cancel();
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 1) {

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            listener.onChecksylItemClickLinstener(checksyledit.getId(),holder.sylEdit.getText().toString());
                        }

                    }, DELAY);
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameView;
        private final EditText sylEdit;
        ViewHolder(View itemView) {
            super(itemView);
            nameView = ( itemView.findViewById(R.id.check_sly_name_view));
            sylEdit= ( itemView.findViewById(R.id.syl_edit));
        }
    }

    public interface OnChecksylItemClick{
        void onChecksylItemClickLinstener(String id,String editmeg);
    }
}
