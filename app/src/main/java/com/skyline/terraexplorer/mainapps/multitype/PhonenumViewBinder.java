package com.skyline.terraexplorer.mainapps.multitype;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;
import com.skyline.terraexplorer.multitype.Phonenum;

import me.drakeet.multitype.ItemViewBinder;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/12/2.
 */
public class PhonenumViewBinder extends ItemViewBinder<Phonenum, PhonenumViewBinder.ViewHolder> {

    public OnPhonenumItemClick listener;

    public void setListener(OnPhonenumItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_phonenum, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Phonenum phonenum) {
        Log.e("TAG", "onBindViewHolder: "+phonenum.getFullName() );
        holder.textView1.setText(phonenum.getFullName());
        holder.textView2.setText( phonenum.getPhone());
        RxViewAction.clickNoDouble(holder.phoneButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                       listener.onPhoneItemClickListener(phonenum.getPhone());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView1;
        private final TextView textView2;
        private final Button phoneButton;
        ViewHolder(View itemView) {
            super(itemView);
            textView1 = ((TextView) itemView.findViewById(R.id.text_view1));
            textView2 = ((TextView) itemView.findViewById(R.id.text_view2));
            phoneButton=itemView.findViewById(R.id.phone_btn);
        }
    }

    public interface OnPhonenumItemClick{
        void onPhoneItemClickListener(String phoneNum);
    }
}
