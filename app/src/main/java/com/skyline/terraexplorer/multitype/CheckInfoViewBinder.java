package com.skyline.terraexplorer.multitype;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.utils.RxViewAction;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static android.content.ContentValues.TAG;
import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;


/**
 * Created by geyang on 2020/3/24.
 */
public class CheckInfoViewBinder extends ItemViewBinder<CheckInfo, CheckInfoViewBinder.ViewHolder> implements ChooseImageViewBinder.OnChooseImageClickListener {

    public Context context;

    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private ChooseImageViewBinder chooseImageViewBinder;
  //  public ChooseImageViewBinder.OnChooseImageClickListener imageListener;
    public OnCheckInfoItemClick listener;
    private ViewHolder holde;
    private String currentInfo;
    private String currentName;

    public void setListener(OnCheckInfoItemClick listener) {
        this.listener = listener;
    }

    public void setContext(Context context) {
        this.context = context;
    }
/*
    public void setImageListener(ChooseImageViewBinder.OnChooseImageClickListener imageListener) {
        this.imageListener = imageListener;
    }*/

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_check_info, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final CheckInfo checkInfo) {

        /*listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);*/
        if (checkInfo.isSava){
            holder.saveDeleteView.setBackgroundResource(R.drawable.bg_button_red);
            holder.saveDeleteView.setText("删除");
            holder.saveLayout.setVisibility(View.GONE);
            holder.deleteLayout.setVisibility(View.VISIBLE);
            holder.nameTextView.setText("名称:" + checkInfo.getCheckName());
            holder.infoText.setText("情况:" + checkInfo.getCheckInfo());
            holder.image1View.setVisibility(View.GONE);
            holder.image2View.setVisibility(View.GONE);
            holder.image3View.setVisibility(View.GONE);
            holder.checkInfoItemView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            holder.checkInfoItemView.setGravity(Gravity.TOP);
            holder.checkInfoItemView.setSingleLine(false);
            holder.checkInfoItemView.setHorizontallyScrolling(false); //水平滚动设置为False

            try {
                for (int i = 0; i < checkInfo.getFullImageList().size(); i++) {
                    if (i == 0) {
                        holder.image1View.setVisibility(View.VISIBLE);
                        Glide.with(context).load("http://27.223.18.10:9180/" + checkInfo.getFullImageList().get(i).getFullStr()).into(holder.image1View);
                    } else if (i == 1) {
                        holder.image2View.setVisibility(View.VISIBLE);
                        Glide.with(context).load("http://27.223.18.10:9180/" + checkInfo.getFullImageList().get(i).getFullStr()).into(holder.image2View);
                    } else if (i == 2) {
                        holder.image3View.setVisibility(View.VISIBLE);
                        Glide.with(context).load("http://27.223.18.10:9180/" + checkInfo.getFullImageList().get(i).getFullStr()).into(holder.image3View);
                    } else {
                        Glide.with(context).load(R.drawable.ic_wanggehua).into(holder.image3View);
                    }
                }
            }catch (Exception e){
                Log.e(TAG, "onBindViewHolder: 出错了" +
                        "");
            }



        }else {
            holder.saveDeleteView.setBackgroundResource(R.drawable.bg_button_grid);
            holder.saveDeleteView.setText("保存");
            holder.saveLayout.setVisibility(View.VISIBLE);
            holder.deleteLayout.setVisibility(View.GONE);
            holder.checkNameItemView.setText(checkInfo.getCheckName());
            holder.checkInfoItemView.setText(checkInfo.getCheckInfo());
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        holder.listView.setLayoutManager(gridLayoutManager);
        adapter = new MultiTypeAdapter(items);
        chooseImageViewBinder = new ChooseImageViewBinder(context);
        chooseImageViewBinder.setListener(this);
        adapter.register(ChooseImage.class, chooseImageViewBinder);

        holder.listView.setAdapter(adapter);
        assertHasTheSameAdapter(holder.listView, adapter);

        updateData(checkInfo);

        RxViewAction.clickNoDouble(holder.saveDeleteView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: " + checkInfo.isSava );
                        Log.e(TAG, "call: " + checkInfo.checkId );
                        listener.OnCheckSaveOrDeleteClickListener(checkInfo.isSava?false:true,checkInfo.checkId,holder.checkNameItemView.getText().toString(),holder.checkInfoItemView.getText().toString());
                    }
                });

        holder.checkNameItemView.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentName = holder.checkNameItemView.getText().toString();
                currentInfo = holder.checkInfoItemView.getText().toString();
            }
        });

        holder.checkInfoItemView.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentInfo = holder.checkInfoItemView.getText().toString();
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private final RecyclerView listView;
        private final EditText checkNameItemView;
        private final EditText checkInfoItemView;
        private final TextView saveDeleteView;
        private final LinearLayout deleteLayout;
        private final LinearLayout saveLayout;
        private final TextView nameTextView;
        private final TextView infoText;
        private final ImageView image1View;
        private final ImageView image2View;
        private final ImageView image3View;

        ViewHolder(View itemView) {
            super(itemView);
            listView = ((RecyclerView) itemView.findViewById(R.id.phote_recycle));
            checkNameItemView = ((EditText) itemView.findViewById(R.id.check_name_item_view));
            checkInfoItemView = ((EditText) itemView.findViewById(R.id.zhengti_item_view));
            saveDeleteView = ((TextView) itemView.findViewById(R.id.save_delete_view));
            saveLayout = ((LinearLayout) itemView.findViewById(R.id.save_check_layout));
            deleteLayout = ((LinearLayout) itemView.findViewById(R.id.delete_layout));
            nameTextView = (TextView) itemView.findViewById(R.id.name_textview);
            infoText = ((TextView) itemView.findViewById(R.id.info_textview));
            image1View = ((ImageView) itemView.findViewById(R.id.image1_view));
            image2View = ((ImageView) itemView.findViewById(R.id.image2_view));
            image3View = ((ImageView) itemView.findViewById(R.id.image3_view));
        }
    }
    private void updateData(CheckInfo checkInfo) {
        items.clear();
        List<ChooseImage> chooseImageList = checkInfo.getChooseImageList();

        if (chooseImageList!=null){
            Log.e(TAG, "updateData: chooseImageList.size==" +chooseImageList.size() );
            for (int i = 0; i < chooseImageList.size(); i++) {
                items.add(chooseImageList.get(i));
            }

            if (chooseImageList.size()<3){
                ChooseImage chooseImage = new ChooseImage();
                chooseImage.setAdd(true);
                chooseImage.setuCheckId(checkInfo.getCheckId());
                items.add(chooseImage);
            }
        }else {
            ChooseImage chooseImage = new ChooseImage();
            chooseImage.setAdd(true);
            items.add(chooseImage);
        }

        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    public interface OnCheckInfoItemClick{
        void OnCheckSaveOrDeleteClickListener(boolean isSave,String id,String checkName,String checkInfoStr);
        void onCheckImageAddClickListener(boolean var1, Uri var2, String id,String name,String info);
        void onCheckImageDelete(Uri var1, String id,String name,String info);
    }
    @Override
    public void onImageAddClickListener(boolean var1, Uri var2, String id,ChooseImage chooseImage) {
        Log.e(TAG, "onImageAddClickListener: " + currentInfo );
        Log.e(TAG, "onImageAddClickListener: " + currentName );
        listener.onCheckImageAddClickListener(var1,var2,id,currentName,currentInfo);
    }

    @Override
    public void onImageDelete(Uri var1, String id) {
        listener.onCheckImageDelete(var1,id,currentName,currentInfo);
    }

}
