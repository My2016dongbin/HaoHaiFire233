package com.haohai.platform.firelibrary.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haohai.platform.firelibrary.R;
import com.haohai.platform.firelibrary.ui.activity.base.HhBaseActivity;
import com.haohai.platform.firelibrary.ui.multitype.Empty;
import com.haohai.platform.firelibrary.ui.multitype.EmptyViewBinder;
import com.haohai.platform.firelibrary.ui.multitype.FireMission;
import com.haohai.platform.firelibrary.ui.multitype.FireMissionViewBinder;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;
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
import rx.functions.Action1;

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
    private  List<FireMission> fireMissionTypeList;
    public static int ORDER_CHANGE = 113;
    @Autowired
    String token;

    private int currentFireListType = 0;  //3"全部，0未开始，1执行中，2已结束")
    private int choose1 = 0;
    private AlertDialog.Builder builder;
    private int currentPage = 1;
    private int totalSize;
    private int lastPage;
    private LinearLayout fireTypeLayout;
    private LinearLayout orderStateLayout;
    private TextView fireTypeView;
    private TextView orderStateView;
    private int shipinChoose1 = 0;
    public int shipinState = 0;  //2：森林防火4：海域监控5：砂石盗采 0全部
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_mission);
        ARouter.getInstance().inject(this);
        progressDialog = new ProgressDialog(this);
        fireMissionList = new ArrayList<>();
        fireMissionTypeList = new ArrayList<>();
        user = new DbConfig(this).getUser();
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
            JSONObject dto = new JSONObject();
            dto.put("groupId",new DbConfig(this).getUser().getGroupId());
            if (shipinState == 0){
                dto.put("taskType", null);
            }else {
                dto.put("taskType", shipinState);
            }
            jsonObject.put("limit", 100);
            jsonObject.put("dto", dto);
            jsonObject.put("page", currentPage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**
         * oa/api/taskManagement/page  {"page":1,"limit":20,"dto":{}} post
         * 分页功能接口
         */
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "oa/api/taskManagement/page");
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
                        JSONObject data = jsonObject1.getJSONObject("data");
                    //  JSONObject getJsonObj = data.getJSONObject(0);//获取json数组中的第一项
                        JSONArray dataList = data.getJSONArray("dataList");
                        Log.i(TAG, "dataList: "+dataList);
                        totalSize=data.getInt("totalSize");
                        Log.i(TAG, "getonebodyDataonSuccess: "+totalSize);
                        lastPage= (totalSize + 50 -1) / 50;     //计算最大分页数

                        Gson gson = new Gson();
                        choose1 = 0;
                        orderStateView.setText("全部");
                        fireMissionList.clear();
                        fireMissionTypeList.clear();

                        fireMissionList = gson.fromJson(String.valueOf(dataList), new TypeToken<List<FireMission>>() {
                        }.getType());
                        fireMissionTypeList.addAll(fireMissionList);
                        if (isShuaxin){
                            swipeRefreshLayout.setRefreshing(false);
                        }
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

        if(currentPage == 1){
            items.clear();
        }

        if (fireMissionTypeList.size() == 0){
            items.add(new Empty());
        }
        for (int i = 0; i < fireMissionTypeList.size(); i++) {
            items.add(fireMissionTypeList.get(i));
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
                    case -3:
                      //  showLeibieChangeDailog();
                        break;
                }
            }
        });
      //  actionBar.setRightView("全部");
        //  actionBar.setRightImage(R.drawable.ic_jia);

        fireTypeLayout = (LinearLayout) findViewById(R.id.fire_type_layout);
        orderStateLayout = (LinearLayout) findViewById(R.id.order_state_layout);
        fireTypeView = (TextView) findViewById(R.id.fire_type_view);
        orderStateView = (TextView) findViewById(R.id.order_state_view);

        if (user.getImToken().equals("0")) {
            fireTypeLayout.setVisibility(View.VISIBLE);
        }else {
            fireTypeLayout.setVisibility(View.GONE);
        }

        RxViewAction.clickNoDouble(fireTypeLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showOneBodyShipinFenleiDailog();
                    }
                });
        RxViewAction.clickNoDouble(orderStateLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showLeibieChangeDailog();
                    }
                });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fire_mission_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        listView = (RecyclerView) findViewById(R.id.fire_mission_listview);

        //下拉刷新
       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               isShowDialog = false;
                isShuaxin = true;
                getDataFromService();
             /* //
                isShuaxin = true;
                //currentPage = 0;


                if (lastPage > currentPage){
                    currentPage += 1;
                    Log.i(TAG, "onLoadMore: "+currentPage);
                    progressDialog.dismiss();

                }
*/
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
        adapter.register(Empty.class, new EmptyViewBinder());
    }


    private void showOneBodyShipinFenleiDailog() {
        //默认选中第一个  //0疑似火情  1是真实火情  2是未处理 3是全部
        final String[] items = {"全部", "森林防火", "砂石盗采","海域监控"};

        builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("视频分类")
                .setSingleChoiceItems(items,shipinChoose1 , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: 类别choose---" + i);
                        shipinChoose1 = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: choose1=" +choose1);
                     //   oneBodyFireFenleiList.clear();
                        //getType 2：森林防火4：海域监控5：砂石盗采 0全部


                        if (shipinChoose1 == 1){
                            shipinState = 2;
                            isShowDialog = true;
                            fireTypeView.setText("森林防火");
                            getDataFromService();

                        } else if (shipinChoose1 == 2){
                            shipinState = 5;
                            isShowDialog = true;
                            fireTypeView.setText("砂石盗采");
                            getDataFromService();

                        } else if (shipinChoose1 == 3){
                            shipinState = 4;
                            isShowDialog = true;
                            fireTypeView.setText("海域监控");
                            getDataFromService();

                        } else {
                            shipinState = 0;
                            isShowDialog = true;
                            fireTypeView.setText("全部");
                            getDataFromService();

                        }
                    }
                });
        builder.create().show();
    }

    private void showLeibieChangeDailog() {
        //默认选中第一个
        final String[] items = {"全部", "未开始","进行中","已结束"};

        builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("任务单类别")
                .setSingleChoiceItems(items, choose1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: 类别choose---" + i);
                        choose1 = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //3"全部，0未开始，1执行中，2已结束")
                        if (choose1 == 0) {
                            currentFireListType = 3;
                            orderStateView.setText("全部");
                            fireMissionTypeList.clear();
                            fireMissionTypeList.addAll(fireMissionList);
                            initData();
                        } else if (choose1 == 1){
                            currentFireListType = 0;
                            orderStateView.setText("未开始");
                            fireMissionTypeList.clear();
                            for (int j = 0; j < fireMissionList.size(); j++) {
                                if (fireMissionList.get(j).getStatus() == 0) {
                                    fireMissionTypeList.add(fireMissionList.get(j));
                                }
                            }
                            initData();
                        }else if (choose1 == 2){
                            currentFireListType = 1;
                            orderStateView.setText("进行中");
                            fireMissionTypeList.clear();
                            for (int j = 0; j < fireMissionList.size(); j++) {
                                if (fireMissionList.get(j).getStatus() == 1) {
                                    fireMissionTypeList.add(fireMissionList.get(j));
                                }
                            }
                            initData();
                        }else if (choose1 == 3){
                            currentFireListType = 3;
                            orderStateView.setText("已结束");
                            fireMissionTypeList.clear();
                            for (int j = 0; j < fireMissionList.size(); j++) {
                                if (fireMissionList.get(j).getStatus() == 2) {
                                    fireMissionTypeList.add(fireMissionList.get(j));
                                }
                            }
                            initData();
                        }

                    }
                });
        builder.create().show();
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
