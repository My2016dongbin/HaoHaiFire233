package com.skyline.terraexplorer.wisdomgarden.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.base.HhBaseActivity;
import com.skyline.terraexplorer.wisdomgarden.ui.adapter.infoViewAdapter;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.wisdomgarden.ui.model.CityInfo;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.request.HhRequestParams;
import com.ruyiruyi.rylibrary.request.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class CityInfoActivity extends HhBaseActivity {
    private LinearLayout minfoView;
    private static final String TAG = "CityInfoActivity";
    private RecyclerView mRecycleView;
    private infoViewAdapter mAdapter;//适配器
    private LinearLayoutManager mLinearLayoutManager;//布局管理器
    private ActionBar actionBar;
    private TextView citybutton;
    private ProgressDialog progressDialog;
    /**
     * 全局数据容器
     */
    private List<CityInfo> mList;
    private List<CityInfo> mListNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        progressDialog = new ProgressDialog(this);
        initView();
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("区市详情列表");
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
        citybutton=findViewById(R.id.city_button);
        mList = new ArrayList();
        mListNew = new ArrayList();
        mRecycleView = findViewById(R.id.city_recycle);
        //初始化数据
        initData();
        //创建布局管理器，垂直设置LinearLayoutManager.VERTICAL，水平设置LinearLayoutManager.HORIZONTAL
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //创建适配器，将数据传递给适配器
        mAdapter = new infoViewAdapter(mList);
        //设置布局管理器
        mRecycleView.setLayoutManager(mLinearLayoutManager);
        //设置适配器adapter
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }
    public void initData() {
        showDialogProgress(progressDialog,"加载中...");
        JSONObject qujson = new JSONObject();
      /*  try {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            qujson.put("year", year);
        } catch (JSONException e) {
        }*/
        HhRequestParams quparams = new HhRequestParams(RequestUtils.REQUEST_URL + "api/statistics/getStatiscs");
        quparams.setAsJsonContent(true);
       // quparams.setBodyContent(qujson.toString());
        quparams.addHeader("Authorization", "bearer " + new DbConfig(getApplication()).getUser().getToken());
        Log.i(TAG, "quparams: "+quparams);
        x.http().get(quparams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
                JSONObject qujson0bject = null;
                try {
                    qujson0bject = new JSONObject(result);
                    JSONArray qudata = qujson0bject.getJSONArray("data");
                    JSONArray dataLists = null;
                    mList.clear();
                    mListNew.clear();
                    for(int i=0; i<qudata.length(); i++){
                        JSONObject jsonObject = qudata.getJSONObject(i);
                        CityInfo cityInfo = new CityInfo();
                        Log.i(TAG, "jsonObject: "+jsonObject.getString("builtArea"));
                        if (jsonObject.getString("builtArea")!="null"){
                            cityInfo.setBuiltArea("建成区面积(公顷)："+jsonObject.getString("builtArea"));
                        }else {
                            cityInfo.setBuiltArea("建成区面积(公顷)：");
                        }
                        if (jsonObject.getString("countyPopulation")!="null"){
                            cityInfo.setCountyPopulation("人口(万)："+jsonObject.getString("countyPopulation"));
                        }else {
                            cityInfo.setBuiltArea("人口(万)：");
                        }
                        if (jsonObject.getString("countyName")!=null){
                            cityInfo.setCountyName(jsonObject.getString("countyName"));
                        }else {
                            cityInfo.setBuiltArea("");
                        }
                        mListNew.add(cityInfo);
                    }
                    initQuData();
                    Log.i(TAG, "mList: "+mList.toString());

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
//        for (int i = 1; i <= 20; i++) {
//            list.add("第" + i + "条数据");
//        }
    }

    private void initQuData() {
        for (int i = 0; i < mListNew.size(); i++) {
            if (mListNew.get(i).getCountyName().equals("青岛市")) {
                mList.add(mListNew.get(i));
            }
        }
        for (int i = 0; i < mListNew.size(); i++) {
            if (mListNew.get(i).getCountyName().equals("市南区")) {
                mList.add(mListNew.get(i));
            }
        }
        for (int i = 0; i < mListNew.size(); i++) {
            if (mListNew.get(i).getCountyName().equals("市北区")) {
                mList.add(mListNew.get(i));
            }
        }
        for (int i = 0; i < mListNew.size(); i++) {
            if (mListNew.get(i).getCountyName().equals("李沧区")) {
                mList.add(mListNew.get(i));
            }
        }
        for (int i = 0; i < mListNew.size(); i++) {
            if (mListNew.get(i).getCountyName().equals("崂山区")) {
                mList.add(mListNew.get(i));
            }
        }
        for (int i = 0; i < mListNew.size(); i++) {
            if (mListNew.get(i).getCountyName().equals("西海岸新区")) {
                mList.add(mListNew.get(i));
            }
        }
        for (int i = 0; i < mListNew.size(); i++) {
            if (mListNew.get(i).getCountyName().equals("城阳区")) {
                mList.add(mListNew.get(i));
            }
        }
        for (int i = 0; i < mListNew.size(); i++) {
            if (mListNew.get(i).getCountyName().equals("即墨区")) {
                mList.add(mListNew.get(i));
            }
        }
        for (int i = 0; i < mListNew.size(); i++) {
            if (mListNew.get(i).getCountyName().equals("胶州市")) {
                mList.add(mListNew.get(i));
            }
        }
        for (int i = 0; i < mListNew.size(); i++) {
            if (mListNew.get(i).getCountyName().equals("平度市")) {
                mList.add(mListNew.get(i));
            }
        }
        for (int i = 0; i < mListNew.size(); i++) {
            if (mListNew.get(i).getCountyName().equals("莱西市")) {
                mList.add(mListNew.get(i));
            }
        }
        for (int i = 0; i < mListNew.size(); i++) {
            if (mListNew.get(i).getCountyName().equals("高新区")) {
                mList.add(mListNew.get(i));
            }
        }

        mAdapter.notifyDataSetChanged();
    }
}
