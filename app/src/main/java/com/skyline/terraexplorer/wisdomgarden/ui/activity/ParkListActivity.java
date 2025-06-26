package com.skyline.terraexplorer.wisdomgarden.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.base.HhBaseActivity;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.wisdomgarden.ui.multitype.Park;
import com.skyline.terraexplorer.wisdomgarden.ui.multitype.ParkViewBinder;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.request.HhRequestParams;
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

public class ParkListActivity extends HhBaseActivity implements ParkViewBinder.OnPatkItemClck {

    private static final String TAG = ParkListActivity.class.getSimpleName();
    private RecyclerView listView;
    private ActionBar actionBar;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private List<Park> parkList;
    private ProgressDialog progressDialog;
    public static final int SEARCH_CODE = 6;
    private String searchStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_list);
        parkList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);

        initView();
        getDataFromService();
    }

    /**
     * 获取公园列表数据
     */
    private void getDataFromService() {
        showDialogProgress(progressDialog,"加载中...");
        //获取公园信息
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject dto = new JSONObject();
            dto.put("name", searchStr);
            jsonObject.put("dto", dto);
            jsonObject.put("limit", 150);
            jsonObject.put("page", 1);
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/garden/page");
        params.setAsJsonContent(true);
        Log.e(TAG, "getDataFromService: " + jsonObject.toString());
        Log.e(TAG, "getDataFromService: " +  Base64.encodeToString(jsonObject.toString().getBytes(), Base64.DEFAULT));

        params.setBodyContent(jsonObject.toString());

        Log.e(TAG, "getDataFromService: " + jsonObject.toString());
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "bearer: --"  + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "park: --"  + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONObject data = jsonObject1.getJSONArray("data").getJSONObject(0);
                        JSONArray dataList = data.getJSONArray("dataList");
                        Gson gson = new Gson();
                        parkList = gson.fromJson(String.valueOf(dataList), new TypeToken<List<Park>>(){}.getType());

                        initData();
                    }else {
                        Toast.makeText(ParkListActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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

        for (int i = 0; i < parkList.size(); i++) {
            items.add(parkList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("公园列表");
        actionBar.setRightImage(R.drawable.ic_search);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -2:
                        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                        intent.putExtra(SearchActivity.TYPE,0);
                        startActivityForResult(intent,SEARCH_CODE);
                        break;
                }
            }
        });
        listView = (RecyclerView) findViewById(R.id.park_listview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);

        ParkViewBinder parkViewBinder = new ParkViewBinder();
        parkViewBinder.setListener(this);
        adapter.register(Park.class, parkViewBinder);
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    /**
     * 公园条目的点击事件
     * @param id
     */
    @Override
    public void onParkItemClickListener(String id) {
        Intent intent = new Intent(this, ParkActivity.class);
        intent.putExtra("PARK_ID",id);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SEARCH_CODE){
            //搜索结果的返回
            searchStr = data.getStringExtra("SEARCH_STR");

            getDataFromService();
        }
    }
}
