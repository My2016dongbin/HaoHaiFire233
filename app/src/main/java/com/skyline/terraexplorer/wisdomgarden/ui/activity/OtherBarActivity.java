package com.skyline.terraexplorer.wisdomgarden.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.base.HhBaseActivity;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.wisdomgarden.ui.multitype.ParkData;
import com.skyline.terraexplorer.wisdomgarden.ui.multitype.ParkDataViewBinder;
import com.skyline.terraexplorer.wisdomgarden.ui.multitype.QuData;
import com.skyline.terraexplorer.wisdomgarden.ui.multitype.QuDataViewBinder;
import com.skyline.terraexplorer.wisdomgarden.ui.multitype.YearData;
import com.skyline.terraexplorer.wisdomgarden.ui.multitype.YearDataViewBinder;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
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

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class OtherBarActivity extends HhBaseActivity implements YearDataViewBinder.OnYearItemClck, QuDataViewBinder.OnQuItemClck,ParkDataViewBinder.OnDataItemClck {

    private static final String TAG = ParkListActivity.class.getSimpleName();
    private RecyclerView listView;
    private RecyclerView dataListView;
    private RecyclerView quListView;
    private ActionBar actionBar;
    private List<Object> items = new ArrayList<>();
    private List<Object> dataItems = new ArrayList<>();
    private List<Object> quItems = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private MultiTypeAdapter dataAdapter;
    private MultiTypeAdapter quAdapter;
    private List<YearData> yearDataList;
    private List<ParkData> parkDataList;
    private List<QuData> quDataList;
    private ProgressDialog progressDialog;
    private TextView chakanButton;
    private String parkDataName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_bar);
        yearDataList = new ArrayList<>();
        parkDataList = new ArrayList<>();
        quDataList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);

        initView();
        getYearDataFromService();
        getParkData();
        getQuData();

    }

    private void getQuData() {
        quDataList.clear();
        QuData quData0 = new QuData("青岛市","001001",false);
        QuData quData1 = new QuData("市北区","001001002",false);
        QuData quData2 = new QuData("市南区","001001001",false);
        QuData quData3 = new QuData("城阳区","001001006",false);
        QuData quData4 = new QuData("高新区","001001007",false);
        QuData quData5 = new QuData("李沧区","001001003",false);
        QuData quData6 = new QuData("崂山区","001001005",false);
        QuData quData7 = new QuData("西海岸新区","001001004",false);
        QuData quData8 = new QuData("即墨区","001001008",false);
        QuData quData9 = new QuData("胶州市","001001009",false);
        QuData quData10 = new QuData("平度市","001001010",false);
        QuData quData11 = new QuData("莱西市","001001011",false);
        quDataList.add(quData1);
        quDataList.add(quData2);
        quDataList.add(quData3);
        quDataList.add(quData4);
        quDataList.add(quData5);
        quDataList.add(quData6);
        quDataList.add(quData7);
        quDataList.add(quData8);
        quDataList.add(quData9);
        quDataList.add(quData10);
        initQuData();
    }

    private void initQuData() {
        quItems.clear();

        for (int i = 0; i < quDataList.size(); i++) {
            quItems.add(quDataList.get(i));
        }
        assertAllRegistered(quAdapter,quItems);
        quAdapter.notifyDataSetChanged();
    }

    private void getParkData() {
        parkDataList.clear();
        parkDataList.add(new ParkData("城区暂住人口(万人)", "temporary_population","temporaryPopulation",true));
        parkDataList.add(new ParkData("城区人口(万人)", "county_population","countyPopulation", false));
        parkDataList.add(new ParkData("建成区面积(平方公里)", "built_area","builtArea", false));
        parkDataList.add(new ParkData("居住用地面积(平方公里)", "resident_area","residentArea", false));
        parkDataList.add(new ParkData("城区绿化覆盖面积(公顷)", "city_green_area","cityGreenArea",false));
        parkDataList.add(new ParkData("建成区绿化覆盖面积(公顷)", "built_green_area", "builtGreenArea",false));
        parkDataList.add(new ParkData("城区园林绿地面积(公顷)", "city_garden_green_area", "cityGardenGreenArea",false));
        parkDataList.add(new ParkData("建成区园林绿地面积(公顷)", "built_garden_green_area", "builtGardenGreenArea",false));
        parkDataList.add(new ParkData("城区公园绿地面积(公顷)", "city_park_green_area", "cityParkGreenArea",false));
        parkDataList.add(new ParkData("建城区公园绿地面积(公顷)", "built_park_green_area","builtParkGreenArea", false));
        parkDataList.add(new ParkData("城区公园绿地服务半径覆盖的居住用地面积(公顷)", "city_park_residential_area","cityParkResidentialArea", false));
        parkDataList.add(new ParkData("建成区公园绿地服务半径覆盖的居住用地面积(公顷)", "built_park_residential_area","builtParkResidentialArea", false));
        parkDataList.add(new ParkData("城区公园个数(个)", "city_park_count", "cityParkCount",false));
        parkDataList.add(new ParkData("建成区公园个数(个)", "built_park_count","builtParkCount", false));
        parkDataList.add(new ParkData("城区免费公园个数(个)", "city_free_park_count","cityFreeParkCount", false));
        parkDataList.add(new ParkData("绿道长度", "greenway_length","greenwayLength", false));
        parkDataList.add(new ParkData("城区公园面积(公顷)", "city_park_area", "cityParkArea",false));
        parkDataList.add(new ParkData("建成区公园面积(公顷)", "built_park_area","builtParkArea", false));
        parkDataList.add(new ParkData("人均公园绿地面积(平方米)", "per_park_green_area","perParkGreenArea", false));
        parkDataList.add(new ParkData("公园绿地服务半径覆盖率(%)", "park_green_radius_cover_rate", "parkGreenRadiusCoverRate",false));
        parkDataList.add(new ParkData("建成区绿化覆盖率(%)", "built_green_cover_rate","builtGreenCoverRate", false));
        parkDataList.add(new ParkData("建成区绿地率(%)", "built_green_land_rate","builtGreenLandRate", false));
        initParkData();
    }

    private void initParkData() {
        dataItems.clear();
        Log.e(TAG, "initParkData: " + parkDataList.size());
        for (int i = 0; i < parkDataList.size(); i++) {
            dataItems.add(parkDataList.get(i));
        }
        assertAllRegistered(dataAdapter,dataItems);
        dataAdapter.notifyDataSetChanged();
    }

    /**
     * cong 服务器获取年份年数据
     */
    private void getYearDataFromService() {
        showDialogProgress(progressDialog,"加载中...");


        HhRequestParams params = new HhRequestParams(RequestUtils.REQUEST_URL + "api/statistics/getYear");
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());

        Log.e(TAG, "park: --"  + params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        yearDataList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            if (i==0){
                                YearData yearData = new YearData(data.getString(i), data.getString(i), true);
                                yearDataList.add(yearData);
                            }else {
                                YearData yearData = new YearData(data.getString(i), data.getString(i), false);
                                yearDataList.add(yearData);
                            }

                        }

                        initData();
                    }else {
                        Toast.makeText(OtherBarActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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

        for (int i = 0; i < yearDataList.size(); i++) {
            items.add(yearDataList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("数据选择");
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
        chakanButton = (TextView) findViewById(R.id.chakan_button);

        RxViewAction.clickNoDouble(chakanButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        boolean isHasCheck = false;
                        for (int i = 0; i < quDataList.size(); i++) {
                            if (quDataList.get(i).isCheck){
                                isHasCheck = true;
                            }
                        }
                        if (isHasCheck){
                            getBarDataFromService();
                        }else {
                            Toast.makeText(OtherBarActivity.this, "请选择区", Toast.LENGTH_SHORT).show();
                        }

                       /* Intent intent = new Intent(getApplicationContext(), OtherBarChartActivity.class);
                        startActivity(intent);*/
                    }
                });

        //年份list
        listView = (RecyclerView) findViewById(R.id.year_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);
        adapter = new MultiTypeAdapter(items);
        YearDataViewBinder yearDataViewBinder = new YearDataViewBinder();
        yearDataViewBinder.setListener(this);
        adapter.register(YearData.class, yearDataViewBinder);
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

        //数据list
        dataListView = (RecyclerView) findViewById(R.id.data_listview);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dataListView.setLayoutManager(linearLayoutManager1);
        dataListView.setHasFixedSize(true);
        dataListView.setNestedScrollingEnabled(false);
        dataAdapter = new MultiTypeAdapter(dataItems);

        ParkDataViewBinder parkDataViewBinder = new ParkDataViewBinder();
        parkDataViewBinder.setListener(this);
        dataAdapter.register(ParkData.class, parkDataViewBinder);
        dataListView.setAdapter(dataAdapter);
        assertHasTheSameAdapter(dataListView, dataAdapter);
        //qu list
        quListView = (RecyclerView) findViewById(R.id.qu_listview);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        quListView.setLayoutManager(linearLayoutManager2);
        quListView.setHasFixedSize(true);
        quListView.setNestedScrollingEnabled(false);
        quAdapter = new MultiTypeAdapter(quItems);
        QuDataViewBinder quDataViewBinder = new QuDataViewBinder();
        quDataViewBinder.setListener(this);
        quAdapter.register(QuData.class, quDataViewBinder);
        quListView.setAdapter(quAdapter);
        assertHasTheSameAdapter(quListView, quAdapter);
    }

    /**
     * 获取图表数据
     */
    private void getBarDataFromService() {
        showDialogProgress(progressDialog,"数据获取中...");
        String yeatStr = "";
        for (int i = 0; i < yearDataList.size(); i++) {
            if (yearDataList.get(i).isCheck) {
                yeatStr = yearDataList.get(i).getYear();
            }
        }
        String parkDataStr = "" ;
        for (int i = 0; i < parkDataList.size(); i++) {
            if (parkDataList.get(i).isCheck) {
                parkDataStr = parkDataList.get(i).getDataStr();
                parkDataName = parkDataList.get(i).getDataName();
            }
        }
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < quDataList.size(); i++) {
                if (quDataList.get(i).isCheck){
                    JSONObject jsonObject = new JSONObject();
                        jsonObject.put("countyName",quDataList.get(i).getQuName());
                    jsonArray.put(jsonObject);
                }
            }
        } catch (JSONException e) {

        }
        HhRequestParams params = new HhRequestParams(RequestUtils.REQUEST_URL + "api/statistics/listNew");
        params.addParameter("name",parkDataStr);
        params.addParameter("year",yeatStr);
        params.setBodyContent(jsonArray.toString());
        params.addHeader("Authorization", "bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "park: --"  + params);
        Log.e(TAG, "park: --"  + jsonArray.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject1.getJSONArray("data");
                        ArrayList<String> countryNameList = new ArrayList<String>();
                        ArrayList<String> itemCountList = new ArrayList<String>();
                        for (int i = 0; i < data.length(); i++) {
                            String countyName = data.getJSONObject(i).getString("countyName");
                            String itemCount = data.getJSONObject(i).getString("itemCount");
                            countryNameList.add(countyName);
                            itemCountList.add(itemCount);
                        }

                        Intent intent=new Intent(getApplicationContext(), OtherBarChartActivity.class);
                        intent.putStringArrayListExtra("barchartList",itemCountList);
                        intent.putStringArrayListExtra("mfristData",countryNameList);
                        intent.putExtra("barname", parkDataName);
                        startActivity(intent);
                    }else {
                        Toast.makeText(OtherBarActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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

    /**
     * 年份的条目点击
     * @param yearData
     */
    @Override
    public void onYearItemClickListener(YearData yearData) {
        for (int i = 0; i < yearDataList.size(); i++) {
            if (yearData.getId().equals(yearDataList.get(i).getId())){
                yearDataList.get(i).setCheck(true);
            }else {
                yearDataList.get(i).setCheck(false);
            }
        }
        initData();
    }

    /**
     * =区数据的条目点击
     * @param quData
     */
    @Override
    public void onQuItemClickListener(QuData quData) {
        for (int i = 0; i < quDataList.size(); i++) {
           if (quData.getQuName().equals(quDataList.get(i).getQuName())){
               if (quData.isCheck){
                   quDataList.get(i).setCheck(false);
               }else {
                   quDataList.get(i).setCheck(true);
               }
           }
        }
        initQuData();
    }

    /**
     * 数据条目的点击
     * @param parkData
     */
    @Override
    public void onDataItemClickListener(ParkData parkData) {
        for (int i = 0; i < parkDataList.size(); i++) {
            if (parkData.getDataName().equals(parkDataList.get(i).getDataName())){
                parkDataList.get(i).setCheck(true);
            }else {
                parkDataList.get(i).setCheck(false);
            }
        }
        initParkData();
    }
}
