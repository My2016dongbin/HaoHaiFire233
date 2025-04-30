package com.haohai.platform.platformmodel.ui.acticity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.haohai.platform.platformmodel.R;
import com.haohai.platform.platformmodel.ui.Multitype.AttendanceRecord;
import com.haohai.platform.platformmodel.ui.Multitype.AttendanceRecordViewBinder;
import com.haohai.platform.platformmodel.ui.Multitype.Bottom;
import com.haohai.platform.platformmodel.ui.Multitype.BottomViewBinder;
import com.haohai.platform.platformmodel.ui.Multitype.Empty;
import com.haohai.platform.platformmodel.ui.Multitype.EmptyViewBinder;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;
import com.haohai.platform.platformmodel.ui.utils.OnLoadMoreListener;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class AttendanceRecordActivity extends HhBaseActivity implements AttendanceRecordViewBinder.OnAttendanceRecordItemClick {

    private static final String TAG = AttendanceRecordActivity.class.getSimpleName();
    private ActionBar actionBar;
    private Dialog addDialog;
    private View addInflate;
    private TextView ribaoView;
    private View zhoubaoView;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private List<AttendanceRecord> attendanceRecordList;
    private ProgressDialog progressDialog;
    private int pageSize = 20;
    private int currentPage = 0;
    private int lastPage = 0;
    private int allTotal = 0;
    private boolean isShowDialog = true;
    private boolean isShuaxin = false;
    private boolean isSearch = false;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_record);
        progressDialog = new ProgressDialog(this);
        attendanceRecordList = new ArrayList<>();

        initView();
        getDataFromService();
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("历史考勤");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -2:
                        addDialog.show();
                        break;
                }
            }
        });
        actionBar.setRightImage(R.drawable.ic_jia);



        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.attendance_record_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        listView = (RecyclerView) findViewById(R.id.attendance_record_listview);
        //加载更多
        listView.setOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                Log.e(TAG, "onLoadMore: lastPage-" + lastPage);
                Log.e(TAG, "onLoadMore: currentPage- " + currentPage);
                if (lastPage > currentPage){
                    currentPage += 1;
                    isShowDialog = false;
                    getDataFromService();
                }
            }
        });
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isShuaxin = true;
                attendanceRecordList.clear();
                currentPage = 0;
                isShowDialog = false;
                getDataFromService();


            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);

        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

    }

    private void register() {
        AttendanceRecordViewBinder attendanceRecordViewBinder = new AttendanceRecordViewBinder();
        attendanceRecordViewBinder.setListener(this);
        adapter.register(AttendanceRecord.class, attendanceRecordViewBinder);
        adapter.register(Empty.class,new EmptyViewBinder());
        adapter.register(Bottom.class,new BottomViewBinder());
    }

    private void getDataFromService() {
        if (isShowDialog){
            showDialogProgress(progressDialog,"加载中...");
        }
        JSONObject jsonObject = new JSONObject();
        try {
          /*  JSONObject dto = new JSONObject();
            dto.put("userId", new DbConfig(this).getUser().getId());
            jsonObject.put("dto", dto);*/
            jsonObject.put("limit", pageSize);
            jsonObject.put("page", currentPage);


        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/attendance/attendanceRecords");
        params.setBodyContent(jsonObject.toString());


        Log.e(TAG, "getDataFromService: " + jsonObject.toString());
        Log.e(TAG, "getDataFromService: " + params);
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());

        Log.e(TAG, "getDataFromService: " + "bearer " + new DbConfig(this).getUser().getToken() );
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
               try {
                    JSONObject object = new JSONObject(result);
                    String code = object.getString("code");
                    if (code.equals("200")){
                        JSONObject data = object.getJSONObject("data");
                        allTotal = data.getInt("totalSize");
                        lastPage = allTotal / pageSize ;
                        Log.e(TAG, "onSuccess: lastPage==" + lastPage);
                        JSONArray dataList = data.getJSONArray("dataList");

                        for (int i = 0; i < dataList.length(); i++) {
                            JSONObject object1 = dataList.getJSONObject(i);
                            String time = object1.getString("time");
                            attendanceRecordList.add(new AttendanceRecord(time));
                        }

                        if (isShuaxin){
                            isShuaxin = false;
                            swipeRefreshLayout.setRefreshing(false);
                        }

                       initData();

                    }else {
                        Toast.makeText(AttendanceRecordActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        });
    }

    private void initData() {
        items.clear();

        if (attendanceRecordList.size() == 0 && currentPage == 0) {      //数据源为空
            items.add(new Empty());
        } else {
            if (items.size() > 1) {
                items.remove(items.size() - 1);//档item 不是空 移除最后加载更多的item
            }
            //加载数据源
            for (int i = 0; i < attendanceRecordList.size(); i++) {
                items.add(attendanceRecordList.get(i));
            }

            if (currentPage > 0){
                if (lastPage > currentPage) {    //显示下拉加载
                    items.add(new Bottom("加载更多..."));
                } else {
                    items.add(new Bottom("全部加载完成"));
                }
            }

        }
        /*for (int i = 0; i < workReportList.size(); i++) {
            items.add(workReportList.get(i));
        }*/

        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    /**
     * 日期的条目点击
     * @param time
     */
    @Override
    public void onAttendanceRecordItemClickListener(String time) {
        Intent intent = new Intent(getApplicationContext(), AttendanceRecordInfoActivity.class);
        intent.putExtra("TIME",time);
        startActivity(intent);
    }
}
