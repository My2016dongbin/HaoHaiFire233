package com.skyline.terraexplorer.mainapps.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.databinding.ActivityCdjylistBinding;
import com.skyline.terraexplorer.databinding.ActivityPhonenumlistBinding;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.db.User;
import com.skyline.terraexplorer.mainapps.multitype.CdjyViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.Empty;
import com.skyline.terraexplorer.mainapps.multitype.EmptyViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.PhonenumViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.cdjyjd;
import com.skyline.terraexplorer.mainapps.utils.ActionBar;
import com.skyline.terraexplorer.mainapps.utils.RequestUtils;
import com.skyline.terraexplorer.mainapps.utils.RxViewAction;
import com.skyline.terraexplorer.multitype.Phonenum;

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


public class PhoneListActivity extends BaseActivity implements PhonenumViewBinder.OnPhonenumItemClick {
    private static final String TAG = PhoneListActivity.class.getSimpleName();
    private ActionBar actionBar;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private boolean isShowDialog = true;
    private boolean isShuaxin = false;
    private boolean isSearch = false;
    private List<Phonenum> firejdList;
    private List<Phonenum> firejdTypeList;
    public static int ORDER_CHANGE = 113;
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
    private ActivityPhonenumlistBinding binding;
    private int requestCode=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_fanghuolist);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phonenumlist);
        progressDialog = new ProgressDialog(this);
        firejdList = new ArrayList<>();
        firejdTypeList = new ArrayList<>();
        user = new DbConfig(this).getUser();
        initView();
        isShowDialog = true;
        getDataFromService();
        checkReadPermission("android.permission.CALL_PHONE",requestCode);
    }

    private void getDataFromService() {
        if (isShowDialog){
            showDialogProgress(progressDialog,"加载中...");
        }
        final JSONObject jsonObject = new JSONObject();
        try {
            JSONObject dto = new JSONObject();

            jsonObject.put("limit", 200);
            jsonObject.put("dto", dto);
            jsonObject.put("page", currentPage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "auth/api/auth/user/page");
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

                        firejdList = gson.fromJson(String.valueOf(dataList), new TypeToken<List<Phonenum>>() {
                        }.getType());
                        firejdTypeList.addAll(firejdList);
                        if (isShuaxin){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        initData();
                    }else {
                        Toast.makeText(PhoneListActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
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
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("联系人");
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
        binding.phonenumListview.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);

        register();

        binding.phonenumListview.setAdapter(adapter);
        assertHasTheSameAdapter(binding.phonenumListview, adapter);
    }
    private void register() {
        PhonenumViewBinder phonenumViewBinder = new PhonenumViewBinder();
        phonenumViewBinder.setListener(this);
        adapter.register(Phonenum.class, phonenumViewBinder);
        adapter.register(Empty.class, new EmptyViewBinder());
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

    @Override
    public void onPhoneItemClickListener(String phoneNum) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
            startActivity(intent);
    }
    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码

    /**
     * 判断是否有某项权限
     * @param string_permission 权限
     * @param request_code 请求码
     * @return
     */
    public boolean checkReadPermission(String string_permission,int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(this, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(this, new String[]{string_permission}, request_code);
        }
        return flag;
    }

    /**
     * 检查权限后的回调
     * @param requestCode 请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: //拨打电话
                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
                    Toast.makeText(this,"请允许拨号权限后再试",Toast.LENGTH_SHORT).show();
                } else {//成功
                    //call("tel:"+"10086");
                }
                break;
        }
    }
}
