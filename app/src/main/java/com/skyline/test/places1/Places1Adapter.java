package com.skyline.test.places1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.skyline.terraexplorer.R;

import java.util.ArrayList;

/**
 * Created by miao on 2017/6/2 16:11.
 */

public class Places1Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<Places1Info> infos;

    public Places1Adapter(Context context, ArrayList<Places1Info> infos) {
        this.context = context;
        this.infos = infos;
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Places1Info info = infos.get(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_places1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.places1_tv_name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(info.getName());
        return view;
    }

    class ViewHolder {
        TextView name;
    }
}
