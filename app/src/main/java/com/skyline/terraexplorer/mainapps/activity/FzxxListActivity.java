package com.skyline.terraexplorer.mainapps.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.skyline.terraexplorer.databinding.ActivityFzxxlistBinding;
import com.skyline.terraexplorer.databinding.ActivityXmgllistBinding;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.db.User;
import com.skyline.terraexplorer.mainapps.multitype.Empty;
import com.skyline.terraexplorer.mainapps.multitype.EmptyViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.FzxxViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.XmglViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.Xmgljd;
import com.skyline.terraexplorer.mainapps.multitype.fzxxjd;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class FzxxListActivity extends BaseActivity implements DatePicker.OnDateChangedListener{
    private static final String TAG = FzxxListActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private boolean isShowDialog = true;
    private boolean isShuaxin = false;
    private boolean isSearch = false;
    private List<fzxxjd> firejdList;
    private List<fzxxjd> firejdTypeList;
    public static int ORDER_CHANGE = 113;
    private int choose1 = 0;
    private AlertDialog.Builder builder;
    private int currentPage = 1;
    private int totalSize;
    private int lastPage;
    private LinearLayout fireTypeLayout;
    private TextView fireTypeView;
    private TextView orderStateView;
    private int shipinChoose1 = 0;
    private User user;
    private ActivityFzxxlistBinding binding;
    private StringBuffer date;
    private int year;
    private int month;
    private int day;
    private String fstime="";
    private String fztime="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_fanghuolist);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fzxxlist);
        progressDialog = new ProgressDialog(this);
        firejdList = new ArrayList<>();
        firejdTypeList = new ArrayList<>();
        user = new DbConfig(this).getUser();
        initView();
        isShowDialog = true;
        getDataFromService();
        initDateTime();
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
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/pestPreventionControlInfo/page");
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

                        firejdList = gson.fromJson(String.valueOf(dataList), new TypeToken<List<fzxxjd>>() {
                        }.getType());
                        firejdTypeList.addAll(firejdList);
                        if (isShuaxin){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        initData();
                    }else {
                        Toast.makeText(FzxxListActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
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
    private void selectDataFromService() {
        if (isShowDialog){
            showDialogProgress(progressDialog,"加载中...");
        }
        final JSONObject jsonObject = new JSONObject();
        try {
            JSONObject dto = new JSONObject();

            jsonObject.put("region", binding.orderStateView.getText().toString().equals("青岛市") ?"":binding.orderStateView.getText().toString());
            jsonObject.put("occurrenceTime", fstime.equals("")?"":fstime+" 00:00:00");
            jsonObject.put("preventTime", fztime.equals("")?"":fztime+" 00:00:00");
            jsonObject.put("dto", dto);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/pestPreventionControlInfo/list");
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
                        firejdList = gson.fromJson(String.valueOf(data), new TypeToken<List<fzxxjd>>() {
                        }.getType());
                        firejdTypeList.addAll(firejdList);
                        if (isShuaxin){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        initData();
                    }else {
                        Toast.makeText(FzxxListActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
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
        date = new StringBuffer();
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("防治信息管理");
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
                        Intent intent=new Intent(FzxxListActivity.this, AddfzxxActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fire_mission_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);
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
                        showLeibieChangeDailog();
                    }
                });
        RxViewAction.clickNoDouble(binding.fstimeEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDataDialog(binding.fstimeEdit,"fstime");
                    }
                });
        RxViewAction.clickNoDouble(binding.fztimeEdit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDataDialog(binding.fztimeEdit,"fztime");
                    }
                });
        RxViewAction.clickNoDouble(binding.chaxunBtn)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        selectDataFromService();
                    }
                });
        RxViewAction.clickNoDouble(binding.cleanBtn)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        binding.fstimeEdit.setText("");
                        binding.fztimeEdit.setText("");
                        binding.orderStateView.setText("青岛市");
                        choose1=0;
                        fstime="";
                        fztime="";
                    }
                });
    }

    private void register() {
        FzxxViewBinder fzxxViewBinder = new FzxxViewBinder();
        adapter.register(fzxxjd.class, fzxxViewBinder);
        adapter.register(Empty.class, new EmptyViewBinder());
    }

    private void showLeibieChangeDailog() {
        //默认选中第一个
        final String[] items = {"青岛市", "市南区","市北区","崂山区","李沧区","城阳区","即墨区","胶州市","西海岸","平度市","莱西市"};

        builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("地区")
                .setSingleChoiceItems(items, choose1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, "onClick: 类别choose---" + i);
                        choose1 = i;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (choose1){
                            case 0:
                                binding.orderStateView.setText("青岛市");
                                selectDataFromService();
                                break;
                            case 1:
                                binding.orderStateView.setText("市南区");
                                selectDataFromService();
                                break;
                            case 2:
                                binding.orderStateView.setText("市北区");
                                selectDataFromService();
                                break;
                            case 3:
                                binding.orderStateView.setText("崂山区");
                                selectDataFromService();
                                break;
                            case 4:
                                binding.orderStateView.setText("李沧区");
                                selectDataFromService();
                                break;
                            case 5:
                                binding.orderStateView.setText("城阳区");
                                selectDataFromService();
                                break;
                            case 6:
                                binding.orderStateView.setText("即墨区");
                                selectDataFromService();
                                break;
                            case 7:
                                binding.orderStateView.setText("胶州市");
                                selectDataFromService();
                                break;
                            case 8:
                                binding.orderStateView.setText("西海岸");
                                selectDataFromService();
                                break;
                            case 9:
                                binding.orderStateView.setText("平度市");
                                selectDataFromService();
                                break;
                            case 10:
                                binding.orderStateView.setText("莱西市");
                                selectDataFromService();
                                break;
                        }
                    }
                });
        builder.create().show();
    }
    private void showDataDialog(EditText editText,String tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (date.length() > 0) { //清除上次记录的日期
                    date.delete(0, date.length());
                }

                date.append(String.valueOf(year));
                if (month < 9){
                    date.append("-0").append(String.valueOf(month + 1));
                }else {
                    date.append("-").append(String.valueOf(month + 1));
                }
                if (day <10){
                    date.append("-0").append(String.valueOf(day));
                } else {
                    date.append("-").append(String.valueOf(day));
                }

                editText.setText(date);
                if (tag.equals("fstime")){
                    fstime= String.valueOf(date);
                }else if (tag.equals("fztime")){
                    fztime= String.valueOf(date);
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog  dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        Calendar date = Calendar.getInstance();
        int year1 = date.get(Calendar.YEAR);
        int month1 = date.get(Calendar.MONTH);
        int day1 = date.get(Calendar.DATE);
        String endData = year1 - 10 + "-" + month1 + "-" + day1;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(endData);
        } catch (ParseException e) {

        }
        long starTimre = date2.getTime();


        long endTimre = System.currentTimeMillis();

        datePicker.setMaxDate(endTimre);
        datePicker.setMinDate(starTimre);

        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(year, month, day, this);
    }
    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
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
