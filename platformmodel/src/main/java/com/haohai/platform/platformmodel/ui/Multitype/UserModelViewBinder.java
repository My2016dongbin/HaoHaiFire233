package com.haohai.platform.platformmodel.ui.Multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haohai.platform.platformmodel.R;
import com.ruyiruyi.rylibrary.db.UserModel;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ImageTextView;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/7/14.
 */
public class UserModelViewBinder extends ItemViewProvider<UserModel, UserModelViewBinder.ViewHolder> {
    private static final String TAG = UserModelViewBinder.class.getSimpleName();
    public Context context;

    public UserModelViewBinder(Context context) {
        this.context = context;
    }

    public OnUserItemClickListener listener;



    public void setListener(OnUserItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_user_model, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final UserModel userModel) {
        if (userModel.isChoose){
            holder.chooseImage.setImageResource(R.drawable.ic_choose);
        }else {
            holder.chooseImage.setImageResource(R.drawable.ic_choose_no);
        }
        if (userModel.getHeadUrl() != null) {
            holder.touxiangImage.setVisibility(View.VISIBLE);
            holder.xingmingImageView.setVisibility(View.GONE);
            Glide.with(context).load(userModel.getHeadUrl()).into(holder.touxiangImage);
        }else {
            holder.touxiangImage.setVisibility(View.GONE);
            holder.xingmingImageView.setVisibility(View.VISIBLE);
            Log.e(TAG, "onBindViewHolder: userModel.getFullName()" + userModel.getFullName() );
            holder.xingmingImageView.setName(userModel.getFullName());
        }
        holder.xingmingView.setText(userModel.getFullName());
        holder.bumenView.setText(userModel.getDeptName());

        RxViewAction.clickNoDouble(holder.userLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                         listener.onUserItemClickListener(userModel);
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView touxiangImage;
        private final ImageTextView xingmingImageView;
        private final TextView xingmingView;
        private final TextView bumenView;
        private final LinearLayout userLayout;
        private final ImageView chooseImage;

        ViewHolder(View itemView) {
            super(itemView);
            touxiangImage = ((ImageView) itemView.findViewById(R.id.touxaing_image));
            xingmingImageView = ((ImageTextView) itemView.findViewById(R.id.xingming_image_view));
            xingmingView = ((TextView) itemView.findViewById(R.id.xingming_view));
            bumenView = ((TextView) itemView.findViewById(R.id.bumen_view));
            userLayout = ((LinearLayout) itemView.findViewById(R.id.user_layout));
            chooseImage = ((ImageView) itemView.findViewById(R.id.choose_image));
        }
    }

    public interface OnUserItemClickListener{
        void onUserItemClickListener(UserModel userModel);
    }
}
