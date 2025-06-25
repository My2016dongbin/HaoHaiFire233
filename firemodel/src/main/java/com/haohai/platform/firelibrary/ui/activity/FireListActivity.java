package com.haohai.platform.firelibrary.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haohai.platform.firelibrary.R;
import com.haohai.platform.firelibrary.ui.activity.base.HhBaseActivity;
import com.ruyiruyi.rylibrary.cell.ActionBar;
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

import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

@Route(path = RouteUtils.FireList)
public class FireListActivity extends HhBaseActivity {
    private static final String TAG = FireListActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private boolean isShowDialog = true;
    private boolean isShuaxin = false;
    private boolean isSearch = false;

    @Autowired
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_list);
        ARouter.getInstance().inject(this);

        progressDialog = new ProgressDialog(this);

        initView();
        getDataFromService();
    }

    private void getDataFromService() {
        if (isShowDialog){
            showDialogProgress(progressDialog,"加载中...");
        }
        JSONObject jsonObject = new JSONObject();


        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/monitorFirealarm/list");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());

        Log.e(TAG, "getDataFromService: " + params);
        Log.e(TAG, "getDataFromService: " + jsonObject.toString());
        params.addHeader("Authorization", "bearer " + token);

        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);

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

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("我的请假");
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

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fire_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        listView = (RecyclerView) findViewById(R.id.fire_listview);

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              /*  isShuaxin = true;
                leaveFlowList.clear();
                currentPage = 0;
                isShowDialog = false;
                getDataFromService();*/


            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);

      //  register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

    }
}
