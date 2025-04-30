package com.skyline.test.plan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skyline.terraexplorer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miao on 2017/5/17 10:07.
 */

public class LocalAdapter extends BaseAdapter {

    private List<WordInfo> list = new ArrayList<WordInfo>();
    private int resourceId;
    private Context context;

    public LocalAdapter(int resouceId,Context context) {
        this.resourceId = resouceId;
        this.context = context;
    }

    public List<WordInfo> getList() {
        return list;
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
        WordInfo info = list.get(position);
        View view;
        final ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.local_item_name);
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
