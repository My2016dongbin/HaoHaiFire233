package com.skyline.terraexplorer.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.multitype.CheckList;
import com.skyline.terraexplorer.multitype.CheckListViewBinder;
import com.skyline.terraexplorer.utils.RequestUtils;

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

public class ComprehensiveCheckListActivity extends HBaseActivity implements CheckListViewBinder.OnCheckListItemClick{

    private static final String TAG = ComprehensiveCheckListActivity.class.getSimpleName();
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<CheckList> checkLists;
    private ProgressDialog progressDialog;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprehensive_check_list);

        checkLists = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromService();
    }

    private void initView() {
        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listView = (RecyclerView) findViewById(R.id.list_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

    }

    private void register() {
        CheckListViewBinder checkListViewBinder = new CheckListViewBinder();
        checkListViewBinder.setListener(this);
        adapter.register(CheckList.class, checkListViewBinder);
    }

    private void getDataFromService() {
        showDialogProgress(progressDialog,"加载中...");
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("createUser",new DbConfig(this).getUser().getFullName());

        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL_RESRCE + "api/generalCheck/list");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "postData:-- jsonObject.toString()--" + jsonObject.toString());
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);

                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("code").equals("200")) {
                        JSONArray data = object.getJSONArray("data");
                        checkLists.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject checkObj = data.getJSONObject(i);
                            String name = checkObj.getString("name");
                            String checkTime = checkObj.getString("checkTime");
                            String checkUser = checkObj.getString("checkUser");
                            String id = checkObj.getString("id");
                            checkLists.add(new CheckList(name,checkTime,checkUser,id));
                        }

                        initData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
        if (checkLists.size()>0){
            for (int i = 0; i < checkLists.size(); i++) {
                items.add(checkLists.get(i));
            }
        }

        if (items.size() == 0){
            Toast.makeText(this, "暂无检查记录", Toast.LENGTH_SHORT).show();

        }else {
            assertAllRegistered(adapter,items);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 综合检查列表的点击回调
     * @param id
     */
    @Override
    public void onCheckListItemClickListener(String id) {
        Intent intent = new Intent(getApplicationContext(), ComprehensiveCheckEditActivity.class);
        intent.putExtra("ID",id);
        startActivity(intent);
    }
}
