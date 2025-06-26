package com.skyline.terraexplorer.wisdomgarden.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.base.HhBaseActivity;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.wisdomgarden.utils.PrefsManager;
import com.rmondjone.locktableview.DisplayUtil;
import com.rmondjone.locktableview.LockTableView;
import com.rmondjone.xrecyclerview.ProgressStyle;
import com.rmondjone.xrecyclerview.XRecyclerView;
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

public class QuActivity extends HhBaseActivity {
    private LinearLayout mContentView;
    private ImageView backButton;
    private static final String TAG = "quActivity";
    private TextView quTitle;
    private ActionBar actionBar;
    private List<String> numList;
    private PopupWindow popWin;
    private Boolean isDown=false;//判断弹窗是否显示
    private ListView listView;
    private List<List<String>> barchartList;
    private List<List<String>> dataListList;
    private String localname;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qu);
        mContentView = (LinearLayout) findViewById(R.id.contentView);
        dataListList = new ArrayList<>();
        Intent intent =getIntent();
        localname = intent.getStringExtra("localname");
        Log.i(TAG, "localname: "+localname);
        initView();
        initListView();
        initDisplayOpinion();
    }
    //点击返回按钮
    @Override
    public void onBackPressed() {
        if(popWin!=null&&popWin.isShowing()){
            popWin.dismiss();
        }
        super.onBackPressed();
    }
    private void initListView() {
        listView = new ListView(this);
        //设置listView的背景
        listView.setBackgroundResource(R.color.white);
        //设置条目之间的分割线及滚动条不可见
        listView.setDivider(null);
        listView.setVerticalScrollBarEnabled(false);
        //设置适配器
        listView.setAdapter(new MyListAdapter());
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        //创建集合 储存号码
        numList = new ArrayList<String>();
        numList.add("青岛市");
        numList.add("市南区");
        numList.add("市北区");
        numList.add("李沧区");
        numList.add("崂山区");
        numList.add("西海岸新区");
        numList.add("城阳区");
        numList.add("即墨区");
        numList.add("胶州市");
        numList.add("平度市");
        numList.add("莱西市");
        numList.add("高新区");

        actionBar = (ActionBar) findViewById(R.id.action_bar);
        if (localname!=null){
            actionBar.setTitle(localname+"详情");
        }else {
            Log.i(TAG, "详情: "+PrefsManager.getpoint().district);
            actionBar.setTitle(PrefsManager.getpoint().district+"详情");
        }
        actionBar.setRightView("切换区市");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        if(!isDown){
                            popWin=new PopupWindow(QuActivity.this);
                            popWin.setWidth(1000);//设置宽度
                            popWin.setHeight(1200); //设置高度
                            //为popWin填充内容
                            popWin.setContentView(listView);
                            //点击popWin区域之外 自动关闭popWin
                            popWin.setOutsideTouchable(true);
                            popWin.showAsDropDown(actionBar, 1500, 0);
                            isDown=true;
                        }else{
                            popWin.dismiss();
                            isDown=false;
                        }
                        break;
                }
            }
        });
        //区市详情
        final ArrayList<String> mfristData = new ArrayList<String>();
        ArrayList<String> peopleData = new ArrayList<String>();
        peopleData.add("城区人口(万人)");
        ArrayList<String> zanzhupeopleData = new ArrayList<String>();
        zanzhupeopleData.add("城区暂住人口(万人)");
        ArrayList<String> jcqareaData = new ArrayList<String>();
        jcqareaData.add("建成区面积(平方公里)");
        ArrayList<String> jzareaData = new ArrayList<String>();
        jzareaData.add("居住用地面积(平方公里)");
        ArrayList<String> cqgreenData = new ArrayList<String>();
        cqgreenData.add("城区绿化覆盖面积(公顷)");
        ArrayList<String> jcqgreenData = new ArrayList<String>();
        jcqgreenData.add("建成区绿化覆盖面积(公顷)");
        ArrayList<String> cqgardensData = new ArrayList<String>();
        cqgardensData.add("城区园林绿地面积(公顷)");
        ArrayList<String> jcqgardensData = new ArrayList<String>();
        jcqgardensData.add("建成区园林绿地面积(公顷)");
        ArrayList<String> qcparkData = new ArrayList<String>();
        qcparkData.add("城区公园绿地面积(公顷)");
        ArrayList<String> jcqparkData = new ArrayList<String>();
        jcqparkData.add("建城区公园绿地面积(公顷)");
        ArrayList<String> cqparkradiusData = new ArrayList<String>();
        cqparkradiusData.add("城区公园绿地服务半径覆盖的居住用地面积(公顷)");
        ArrayList<String> jcqparkradiusData = new ArrayList<String>();
        jcqparkradiusData.add("建成区公园绿地服务半径覆盖的居住用地面积(公顷)");
        ArrayList<String> cqparknumData = new ArrayList<String>();
        cqparknumData.add("城区公园个数(个)");
        ArrayList<String> jcqparknumData = new ArrayList<String>();
        jcqparknumData.add("建成区公园个数(个)");
        ArrayList<String> cqparkfreenumData = new ArrayList<String>();
        cqparkfreenumData.add("城区免费公园个数(个)");
        ArrayList<String> ldlangData = new ArrayList<String>();
        ldlangData.add("绿道长度");
        ArrayList<String> cqparkareaData = new ArrayList<String>();
        cqparkareaData.add("城区公园面积(公顷)");
        ArrayList<String> jcqparkareaData = new ArrayList<String>();
        jcqparkareaData.add("建成区公园面积(公顷)");
        ArrayList<String> rjparkareaData = new ArrayList<String>();
        rjparkareaData.add("人均公园绿地面积(平方米)");
        ArrayList<String> gyradiusData = new ArrayList<String>();
        gyradiusData.add("公园绿地服务半径覆盖率(%)");
        ArrayList<String> jcqlvfgData = new ArrayList<String>();
        jcqlvfgData.add("建成区绿化覆盖率(%)");
        ArrayList<String> jcqldlData = new ArrayList<String>();
        jcqldlData.add("建成区绿地率(%)");
        ArrayList<ArrayList<String>> mTableDatas = new ArrayList<ArrayList<String>>();
        mfristData.add("类别");
        showDialogProgress(progressDialog,"加载中...");
        JSONObject qujson = new JSONObject();
        try {
            if (localname!=null){
                qujson.put("countyName", localname);
            }else {
                qujson.put("countyName", PrefsManager.getpoint().district);
            }
        } catch (JSONException e) {
        }
        RequestParams quparams = new RequestParams(RequestUtils.REQUEST_URL + "api/statistics/list");
        quparams.setAsJsonContent(true);
        quparams.setBodyContent(qujson.toString());
        quparams.addHeader("Authorization", "bearer " + new DbConfig(getApplication()).getUser().getToken());
        Log.e(TAG, "initView: " + new DbConfig(getApplication()).getUser().getToken());
        Log.e(TAG, "quparams: "+quparams );
        x.http().post(quparams, new Callback.CommonCallback<String>() {
            private JSONArray qudata;
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: --1-" + result );
                JSONObject qujson0bject = null;
                try {
                    qujson0bject = new JSONObject(result);
                    Log.e("result",qujson0bject.toString());
                    qudata = qujson0bject.getJSONArray("data");
                    Log.e("data1",qudata.toString());
                    for(int i=0; i<qudata.length(); i++){
                        JSONObject dataList = qudata.getJSONObject(i);
                        Log.e("dataList11", String.valueOf(dataList));
                        final ArrayList<String> qushidetail =new ArrayList<String>();
                        if (dataList.getString("countyPopulation")!="null"){
                            peopleData.add(dataList.getString("countyPopulation"));
                        }else {
                            peopleData.add("");
                        }
                        if (dataList.getString("temporaryPopulation")!="null"){
                            zanzhupeopleData.add(dataList.getString("temporaryPopulation"));
                        }else {
                            zanzhupeopleData.add("");
                        }
                        if (dataList.getString("builtArea")!="null"){
                            jcqareaData.add(dataList.getString("builtArea"));
                        }else {
                            jcqareaData.add("");
                        }
                        if (dataList.getString("residentArea")!="null"){
                            jzareaData.add(dataList.getString("residentArea"));
                        }else {
                            jzareaData.add("");
                        }
                        if (dataList.getString("cityGreenArea")!="null"){
                            cqgreenData.add(dataList.getString("cityGreenArea"));
                        }else {
                            cqgreenData.add("");
                        }
                        if (dataList.getString("builtGreenArea")!="null"){
                            jcqgreenData.add(dataList.getString("builtGreenArea"));
                        }else {
                            jcqgreenData.add("");
                        }
                        if (dataList.getString("cityGardenGreenArea")!="null"){
                            cqgardensData.add(dataList.getString("cityGardenGreenArea"));
                        }else {
                            cqgardensData.add("");
                        }
                        if (dataList.getString("builtGardenGreenArea")!="null"){
                            jcqgardensData.add(dataList.getString("builtGardenGreenArea"));
                        }else {
                            jcqgardensData.add("");
                        }
                        if (dataList.getString("cityParkGreenArea")!="null"){
                            qcparkData.add(dataList.getString("cityParkGreenArea"));
                        }else {
                            qcparkData.add("");
                        }
                        if (dataList.getString("builtParkGreenArea")!="null"){
                            jcqparkData.add(dataList.getString("builtParkGreenArea"));
                        }else {
                            jcqparkData.add("");
                        }
                        if (dataList.getString("cityParkResidentialArea")!="null"){
                            cqparkradiusData.add(dataList.getString("cityParkResidentialArea"));
                        }else {
                            cqparkradiusData.add("");
                        }
                        if (dataList.getString("builtParkResidentialArea")!="null"){
                            jcqparkradiusData.add(dataList.getString("builtParkResidentialArea"));
                        }else {
                            jcqparkradiusData.add("");
                        }
                        if (dataList.getString("cityParkCount")!="null"){
                            cqparknumData.add(dataList.getString("cityParkCount"));
                        }else {
                            cqparknumData.add("");
                        }
                        if (dataList.getString("builtParkCount")!="null"){
                            jcqparknumData.add(dataList.getString("builtParkCount"));
                        }else {
                            jcqparknumData.add("");
                        }
                        if (dataList.getString("builtGreenLandRate")!="null"){
                            jcqldlData.add(dataList.getString("builtGreenLandRate"));
                        }else {
                            jcqldlData.add("");
                        }
                        if (dataList.getString("cityFreeParkCount")!="null"){
                            cqparkfreenumData.add(dataList.getString("cityFreeParkCount"));
                        }else {
                            cqparkfreenumData.add("");
                        }
                        if (dataList.getString("greenwayLength")!="null"){
                            ldlangData.add(dataList.getString("greenwayLength"));
                        }else {
                            ldlangData.add("");
                        }
                        if (dataList.getString("cityParkArea")!="null"){
                            cqparkareaData.add(dataList.getString("cityParkArea"));
                        }else {
                            cqparkareaData.add("");
                        }
                        if (dataList.getString("builtParkArea")!="null"){
                            jcqparkareaData.add(dataList.getString("builtParkArea"));
                        }else {
                            jcqparkareaData.add("");
                        }
                        if (dataList.getString("builtParkArea")!="null"){
                            rjparkareaData.add(dataList.getString("perParkGreenArea"));
                        }else {
                            rjparkareaData.add("");
                        }
                        if (dataList.getString("builtParkArea")!="null"){
                            gyradiusData.add(dataList.getString("parkGreenRadiusCoverRate"));
                        }else {
                            gyradiusData.add("");
                        }
                        if (dataList.getString("builtGreenCoverRate")!="null"){
                            jcqlvfgData.add(dataList.getString("builtGreenCoverRate"));
                        }else {
                            jcqlvfgData.add("");
                        }


                        mfristData.add(dataList.getString("year"));
                        dataListList.add(peopleData);dataListList.add(zanzhupeopleData);dataListList.add(jcqareaData);dataListList.add(jzareaData);
                        dataListList.add(cqgreenData);dataListList.add(jcqgreenData);dataListList.add(cqgardensData);dataListList.add(jcqgardensData);
                        dataListList.add(qcparkData);dataListList.add(jcqparkData);dataListList.add(cqparkradiusData);dataListList.add(jcqparkradiusData);
                        dataListList.add(cqparknumData);dataListList.add(jcqparknumData);dataListList.add(cqparkfreenumData);dataListList.add(ldlangData);dataListList.add(cqparkareaData);
                        dataListList.add(jcqparkareaData);dataListList.add(rjparkareaData);dataListList.add(gyradiusData);dataListList.add(jcqlvfgData);
                        dataListList.add(jcqldlData);

                        Log.i(TAG, "onSuccess: "+qushidetail);
                        Log.i(TAG, "qushidetail: "+qushidetail.size());
                        Log.i(TAG, "peopleData: "+peopleData);
                    }
                    mTableDatas.add(mfristData);
                    mTableDatas.add(peopleData);mTableDatas.add(zanzhupeopleData);mTableDatas.add(jcqareaData);mTableDatas.add(jzareaData);
                    mTableDatas.add(cqgreenData);mTableDatas.add(jcqgreenData);mTableDatas.add(cqgardensData);mTableDatas.add(jcqgardensData);
                    mTableDatas.add(qcparkData);mTableDatas.add(jcqparkData);mTableDatas.add(cqparkradiusData);mTableDatas.add(jcqparkradiusData);
                    mTableDatas.add(cqparknumData);mTableDatas.add(jcqparknumData);mTableDatas.add(cqparkfreenumData);mTableDatas.add(ldlangData);mTableDatas.add(cqparkareaData);
                    mTableDatas.add(jcqparkareaData);mTableDatas.add(rjparkareaData);mTableDatas.add(gyradiusData);mTableDatas.add(jcqlvfgData);
                    mTableDatas.add(jcqldlData);
                    Log.e(TAG, "initView: "+mTableDatas );
                    final LockTableView mLockTableView = new LockTableView(getApplicationContext(), mContentView, mTableDatas);
                    Log.e("表格加载开始", "当前线程：" + Thread.currentThread());
                    mLockTableView.setLockFristColumn(true) //是否锁定第一列
                            .setLockFristRow(true) //是否锁定第一行
                            .setMaxColumnWidth(100) //列最大宽度
                            .setMinColumnWidth(60) //列最小宽度
//                .setColumnWidth(1,30) //设置指定列文本宽度
//                .setColumnWidth(2,20)
                            .setMinRowHeight(20)//行最小高度
                            .setMaxRowHeight(60)//行最大高度
                            .setTextViewSize(16) //单元格字体大小
                            .setFristRowBackGroudColor(R.color.table_head)//表头背景色
                            .setTableHeadTextColor(R.color.beijin)//表头字体颜色
                            .setTableContentTextColor(R.color.blue_btn_bg_pressed_color)//单元格字体颜色
                            .setCellPadding(15)//设置单元格内边距(dp)
                            .setNullableString(" ") //空值替换值
                            .setTableViewListener(new LockTableView.OnTableViewListener() {
                                @Override
                                public void onTableViewScrollChange(int x, int y) {
//                        Log.e("滚动值","["+x+"]"+"["+y+"]");
                                }
                            })//设置横向滚动回调监听
                            .setTableViewRangeListener(new LockTableView.OnTableViewRangeListener() {
                                @Override
                                public void onLeft(HorizontalScrollView view) {
                                    Log.e("滚动边界","滚动到最左边");
                                }

                                @Override
                                public void onRight(HorizontalScrollView view) {
                                    Log.e("滚动边界","滚动到最右边");
                                }
                            })
                            //设置横向滚动边界监听
                            .setOnLoadingListener(new LockTableView.OnLoadingListener() {
                                @Override
                                public void onRefresh(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                                    Log.e("onRefresh",Thread.currentThread().toString());
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
//                                Log.e("现有表格数据", mTableDatas.toString());
                                            //构造假数据
                                            ArrayList<ArrayList<String>> mTableDatas = new ArrayList<ArrayList<String>>();
                                            ArrayList<String> mfristData = new ArrayList<String>();
                                            mfristData.add("标题");
                                            for (int i = 0; i < 10; i++) {
                                                mfristData.add("标题" + i);
                                            }
                                            mTableDatas.add(mfristData);
                                            for (int i = 0; i < 20; i++) {
                                                ArrayList<String> mRowDatas = new ArrayList<String>();
                                                mRowDatas.add("标题" + i);
                                                for (int j = 0; j < 10; j++) {
                                                    mRowDatas.add("数据" + j);
                                                }
                                                mTableDatas.add(mRowDatas);
                                            }
                                            mLockTableView.setTableDatas(mTableDatas);
                                            mXRecyclerView.refreshComplete();
                                        }
                                    }, 1000);
                                }

                                @Override
                                public void onLoadMore(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                                    Log.e("onLoadMore",Thread.currentThread().toString());
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mTableDatas.size() <= 60) {
                                                for (int i = 0; i < 10; i++) {
                                                    ArrayList<String> mRowDatas = new ArrayList<String>();
                                                    mRowDatas.add("标题" + (mTableDatas.size() - 1));
                                                    for (int j = 0; j < 10; j++) {
                                                        mRowDatas.add("数据" + j);
                                                    }
                                                    mTableDatas.add(mRowDatas);
                                                }
                                                mLockTableView.setTableDatas(mTableDatas);
                                            } else {
                                                mXRecyclerView.setNoMore(true);
                                            }
                                            mXRecyclerView.loadMoreComplete();
                                        }
                                    }, 1000);
                                }
                            })
                            .setOnItemClickListenter(new LockTableView.OnItemClickListenter() {
                                @Override
                                public void onItemClick(View item, int position) {
                                    Log.e("点击事件",position+"");
                                    //Log.e("点击返回了", mnumDatafor1.get(position-1));
                                }
                            })
                            .setOnItemLongClickListenter(new LockTableView.OnItemLongClickListenter() {
                                @Override
                                public void onItemLongClick(View item, int position) {

                                    Log.e("长按事件",position+"" );
                                    barchartList = new ArrayList<>();
                                   /* barchartList.add(mnumDatafor1.get(position-1));
                                    barchartList.add(mnumDatafor2.get(position-1));*/
                                    //Log.e("长按事件", String.valueOf(dataListList.get(position-1)));
                                    if (position>0) {
                                        barchartList.add(dataListList.get(position - 1));


                                    /**
                                     * 第一次进来的点击
                                     */
                                    Intent intent=new Intent(QuActivity.this, OtherBarChartActivity.class);
                                    /*intent.putStringArrayListExtra("barchartList", (ArrayList<String>) dataListList.get(position-1));
                                    intent.putStringArrayListExtra("mfristData", mfristData);
                                    intent.putExtra("barname","");*/
                                    intent.putExtra("isHasData",true);
                                    intent.putExtra("parkData",dataListList.get(position-1).get(0));

                                    if (localname!=null){
                                        intent.putExtra("quData",localname);
                                    }else {
                                        intent.putExtra("quData",PrefsManager.getpoint().district);
                                    }
                                    startActivity(intent);
                                    }
                                }
                            })
                            .setOnItemSeletor(R.color.dashline_color)//设置Item被选中颜色
                            .show(); //显示表格,此方法必须调用
                    mLockTableView.getTableScrollView().setPullRefreshEnabled(false);
                    mLockTableView.getTableScrollView().setLoadingMoreEnabled(false);
                    mLockTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.SquareSpin);
                    //属性值获取
                    Log.e("每列最大宽度(dp)", mLockTableView.getColumnMaxWidths().toString());
                    Log.e("每行最大高度(dp)", mLockTableView.getRowMaxHeights().toString());
                    Log.e("表格所有的滚动视图", mLockTableView.getScrollViews().toString());
                    Log.e("表格头部固定视图(锁列)", mLockTableView.getLockHeadView().toString());
                    Log.e("表格头部固定视图(不锁列)", mLockTableView.getUnLockHeadView().toString());
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
//        //构造假数据
//        ArrayList<ArrayList<String>> mTableDatas = new ArrayList<ArrayList<String>>();
//
//        ArrayList<String> mnumData = new ArrayList<String>();
//        final ArrayList<String> mnumDatafor1 = new ArrayList<String>();
//        final ArrayList<String> mnumDatafor2 = new ArrayList<String>();
//        mnumData.add("人口(万人)");mnumData.add("暂住人口(万人)");mnumData.add("建成区面积(平方公里)");mnumData.add("居住用地(平方公里)");mnumData.add("绿化覆盖面积(公顷)");
//        mnumData.add("绿化覆盖面积建成区(公顷)"); mnumData.add("园林绿地面积(公顷)");mnumData.add("园林绿地面积建成区(公顷)");mnumData.add("公园绿地面积(公顷)");mnumData.add("公园绿地面积建成区(公顷)");
//        mnumData.add("公园绿地服务半径覆盖的居住用地面积(公顷)");mnumData.add("公园绿地服务半径覆盖的居住用地面积建成区(公顷)");mnumData.add("公园个数(个)");mnumData.add("公园个数建成区(个)");mnumData.add("城区免费公园个数(个)");
//        mnumData.add("绿道长度");mnumData.add("公园面积(公顷)");mnumData.add("公园面积建成区(公顷)");mnumData.add("人均公园绿地面积(平方米)");mnumData.add("公园绿地服务半径覆盖率(%)");
//        mnumData.add("建成区绿化覆盖率(%)");mnumData.add("建成区绿地率(%)");
//        mfristData.add("类别");
//        mfristData.add("2018");
//        mfristData.add("2019");
//        mfristData.add("2020");
//        mfristData.add(" ");
//        mfristData.add(" ");
//        mnumDatafor1.add("55.04");mnumDatafor1.add("3.84");mnumDatafor1.add("28.13");mnumDatafor1.add("10.33");mnumDatafor1.add("1065.57");
//        mnumDatafor1.add("1065.57");mnumDatafor1.add("1007.97");mnumDatafor1.add("1007.97");mnumDatafor1.add("544.18");mnumDatafor1.add("544.18");
//        mnumDatafor1.add("908.00");mnumDatafor1.add("908.00");mnumDatafor1.add("21");mnumDatafor1.add("21");mnumDatafor1.add("17");
//        mnumDatafor1.add("5.17");mnumDatafor1.add("397.31");mnumDatafor1.add("397.31");mnumDatafor1.add("9.24");mnumDatafor1.add("87.90");
//        mnumDatafor1.add("37.88");mnumDatafor1.add("35.83");
//        mnumDatafor2.add("54.9937");mnumDatafor2.add("3.8363");mnumDatafor2.add("29.31");mnumDatafor2.add("10.33");mnumDatafor2.add("1065.43");
//        mnumDatafor2.add("1065.43");mnumDatafor2.add("1007.84");mnumDatafor2.add("1007.84");mnumDatafor2.add("544.05");mnumDatafor2.add("544.05");
//        mnumDatafor2.add("908.00");mnumDatafor2.add("908.00");mnumDatafor2.add("21");mnumDatafor2.add("21");mnumDatafor2.add("17");
//        mnumDatafor2.add("397.31");mnumDatafor2.add(" ");mnumDatafor2.add("397.31");mnumDatafor2.add("9.25");mnumDatafor2.add("87.90");
//        mnumDatafor2.add("36.35");mnumDatafor2.add("34.39");
//

    }


    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }
    private class MyListAdapter extends BaseAdapter {
        private List<List<String>> dataListList1 = new ArrayList<>();
        @Override
        public int getCount() {
            return numList==null?0:numList.size();
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView=View.inflate(getApplicationContext(), R.layout.item_pop, null);
                holder=new ViewHolder();
                holder.tvNum=(TextView) convertView.findViewById(R.id.tv_list_item);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            holder.tvNum.setText(numList.get(position));
//对条目设置监听事件 点击条目后 将num设置到编辑框中
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClick: "+numList.get(position));
                    String localname =numList.get(position);
                    actionBar.setTitle(localname+"详情");
                    //区市详情
                    final ArrayList<String> mfristData = new ArrayList<String>();
                    ArrayList<String> peopleData = new ArrayList<String>();
                    peopleData.add("城区人口(万人)");
                    ArrayList<String> zanzhupeopleData = new ArrayList<String>();
                    zanzhupeopleData.add("城区暂住人口(万人)");
                    ArrayList<String> jcqareaData = new ArrayList<String>();
                    jcqareaData.add("建成区面积(平方公里)");
                    ArrayList<String> jzareaData = new ArrayList<String>();
                    jzareaData.add("居住用地面积(平方公里)");
                    ArrayList<String> cqgreenData = new ArrayList<String>();
                    cqgreenData.add("城区绿化覆盖面积(公顷)");
                    ArrayList<String> jcqgreenData = new ArrayList<String>();
                    jcqgreenData.add("建成区绿化覆盖面积(公顷)");
                    ArrayList<String> cqgardensData = new ArrayList<String>();
                    cqgardensData.add("城区园林绿地面积(公顷)");
                    ArrayList<String> jcqgardensData = new ArrayList<String>();
                    jcqgardensData.add("建成区园林绿地面积(公顷)");
                    ArrayList<String> qcparkData = new ArrayList<String>();
                    qcparkData.add("城区公园绿地面积(公顷)");
                    ArrayList<String> jcqparkData = new ArrayList<String>();
                    jcqparkData.add("建城区公园绿地面积(公顷)");
                    ArrayList<String> cqparkradiusData = new ArrayList<String>();
                    cqparkradiusData.add("城区公园绿地服务半径覆盖的居住用地面积(公顷)");
                    ArrayList<String> jcqparkradiusData = new ArrayList<String>();
                    jcqparkradiusData.add("建成区公园绿地服务半径覆盖的居住用地面积(公顷)");
                    ArrayList<String> cqparknumData = new ArrayList<String>();
                    cqparknumData.add("城区公园个数(个)");
                    ArrayList<String> jcqparknumData = new ArrayList<String>();
                    jcqparknumData.add("建成区公园个数(个)");
                    ArrayList<String> cqparkfreenumData = new ArrayList<String>();
                    cqparkfreenumData.add("城区免费公园个数(个)");
                    ArrayList<String> ldlangData = new ArrayList<String>();
                    ldlangData.add("绿道长度");
                    ArrayList<String> cqparkareaData = new ArrayList<String>();
                    cqparkareaData.add("城区公园面积(公顷)");
                    ArrayList<String> jcqparkareaData = new ArrayList<String>();
                    jcqparkareaData.add("建成区公园面积(公顷)");
                    ArrayList<String> rjparkareaData = new ArrayList<String>();
                    rjparkareaData.add("人均公园绿地面积(平方米)");
                    ArrayList<String> gyradiusData = new ArrayList<String>();
                    gyradiusData.add("公园绿地服务半径覆盖率(%)");
                    ArrayList<String> jcqlvfgData = new ArrayList<String>();
                    jcqlvfgData.add("建成区绿化覆盖率(%)");
                    ArrayList<String> jcqldlData = new ArrayList<String>();
                    jcqldlData.add("建成区绿地率(%)");
                    ArrayList<ArrayList<String>> mTableDatas = new ArrayList<ArrayList<String>>();
                    mfristData.add("类别");
                    showDialogProgress(progressDialog,"加载中...");
                    JSONObject qujson = new JSONObject();
                    try {
                        if (localname!=null){
                            qujson.put("countyName", localname);
                        }else {
                            qujson.put("countyName", PrefsManager.getpoint().district);
                        }
                    } catch (JSONException e) {
                    }
                    RequestParams quparams = new RequestParams(RequestUtils.REQUEST_URL + "api/statistics/list");
                    quparams.setAsJsonContent(true);
                    quparams.setBodyContent(qujson.toString());
                    quparams.addHeader("Authorization", "bearer " + new DbConfig(getApplication()).getUser().getToken());
                    x.http().post(quparams, new Callback.CommonCallback<String>() {
                        private JSONArray qudata;

                        @Override
                        public void onSuccess(String result) {
                            Log.e(TAG, "onSuccess: --1-" + result );
                            JSONObject qujson0bject = null;
                            try {
                                qujson0bject = new JSONObject(result);
                                Log.e("result",qujson0bject.toString());
                                qudata = qujson0bject.getJSONArray("data");
                                Log.e("data1",qudata.toString());
                                for(int i=0; i<qudata.length(); i++){
                                    JSONObject dataList = qudata.getJSONObject(i);
                                    Log.e("dataList11", String.valueOf(dataList));
                                    final ArrayList<String> qushidetail =new ArrayList<String>();
                                    if (dataList.getString("countyPopulation")!="null"){
                                        peopleData.add(dataList.getString("countyPopulation"));
                                    }else {
                                        peopleData.add("");
                                    }
                                    if (dataList.getString("temporaryPopulation")!="null"){
                                        zanzhupeopleData.add(dataList.getString("temporaryPopulation"));
                                    }else {
                                        zanzhupeopleData.add("");
                                    }
                                    if (dataList.getString("builtArea")!="null"){
                                        jcqareaData.add(dataList.getString("builtArea"));
                                    }else {
                                        jcqareaData.add("");
                                    }
                                    if (dataList.getString("residentArea")!="null"){
                                        jzareaData.add(dataList.getString("residentArea"));
                                    }else {
                                        jzareaData.add("");
                                    }
                                    if (dataList.getString("cityGreenArea")!="null"){
                                        cqgreenData.add(dataList.getString("cityGreenArea"));
                                    }else {
                                        cqgreenData.add("");
                                    }
                                    if (dataList.getString("builtGreenArea")!="null"){
                                        jcqgreenData.add(dataList.getString("builtGreenArea"));
                                    }else {
                                        jcqgreenData.add("");
                                    }
                                    if (dataList.getString("cityGardenGreenArea")!="null"){
                                        cqgardensData.add(dataList.getString("cityGardenGreenArea"));
                                    }else {
                                        cqgardensData.add("");
                                    }
                                    if (dataList.getString("builtGardenGreenArea")!="null"){
                                        jcqgardensData.add(dataList.getString("builtGardenGreenArea"));
                                    }else {
                                        jcqgardensData.add("");
                                    }
                                    if (dataList.getString("cityParkGreenArea")!="null"){
                                        qcparkData.add(dataList.getString("cityParkGreenArea"));
                                    }else {
                                        qcparkData.add("");
                                    }
                                    if (dataList.getString("builtParkGreenArea")!="null"){
                                        jcqparkData.add(dataList.getString("builtParkGreenArea"));
                                    }else {
                                        jcqparkData.add("");
                                    }
                                    if (dataList.getString("cityParkResidentialArea")!="null"){
                                        cqparkradiusData.add(dataList.getString("cityParkResidentialArea"));
                                    }else {
                                        cqparkradiusData.add("");
                                    }
                                    if (dataList.getString("builtParkResidentialArea")!="null"){
                                        jcqparkradiusData.add(dataList.getString("builtParkResidentialArea"));
                                    }else {
                                        jcqparkradiusData.add("");
                                    }
                                    if (dataList.getString("cityParkCount")!="null"){
                                        cqparknumData.add(dataList.getString("cityParkCount"));
                                    }else {
                                        cqparknumData.add("");
                                    }
                                    if (dataList.getString("builtParkCount")!="null"){
                                        jcqparknumData.add(dataList.getString("builtParkCount"));
                                    }else {
                                        jcqparknumData.add("");
                                    }
                                    if (dataList.getString("builtGreenLandRate")!="null"){
                                        jcqldlData.add(dataList.getString("builtGreenLandRate"));
                                    }else {
                                        jcqldlData.add("");
                                    }
                                    if (dataList.getString("cityFreeParkCount")!="null"){
                                        cqparkfreenumData.add(dataList.getString("cityFreeParkCount"));
                                    }else {
                                        cqparkfreenumData.add("");
                                    }
                                    if (dataList.getString("greenwayLength")!="null"){
                                        ldlangData.add(dataList.getString("greenwayLength"));
                                    }else {
                                        ldlangData.add("");
                                    }
                                    if (dataList.getString("cityParkArea")!="null"){
                                        cqparkareaData.add(dataList.getString("cityParkArea"));
                                    }else {
                                        cqparkareaData.add("");
                                    }
                                    if (dataList.getString("builtParkArea")!="null"){
                                        jcqparkareaData.add(dataList.getString("builtParkArea"));
                                    }else {
                                        jcqparkareaData.add("");
                                    }
                                    if (dataList.getString("builtParkArea")!="null"){
                                        rjparkareaData.add(dataList.getString("perParkGreenArea"));
                                    }else {
                                        rjparkareaData.add("");
                                    }
                                    if (dataList.getString("builtParkArea")!="null"){
                                        gyradiusData.add(dataList.getString("parkGreenRadiusCoverRate"));
                                    }else {
                                        gyradiusData.add("");
                                    }
                                    if (dataList.getString("builtGreenCoverRate")!="null"){
                                        jcqlvfgData.add(dataList.getString("builtGreenCoverRate"));
                                    }else {
                                        jcqlvfgData.add("");
                                    }


                                    mfristData.add(dataList.getString("year"));

                                    dataListList.add(qushidetail);

                                    Log.i(TAG, "onSuccess: "+qushidetail);
                                    Log.i(TAG, "qushidetail: "+qushidetail.size());
                                    Log.i(TAG, "peopleData: "+peopleData);
                                }
                                mTableDatas.add(mfristData);
                                mTableDatas.add(peopleData);mTableDatas.add(zanzhupeopleData);mTableDatas.add(jcqareaData);mTableDatas.add(jzareaData);
                                mTableDatas.add(cqgreenData);mTableDatas.add(jcqgreenData);mTableDatas.add(cqgardensData);mTableDatas.add(jcqgardensData);
                                mTableDatas.add(qcparkData);mTableDatas.add(jcqparkData);mTableDatas.add(cqparkradiusData);mTableDatas.add(jcqparkradiusData);
                                mTableDatas.add(cqparknumData);mTableDatas.add(jcqparknumData);mTableDatas.add(cqparkfreenumData);mTableDatas.add(ldlangData);mTableDatas.add(cqparkareaData);
                                mTableDatas.add(jcqparkareaData);mTableDatas.add(rjparkareaData);mTableDatas.add(gyradiusData);mTableDatas.add(jcqlvfgData);
                                mTableDatas.add(jcqldlData);
                                Log.e(TAG, "initView: "+mTableDatas );
                                final LockTableView mLockTableView = new LockTableView(getApplicationContext(), mContentView, mTableDatas);
                                mLockTableView.setLockFristColumn(true) //是否锁定第一列
                                        .setLockFristRow(true) //是否锁定第一行
                                        .setMaxColumnWidth(100) //列最大宽度
                                        .setMinColumnWidth(60) //列最小宽度
//                .setColumnWidth(1,30) //设置指定列文本宽度
//                .setColumnWidth(2,20)
                                        .setMinRowHeight(20)//行最小高度
                                        .setMaxRowHeight(60)//行最大高度
                                        .setTextViewSize(16) //单元格字体大小
                                        .setFristRowBackGroudColor(R.color.table_head)//表头背景色
                                        .setTableHeadTextColor(R.color.beijin)//表头字体颜色
                                        .setTableContentTextColor(R.color.blue_btn_bg_pressed_color)//单元格字体颜色
                                        .setCellPadding(15)//设置单元格内边距(dp)
                                        .setNullableString(" ") //空值替换值
                                        .setTableViewListener(new LockTableView.OnTableViewListener() {
                                            @Override
                                            public void onTableViewScrollChange(int x, int y) {
//                        Log.e("滚动值","["+x+"]"+"["+y+"]");
                                            }
                                        })//设置横向滚动回调监听
                                        .setTableViewRangeListener(new LockTableView.OnTableViewRangeListener() {
                                            @Override
                                            public void onLeft(HorizontalScrollView view) {
                                                Log.e("滚动边界","滚动到最左边");
                                            }

                                            @Override
                                            public void onRight(HorizontalScrollView view) {
                                                Log.e("滚动边界","滚动到最右边");
                                            }
                                        })
                                        //设置横向滚动边界监听
                                        .setOnLoadingListener(new LockTableView.OnLoadingListener() {
                                            @Override
                                            public void onRefresh(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                                                Log.e("onRefresh",Thread.currentThread().toString());
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
//                                Log.e("现有表格数据", mTableDatas.toString());
                                                        //构造假数据
                                                        ArrayList<ArrayList<String>> mTableDatas = new ArrayList<ArrayList<String>>();
                                                        ArrayList<String> mfristData = new ArrayList<String>();
                                                        mfristData.add("标题");
                                                        for (int i = 0; i < 10; i++) {
                                                            mfristData.add("标题" + i);
                                                        }
                                                        mTableDatas.add(mfristData);
                                                        for (int i = 0; i < 20; i++) {
                                                            ArrayList<String> mRowDatas = new ArrayList<String>();
                                                            mRowDatas.add("标题" + i);
                                                            for (int j = 0; j < 10; j++) {
                                                                mRowDatas.add("数据" + j);
                                                            }
                                                            mTableDatas.add(mRowDatas);
                                                        }
                                                        mLockTableView.setTableDatas(mTableDatas);
                                                        mXRecyclerView.refreshComplete();
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onLoadMore(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                                                Log.e("onLoadMore",Thread.currentThread().toString());
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (mTableDatas.size() <= 60) {
                                                            for (int i = 0; i < 10; i++) {
                                                                ArrayList<String> mRowDatas = new ArrayList<String>();
                                                                mRowDatas.add("标题" + (mTableDatas.size() - 1));
                                                                for (int j = 0; j < 10; j++) {
                                                                    mRowDatas.add("数据" + j);
                                                                }
                                                                mTableDatas.add(mRowDatas);
                                                            }
                                                            mLockTableView.setTableDatas(mTableDatas);
                                                        } else {
                                                            mXRecyclerView.setNoMore(true);
                                                        }
                                                        mXRecyclerView.loadMoreComplete();
                                                    }
                                                }, 1000);
                                            }
                                        })
                                        .setOnItemClickListenter(new LockTableView.OnItemClickListenter() {
                                            @Override
                                            public void onItemClick(View item, int position) {
                                                Log.e("点击事件",position+"");
                                                //Log.e("点击返回了", mnumDatafor1.get(position-1));
                                            }
                                        })
                                        .setOnItemLongClickListenter(new LockTableView.OnItemLongClickListenter() {
                                            @Override
                                            public void onItemLongClick(View item, int position) {

                                                Log.e("长按事件",position+"" );
                                                barchartList = new ArrayList<>();
                                   /* barchartList.add(mnumDatafor1.get(position-1));
                                    barchartList.add(mnumDatafor2.get(position-1));*/
                                                Log.e("长按事件", String.valueOf(dataListList.get(position-1)));

                                                barchartList.add(dataListList.get(position-1));


                                                /**
                                                 * 切换区市后的长安点击
                                                 */

                                                Intent intent=new Intent(QuActivity.this, OtherBarChartActivity.class);
                                               /* intent.putStringArrayListExtra("barchartList", (ArrayList<String>) dataListList.get(position-1));
                                                intent.putStringArrayListExtra("mfristData", mfristData);*/
                                                intent.putExtra("isHasData",true);
                                                intent.putExtra("parkData",dataListList.get(position-1).get(0));
                                                intent.putExtra("quData",localname);

                                                startActivity(intent);
                                            }
                                        })
                                        .setOnItemSeletor(R.color.dashline_color)//设置Item被选中颜色
                                        .show(); //显示表格,此方法必须调用
                                mLockTableView.getTableScrollView().setPullRefreshEnabled(false);
                                mLockTableView.getTableScrollView().setLoadingMoreEnabled(false);
                                mLockTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.SquareSpin);
                                //属性值获取
                                Log.e("每列最大宽度(dp)", mLockTableView.getColumnMaxWidths().toString());
                                Log.e("每行最大高度(dp)", mLockTableView.getRowMaxHeights().toString());
                                Log.e("表格所有的滚动视图", mLockTableView.getScrollViews().toString());
                                Log.e("表格头部固定视图(锁列)", mLockTableView.getLockHeadView().toString());
                                Log.e("表格头部固定视图(不锁列)", mLockTableView.getUnLockHeadView().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.e(TAG, "onError: " + ex.toString());
                            mTableDatas.clear();
                            final LockTableView mLockTableView = new LockTableView(getApplicationContext(), mContentView, mTableDatas);
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {
                            progressDialog.dismiss();
                        }
                    });
                    popWin.dismiss();
                }
            });
            return convertView;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    private class ViewHolder {
        TextView tvNum;
    }
}

