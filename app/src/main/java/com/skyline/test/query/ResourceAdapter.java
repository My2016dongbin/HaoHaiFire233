package com.skyline.test.query;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skyline.terraexplorer.R;

import java.util.ArrayList;

/**
 * Created by miao on 2017/5/31 17:00.
 */

public class ResourceAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ResourceInfo> list;


    public ResourceAdapter(Context context, ArrayList<ResourceInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ResourceInfo resourceInfo = list.get(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.ada_resource_tv);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(resourceInfo.getName());
        return view;
    }

    class ViewHolder {
        TextView textView;
    }

}
