package com.skyline.terraexplorer.mainapps.multitype;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;

import me.drakeet.multitype.ItemViewBinder;
import rx.functions.Action1;

/**
 * Created by geyang on 2020/3/24.
 */
public class AddLcpItemViewBinder extends ItemViewBinder<AddLcp, AddLcpItemViewBinder.ViewHolder> {

    public OnAddCheckItemClick listener;

    public void setListener(OnAddCheckItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_add_lcp, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull AddLcp addLcp) {
        RxViewAction.clickNoDouble(holder.addCheckView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onAddCheckItemClickListener();
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView addCheckView;

        ViewHolder(View itemView) {
            super(itemView);
            addCheckView = ((TextView) itemView.findViewById(R.id.add_check_view));
        }
    }

    public interface OnAddCheckItemClick{
        void  onAddCheckItemClickListener();
    }
}
