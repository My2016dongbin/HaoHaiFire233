package com.hht.hsatellitemobile.ui.multitype;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hht.hsatellitemobile.R;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;
import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;


/**
 * Created by 13589 on 2019/8/8.
 */
public class FireInfoListViewBinder extends ItemViewProvider<FireInfoList, FireInfoListViewBinder.ViewHolder> {

    public Context context;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;

    public FireInfoListViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_fire_info_list, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull FireInfoList fireInfoList) {

        holder.fireTimeText.setText(fireInfoList.getFireTime());
        if (fireInfoList.getBackgroundId() == 0){
            holder.mLinearLayout.setBackgroundColor(Color.WHITE);
        }else {
            holder.mLinearLayout.setBackgroundColor(0xFFF5F5F5);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        adapter.register(FireInfo.class,new FireInfoViewBinder());
        holder.listView.setAdapter(adapter);
        assertHasTheSameAdapter(holder.listView, adapter);

        initData(fireInfoList);
    }

    private void initData(FireInfoList fireInfoList) {
        items.clear();
        List<FireInfo> infoList = fireInfoList.getFireInfoList();
        for (int i = 0; i < infoList.size(); i++) {
            items.add(infoList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final RecyclerView listView;
        private final LinearLayout mLinearLayout;
        private final TextView fireTimeText;

        ViewHolder(View itemView) {
            super(itemView);
            listView = ((RecyclerView) itemView.findViewById(R.id.fire_info));
            mLinearLayout = ((LinearLayout) itemView.findViewById(R.id.linear_layout));
            fireTimeText = ((TextView) itemView.findViewById(R.id.fire_time_text));
        }
    }
}
