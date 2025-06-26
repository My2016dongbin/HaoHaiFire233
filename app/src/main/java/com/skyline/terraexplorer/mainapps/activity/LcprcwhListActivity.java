package com.skyline.terraexplorer.mainapps.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.databinding.ActivityScjdlistBinding;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.db.User;
import com.skyline.terraexplorer.mainapps.multitype.Empty;
import com.skyline.terraexplorer.mainapps.multitype.EmptyViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.Lcprcwh;
import com.skyline.terraexplorer.mainapps.multitype.LcprcyhViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.Lcpzljg;
import com.skyline.terraexplorer.mainapps.multitype.LcpzljgViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.Scjd;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;

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


public class LcprcwhListActivity extends BaseActivity {
    private static final String TAG = LcprcwhListActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private boolean isShowDialog = true;
    private boolean isShuaxin = false;
    private boolean isSearch = false;
    private List<Lcprcwh> firejdList;
    private List<Lcprcwh> firejdTypeList;
    public static int ORDER_CHANGE = 113;
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
    private User user;
    private List<String> xmnameList;
    private List<String> xmidList;
    private String jdId;
    private ActivityScjdlistBinding binding;
    public List<Scjd> xmjdTypeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_fanghuolist);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scjdlist);
        progressDialog = new ProgressDialog(this);
        firejdList = new ArrayList<>();
        firejdTypeList = new ArrayList<>();
        xmjdTypeList = new ArrayList<>();
        user = new DbConfig(this).getUser();
        initView();
        isShowDialog = true;
        getDataFromService();
        getjdFromService();
    }

    private void getDataFromService() {
        if (isShowDialog){
            showDialogProgress(progressDialog,"加载中...");
        }
        final JSONObject jsonObject = new JSONObject();
        try {
            JSONObject dto = new JSONObject();

            jsonObject.put("limit", 100);
            jsonObject.put("dto", dto);
            jsonObject.put("page", currentPage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "forestry/api/forestProductDailyMaintenance/page");
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
                        JSONObject getJsonObj = data.getJSONObject(0);//获取json数组中的第一项
                        JSONArray dataList = getJsonObj.getJSONArray("dataList");
                        Log.i(TAG, "dataList: "+dataList);
                        totalSize=getJsonObj.getInt("totalSize");
                        Log.i(TAG, "getonebodyDataonSuccess: "+totalSize);
                        lastPage= (totalSize + 50 -1) / 50;     //计算最大分页数

                        Gson gson = new Gson();
                        choose1 = 0;
                        firejdList.clear();
                        firejdTypeList.clear();
                        firejdList = gson.fromJson(String.valueOf(dataList), new TypeToken<List<Lcprcwh>>() {
                        }.getType());
                        firejdTypeList.addAll(firejdList);
                        if (isShuaxin){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        initData();
                    }else {
                        Toast.makeText(LcprcwhListActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
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
    private void getjdFromService() {
        final JSONObject jsonObject = new JSONObject();
        try {
            JSONObject dto = new JSONObject();

            jsonObject.put("limit", 100);
            jsonObject.put("dto", dto);
            jsonObject.put("page", 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "forestry/api/forestProductProductionBaseInfo/page");
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
                        JSONObject getJsonObj = data.getJSONObject(0);//获取json数组中的第一项
                        JSONArray dataList = getJsonObj.getJSONArray("dataList");
                        Log.i(TAG, "dataList: "+dataList);
                        choose1 = 0;
                        xmnameList.clear();
                        Gson gson = new Gson();
                        xmjdTypeList = gson.fromJson(String.valueOf(dataList), new TypeToken<List<Scjd>>(){}.getType());
                        Log.e(TAG, "showxxmcChangeDailog: "+xmjdTypeList.size() );
                        for (int i = 0; i < xmjdTypeList.size(); i++) {
                            xmnameList.add(xmjdTypeList.get(i).getName());
                            xmidList.add(xmjdTypeList.get(i).getId());
                        }
                    }else {
                        Toast.makeText(LcprcwhListActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
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
            }
        });
    }
    private void selectDataFromService() {
        if (isShowDialog){
            showDialogProgress(progressDialog,"加载中...");
        }
        final JSONObject jsonObject = new JSONObject();
        try {
            JSONObject dto = new JSONObject();
            jsonObject.put("productionBaseId", jdId);
            jsonObject.put("dto", dto);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/forestProductDailyMaintenance/list");
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
                        firejdList.clear();
                        firejdTypeList.clear();

                        firejdList = gson.fromJson(String.valueOf(data), new TypeToken<List<Lcprcwh>>() {
                        }.getType());
                        firejdTypeList.addAll(firejdList);
                        if (isShuaxin){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        initData();
                    }else {
                        Toast.makeText(LcprcwhListActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
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

        if (firejdTypeList.size() == 0){
            items.add(new Empty());
        }
        for (int i = 0; i < firejdTypeList.size(); i++) {
            items.add(firejdTypeList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        xmnameList =new ArrayList<>();
        xmidList=new ArrayList<>();
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("林产品日常维护");
        actionBar.setRightView("添加");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                      //  showLeibieChangeDailog();
                        Intent intent=new Intent(LcprcwhListActivity.this, AddlcprcyhActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
      //  actionBar.setRightView("全部");
        //  actionBar.setRightImage(R.drawable.ic_jia);
        //orderStateLayout = (LinearLayout) findViewById(R.id.order_state_layout);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fire_mission_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        //listView = (RecyclerView) findViewById(R.id.fire_mission_listview);

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
        binding.fireMissionListview.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);

        register();

        binding.fireMissionListview.setAdapter(adapter);
        assertHasTheSameAdapter(binding.fireMissionListview, adapter);
        RxViewAction.clickNoDouble(binding.orderStateLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showxxmcChangeDailog();
                    }
                });
    }

    private void register() {
        LcprcyhViewBinder lcprcyhViewBinder = new LcprcyhViewBinder();
        adapter.register(Lcprcwh.class, lcprcyhViewBinder);
        adapter.register(Empty.class, new EmptyViewBinder());
    }

    private void showxxmcChangeDailog() {

        int size=xmnameList.size();
        String[] items = (String[])xmnameList.toArray(new String[size]);
        builder = new AlertDialog.Builder(this).setTitle("项目名称")
                .setSingleChoiceItems(items, choose1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: 类别choose---" + i);
                        choose1 = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: "+i );
                        binding.orderStateView.setText(xmjdTypeList.get(choose1).getName());
                        Log.e(TAG, "onClick: "+xmidList.get(choose1) );
                        jdId = xmidList.get(choose1);
                        selectDataFromService();
                    }
                });
        builder.create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult:requestCode " + requestCode);
        Log.e(TAG, "onActivityResult:resultCode " +  resultCode);
        if (resultCode == ORDER_CHANGE) {
            Log.e(TAG, "onActivityResult: 111" );
            firejdList.clear();
            isShowDialog = true;
            getDataFromService();
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        getDataFromService();
    }
}
