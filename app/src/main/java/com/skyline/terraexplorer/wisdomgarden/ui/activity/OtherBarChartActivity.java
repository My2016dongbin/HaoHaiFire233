package com.skyline.terraexplorer.wisdomgarden.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class OtherBarChartActivity extends HhBaseActivity implements YearDataViewBinder.OnYearItemClck, QuDataViewBinder.OnQuItemClck, ParkDataViewBinder.OnDataItemClck  {
    private static final String TAG = OtherBarChartActivity.class.getSimpleName();
    private BarChart chart;
    private YAxis yAxis;
    private YAxis rightAxis; //右侧Y轴
    private XAxis xAxis; //X轴
    private Legend legend; //图例
    private LimitLine limitLine; //限制线
    private ActionBar actionBar;
    private List<String> getbarchartList;
    private List<String> getmfristData;
    private TextView barNameText;
    private TextView nianfenView;
    private TextView nianfenButton;
    private TextView shujuView;
    private TextView shujuButton;
    private TextView chengquView;
    private TextView chengquButton;
    private View nianfenListInflater;
    private Dialog nianfenListDialog;
    private RecyclerView listView;
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
    private Dialog parkDataDialog;
    private View parkDataInflater;
    private RecyclerView parkDataListView;
    private Dialog quDialog;
    private View quInflater;
    private RecyclerView quListView;
    private boolean isHasData = false;
    private boolean isFirstData = true;
    private int barType = 0;  //0是年份多选  1是区多选
    private String barName = "";
    private String currentDataName;
    private String currentQuName;
    private String currentNianfenName;
    private TextView shengchengButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_bar_chart);
        getbarchartList = new ArrayList<>();
        getmfristData = new ArrayList<>();
/*        Intent intent =getIntent();
        getbarchartList=intent.getStringArrayListExtra("barchartList");
        getmfristData=intent.getStringArrayListExtra("mfristData");
        barname=intent.getStringExtra("barname");*/
        Intent intent = getIntent();
        isHasData = intent.getBooleanExtra("isHasData",false);
        currentDataName = intent.getStringExtra("parkData");
        currentQuName = intent.getStringExtra("quData");

        Log.e(TAG, "onCreate:isHasData= " +isHasData );

        progressDialog = new ProgressDialog(this);
        yearDataList = new ArrayList<>();
        parkDataList = new ArrayList<>();
        quDataList = new ArrayList<>();


        initView();
        bindView();

      //  initData();
        getQuData();
        getParkData();
        getYearDataFromService();




    }

    /**
     * 获取不同年份的数据
     */
    private void getYearTypeDataFromService() {
        showDialogProgress(progressDialog,"数据获取中...");
        JSONObject qujson = new JSONObject();
        try {
            qujson.put("countyName", currentQuName);
        } catch (JSONException e) {
        }
        RequestParams quparams = new RequestParams(RequestUtils.REQUEST_URL + "api/statistics/list");
        quparams.setAsJsonContent(true);
        quparams.setBodyContent(qujson.toString());
        quparams.addHeader("Authorization", "bearer " + new DbConfig(getApplication()).getUser().getToken());
        x.http().post(quparams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    String parkDataStr = "";
                    for (int i = 0; i < parkDataList.size(); i++) {
                        if (parkDataList.get(i).getDataName().equals(currentDataName)) {
                            parkDataStr = parkDataList.get(i).getDataJson();
                        }
                    }
                    Log.e(TAG, "onSuccess: parkDataStr" +parkDataStr);
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")){
                        JSONArray data = jsonObject.getJSONArray("data");
                        getbarchartList.clear();
                        getmfristData.clear();
                        for (int i = 0; i < yearDataList.size(); i++) {
                            if (yearDataList.get(i).isCheck()) {
                                getbarchartList.add("0.0");
                                getmfristData.add(yearDataList.get(i).getYear());
                            }
                        }
                        for (int i = 0; i < data.length(); i++) {
                            String year = data.getJSONObject(i).getString("year");
                            //如果当前年份已被选中
                            for (int j = 0; j < getmfristData.size(); j++) {
                                if (getmfristData.get(j).equals(year)) {
                                    getbarchartList.set(j,data.getJSONObject(i).getString(parkDataStr).equals("null")? "0.0":data.getJSONObject(i).getString(parkDataStr) );

                                    /*if (yearDataList.get(j).isCheck){
                                        Log.e(TAG, "onSuccess: parkDataStr" +data.getJSONObject(i).getString(parkDataStr) );
                                        Log.e(TAG, "onSuccess: year" +data.getJSONObject(i).getString("year") );
                                        getbarchartList.add(data.getJSONObject(i).getString(parkDataStr).equals("null")? "0.0":data.getJSONObject(i).getString(parkDataStr) );
                                        getmfristData.add(data.getJSONObject(i).getString("year").equals("null")? "0.0" : data.getJSONObject(i).getString("year") );

                                    }*/
                                }
                            }

                        }
                        initData();
                    }else {
                        Toast.makeText(OtherBarChartActivity.this, "数据获取错误", Toast.LENGTH_SHORT).show();
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

    private void bindView() {
        RxViewAction.clickNoDouble(shengchengButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        int quIscheckNum = 0;
                        for (int i = 0; i < quDataList.size(); i++) {
                            if (quDataList.get(i).isCheck){
                                quIscheckNum = quIscheckNum + 1;
                            }
                        }
                        int yearIsCheckNum = 0;
                        for (int i = 0; i < yearDataList.size(); i++) {
                            if (yearDataList.get(i).isCheck()) {
                                yearIsCheckNum = yearIsCheckNum + 1;
                            }
                        }
                        Log.e(TAG, "call:quIscheckNum= " + quIscheckNum);
                        if (quIscheckNum > 1){  //同一年份多个区
                            barType = 1;
                            getBarDataFromService();

                            barNameText.setText(currentNianfenName+"年" + currentDataName +"图表");
                        }else {         //年份多选
                            barType = 0;
                            getYearTypeDataFromService();
                            if (yearIsCheckNum == 1){
                                barNameText.setText(currentNianfenName+"年" + currentQuName + currentDataName +"图表");
                            }else {
                                barNameText.setText(currentQuName + currentDataName +"图表");
                            }
                           // barNameText.setText(currentQuName + currentDataName +"图表");
                        }


                    }
                });
        RxViewAction.clickNoDouble(nianfenButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        nianfenListDialog.show();
                    }
                });
        RxViewAction.clickNoDouble(shujuButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        parkDataDialog.show();
                    }
                });
        RxViewAction.clickNoDouble(chengquButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        quDialog.show();
                    }
                });
    }

    /**
     * 获取同一年份多个区的数据
     */
    private void getBarDataFromService() {
        showDialogProgress(progressDialog,"图表生成中...");
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
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/statistics/listNew");
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
                        getbarchartList.clear();
                        getmfristData.clear();
                        for (int i = 0; i < quDataList.size(); i++) {
                            if (quDataList.get(i).isCheck){
                                getmfristData.add(quDataList.get(i).getQuName());
                                getbarchartList.add("0.0");
                            }

                        }
                        for (int i = 0; i < data.length(); i++) {
                            for (int j = 0; j < getmfristData.size(); j++) {
                                if (getmfristData.get(j).equals(data.getJSONObject(i).getString("countyName"))) {
                                    getbarchartList.set(j,data.getJSONObject(i).getString("itemCount"));
                                }
                            }
                           /* String countyName = data.getJSONObject(i).getString("countyName");
                            String itemCount = data.getJSONObject(i).getString("itemCount");
                            getmfristData.add(countyName);
                            getbarchartList.add(itemCount);*/
                        }

                        initData();
                    }else {
                        Toast.makeText(OtherBarChartActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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

    private void initView() {
        barNameText=findViewById(R.id.barNameText);
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("图表展示");
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

        shengchengButton = (TextView) findViewById(R.id.shengcheng_button);
        nianfenView = (TextView) findViewById(R.id.nianfen_view);
        nianfenButton = (TextView) findViewById(R.id.nianfen_button);
        shujuView = (TextView) findViewById(R.id.shuju_view);
        shujuButton = (TextView) findViewById(R.id.shuju_button);
        chengquView = (TextView) findViewById(R.id.chengqu_view);
        chengquButton = (TextView) findViewById(R.id.chengqu_button);

        /**
         * 年份列表fdialog
         */
        nianfenListDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        nianfenListInflater = LayoutInflater.from(this).inflate(R.layout.dialog_nianfen_list_wisdomgarden, null);
        nianfenListInflater.setMinimumWidth(10000);
        listView = ((RecyclerView) nianfenListInflater.findViewById(R.id.nianfen_listview));
        nianfenListDialog.setContentView(nianfenListInflater);
        Window nianfenListWindow = nianfenListDialog.getWindow();
        nianfenListWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams nianfenListLp = nianfenListWindow.getAttributes();

        WindowManager obwm = (WindowManager)
                getSystemService(Context.WINDOW_SERVICE);
        int nianfenheight = obwm.getDefaultDisplay().getHeight();
        nianfenListLp.height = (int) (nianfenheight * 0.8);
        nianfenListWindow.setAttributes(nianfenListLp);
        nianfenListDialog.setCanceledOnTouchOutside(true);

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
        /**
         * 数据列表dialog
         */

        parkDataDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        parkDataInflater = LayoutInflater.from(this).inflate(R.layout.dialog_parkdata_list, null);
        parkDataInflater.setMinimumWidth(10000);
        parkDataListView = ((RecyclerView) parkDataInflater.findViewById(R.id.parkdata_listview));
        parkDataDialog.setContentView(parkDataInflater);
        Window parkDataWindow = parkDataDialog.getWindow();
        parkDataWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams parkDataLp = parkDataWindow.getAttributes();

        int parkDataheight = obwm.getDefaultDisplay().getHeight();
        parkDataLp.height = (int) (parkDataheight * 0.8);
        parkDataWindow.setAttributes(parkDataLp);
        parkDataDialog.setCanceledOnTouchOutside(true);
        //数据list
        LinearLayoutManager linearLayoutManager11 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        parkDataListView.setLayoutManager(linearLayoutManager11);
        parkDataListView.setHasFixedSize(true);
        parkDataListView.setNestedScrollingEnabled(false);
        dataAdapter = new MultiTypeAdapter(dataItems);

        ParkDataViewBinder parkDataViewBinder = new ParkDataViewBinder();
        parkDataViewBinder.setListener(this);
        dataAdapter.register(ParkData.class, parkDataViewBinder);
        parkDataListView.setAdapter(dataAdapter);
        assertHasTheSameAdapter(parkDataListView, dataAdapter);
        /**
         * 区列表Dialog
         */
        quDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        quInflater = LayoutInflater.from(this).inflate(R.layout.dialog_qu_list, null);
        quInflater.setMinimumWidth(10000);
        quListView = ((RecyclerView) quInflater.findViewById(R.id.qu_listview));
        quDialog.setContentView(quInflater);
        Window quWindow = quDialog.getWindow();
        quWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams quLp = quWindow.getAttributes();

        int quheight = obwm.getDefaultDisplay().getHeight();
        quLp.height = (int) (quheight * 0.8);
        quWindow.setAttributes(quLp);
        quDialog.setCanceledOnTouchOutside(true);
        //qu list
        LinearLayoutManager linearLayoutManager12 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        quListView.setLayoutManager(linearLayoutManager12);
        quListView.setHasFixedSize(true);
        quListView.setNestedScrollingEnabled(false);
        quAdapter = new MultiTypeAdapter(quItems);
        QuDataViewBinder quDataViewBinder = new QuDataViewBinder();
        quDataViewBinder.setListener(this);
        quAdapter.register(QuData.class, quDataViewBinder);
        quListView.setAdapter(quAdapter);
        assertHasTheSameAdapter(quListView, quAdapter);




        chart = (BarChart)findViewById(R.id.chart1);
        chart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        xAxis = chart.getXAxis();
        xAxis.setLabelRotationAngle(-60);
        xAxis.setLabelCount(20);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);       //y轴的数值显示在外侧
        yAxis.setAxisMinimum(0f); ////为这个轴设置一个自定义的最小值。如果设置,这个值不会自动根据所提供的数据计算
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);

        // add a nice and smooth animation
        //chart.animateY(1500);

        //initData();

        chart.getLegend().setEnabled(false);
        chart.setScaleYEnabled(true);
        chart.setScaleXEnabled(true);
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
        quDataList.add(quData0);
        quDataList.add(quData2);
        quDataList.add(quData1);
        quDataList.add(quData5);
        quDataList.add(quData6);
        quDataList.add(quData7);
        quDataList.add(quData3);
        quDataList.add(quData8);
        quDataList.add(quData9);
        quDataList.add(quData10);
        quDataList.add(quData11);
        quDataList.add(quData4);

        chengquView.setText(currentQuName);
        for (int i = 0; i < quDataList.size(); i++) {
            if (quDataList.get(i).getQuName().equals(currentQuName)) {
                quDataList.get(i).setCheck(true);
            }else {
                quDataList.get(i).setCheck(false);
            }
        }
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
        shujuView.setText(currentDataName);
        for (int i = 0; i < parkDataList.size(); i++) {
            if (parkDataList.get(i).getDataName().equals(currentDataName)) {
                parkDataList.get(i).setCheck(true);
            }else {
                parkDataList.get(i).setCheck(false);
            }
        }
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
                            YearData yearData = new YearData(data.getString(i), data.getString(i), true);
                            yearDataList.add(yearData);
                        }

                        String yearChooseStr = "";
                        for (int i = 0; i < yearDataList.size(); i++) {
                            if (yearDataList.get(i).isCheck){
                                yearChooseStr = yearChooseStr + yearDataList.get(i).getYear() + ",";
                            }
                        }
                        currentNianfenName = yearChooseStr.substring(0,yearChooseStr.length()-1);
                        barName =  currentQuName + currentDataName + "图表";
                        barNameText.setText(barName);
                        nianfenView.setText(yearChooseStr.substring(0,yearChooseStr.length()-1));


                        //第一次进来 加载图表数据
                        if (isFirstData){
                            isFirstData = false;
                            barType = 0;
                            getYearTypeDataFromService();
                        }
                        initYearData();
                    }else {
                        Toast.makeText(OtherBarChartActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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

    private void initYearData() {
        items.clear();
        for (int i = 0; i < yearDataList.size(); i++) {
            items.add(yearDataList.get(i));
        }
        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    private void initData() {
    //    barNameText.setText(barname);

        Log.e(TAG, "initData: " + getbarchartList.size());
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < getbarchartList.size(); i++) {
            Log.i("initData: ", String.valueOf(getbarchartList.get(i).equals(" ")));
            String val=(getbarchartList.get(i).equals(" "))? String.valueOf(0) :getbarchartList.get(i);
            values.add(new BarEntry(i, Float.parseFloat(val)));
        }
        BarDataSet set1;
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getmfristData) );
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setBarWidth(0.4f);
            chart.setData(data);
            chart.setFitBars(true);
        }
        chart.getViewPortHandler().refresh(new Matrix(), chart, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chart.getViewPortHandler().refresh(new Matrix(), chart, true);
    }

    /**
     * 年份的Item点击
     * @param yearData
     */
    @Override
    public void onYearItemClickListener(YearData yearData) {
        //判断年份不能全部取消
        int yearIscheckNum = 0;
        for (int i = 0; i < yearDataList.size(); i++) {
            if (yearDataList.get(i).isCheck){
                yearIscheckNum = yearIscheckNum + 1;
            }
        }
        if (yearIscheckNum == 1 && yearData.isCheck == true){
            return;
        }

        //判断年份 跟区域是否多选
        int quIscheckNum = 0;
        for (int i = 0; i < quDataList.size(); i++) {
            if (quDataList.get(i).isCheck){
                quIscheckNum = quIscheckNum + 1;
            }
        }

        String yearChooseStr = "";
        for (int i = 0; i < yearDataList.size(); i++) {
            if (quIscheckNum > 1){  //区域多选 年份单选
                if (yearData.getId().equals(yearDataList.get(i).getId())){
                    yearDataList.get(i).setCheck(true);
                }else {
                    yearDataList.get(i).setCheck(false);
                }
            }else {     //区域单选 年份多选
                if (yearData.getId().equals(yearDataList.get(i).getId())){
                    if (yearData.isCheck){
                        yearDataList.get(i).setCheck(false);
                    }else {
                        yearDataList.get(i).setCheck(true);
                    }
                }
            }


            if (yearDataList.get(i).isCheck){
                yearChooseStr = yearChooseStr + yearDataList.get(i).getYear() + ",";
            }

        }
        currentNianfenName = yearChooseStr.substring(0,yearChooseStr.length()-1);
        nianfenView.setText(currentNianfenName);
        initYearData();
    }

    /**
     * 区域的Item点击
     * @param quData
     */
    @Override
    public void onQuItemClickListener(QuData quData) {
        //判断区域不能全部取消
        int quIscheckNum = 0;
        for (int i = 0; i < quDataList.size(); i++) {
            if (quDataList.get(i).isCheck){
                quIscheckNum = quIscheckNum + 1;
            }
        }
        if (quIscheckNum == 1 && quData.isCheck == true){
            return;
        }
        //判断年份 跟区域是否多选
        int yearIscheckNum = 0;
        for (int i = 0; i < yearDataList.size(); i++) {
            if (yearDataList.get(i).isCheck){
                yearIscheckNum = yearIscheckNum + 1;
            }
        }
        String quStr = "";
        for (int i = 0; i < quDataList.size(); i++) {
            if (yearIscheckNum > 1){    //年份多选  区域要单选
                if (quData.getQuName().equals(quDataList.get(i).getQuName())){
                    quDataList.get(i).setCheck(true);
                }else {
                    quDataList.get(i).setCheck(false);
                }
            }else {     //年份单选 区域多选
                if (quData.getQuName().equals(quDataList.get(i).getQuName())){

                    if (quData.isCheck){
                        quDataList.get(i).setCheck(false);
                    }else {
                        quDataList.get(i).setCheck(true);
                    }
                }
            }

            if (quDataList.get(i).isCheck){
                quStr = quStr + quDataList.get(i).getQuName() + ",";
            }
        }
        currentQuName = quStr.substring(0,quStr.length()-1);
        chengquView.setText(currentQuName);
        initQuData();
    }

    /**
     * 数据的item点击
     * @param parkData
     */
    @Override
    public void onDataItemClickListener(ParkData parkData) {
        for (int i = 0; i < parkDataList.size(); i++) {
            if (parkData.getDataName().equals(parkDataList.get(i).getDataName())){
                currentDataName = parkDataList.get(i).getDataName();
                parkDataList.get(i).setCheck(true);
                shujuView.setText(parkDataList.get(i).getDataName());
            }else {
                parkDataList.get(i).setCheck(false);
            }
        }
        initParkData();
    }
}
