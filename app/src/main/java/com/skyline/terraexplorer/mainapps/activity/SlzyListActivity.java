package com.skyline.terraexplorer.mainapps.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.skyline.terraexplorer.databinding.ActivityZyjslistBinding;
import com.skyline.terraexplorer.db.Grid;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.db.User;
import com.skyline.terraexplorer.mainapps.multitype.Empty;
import com.skyline.terraexplorer.mainapps.multitype.EmptyViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.Firejd;
import com.skyline.terraexplorer.mainapps.multitype.FirejdViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.SlzyjsViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.Slzyjsjd;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;
import com.skyline.terraexplorer.views.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class SlzyListActivity extends BaseActivity {
    private static final String TAG = SlzyListActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private boolean isShowDialog = true;
    private boolean isShuaxin = false;
    private boolean isSearch = false;
    private List<Slzyjsjd> firejdList;
    private List<Slzyjsjd> firejdTypeList;
    public static int ORDER_CHANGE = 113;
    private DbManager db;
    private List<Grid> gridList ;
    private int currentFireListType = 0;  //3"全部，0未开始，1执行中，2已结束")
    private int choose1 = 0;
    private AlertDialog.Builder builder;
    private int currentPage = 1;
    private int totalSize;
    private int lastPage;
    private LinearLayout fireTypeLayout;
    private LinearLayout orderStateLayout;
    private TextView fireTypeView;
    private TextView orderStateView;
    private int shipinChoose1 = 0;
    private User user;
    private ActivityZyjslistBinding binding;
    public int currentChooseArea = 0;  //当前在选择区还是街道   0选择区  1选择街道 2选择社区
    public List<Grid> quList;
    public List<String> quStrList;
    public List<Grid> jiedaoList;
    public List<Grid> shequList;
    public List<String> shequStrList;
    public List<String> jiedaoStrList;
    public String currentChooseQu = "";
    public String currentChooseJiedao = "";
    private WheelView areaWy;
    public int quSelectIndex = 0;
    public int jieDaoSelectIndex = 0;
    public int shequSelectIndex = 0;
    public boolean isChooseQu = false;
    public String currentChooseSheQu = "";
    public String currentQuId;
    public String currentSheQuId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanghuolist);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_zyjslist);
        progressDialog = new ProgressDialog(this);
        firejdList = new ArrayList<>();
        firejdTypeList = new ArrayList<>();
        user = new DbConfig(this).getUser();
        initView();
        isShowDialog = true;
        getDataFromService();
//        initGridIntoDb();
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
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/forestry/api/forestResourceRetrieval/page");
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

                        firejdList = gson.fromJson(String.valueOf(dataList), new TypeToken<List<Slzyjsjd>>() {
                        }.getType());
                        firejdTypeList.addAll(firejdList);
                        if (isShuaxin){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        initData();
                    }else {
                        Toast.makeText(SlzyListActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
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
        gridList = new ArrayList<>();
        quList = new ArrayList<>();
        quStrList = new ArrayList<>();
        jiedaoList = new ArrayList<>();
        jiedaoStrList = new ArrayList<>();
        shequList=new ArrayList<>();
        shequStrList=new ArrayList<>();
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("森林资源检索");
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
      //  actionBar.setRightView("全部");
        //  actionBar.setRightImage(R.drawable.ic_jia);
        DbConfig dbConfig = new DbConfig(this);
        db = dbConfig.getDbManager();
        orderStateLayout = (LinearLayout) findViewById(R.id.order_state_layout);
        orderStateView = (TextView) findViewById(R.id.order_state_view);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fire_mission_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        listView = (RecyclerView) findViewById(R.id.fire_mission_listview);

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
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);

        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        RxViewAction.clickNoDouble(binding.quLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseArea = 0;
                        getAllQu();
                        showAreaDialog(quStrList);
                    }
                });
        RxViewAction.clickNoDouble(binding.jiedaoLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseArea = 1;
                        for (int i = 0; i < quList.size(); i++) {
                            if (quList.get(i).getName().equals(currentChooseQu)) {
                                currentQuId = quList.get(i).getId();
                            }
                        }
                        if (binding.quText.getText().equals("请选择区")){
                            Toast.makeText(SlzyListActivity.this, "请先选择区", Toast.LENGTH_SHORT).show();

                        }else {
                            getAllJieDao();
                        }

                    }
                });
        RxViewAction.clickNoDouble(binding.shequLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        currentChooseArea = 2;
                        for (int i = 0; i < jiedaoList.size(); i++) {
                            if (jiedaoList.get(i).getName().equals(currentChooseJiedao)) {
                                currentSheQuId = jiedaoList.get(i).getId();
                            }
                        }
                        if (binding.jiedaoText.getText().equals("请选择街道")){
                            Toast.makeText(SlzyListActivity.this, "请先选择街道", Toast.LENGTH_SHORT).show();

                        }else {
                            getAllSheQu();
                        }

                    }
                });
        initGridIntoDb();
    }

    private void register() {
        SlzyjsViewBinder slzyjsViewBinder = new SlzyjsViewBinder();
        adapter.register(Slzyjsjd.class, slzyjsViewBinder);
        adapter.register(Empty.class, new EmptyViewBinder());
    }

    /**
     * 网格信息
     */
    private void initGridIntoDb() {
        List<Grid> gridbList = null;
        try {
            gridbList = db.selector(Grid.class)
                    .orderBy("updatetime")
                    .findAll();

        } catch (DbException e) {

        }
        JSONObject jsonObject = new JSONObject();

        try {
            if (gridbList == null) {
                jsonObject.put("updateTime", "2010-11-21T08:36:31.420Z");
            } else {
                String time = gridbList.get(gridbList.size() - 1).getUpdateTime();
                jsonObject.put("updateTime", time);
            }
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "/resource/api/grid/list");
        params.setConnectTimeout(20000);
        //params.setBodyContent(jsonObject.toString());
        params.setBodyContent(new JSONObject().toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess13------ " + result);
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    String code = jsonObject1.getString("code");
                    if (code.equals("200")){
                        Log.e(TAG, "onSuccess13:1 "   );
                        JSONArray data = jsonObject1.getJSONArray("data");
                        gridList.clear();
                        Gson gson = new Gson();
                        Log.e(TAG, "onSuccess13:4 "   );
                        gridList = gson.fromJson(String.valueOf(data), new TypeToken<List<Grid>>(){}.getType());
                        Log.e(TAG, "onSuccess13: " +gridList.size() );

                        DbConfig dbConfig = new DbConfig(getApplicationContext());
                        DbManager db = dbConfig.getDbManager();
                        try {

                            db.saveOrUpdate(gridList);
                        } catch (DbException e) {
                            Log.e(TAG, "onSuccess13: " + e.getMessage() );
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError: materialRepository请求失败" + ex.toString());
                if (ex.toString().contains("timeout")){
                    initGridIntoDb();
                }else {
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }
    /**
     * 获取所有区的数据
     */
    private void getAllQu() {
        quList.clear();
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        try {
            quList = db.selector(Grid.class)
                    .where("level", "=", "3")
                    .and("parentName", "=", "青岛市")
                    .findAll();

            quStrList.clear();
            quStrList.add("请选择区");
            for (int i = 0; i < quList.size(); i++) {
                if (!quList.get(i).getName().equals("高新区")){
                    quStrList.add(quList.get(i).getName());
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    private void showAreaDialog(List<String> strList) {
        View areaView = LayoutInflater.from(this).inflate(R.layout.dialog_area, null);
        areaWy = ((WheelView) areaView.findViewById(R.id.wheel_view_area));
        areaWy.setIsLoop(false);
        if (currentChooseArea == 0){
            areaWy.setItems(strList, quSelectIndex);//init selected position is 0 初始选中位置为0
        }else if (currentChooseArea == 1) {
            areaWy.setItems(strList, jieDaoSelectIndex);//init selected position is 0 初始选中位置为0
        }else  {
            areaWy.setItems(strList, shequSelectIndex);//init selected position is 0 初始选中位置为0
        }

        areaWy.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                if (currentChooseArea == 0) {   //选择省
                    isChooseQu = true;
                    currentChooseQu = areaWy.getSelectedItem();
                    quSelectIndex = areaWy.getSelectedPosition();
                    binding.quText.setText(currentChooseQu);
                    currentChooseJiedao = "";
                    currentChooseSheQu="";
                    binding.jiedaoText.setText("请选择街道");
                    binding.shequText.setText("请选择社区");
                    jieDaoSelectIndex = 0;
                }else if (currentChooseArea == 2){  //选择社区
                    currentChooseSheQu = areaWy.getSelectedItem();
                    shequSelectIndex = areaWy.getSelectedPosition();
                    binding.shequText.setText(currentChooseSheQu);
                }else {                          //选择市
                    currentChooseJiedao = areaWy.getSelectedItem();
                    jieDaoSelectIndex = areaWy.getSelectedPosition();
                    binding.jiedaoText.setText(currentChooseJiedao);
                    currentChooseSheQu="";
                    binding.shequText.setText("请选择社区");
                }

            }
        });
        new AlertDialog.Builder(this)
                .setTitle("请选择区域")
                .setView(areaView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String areStr = "";
                        String area = areaWy.getSelectedItem();
                    }
                })
                .show();
    }
    /**
     * 获取所有街道数据
     */
    private void getAllJieDao() {
        jiedaoList.clear();
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        try {
            jiedaoList = db.selector(Grid.class)
                    .where("state", "=", "ACTIVE")
                    .where("level", "=", "1")
                    .where("parentid", "=", currentQuId)
                    .findAll();

            jiedaoStrList.clear();
            jiedaoStrList.add("请选择街道");
            for (int i = 0; i < jiedaoList.size(); i++) {
                jiedaoStrList.add(jiedaoList.get(i).getName());
            }
            showAreaDialog(jiedaoStrList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取所有社区数据
     */
    private void getAllSheQu() {
        shequList.clear();
        DbManager db = new DbConfig(getApplicationContext()).getDbManager();
        try {
            shequList = db.selector(Grid.class)
                    .where("state", "=", "ACTIVE")
                    .where("level", "=", "1")
                    .where("parentid", "=", currentSheQuId)
                    .findAll();
            Log.e(TAG, "getAllSheQu: "+currentSheQuId);
            shequStrList.clear();
            shequStrList.add("请选择社区");
            for (int i = 0; i < shequList.size(); i++) {
                shequStrList.add(shequList.get(i).getName());
            }
            Log.e(TAG, "getAllSheQu2: "+shequStrList);
            showAreaDialog(shequStrList);
        } catch (DbException e) {
            e.printStackTrace();
        }
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
