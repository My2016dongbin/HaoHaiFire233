package com.haohai.platform.platformmodel.ui.acticity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haohai.platform.platformmodel.R;
import com.haohai.platform.platformmodel.ui.Multitype.FlowApprove;
import com.haohai.platform.platformmodel.ui.Multitype.FlowApproveTop;
import com.haohai.platform.platformmodel.ui.Multitype.FlowApproveTopViewBinder;
import com.haohai.platform.platformmodel.ui.Multitype.FlowApproveViewBinder;
import com.haohai.platform.platformmodel.ui.Multitype.LeaveFlow;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;
import com.haohai.platform.platformmodel.ui.utils.RequestCode;
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

public class LeaveFlowInfoActivity extends HhBaseActivity {

    private static final String TAG = LeaveFlowInfoActivity.class.getSimpleName();
    private LeaveFlow leaveFlow;
    private ImageView touxiangImage;
    private TextView nameView;
    private TextView bumenView;
    private TextView shijianView;
    private TextView qingjiashijianView;
    private TextView qingjialiyouView;
    private User user;
    private TextView jieshushijianView;
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private List<FlowApprove> flowApproveList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_flow_info);
        flowApproveList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        leaveFlow = ((LeaveFlow) intent.getSerializableExtra("LEAVE_FLOW"));

        user = new DbConfig(this).getUser();

        initView();

        getDataFromService();

    }

    private void getDataFromService() {
        showDialogProgress(progressDialog,"加载中...");
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/process/selectComment");
        params.setAsJsonContent(true);
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        params.addParameter("ProcessInstanceId",leaveFlow.getProcessInstanceId());
        Log.e(TAG, "postData:-- params--" + params);
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    flowApproveList.clear();
                    Gson gson = new Gson();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dto = data.getJSONObject(i).getJSONObject("dto");
                        FlowApprove flowApprove = gson.fromJson(dto.toString(), FlowApprove.class);
                        flowApproveList.add(flowApprove);
                    }

                  /*  List<FlowApprove> list = gson.fromJson(String.valueOf(data), new TypeToken<List<FlowApprove>>() {
                    }.getType());*/

                    initData();
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


        Log.e(TAG, "initData: size=" + flowApproveList.size() );
        for (int i = 0; i < flowApproveList.size(); i++) {
            items.add(flowApproveList.get(i));
        }
        items.add(new FlowApproveTop());
        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("审批流程");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;

                }
            }
        });

        touxiangImage = (ImageView) findViewById(R.id.touxiang_image);
        nameView = (TextView) findViewById(R.id.xingming_view);
        bumenView = (TextView) findViewById(R.id.bumen_view);
        shijianView = (TextView) findViewById(R.id.shijian_view);
        qingjiashijianView = (TextView) findViewById(R.id.qingjiashijian_view);
        qingjialiyouView = (TextView) findViewById(R.id.qingjialiyou_view);
        jieshushijianView = (TextView) findViewById(R.id.jieshushijian_view);

        nameView.setText(user.getUserName());
     //   bumenView.setText(user.);
        shijianView.setText(leaveFlow.getUpdateTime().substring(0,leaveFlow.getUpdateTime().indexOf(".")).replace("T"," "));
        if (leaveFlow.getTimeType().equals("1")){
            qingjiashijianView.setText("请假时间:　" +leaveFlow.getOneDayTime());
            jieshushijianView.setVisibility(View.GONE);
        }else {
            qingjiashijianView.setText("开始时间:　" + leaveFlow.getStartTime());
            jieshushijianView.setText("结束时间:　" + leaveFlow.getEndTime());
            jieshushijianView.setVisibility(View.VISIBLE);
        }
        qingjialiyouView.setText("请假理由:　" + leaveFlow.getReason());


        listView = (RecyclerView) findViewById(R.id.leave_flow_listview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);
        adapter = new MultiTypeAdapter(items);

        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    private void register() {
        adapter.register(FlowApprove.class,new FlowApproveViewBinder(this));
        adapter.register(FlowApproveTop.class,new FlowApproveTopViewBinder());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult:requestCode " + requestCode);
        Log.e(TAG, "onActivityResult:resultCode " +  resultCode);
        if (resultCode == RequestCode.LIST_CHANGE) {
            Log.e(TAG, "onActivityResult: 111" );
          //  curr = 0;
            flowApproveList.clear();
           // isShowDialog = true;
            getDataFromService();
        }
    }
}
