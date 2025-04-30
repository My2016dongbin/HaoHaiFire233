package com.skyline.test.query;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.models.FavoritesStorage;

import java.util.ArrayList;

/**
 * Created by haohai on 2017-06-19.
 */

public class ResourceBaseAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ResourceInfo> resourceInfos;
//    private  OnOperateListerner OnOperateListerner;


    public ResourceBaseAdapter(Context context, ArrayList<ResourceInfo> resourceInfos) {
        this.mContext = context;
        this.resourceInfos=resourceInfos;
    }

    @Override
    public int getCount() {
        return resourceInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return resourceInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View mView;
        ResourceInfo resourceInfo=resourceInfos.get(i);
        ViewHolder viewHolder;
        if (view==null)
        {
            mView=LayoutInflater.from(mContext).inflate(R.layout.activity_resource_item,viewGroup,false);
            viewHolder=new ViewHolder();
//            viewHolder.textView_content=(TextView)mView.findViewById(R.id.textView_content);
            viewHolder.imageView=(ImageView)mView.findViewById(R.id.imageView_icon);
//            viewHolder.textView_location=(TextView)mView.findViewById(R.id.textView_flyto);
            viewHolder.textView_name=(TextView)mView.findViewById(R.id.textView_item);
//            viewHolder.linearLayout=(LinearLayout)mView.findViewById(R.id.linearlayout_item);
            mView.setTag(viewHolder);
        }
        else
        {
            mView=view;
            viewHolder=(ViewHolder) mView.getTag();
        }
        viewHolder.textView_name.setText(resourceInfo.getName());
        viewHolder.imageView.setImageResource(FavoritesStorage.defaultStorage.resourceForIcon(resourceInfo.getIcon()));
      /*  viewHolder.textView_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnOperateListerner.GetLocation(i);

            }
        });
        viewHolder.textView_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnOperateListerner.showContent(i);

            }
        });*/
        return mView;
    }
    class  ViewHolder
    {
        TextView textView_name;
//        TextView textView_location;
//        TextView textView_content;
        ImageView imageView;
//        LinearLayout linearLayout;
    }
//    public  interface  OnOperateListerner
//    {
//        void GetLocation(int position);
//        void showContent(int position);
//    }
//    public void setOnOperateListerner(ResourceBaseAdapter.OnOperateListerner onOperateListerner) {
//        OnOperateListerner = onOperateListerner;
//    }


}
