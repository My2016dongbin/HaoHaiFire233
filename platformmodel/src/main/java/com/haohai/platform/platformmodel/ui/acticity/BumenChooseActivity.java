package com.haohai.platform.platformmodel.ui.acticity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.haohai.platform.platformmodel.R;
import com.haohai.platform.platformmodel.ui.Multitype.DepartmentViewBinder;
import com.haohai.platform.platformmodel.ui.Multitype.UserModelViewBinder;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.db.User;
import com.ruyiruyi.rylibrary.db.Department;
import com.ruyiruyi.rylibrary.db.UserModel;
import com.haohai.platform.platformmodel.ui.utils.RequestCode;
import com.ruyiruyi.rylibrary.cell.ActionBar;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class BumenChooseActivity extends HhBaseActivity implements DepartmentViewBinder.OnBumenItemClick ,UserModelViewBinder.OnUserItemClickListener {

    private static final String TAG = BumenChooseActivity.class.getSimpleName();
    private User user;
    private String groupId;
    private String token;
    public String currentParentId = "001002001";
    private List<Department> departmentList;
    private List<Department> currentDepartmentList;
    private ActionBar actionBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private RecyclerView listView;
    private ProgressDialog progressDialog;
    private Department currentDepartment = null;
    private int currentCengji = 1;
    private String currentCengjiOneId = "001002001";
    private String currentCengjiTwoId = "";
    private String currentCengjiThreeId = "";
    private String currentCengjiFourId = "";
    public boolean isBumen = true;
    private List<UserModel> userModelList;
    private List<UserModel> currentUserModelList;
    private String currentChooseUser =  "";
    private TextView shangjiView;
    private int choose_state;       //0是选择上级 1是选择经理

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bumen_choose);

        Intent intent = getIntent();
        choose_state = intent.getIntExtra("CHOOSE_STATE",0);

        userModelList = new ArrayList<>();
        currentUserModelList = new ArrayList<>();
        departmentList = new ArrayList<>();
        currentDepartmentList = new ArrayList<>();
        user = new DbConfig(this).getUser();
        groupId = user.getGroupId();
        token = user.getToken();
        progressDialog = new ProgressDialog(this);

        initView();

        getBumenDataFromService();
        getUserModelDataFromDb();
    }



    private void initView() {
        shangjiView = (TextView) findViewById(R.id.shangji_view);
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        if (choose_state == 0){
            actionBar.setTitle("选择上级");
            shangjiView.setText("请选择上级");
        }else {
            actionBar.setTitle("选择经理");
            shangjiView.setText("请选择经理");
        }

        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int var1) {
                switch ((var1)) {
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        Intent intent = new Intent();
                        intent.putExtra("SHANGJI",currentChooseUser);
                        intent.putExtra("CHOOSE_STATE",choose_state);
                        setResult(RequestCode.DATA_CHANGE,intent);
                        finish();
                        break;
                }
            }
        });
        actionBar.setRightView("完成");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.bumen_refresh_layout);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        listView = (RecyclerView) findViewById(R.id.bumen_listview);


        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               /* isShuaxin = true;
                leaveFlowList.clear();
                currentPage = 0;
                isShowDialog = false;
                getDataFromService();*/
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter(items);

        register();

        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);

    }

    private void register() {
        DepartmentViewBinder departmentViewBinder = new DepartmentViewBinder();
        departmentViewBinder.setListener(this);
        adapter.register(Department.class, departmentViewBinder);
        UserModelViewBinder userModelViewBinder = new UserModelViewBinder(this);
        userModelViewBinder.setListener(this);
        adapter.register(UserModel.class, userModelViewBinder);
    }

    /**、
     * 从数据库获取人员数据
     */
    private void getUserModelDataFromDb() {
        userModelList = new DbConfig(this).getUserModel();
        Log.e(TAG, "getUserModelDataFromDb: userModelList.size()== " + userModelList.size() );
    }

    /**
     * 从数据库获取部门数据
     */
    private void getBumenDataFromService() {
        departmentList =  new DbConfig(this).getDepartment();
        currentDepartmentList.clear();
        for (int i = 0; i < departmentList.size(); i++) {
            if (departmentList.get(i).getParentId().equals(currentParentId)) {
                currentDepartmentList.add(departmentList.get(i));
            }
        }
        initData();
    }

    private void initData() {
        items.clear();

        Log.e(TAG, "initData: isbumen" + isBumen);
        if (isBumen) {
            for (int i = 0; i < currentDepartmentList.size(); i++) {
                items.add(currentDepartmentList.get(i));
            }
        }else {
            for (int i = 0; i < currentUserModelList.size(); i++) {
                items.add(currentUserModelList.get(i));
            }
        }


        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }


    /**
     * 回退键监听
     */
    @Override
    public void onBackPressed() {
        Log.e(TAG, "onBackPressed: currentCengji = " + currentCengji );
        Log.e(TAG, "onBackPressed: currentCengjiTwoId = " + currentCengjiTwoId );


        if (currentCengji == 1){
            finish();
        }else {

            if (isBumen){
                currentDepartmentList.clear();
            }else {
                currentUserModelList.clear();
            }
            isBumen = true;    //上一层是部门
            if (currentCengji == 2){
                for (int i = 0; i < departmentList.size(); i++) {
                    Log.e(TAG, "onBackPressed: departmentList.get(i).getParentId()=" +departmentList.get(i).getParentId() );
                    if (departmentList.get(i).getParentId().equals(currentCengjiTwoId)) {
                        currentDepartmentList.add(departmentList.get(i));
                    }
                }
                initData();
            }else if (currentCengji == 3){
                for (int i = 0; i < departmentList.size(); i++) {
                    if (departmentList.get(i).getParentId().equals(currentCengjiThreeId)) {
                        currentDepartmentList.add(departmentList.get(i));
                    }
                }
                initData();
            }else if (currentCengji == 4){
                for (int i = 0; i < departmentList.size(); i++) {
                    if (departmentList.get(i).getParentId().equals(currentCengjiFourId)) {
                        currentDepartmentList.add(departmentList.get(i));
                    }
                }
                initData();
            }
            currentCengji = currentCengji -1;
        }
    }

    /**
     * 用户点击的条目点击
     * @param userModel
     */
    @Override
    public void onUserItemClickListener(UserModel userModel) {
        currentChooseUser = userModel.getFullName();
        shangjiView.setText("上级人为 " + currentChooseUser);
        isBumen = false;
/*
        if (currentCengji == 2){
            currentCengjiTwoId = userModel.getDeptId();
        }else if (currentCengji == 3){
            currentCengjiThreeId = userModel.getDeptId();
        }else if (currentCengji == 4){
            currentCengjiFourId = userModel.getDeptId();
        }*/

        for (int i = 0; i < userModelList.size(); i++) {
            userModelList.get(i).setChoose(false);
            if (userModelList.get(i).getId().equals(userModel.getId())){
                userModelList.get(i).setChoose(true);
            }
        }
        for (int i = 0; i < currentUserModelList.size(); i++) {
            currentUserModelList.get(i).setChoose(false);
            if (currentUserModelList.get(i).getId().equals(userModel.getId())){
                currentUserModelList.get(i).setChoose(true);
            }
        }
        initData();
    }
    /**
     * 部门的条目点击
     * @param department
     */
    @Override
    public void onBumenItemClick(Department department) {
        isBumen = true;
        if (currentCengji < 4){
            currentCengji = currentCengji + 1;
        }

        if (currentCengji == 2){
            currentCengjiTwoId = department.getParentId();
        }else if (currentCengji == 3){
            currentCengjiThreeId = department.getParentId();
        }else if (currentCengji == 4){
            currentCengjiFourId = department.getParentId();
        }
        currentDepartmentList.clear();
        for (int i = 0; i < departmentList.size(); i++) {
            if (departmentList.get(i).getParentId().equals(department.getId())) {
                currentDepartmentList.add(departmentList.get(i));
            }
        }

        if (currentDepartmentList.size() == 0){
            isBumen = false;
            currentUserModelList.clear();
            for (int i = 0; i < userModelList.size(); i++) {
                try {
                    if (userModelList.get(i).getDeptId().equals(department.getId())) {
                        currentUserModelList.add(userModelList.get(i));
                    }
                }catch (Exception e){

                }

            }
            if (currentUserModelList.size() == 0){
                Toast.makeText(this, "部门暂无成员，请重新选择", Toast.LENGTH_SHORT).show();
                return;
            }

        }

        initData();
    }

}
