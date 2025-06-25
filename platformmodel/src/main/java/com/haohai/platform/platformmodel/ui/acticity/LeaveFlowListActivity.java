package com.haohai.platform.platformmodel.ui.acticity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haohai.platform.platformmodel.R;
import com.haohai.platform.platformmodel.ui.Multitype.Bottom;
import com.haohai.platform.platformmodel.ui.Multitype.BottomViewBinder;
import com.haohai.platform.platformmodel.ui.Multitype.Empty;
import com.haohai.platform.platformmodel.ui.Multitype.EmptyViewBinder;
import com.haohai.platform.platformmodel.ui.Multitype.LeaveFlow;
import com.haohai.platform.platformmodel.ui.Multitype.LeaveFlowViewBinder;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;
import com.haohai.platform.platformmodel.ui.utils.OnLoadMoreListener;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
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
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class LeaveFlowListActivity extends HhBaseActivity implements LeaveFlowViewBinder.OnLeaveFlowItemClick {
    private static final String TAG = LeaveFlowListActivity.class.getSimpleName();
    private ActionBar actionBar;
    private Dialog addDialog;
    private View addInflate;
    private TextView ribaoView;
    private View zhoubaoView;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private List<LeaveFlow> leaveFlowList;
    private ProgressDialog progressDialog;
    private int pageSize = 20;
    private int currentPage = 0;
    private int lastPage = 1;
    private int allTotal = 0;
    private boolean isShowDialog = true;
    private boolean isShuaxin = false;
    private boolean isSearch = false;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_flow_list);

        leaveFlowList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);

        initView();

        getDataFromService();
    }
    private void getDataFromService() {
        if (isShowDialog){
            showDialogProgress(progressDialog,"加载中...");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("limit", pageSize);
            jsonObject.put("page", currentPage);
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/process/selectIPublishProcess");
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
                    JSONObject object = new JSONObject(result);
                    String code = object.getString("code");
                    if (code.equals("200")){
                        JSONObject data = object.getJSONObject("data");
                        allTotal = data.getInt("totalSize");
                        lastPage = allTotal / pageSize;
                        JSONArray dataList = data.getJSONArray("dataList");
                        Gson gson = new Gson();
                        List<LeaveFlow> list = gson.fromJson(String.valueOf(dataList), new TypeToken<List<LeaveFlow>>() {
                        }.getType());
                        if (isShowDialog){
                            progressDialog.dismiss();
                        }
                        if (isSearch){
                            isSearch = false;
                        }
                        leaveFlowList.addAll(list);

                        if (isShuaxin){
                            isShuaxin = false;
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        Log.e(TAG, "onSuccess:11 ");
                        initData();

                    }else {
                        Toast.makeText(LeaveFlowListActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
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
        if (leaveFlowList.size() == 0 && currentPage == 0) {      //数据源为空
            items.add(new Empty());
        } else {
            if (items.size() > 1) {
                items.remove(items.size() - 1);//档item 不是空 移除最后加载更多的item
            }
            //加载数据源
            for (int i = 0; i < leaveFlowList.size(); i++) {
                items.add(leaveFlowList.get(i));
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
                        startActivity(new Intent(getApplicationContext(),LeaveFlowAddActivity.class));
                        break;
                }
            }
        });
        actionBar.setRightImage(R.drawable.ic_jia);


        addDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        addInflate = LayoutInflater.from(this).inflate(R.layout.dialog_add_word, null);
        addInflate.setMinimumWidth(10000);
        ribaoView = ((TextView) addInflate.findViewById(R.id.ribao_view));
        zhoubaoView = addInflate.findViewById(R.id.zhoubao_view);

        addDialog.setContentView(addInflate);

        Window addWindow = addDialog.getWindow();
        addWindow.setWindowAnimations(R.style.ActionSheetDialogTopScaleAnimation);
        addWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        addWindow.setGravity(Gravity.TOP | Gravity.RIGHT);
        WindowManager.LayoutParams addListLp = addWindow.getAttributes();

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int width = wm.getDefaultDisplay().getWidth();

        addListLp.width = (int) (width * 0.4);
        addListLp.height = 330;
        addListLp.y = 90;
        addWindow.setAttributes(addListLp);
        addDialog.setCanceledOnTouchOutside(true);

        RxViewAction.clickNoDouble(ribaoView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        addDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), WorkReportDayAddActivity.class));
                    }
                });

        RxViewAction.clickNoDouble(zhoubaoView)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        addDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), WorkReportWeekAddActivity.class));
                    }
                });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.leave_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        listView = (RecyclerView) findViewById(R.id.leave_listview);
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
                leaveFlowList.clear();
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
        LeaveFlowViewBinder leaveFlowViewBinder = new LeaveFlowViewBinder(this);
        leaveFlowViewBinder.setListener(this);
        adapter.register(LeaveFlow.class, leaveFlowViewBinder);

        adapter.register(Empty.class,new EmptyViewBinder());
        adapter.register(Bottom.class,new BottomViewBinder());
    }

    /**
     * 流程item点击
     * @param leaveFlow
     */
    @Override
    public void onLeaveFlowItemClickListener(LeaveFlow leaveFlow) {
        Intent intent = new Intent(this, LeaveFlowInfoActivity.class);
        intent.putExtra("LEAVE_FLOW",leaveFlow);
        startActivity(intent);
    }
}
