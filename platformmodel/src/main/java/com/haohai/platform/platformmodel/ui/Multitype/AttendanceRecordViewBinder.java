package com.haohai.platform.platformmodel.ui.Multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haohai.platform.platformmodel.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by geyang on 2020/6/16.
 */
public class AttendanceRecordViewBinder extends ItemViewProvider<AttendanceRecord, AttendanceRecordViewBinder.ViewHolder> {

    public OnAttendanceRecordItemClick listener;

    public void setListener(OnAttendanceRecordItemClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_attendance_record, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final AttendanceRecord attendanceRecord) {
        holder.timeView.setText(attendanceRecord.getTime());
        RxViewAction.clickNoDouble(holder.timeLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onAttendanceRecordItemClickListener(attendanceRecord.getTime());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final FrameLayout timeLayout;
        private final TextView timeView;

        ViewHolder(View itemView) {
            super(itemView);
            timeLayout = ((FrameLayout) itemView.findViewById(R.id.time_layout));
            timeView = ((TextView) itemView.findViewById(R.id.time_view));
        }
    }

    public interface OnAttendanceRecordItemClick{
        void onAttendanceRecordItemClickListener(String time);
    }
}
