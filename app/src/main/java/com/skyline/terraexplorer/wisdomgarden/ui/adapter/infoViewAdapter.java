package com.skyline.terraexplorer.wisdomgarden.ui.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.QuActivity;
import com.skyline.terraexplorer.wisdomgarden.ui.model.CityInfo;

import java.util.List;

/**
 * Created by Administrator on 2020/10/20.
 */

public class infoViewAdapter extends RecyclerView.Adapter<infoViewAdapter.MyHolder> {

    private List<CityInfo> mList;//数据源
    public infoViewAdapter(List list) {
        Log.i("infoViewAdapter: ", String.valueOf(mList));
        mList = list;
    }

    //创建ViewHolder并返回，后续item布局里控件都是从ViewHolder中取出
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //将我们自定义的item布局R.layout.item_one转换为View
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_info, parent, false);
        //将view传递给我们自定义的ViewHolder
        MyHolder holder = new MyHolder(view);
        //返回这个MyHolder实体
        return holder;
    }

    //通过方法提供的ViewHolder，将数据绑定到ViewHolder中
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.nametext.setText(mList.get(position).getCountyName());
        holder.areatext.setText(mList.get(position).getBuiltArea());
        holder.citypople.setText(mList.get(position).getCountyPopulation());
        //添加点击监听事件
        holder.citybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i( "onClick: ",mList.get(position).getCountyName());
                Intent intent=new Intent(holder.itemView.getContext(), QuActivity.class);
                intent.putExtra("localname",mList.get(position).getCountyName());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    //获取数据源总的条数
    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 自定义的ViewHolder
     */
    class MyHolder extends RecyclerView.ViewHolder {

        TextView nametext;
        TextView areatext;
        TextView citypople;
        TextView citybutton;
        public MyHolder(View itemView) {
            super(itemView);
            nametext = itemView.findViewById(R.id.name_text);
            areatext= itemView.findViewById(R.id.area_text);
            citypople= itemView.findViewById(R.id.citypople_text);
            citybutton=itemView.findViewById(R.id.city_button);
        }
    }
}