package com.skyline.terraexplorer.mainapps.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.mainapps.db.DbConfig;
import com.skyline.terraexplorer.mainapps.multitype.AddCheck;
import com.skyline.terraexplorer.mainapps.multitype.AddLcp;
import com.skyline.terraexplorer.mainapps.multitype.AddLcpItemViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.AddlcpViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.LcpInfo;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.drakeet.multitype.MultiTypeAdapter;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class AddlcpActivity extends BaseActivity implements AddLcpItemViewBinder.OnAddCheckItemClick, AddlcpViewBinder.OnCheckInfoItemClick{

    private static final String TAG = AddlcpActivity.class.getSimpleName();

    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private List<Object> photoItems = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<LcpInfo> lcpInfoList;
    public String currentId = "";
    private ProgressDialog  progressDialog;
    private String token;
    private String currentCheckName;
    private String currentCheckInfoStr;
    private DbConfig dbConfig;
    private ImageView backButton;
    private DbManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lcp);
        lcpInfoList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        dbConfig = new DbConfig(this);
        token = dbConfig.getUser().getToken();
        db = new DbConfig(this).getDbManager();
        initView();
        try {
            List<LcpInfo> checkListFromDb = dbConfig.getDbManager().selector(LcpInfo.class).findAll();
            if (checkListFromDb!=null){
                for (int i = 0; i <checkListFromDb.size() ; i++) {
                    Log.e(TAG, "onCreate: "+checkListFromDb.get(i).isSava );
                }
                lcpInfoList = checkListFromDb;
                updateData();
            }else {
                Date date = new Date();
                String time = date.toLocaleString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
                String checkTimeOne = dateFormat.format(date);
                lcpInfoList.add(new LcpInfo(checkTimeOne,0,0,"","","","",""));
                updateData();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listView = (RecyclerView) findViewById(R.id.check_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);
        adapter = new MultiTypeAdapter(items);
        register();
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
    }

    private void register() {
        AddlcpViewBinder addlcpViewBinder = new AddlcpViewBinder();

        //AddlcpViewBinder.setImageListener(this);
        addlcpViewBinder.setContext(this);
        addlcpViewBinder.setListener(this);
        adapter.register(LcpInfo.class, addlcpViewBinder);

        AddLcpItemViewBinder addLcpItemViewBinder = new AddLcpItemViewBinder();
        addLcpItemViewBinder.setListener(this);
        adapter.register(AddLcp.class, addLcpItemViewBinder);
    }
    private void updateData() {
        items.clear();
        if (lcpInfoList.size() == 0){
            Date date = new Date();
            String time = date.toLocaleString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
            String checkTimeOne = dateFormat.format(date);
            lcpInfoList.add(new LcpInfo(checkTimeOne,0,0,"","","","",""));
        }

        for (int i = 0; i < lcpInfoList.size(); i++) {
            items.add(lcpInfoList.get(i));
        }
        items.add(new AddLcp());

        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
    }

    /**\
     * 添加资源点的饿点击回调
     */
    @Override
    public void onAddCheckItemClickListener() {
        if (lcpInfoList.get(lcpInfoList.size()-1).isSava) {     //上一条数据已经提交 //生成新的资源添加数据
            Date date = new Date();
            String time = date.toLocaleString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
            String checkTimeOne = dateFormat.format(date);
            currentId = checkTimeOne;
            currentCheckInfoStr = "";
            currentCheckName = "";
            lcpInfoList.add(new LcpInfo(checkTimeOne,0,0,"","","","",""));
            updateData();
        }else {
            Toast.makeText(this, "请保存资源点后继续添加下一资源点", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 资源点的保存或者删除
     * @param isSave
     */
    @Override
    public void OnCheckSaveOrDeleteClickListener(boolean isSave,String id,String checkName,String checkInfoStr,String spec,String unit,String count,String unitPrice,String remark) {
        currentId = id;
        currentCheckName = checkName;
        currentCheckInfoStr = checkInfoStr;
        if (isSave){        //保存  将图片上传到服务器
            if (currentCheckName.equals("")){
                Toast.makeText(this, "请输入种类", Toast.LENGTH_SHORT).show();
                return;
            }
            if (checkInfoStr.equals("")){
                Toast.makeText(this, "请输入品种", Toast.LENGTH_SHORT).show();
                return;
            }
            showDialogProgress(progressDialog,"上传中...");
                for (int i = 0; i < lcpInfoList.size(); i++) {
                    if (lcpInfoList.get(i).getLcpid().equals(currentId)) {
                        lcpInfoList.get(i).setSava(true);
                        lcpInfoList.get(i).setKind(checkName);
                        lcpInfoList.get(i).setVariety(checkInfoStr);
                        if (!spec.equals("")){
                            lcpInfoList.get(i).setArea(Double.valueOf(spec));
                        }
                        if(!unit.equals("")){
                            lcpInfoList.get(i).setAnnualOutput(Double.valueOf(unit));
                        }
                        lcpInfoList.get(i).setSalesWay(count);
                        lcpInfoList.get(i).setSalesArea(unitPrice);
                        lcpInfoList.get(i).setRemark(remark);
                        Log.e(TAG, "OnCheckSaveOrDeleteClickListener: "+ lcpInfoList.get(i).isSava);
                        try {
                            dbConfig.getDbManager().saveOrUpdate(lcpInfoList.get(i));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        updateData();

                    }
                }
            List<LcpInfo> checkInfoList = new ArrayList<>();
            try {
                checkInfoList = db.selector(LcpInfo.class).findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "checkInfoList: "+checkInfoList.toString() );
        }else {      //删除  将该条数据从本地删除
            for (int i = 0; i < lcpInfoList.size(); i++) {
                if (lcpInfoList.get(i).getLcpid().equals(id)) {
                    try {
                        dbConfig.getDbManager().delete(lcpInfoList.get(i));
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    lcpInfoList.remove(i);

                }
            }

            updateData();
        }
    }
}
