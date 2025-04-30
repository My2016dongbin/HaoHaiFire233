package com.haohai.platform.platformmodel.ui.Multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haohai.platform.platformmodel.R;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;

import me.drakeet.multitype.ItemViewProvider;


/**
 * Created by geyang on 2020/6/6.
 */
public class WorkReportViewBinder extends ItemViewProvider<WorkReport, WorkReportViewBinder.ViewHolder> {

    public Context context;

    public WorkReportViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_work_report, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull WorkReport workReport) {
        User user = new DbConfig(context).getUser();
        Glide.with(context).load(R.drawable.ic_launcher).into(holder.touxiangImageView);
        holder.nameView.setText(user.getUserName());
        holder.shijianView.setText(workReport.getCommitReportTime());
        holder.riqiView.setText(workReport.getDailyReportTime());
        if (workReport.getReportType().equals("1")){        //周报

            SpannableString todayString = new SpannableString("今日工作总结: " + workReport.getTodaySummary());
            todayString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.c6)), 0,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.text1View.setText(todayString);
            SpannableString tomorrowString = new SpannableString("明日工作计划: " + workReport.getTomorrowPlan());
            tomorrowString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.c6)), 0,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.text2View.setText(tomorrowString);
            holder.text3View.setVisibility(View.GONE);
        }else {
            SpannableString todayString = new SpannableString("本周已完成工作及总结: " + workReport.getTodaySummary());
            todayString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.c6)), 0,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.text1View.setText(todayString);
            SpannableString weiwanchengString = new SpannableString("本周未完成工作及原因: " + workReport.getTomorrowPlan());
            weiwanchengString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.c6)), 0,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.text2View.setText(weiwanchengString);
            SpannableString tomorrowString = new SpannableString("本周补回措施及下周计划: " + workReport.getTomorrowPlan());
            tomorrowString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.c6)), 0,12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.text3View.setVisibility(View.VISIBLE);
            holder.text3View.setText(tomorrowString);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView touxiangImageView;
        private final TextView nameView;
        private final TextView shijianView;
        private final TextView riqiView;
        private final TextView text1View;
        private final TextView text2View;
        private final TextView text3View;

        ViewHolder(View itemView) {
            super(itemView);
            touxiangImageView = ((ImageView) itemView.findViewById(R.id.touxiang_image_view));
            nameView = ((TextView) itemView.findViewById(R.id.name_view));
            shijianView = ((TextView) itemView.findViewById(R.id.shijian_View));
            riqiView = ((TextView) itemView.findViewById(R.id.riqi_view));
            text1View = ((TextView) itemView.findViewById(R.id.text1_view));
            text2View = ((TextView) itemView.findViewById(R.id.text2_view));
            text3View = ((TextView) itemView.findViewById(R.id.text3_view));

        }
    }
}
