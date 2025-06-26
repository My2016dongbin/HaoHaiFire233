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
import com.skyline.terraexplorer.mainapps.multitype.AddCheckViewBinder;
import com.skyline.terraexplorer.mainapps.multitype.BjdInfo;
import com.skyline.terraexplorer.mainapps.multitype.CheckInfoViewBinder;

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

public class AddbjdActivity extends BaseActivity implements AddCheckViewBinder.OnAddCheckItemClick, CheckInfoViewBinder.OnCheckInfoItemClick{

    private static final String TAG = AddbjdActivity.class.getSimpleName();

    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private List<Object> photoItems = new ArrayList<>();
    private MultiTypeAdapter adapter;
    public List<BjdInfo> bjdInfoList;
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
        setContentView(R.layout.activity_comprehensive_check_mainapps);
        bjdInfoList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        dbConfig = new DbConfig(this);
        token = dbConfig.getUser().getToken();
        db = new DbConfig(this).getDbManager();
        initView();

        try {
            List<BjdInfo> checkListFromDb = dbConfig.getDbManager().selector(BjdInfo.class).findAll();
            if (checkListFromDb!=null){
                bjdInfoList = checkListFromDb;
                updateData();
            }else {
                Date date = new Date();
                String time = date.toLocaleString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
                String checkTimeOne = dateFormat.format(date);
                bjdInfoList.add(new BjdInfo(checkTimeOne,"","",0,"","","","","","","",0));
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
        CheckInfoViewBinder checkInfoViewBinder = new CheckInfoViewBinder();

        //checkInfoViewBinder.setImageListener(this);
        checkInfoViewBinder.setContext(this);
        checkInfoViewBinder.setListener(this);
        adapter.register(BjdInfo.class, checkInfoViewBinder);

        AddCheckViewBinder addCheckViewBinder = new AddCheckViewBinder();
        addCheckViewBinder.setListener(this);
        adapter.register(AddCheck.class, addCheckViewBinder);
    }
    private void updateData() {
        items.clear();
        if (bjdInfoList.size() == 0){
            Date date = new Date();
            String time = date.toLocaleString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
            String checkTimeOne = dateFormat.format(date);
            bjdInfoList.add(new BjdInfo(checkTimeOne,"","",0,"","","","","","","",0));
        }

        for (int i = 0; i < bjdInfoList.size(); i++) {
            items.add(bjdInfoList.get(i));
        }
        items.add(new AddCheck());

        assertAllRegistered(adapter, items);
        adapter.notifyDataSetChanged();
        Log.e(TAG, "updateData: "+ bjdInfoList.get(0).getSpec());
    }

    /**\
     * 添加资源点的饿点击回调
     */
    @Override
    public void onAddCheckItemClickListener() {
        if (bjdInfoList.get(bjdInfoList.size()-1).isSava) {     //上一条数据已经提交 //生成新的资源添加数据
            Date date = new Date();
            String time = date.toLocaleString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
            String checkTimeOne = dateFormat.format(date);
            currentId = checkTimeOne;
            currentCheckInfoStr = "";
            currentCheckName = "";
            bjdInfoList.add(new BjdInfo(checkTimeOne,"","",0,"","","","","","","",0));
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
    public void OnCheckSaveOrDeleteClickListener(boolean isSave,String id,String checkName,String checkInfoStr,String spec,String unit,String count,String unitPrice,String packaging,String remark) {
        currentId = id;
        currentCheckName = checkName;
        currentCheckInfoStr = checkInfoStr;
        if (isSave){        //保存  将图片上传到服务器
            if (currentCheckName.equals("")){
                Toast.makeText(this, "请输入检查点名称", Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentCheckInfoStr.equals("")){
                Toast.makeText(this, "请输入整体情况说明", Toast.LENGTH_SHORT).show();
                return;
            }
            showDialogProgress(progressDialog,"资源点上传中...");
                for (int i = 0; i < bjdInfoList.size(); i++) {
                    if (bjdInfoList.get(i).getBjdid().equals(currentId)) {
                        bjdInfoList.get(i).setSava(true);
                        bjdInfoList.get(i).setPlantname(checkName);
                        bjdInfoList.get(i).setVariety(checkInfoStr);
                        bjdInfoList.get(i).setSpec(spec);
                        bjdInfoList.get(i).setUnit(unit);
                        bjdInfoList.get(i).setCount(Integer.parseInt(count));
                        bjdInfoList.get(i).setUnitPrice(Integer.parseInt(unitPrice));
                        bjdInfoList.get(i).setPackaging(packaging);
                        bjdInfoList.get(i).setRemark(remark);
                        try {
                            dbConfig.getDbManager().saveOrUpdate(bjdInfoList.get(i));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        updateData();

                    }
                }
            List<BjdInfo> checkInfoList = new ArrayList<>();
            try {
                checkInfoList = db.selector(BjdInfo.class).findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "checkInfoList: "+checkInfoList.toString() );
        }else {      //删除  将该条数据从本地删除
            for (int i = 0; i < bjdInfoList.size(); i++) {
                if (bjdInfoList.get(i).getBjdid().equals(id)) {
                    try {
                        dbConfig.getDbManager().delete(bjdInfoList.get(i));
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    bjdInfoList.remove(i);

                }
            }

            updateData();
        }
    }
}
