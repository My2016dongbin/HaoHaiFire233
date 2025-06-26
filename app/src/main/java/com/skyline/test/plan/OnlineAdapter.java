package com.skyline.test.plan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skyline.terraexplorer.R;

import java.util.ArrayList;

/**
 * Created by miao on 2017/5/15 9:15.
 */

public class OnlineAdapter extends BaseAdapter {

    private ArrayList<OnlineInfo1> mOnlineInfo1s;
    private Context context;
    private int resourceId;
    private OnDownloadListener mOnDownloadListener;

    public OnlineAdapter(ArrayList<OnlineInfo1> mOnlineInfos, Context context, int resourceId) {
        this.mOnlineInfo1s = mOnlineInfos;
        this.context = context;
        this.resourceId = resourceId;
    }

    @Override
    public int getCount() {
        return mOnlineInfo1s.size();
    }

    @Override
    public Object getItem(int position) {
        return mOnlineInfo1s.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final OnlineInfo1 onlineInfo1 = mOnlineInfo1s.get(position);
        final ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.onlineDownload = (RelativeLayout) view.findViewById(R.id.online_item_download);
            viewHolder.onlineFinish = (RelativeLayout) view.findViewById(R.id.online_item_finish);
            viewHolder.progressBar = (ProgressBar) view.findViewById(R.id.online_item_pb);
            viewHolder.textView = (TextView) view.findViewById(R.id.online_item_name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if(onlineInfo1.isDownload()) {
            setVisisble(viewHolder,viewHolder.onlineFinish);
        } else {
            setVisisble(viewHolder,viewHolder.onlineDownload);
        }
        if(onlineInfo1.getName() != null) {
            viewHolder.textView.setText(onlineInfo1.getName());
        } else {
            viewHolder.textView.setText(" ");
        }

        viewHolder.onlineDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDownloadListener.downLoad(onlineInfo1.getDownloadUrl(),viewHolder.onlineDownload,viewHolder.progressBar,viewHolder.onlineFinish);
            }
        });


        return view;
    }

    class ViewHolder {
        TextView textView;
        RelativeLayout onlineDownload;
        RelativeLayout onlineFinish;
        ProgressBar progressBar;
    }

    private void setVisisble(ViewHolder viewHolder,View view) {
        viewHolder.onlineDownload.setVisibility(View.GONE);
        viewHolder.onlineFinish.setVisibility(View.GONE);
        viewHolder.progressBar.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }

    public void setOnDownloadListener(OnDownloadListener mOnDownloadListener) {
        this.mOnDownloadListener = mOnDownloadListener;
    }

    public interface OnDownloadListener {
        void downLoad(String downloadUrl,View download,View progressbar,View downfinish);
    }
}