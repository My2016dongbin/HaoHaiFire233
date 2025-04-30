package com.haohai.platform.firelibrary.ui.activity;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haohai.platform.firelibrary.R;
import com.haohai.platform.firelibrary.ui.activity.base.HhBaseActivity;
import com.haohai.platform.firelibrary.ui.multitype.Empty;
import com.haohai.platform.firelibrary.ui.multitype.EmptyViewBinder;
import com.haohai.platform.firelibrary.ui.multitype.FireMission;
import com.haohai.platform.firelibrary.ui.multitype.FireStatistics;
import com.haohai.platform.firelibrary.ui.multitype.FireStatisticsViewBinder;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.request.RequestUtils;

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

public class FireStatisticsActivity extends HhBaseActivity {
    private static final String TAG = FireStatisticsActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private boolean isShowDialog = true;
    private boolean isShuaxin = false;
    private List<FireStatistics> fireStatisticsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_statistics);
        progressDialog = new ProgressDialog(this);
        fireStatisticsList = new ArrayList<>();

        initView();
        getDataFromService();
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("统计列表");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        //  showLeibieChangeDailog();
                        break;
                }
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fire_statistics_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        listView = (RecyclerView) findViewById(R.id.fire_statistics_listview);

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isShowDialog = false;
                isShuaxin = true;
                getDataFromService();
              /*  isShowDialog = false;
                isShuaxin = true;
                getDataFromService();*/

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);

        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);


    }

    private void getDataFromService() {
        if (isShowDialog){
            showDialogProgress(progressDialog,"加载中...");
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "fire/api/monitorFirealarm/getFireAlarmByDis");
        params.setAsJsonContent(true);

        Log.e(TAG, "getDataFromService: " + params);
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());

        params.setConnectTimeout(10000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        fireStatisticsList.clear();
                        Gson gson = new Gson();
                        fireStatisticsList = gson.fromJson(String.valueOf(data), new TypeToken<List<FireStatistics>>() {
                        }.getType());
                        if (isShuaxin){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        initData();
                    }else {
                        Toast.makeText(FireStatisticsActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
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
        if (fireStatisticsList.size() == 0){
            items.add(new Empty());
        }
        for (int i = 0; i < fireStatisticsList.size(); i++) {
            items.add(fireStatisticsList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void register() {
        FireStatisticsViewBinder fireStatisticsViewBinder = new FireStatisticsViewBinder();
        adapter.register(FireStatistics.class, fireStatisticsViewBinder);
        adapter.register(Empty.class,new EmptyViewBinder());
    }
}
