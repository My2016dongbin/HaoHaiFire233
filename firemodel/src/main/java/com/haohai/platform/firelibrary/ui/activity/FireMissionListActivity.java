package com.haohai.platform.firelibrary.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haohai.platform.firelibrary.R;
import com.haohai.platform.firelibrary.ui.activity.base.HhBaseActivity;
import com.haohai.platform.firelibrary.ui.multitype.FireMission;
import com.haohai.platform.firelibrary.ui.multitype.FireMissionViewBinder;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.route.RouteUtils;

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

@Route(path = RouteUtils.FireMissionList)
public class FireMissionListActivity extends HhBaseActivity implements FireMissionViewBinder.OnFireMissionItemClick {
    private static final String TAG = FireMissionListActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private boolean isShowDialog = true;
    private boolean isShuaxin = false;
    private boolean isSearch = false;
    private  List<FireMission> fireMissionList;
    public static int ORDER_CHANGE = 113;
    @Autowired
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_mission);
        ARouter.getInstance().inject(this);
        progressDialog = new ProgressDialog(this);
        fireMissionList = new ArrayList<>();
        initView();
        isShowDialog = true;
        getDataFromService();
    }

    private void getDataFromService() {
        if (isShowDialog){
            showDialogProgress(progressDialog,"加载中...");
        }
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("groupId",new DbConfig(this).getUser().getGroupId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**
         * oa/api/taskManagement/page  {"page":1,"limit":20,"dto":{}} post
         * 分页功能接口
         */
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "oa/api/taskManagement/listNew");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());

        Log.e(TAG, "getDataFromService: " + params);
        Log.e(TAG, "getDataFromService: " + jsonObject.toString());
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());

        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    if (jsonObject1.getString("code").equals("200")) {
                        JSONArray data = jsonObject1.getJSONArray("data");
                        Gson gson = new Gson();
                        fireMissionList.clear();
                        fireMissionList = gson.fromJson(String.valueOf(data), new TypeToken<List<FireMission>>() {
                        }.getType());

                        initData();
                    }else {
                        Toast.makeText(FireMissionListActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
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
        for (int i = 0; i < fireMissionList.size(); i++) {
            items.add(fireMissionList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("任务列表");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                        case -2:
                        break;
                }
            }
        });
        //  actionBar.setRightImage(R.drawable.ic_jia);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fire_mission_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        listView = (RecyclerView) findViewById(R.id.fire_mission_listview);

        //下拉刷新
       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
               /* isShuaxin = true;
                leaveFlowList.clear();
                currentPage = 0;
                isShowDialog = false;
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

    private void register() {
        FireMissionViewBinder fireMissionViewBinder = new FireMissionViewBinder();
        fireMissionViewBinder.setListener(this);
        adapter.register(FireMission.class, fireMissionViewBinder);
    }

    /**
     * 任务单条目点击
     * @param fireMission
     */
    @Override
    public void onFireMissionItemClickListener(FireMission fireMission) {
        Intent intent = new Intent(getApplicationContext(), FireMissionInfoActivity.class);
        intent.putExtra("ID",fireMission.getId());
        startActivityForResult(intent,ORDER_CHANGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult:requestCode " + requestCode);
        Log.e(TAG, "onActivityResult:resultCode " +  resultCode);
        if (resultCode == ORDER_CHANGE) {
            Log.e(TAG, "onActivityResult: 111" );
            fireMissionList.clear();
            isShowDialog = true;
            getDataFromService();
        }
    }
}
