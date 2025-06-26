package com.skyline.terraexplorer.mainapps.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.databinding.ActivityScxclistBinding;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.db.User;
import com.skyline.terraexplorer.mainapps.multitype.Bsfjjd;
import com.skyline.terraexplorer.mainapps.multitype.BsxhViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.Bsxhjd;
import com.skyline.terraexplorer.mainapps.multitype.BsysViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.Bsysjd;
import com.skyline.terraexplorer.mainapps.multitype.Empty;
import com.skyline.terraexplorer.mainapps.multitype.EmptyViewBinder;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;

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


public class BsxhListActivity extends BaseActivity implements BsxhViewBinder.OnBsxxItemClick{
    private static final String TAG = BsxhListActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private boolean isShowDialog = true;
    private boolean isShuaxin = false;
    private boolean isSearch = false;
    private List<Bsxhjd> firejdList;
    private List<Bsxhjd> firejdTypeList;
    public static int ORDER_CHANGE = 113;
    private int choose1 = 0;
    private AlertDialog.Builder builder;
    private int currentPage = 1;
    private int totalSize;
    private int lastPage;
    private int shipinChoose1 = 0;
    private User user;
    private String BX_ID;
    private ActivityScxclistBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_fanghuolist);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scxclist);
        progressDialog = new ProgressDialog(this);
        firejdList = new ArrayList<>();
        firejdTypeList = new ArrayList<>();
        user = new DbConfig(this).getUser();
        Intent intent = getIntent();
        BX_ID = intent.getStringExtra("BX_ID");
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
            dto.put("id", BX_ID);
            jsonObject.put("limit", 100);
            jsonObject.put("dto", dto);
            jsonObject.put("page", currentPage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/sickTreeDestructionInfo/page");
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

                        firejdList = gson.fromJson(String.valueOf(dataList), new TypeToken<List<Bsxhjd>>() {
                        }.getType());
                        firejdTypeList.addAll(firejdList);
                        if (isShuaxin){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        initData();
                    }else {
                        Toast.makeText(BsxhListActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
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
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("病树销毁列表");
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
                        Intent intent=new Intent(BsxhListActivity.this, AddbsxhActivity.class);
                        intent.putExtra("BX_ID",BX_ID);
                        startActivity(intent);
                        break;
                }
            }
        });
        binding.orderStateLayout.setVisibility(View.GONE);
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

    }

    private void register() {
        BsxhViewBinder bsxhViewBinder = new BsxhViewBinder();
        bsxhViewBinder.setListener(this);
        adapter.register(Bsxhjd.class, bsxhViewBinder);
        adapter.register(Empty.class, new EmptyViewBinder());

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

    @Override
    public void onBsxxItemClickListener(String bsxxjd) {
        Log.e(TAG, "onBsxxItemClickListener: "+bsxxjd );
//        Intent intent = new Intent(this, DetailbsxxActivity.class);
//        intent.putExtra("BX_ID",bsxxjd);
//        startActivity(intent);
    }
}
