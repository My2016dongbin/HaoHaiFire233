package com.skyline.terraexplorer.wisdomgarden.ui.activity;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

public class BarChartActivity extends HhBaseActivity {

    private BarChart chart;
    private YAxis yAxis;
    private YAxis rightAxis; //右侧Y轴
    private XAxis xAxis; //X轴
    private Legend legend; //图例
    private LimitLine limitLine; //限制线
    private ActionBar actionBar;
    private List<String> getbarchartList;
    private List<String> getmfristData;
    private String barname;
    private TextView barNameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart_wisdomgarden);
        barNameText=findViewById(R.id.barNameText);
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("图表展示");
        actionBar.setRightView("其他图表");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        startActivity(new Intent(getApplicationContext(),OtherBarActivity.class));
                        break;
                }
            }
        });
        Intent intent =getIntent();
        getbarchartList=intent.getStringArrayListExtra("barchartList");
        Log.i("getbarchartList: ", String.valueOf(getbarchartList));
        getmfristData=intent.getStringArrayListExtra("mfristData");
        Log.i("getmfristData: ", String.valueOf(getmfristData));
        barname=intent.getStringExtra("barname");
        barNameText.setText(barname+getbarchartList.get(0));

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

        initData();

        chart.getLegend().setEnabled(false);
        chart.setScaleYEnabled(false);
        chart.setScaleXEnabled(false);
    }


    private void initData() {
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 1; i < getbarchartList.size(); i++) {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        chart.getViewPortHandler().refresh(new Matrix(), chart, true);
    }
}
