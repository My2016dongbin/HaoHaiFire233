package com.haohai.platform.platformmodel.ui.acticity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haohai.platform.platformmodel.R;
import com.haohai.platform.platformmodel.ui.Multitype.ChooseImage;
import com.haohai.platform.platformmodel.ui.Multitype.ChooseImageViewBinder;
import com.haohai.platform.platformmodel.ui.Multitype.WeekChoose;
import com.haohai.platform.platformmodel.ui.Multitype.WeekChooseViewBinder;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;
import com.haohai.platform.platformmodel.ui.utils.DateWeekUtils;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.ActionBar;
import com.ruyiruyi.rylibrary.utils.GifSizeFilter;
import com.ruyiruyi.rylibrary.utils.image.ImagPagerUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.ielse.view.imagewatcher.ImageWatcher;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class WorkReportWeekAddActivity extends HhBaseActivity implements WeekChooseViewBinder.OnDateWeekChooseClick,ChooseImageViewBinder.OnChooseImageClickListener{

    private static final String TAG = WorkReportWeekAddActivity.class.getSimpleName();
    public List<Uri> uriChooseList;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private ChooseImageViewBinder chooseImageViewBinder;
    private List<ChooseImage> list = new ArrayList<>();
    private ImageWatcher vImageWatcher;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private String currentData;
    public List<WeekChoose> weekChooseList;
    private TextView dataView;
    private ActionBar actionBar;
    private Dialog dataDialog;
    private View dataInflate;
    private RecyclerView dataListView;
    private List<Object> dataItems = new ArrayList<>();
    private MultiTypeAdapter dataAdapter;
    private String currentData_day;
    private FrameLayout dateLayout;
    private EditText benzhouEdit;
    private EditText xiazhouEdit;
    private EditText wanchengEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report_week_add);
        uriChooseList = new ArrayList<>();
        weekChooseList = new ArrayList<>();
        initView();

        getWeekDate();

    }

    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("写周报");
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick(){
            @Override
            public void onItemClick(int var1) {
                switch ((var1)){
                    case -1:
                        onBackPressed();
                        break;
                    case -3:
                        postDataToService();
                        break;
                }
            }
        });
        actionBar.setRightView("完成");
        dataView = (TextView) findViewById(R.id.data_view);
        dateLayout = (FrameLayout) findViewById(R.id.date_layout);

        benzhouEdit = (EditText) findViewById(R.id.benzhou_edit);
        xiazhouEdit = (EditText) findViewById(R.id.xiazhou_edit);
        wanchengEdit = (EditText) findViewById(R.id.weiwancheng_edit);
        RxViewAction.clickNoDouble(dateLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dataDialog.show();
                    }
                });

        listView = (RecyclerView) findViewById(R.id.phote_recycle);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        listView.setLayoutManager(gridLayoutManager);
        adapter = new MultiTypeAdapter(items);
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);

        chooseImageViewBinder = new ChooseImageViewBinder(this);
        chooseImageViewBinder.setListener(this);
        adapter.register(ChooseImage.class, chooseImageViewBinder);
        listView.setAdapter(adapter);
        assertHasTheSameAdapter(listView, adapter);
        updateData();

        dataDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        dataInflate = LayoutInflater.from(this).inflate(R.layout.dialog_data_choose,null);
        dataInflate.setMinimumWidth(10000);

        //    chuliEditView = ((EditText) infoInflate.findViewById(R.id.chuli_edit));

        dataListView = (RecyclerView) dataInflate.findViewById(R.id.data_choose_recycle);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dataListView.setLayoutManager(linearLayoutManager);
        dataAdapter = new MultiTypeAdapter(dataItems);
        WeekChooseViewBinder weekChooseViewBinder = new WeekChooseViewBinder(this);
        weekChooseViewBinder.setListener(this);
        dataAdapter.register(WeekChoose.class, weekChooseViewBinder);
        dataListView.setAdapter(dataAdapter);
        assertHasTheSameAdapter(dataListView, dataAdapter);

        dataDialog.setContentView(dataInflate);
        Window dataWindow = dataDialog.getWindow();
        dataWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams dataListLp = dataWindow.getAttributes();

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int width = wm.getDefaultDisplay().getWidth();
        dataListLp.height = (int) (height * 0.7);
        dataWindow.setAttributes(dataListLp);
        dataDialog.setCanceledOnTouchOutside(true);
    }
    /**
     * 提交数据到服务器
     */
    private void postDataToService() {

        if (benzhouEdit.getText().toString().equals("")){
            Toast.makeText(this, "请输入本周工作汇报", Toast.LENGTH_SHORT).show();
            return;
        }
        if (xiazhouEdit.getText().toString().equals("")){
            Toast.makeText(this, "请输入下周工作汇报", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",new DbConfig(this).getUser().getId());
            jsonObject.put("dailyReportTime",currentData_day);
            jsonObject.put("reportType",1); //类型有1=每日 和 2=每周
            jsonObject.put("todaySummary",benzhouEdit.getText().toString());  //今日工作总结
            jsonObject.put("tomorrowPlan",xiazhouEdit.getText().toString());  //明日工作计划
            jsonObject.put("unfinishedWork",wanchengEdit.getText().toString());  //明日工作计划
            jsonObject.put("commitReportTime", formatter.format(curDate));  //上传时间


        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams(RequestUtils.REQUEST_URL + "api/workReport/saveReport");
        params.setAsJsonContent(true);
        params.setBodyContent(jsonObject.toString());
        params.addHeader("Authorization","bearer " + new DbConfig(this).getUser().getToken());
        Log.e(TAG, "postData:-- params--" + params);
        Log.e(TAG, "postData:-- jsonObject.toString()--" + jsonObject.toString());
        Log.e(TAG, "postData:-- jsonObject.toString()--" +"bearer " + new DbConfig(this).getUser().getToken());
        params.setConnectTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
                JSONObject jsonObject = null;

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
            }
        });
    }

    private void getWeekDate() {
        DateWeekUtils dateWeekUtils = new DateWeekUtils();

        String weeksLast = dateWeekUtils.getLastWeek();
        String weeks = dateWeekUtils.getWeeks();
        String weeksNext = dateWeekUtils.getNextWeek();
        String weeksNextNext = dateWeekUtils.getNextNextWeek();
        weekChooseList.add(new WeekChoose(0,weeksLast,false));
        weekChooseList.add(new WeekChoose(1,weeks,true));
        weekChooseList.add(new WeekChoose(2,weeksNext,false));
        weekChooseList.add(new WeekChoose(3,weeksNextNext,false));
        Log.e(TAG, "onCreate: " + weeks );
        Log.e(TAG, "onCreate: " + weeksLast );
        Log.e(TAG, "onCreate: " + weeksNext );
        Log.e(TAG, "onCreate: " + weeksNextNext );
        initDateData();
        dataView.setText(weeksLast);
    }

    private void initDateData() {
        dataItems.clear();

        for (int i = 0; i < weekChooseList.size(); i++) {
            dataItems.add(weekChooseList.get(i));
        }
        assertAllRegistered(dataAdapter,dataItems);
        dataAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDateWeekChooseClickListener(int id) {
        for (int i = 0; i < weekChooseList.size(); i++) {
            if (weekChooseList.get(i).getId() == id) {
                weekChooseList.get(i).setChosoe(true);
                dataView.setText(weekChooseList.get(i).getWeek());
            }else {
                weekChooseList.get(i).setChosoe(false);
            }
        }

        dataDialog.dismiss();
        initDateData();
    }

    @Override
    public void onImageAddClickListener(boolean add, Uri uri, String id) {
        if (add){
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            int size = 9 - list.size();
                            Matisse.from(WorkReportWeekAddActivity.this)
                                    .choose(MimeType.allOf())
                                    .countable(true)
                                    .capture(true)
                                    .captureStrategy(
                                            new CaptureStrategy(true,"com.haohai.platform.fireforestplatform")
                                    )
                                    .maxSelectable(size)
                                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                    .gridExpectedSize(
                                            getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new GlideEngine())
                                    .forResult(REQUEST_CODE_CHOOSE);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else {
            //点击查看大图
            ArrayList<String> picList = new ArrayList<>();
            String oneUri = uri.toString();
            picList.add(oneUri); //点击哪张 把哪张放第一个
            for (int i = 0; i < list.size(); i++) {     //除去点击那张  其他放进去
                if (!oneUri.equals(list.get(i).getUri().toString())){
                    picList.add(list.get(i).getUri().toString());
                }
            };
            String content = "";     //放评论
            ImagPagerUtil imagPagerUtil = new ImagPagerUtil(WorkReportWeekAddActivity.this, picList);
            imagPagerUtil.setContentText(content);
            imagPagerUtil.show();
        }
    }

    @Override
    public void onImageDelete(Uri uri, String id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUri().equals(uri)) {
                list.remove(i);
            }
        }
        updateData();
    }

    private void updateData() {
        items.clear();
        if (list==null){
            ChooseImage chooseImage = new ChooseImage();
            chooseImage.setAdd(true);
            items.add(chooseImage);
        }else {
            if (list.size()<9){
                for (int i = 0; i < list.size(); i++) {
                    items.add(list.get(i));
                }
                ChooseImage chooseImage = new ChooseImage();
                chooseImage.setAdd(true);
                items.add(chooseImage);
            }else {
                for (int i = 0; i < list.size(); i++) {
                    items.add(list.get(i));
                }
            }
        }

        assertAllRegistered(adapter,items);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            List<Uri> uriList = Matisse.obtainResult(data);

            //  Log.e(TAG, "onActivityResult: " + uriList.get(0).toString());
            //去掉重复图片
            int uriSize = uriList.size();
            int listSize = list.size();
            int index = 0;
            for (int i = 0; i < uriList.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (uriList.get(i).toString().equals(list.get(j).getUri().toString())) {

                        Toast.makeText(this, "不可添加重复图片！", Toast.LENGTH_SHORT).show();
                        uriList.remove(i);
                        if (uriList.size() == 0) {
                            return;
                        }

                    }
                }
            }


            // 判断只能添加五张图片
            if ( (uriList.size() + list.size()) > 9){
                Toast.makeText(this, "最多只能添加9张", Toast.LENGTH_SHORT).show();
                int size =  9 - list.size();
                for (int i = 0; i < size; i++) {
                    ChooseImage chooseImage = new ChooseImage();
                    chooseImage.setUri(uriList.get(i));
                    chooseImage.setAdd(false);
                    list.add(chooseImage);
                    // items.add(evaluateImage);
                }
            }else {
                //不足5张的添加  添加图片按钮
                int size = uriList.size();
                for (int i = 0; i < size; i++) {
                    ChooseImage chooseImage = new ChooseImage();
                    chooseImage.setUri(uriList.get(i));
                    chooseImage.setAdd(false);
                    list.add(chooseImage);
                    // items.add(evaluateImage);
                }
                ChooseImage chooseImage = new ChooseImage();
                chooseImage.setAdd(true);
                // items.add(evaluateImage);
            }
            // assertAllRegistered(adapter,items);;
            // adapter.notifyDataSetChanged();
            updateData();
        }

    }
}
