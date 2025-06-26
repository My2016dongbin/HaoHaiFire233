package com.skyline.terraexplorer.mainapps.multitype;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static android.content.ContentValues.TAG;


/**
 * Created by geyang on 2020/3/24.
 */
public class CheckInfoViewBinder extends ItemViewBinder<BjdInfo, CheckInfoViewBinder.ViewHolder> {

    public Context context;

    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
  //  public ChooseImageViewBinder.OnChooseImageClickListener imageListener;
    public OnCheckInfoItemClick listener;
    private ViewHolder holde;
    private String currentInfo;
    private String currentName;
    private StringBuffer date = new StringBuffer();;
    private int year;
    private int month;
    private int day;
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
        View root = inflater.inflate(R.layout.item_bjd_info, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final BjdInfo bjdInfo) {

        /*listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);*/
        if (bjdInfo.isSava){
            holder.saveDeleteView.setBackgroundResource(R.drawable.bg_button_red);
            holder.saveDeleteView.setText("删除");
            holder.saveLayout.setVisibility(View.GONE);
            holder.deleteLayout.setVisibility(View.VISIBLE);
            holder.nameTextView.setText("名称:" + bjdInfo.getPlantname());
            holder.infoText.setText("品种:" + bjdInfo.getVariety());
            holder.checkInfoItemView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            holder.checkInfoItemView.setGravity(Gravity.TOP);
            holder.checkInfoItemView.setSingleLine(false);
            holder.checkInfoItemView.setHorizontallyScrolling(false); //水平滚动设置为False
        }else {
            holder.saveDeleteView.setBackgroundResource(R.drawable.bg_button);
            holder.saveDeleteView.setText("保存");
            holder.saveLayout.setVisibility(View.VISIBLE);
            holder.deleteLayout.setVisibility(View.GONE);
            holder.checkNameItemView.setText(bjdInfo.getPlantname());
            holder.checkInfoItemView.setText(bjdInfo.getVariety());
        }

        adapter = new MultiTypeAdapter(items);

        RxViewAction.clickNoDouble(holder.saveDeleteView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(TAG, "call: " + bjdInfo.isSava );
                        Log.e(TAG, "call: " + bjdInfo.bjdid );
                        listener.OnCheckSaveOrDeleteClickListener(bjdInfo.isSava?false:true, bjdInfo.bjdid,holder.checkNameItemView.getText().toString(),holder.checkInfoItemView.getText().toString(),holder.ggEdit.getText().toString(),holder.dwEdit.getText().toString(),holder.slEdit.getText().toString(),holder.djEdit.getText().toString(),holder.bzEdit.getText().toString(),holder.bz1Edit.getText().toString());
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

        private final EditText checkNameItemView;
        private final EditText checkInfoItemView;
        private final TextView saveDeleteView;
        private final LinearLayout deleteLayout;
        private final LinearLayout saveLayout;
        private final TextView nameTextView;
        private final TextView infoText;
        private final EditText ggEdit;
        private final EditText dwEdit;
        private final EditText slEdit;
        private final EditText djEdit;
        private final EditText bzEdit;
        private final EditText bz1Edit;

        ViewHolder(View itemView) {
            super(itemView);
            checkNameItemView = ((EditText) itemView.findViewById(R.id.check_name_item_view));
            checkInfoItemView = ((EditText) itemView.findViewById(R.id.zhengti_item_view));
            saveDeleteView = ((TextView) itemView.findViewById(R.id.save_delete_view));
            saveLayout = ((LinearLayout) itemView.findViewById(R.id.save_check_layout));
            deleteLayout = ((LinearLayout) itemView.findViewById(R.id.delete_layout));
            nameTextView = (TextView) itemView.findViewById(R.id.name_textview);
            infoText = ((TextView) itemView.findViewById(R.id.info_textview));
            ggEdit = ( itemView.findViewById(R.id.gg_edit));
            dwEdit= ( itemView.findViewById(R.id.dw_edit));
            slEdit= ( itemView.findViewById(R.id.sl_edit));
            djEdit= ( itemView.findViewById(R.id.dj_edit));
            bzEdit= ( itemView.findViewById(R.id.bz_edit));
            bz1Edit= ( itemView.findViewById(R.id.bz1_edit));
        }
    }

    public interface OnCheckInfoItemClick{
        void OnCheckSaveOrDeleteClickListener(boolean isSave,String id,String checkName,String checkInfoStr,String spec,String unit,String count,String unitPrice,String packaging,String remark);
    }
}
