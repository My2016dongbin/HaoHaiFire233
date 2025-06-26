package com.skyline.terraexplorer.mainapps.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.multitype.BarListViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.LcpbarViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.Scjd;
import com.skyline.terraexplorer.mainapps.multitype.YllhBarList;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;
import com.skyline.terraexplorer.multitype.Lcpbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class LcpBarChartActivity extends BaseActivity  {
    private static final String TAG = LcpBarChartActivity.class.getSimpleName();
    private BarChart chart;
    private YAxis yAxis;
    private YAxis rightAxis; //右侧Y轴
    private XAxis xAxis; //X轴
    private Legend legend; //图例
    private LimitLine limitLine; //限制线
    private List<String> getbarchartList;
    private List<String> getmfristData;
    private TextView nianfenView;
    private TextView nianfenButton;
    private RecyclerView datalistView;
    private List<Object> items = new ArrayList<>();
    private List<Object> items1 = new ArrayList<>();
    private List<Object> dataItems = new ArrayList<>();
    private List<Object> quItems = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private MultiTypeAdapter adapter1;
    private ProgressDialog progressDialog;
    private boolean isHasData = false;
    private boolean isFirstData = true;
    private int barType = 0;  //0是年份多选  1是区多选
    private String barName = "";
    private String currentDataName;
    private String currentQuName;
    private String currentNianfenName;
    private TextView shengchengButton;
    private ImageView goback;
    private List<Lcpbar> lcpbarList;
    private TextView glView;
    private TextView xmView;
    private List<String> YearList;
    private AlertDialog.Builder builder;
    private int choose1 = 0;
    private int year;
    private int month;
    private int day;
    private double ncl=0;
    private double zmj=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcp_barchart);
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
        progressDialog = new ProgressDialog(this);
        initView();
        bindView();
        initDateTime();
        //getYearFromService();
        getDataFromService();
        initDateTime();
    }


    private void bindView() {
        RxViewAction.clickNoDouble(nianfenButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showYearDailog();
                    }
                });
        RxViewAction.clickNoDouble(goback)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onBackPressed();
                    }
                });
    }

    private void initView() {
        YearList=new ArrayList<>();
        shengchengButton = (TextView) findViewById(R.id.shengcheng_button);
        nianfenView = (TextView) findViewById(R.id.nianfen_view);
        nianfenButton = (TextView) findViewById(R.id.nianfen_button);
        goback = findViewById(R.id.backhome);
        xmView=(TextView) findViewById(R.id.shengcheng_button);
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
        datalistView = findViewById(R.id.datalist_view);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        datalistView.setLayoutManager(linearLayoutManager1);
        datalistView.setHasFixedSize(true);
        datalistView.setNestedScrollingEnabled(false);
        adapter1 = new MultiTypeAdapter(items1);
        LcpbarViewBinder lcpbarViewBinder = new LcpbarViewBinder();
        adapter1.register(Lcpbar.class, lcpbarViewBinder);
        datalistView.setAdapter(adapter1);
        assertHasTheSameAdapter(datalistView, adapter1);
    }

    private void initData() {
        items1.clear();
        //    barNameText.setText(barname);
//        getbarchartList.add("236.0");getbarchartList.add("249.0");getbarchartList.add("0.0");getbarchartList.add("0.0");
//        getmfristData.add("2018");getmfristData.add("2019");getmfristData.add("2020");getmfristData.add("2021");
//        Log.e(TAG, "initData: " + getbarchartList.size());
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < lcpbarList.size(); i++) {
            String val=(String.valueOf(lcpbarList.get(i).getAnnualOutput()));
            values.add(new BarEntry(i, Float.parseFloat(val)));
            getmfristData.add(lcpbarList.get(i).getRegion());
            items1.add(lcpbarList.get(i));
            assertAllRegistered(adapter1,items1);
            adapter1.notifyDataSetChanged();
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
    private void getDataFromService() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/forestProductInfo/dataStatistics");
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "postData:-- params--" + params);
        params.setConnectTimeout(10000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("200")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        Gson gson = new Gson();
                        lcpbarList = gson.fromJson(String.valueOf(data), new TypeToken<List<Lcpbar>>() {
                        }.getType());
                        initData();
                        for (int i = 0; i <lcpbarList.size() ; i++) {
                            ncl=ncl+lcpbarList.get(i).getAnnualOutput();
                            zmj=zmj+lcpbarList.get(i).getArea();
                        }
                        xmView.setText("总年产量"+ncl+"Kg，总面积"+zmj+"亩");
                    }else {
                        Toast.makeText(LcpBarChartActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException: "+e);
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
    private void getYearFromService() {
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/forestryDecision/forestryDecisionYears");
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        params.addParameter("type","yllh");
        params.addParameter("rootCode","370200000000");
        Log.e(TAG, "postData:-- params--" + params);
        params.setConnectTimeout(10000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("200")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            YearList.add(data.get(i).toString());
                        }
                        Log.e(TAG, "onSuccess: "+YearList.toString() );
                    }else {
                        Toast.makeText(LcpBarChartActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException: "+e);
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
    private void showYearDailog() {

        int size=YearList.size();
        String[] items = (String[])YearList.toArray(new String[size]);
        builder = new AlertDialog.Builder(this).setTitle("年份")
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
                        nianfenView.setText(YearList.get(choose1));
                        getDataFromService();
                    }
                });
        builder.create().show();
    }
    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.e(TAG, "initDateTime: "+year );
        nianfenView.setText(String.valueOf(year));
    }
}
