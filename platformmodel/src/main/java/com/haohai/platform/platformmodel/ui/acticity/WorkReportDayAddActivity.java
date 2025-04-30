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
import com.haohai.platform.platformmodel.ui.Multitype.DayChoose;
import com.haohai.platform.platformmodel.ui.Multitype.DayChooseViewBinder;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;
import com.ruyiruyi.rylibrary.db.DbConfig;
import com.ruyiruyi.rylibrary.request.RequestUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.ielse.view.imagewatcher.ImageWatcher;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.functions.Action1;

import static me.drakeet.multitype.MultiTypeAsserts.assertAllRegistered;
import static me.drakeet.multitype.MultiTypeAsserts.assertHasTheSameAdapter;

public class WorkReportDayAddActivity extends HhBaseActivity implements ChooseImageViewBinder.OnChooseImageClickListener,DayChooseViewBinder.OnDateChooseClick {
    private static final String TAG = WorkReportDayAddActivity.class.getSimpleName();
    public List<Uri> uriChooseList;
    private RecyclerView listView;
    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private ChooseImageViewBinder chooseImageViewBinder;
    private List<ChooseImage> list = new ArrayList<>();
    private ImageWatcher vImageWatcher;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private String currentData;
    public List<DayChoose> dayChooseList;
    private TextView dataView;
    private ActionBar actionBar;
    private Dialog dataDialog;
    private View dataInflate;
    private RecyclerView dataListView;
    private List<Object> dataItems = new ArrayList<>();
    private MultiTypeAdapter dataAdapter;
    private String currentData_day;
    private FrameLayout dateLayout;
    private EditText jinriEdit;
    private EditText mingtiEdit;
    private EditText zhongdianEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report_day_add);
        uriChooseList = new ArrayList<>();
        dayChooseList = new ArrayList<>();

        initView();
        getData();

        //配置点击查看大图
        initImageLoader();
    }

    private void getData() {
        //创建集合储存日期
        ArrayList<String> dateList = new ArrayList<>();
        //获取当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE");
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy年");
        SimpleDateFormat sdfWeek = new SimpleDateFormat("EEEE");
        SimpleDateFormat sdfMonthDay = new SimpleDateFormat("MM月dd日");
        SimpleDateFormat sdfYearMonthDay = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdfYear_month_day = new SimpleDateFormat("yyyy-MM-dd");
        currentData = sdfYearMonthDay.format(new Date().getTime());
        currentData_day = sdfYear_month_day.format(new Date().getTime());
        String format = sdf.format(new Date().getTime());
        sdf.format(new Date().getTime());

        //先将日期往前加1天
        for (int i = 0; i < 2;i++){
            Date date = sdf.parse(format, new ParsePosition(0));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, +1);
            Date date1 = calendar.getTime();
            format = sdf.format(date1);
        }
        //去前一天跟后14天的时间
        for (int i = 0; i < 16;i++){
            // 将当前的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
            Date date = sdf.parse(format, new ParsePosition(0));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            // add方法的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天，你可根据自己的需求自行决定,
            //如果项目中需要多次调用，你也可把这个参数，通过方法动态传入
            calendar.add(Calendar.DATE, -1);
            Date date1 = calendar.getTime();
            format = sdf.format(date1);
            String year = sdfYear.format(date1);
            String week = sdfWeek.format(date1);
            String monthDay = sdfMonthDay.format(date1);
            String yearMonthDay = sdfYear_month_day.format(date1);
            if ( i == 1){
                dayChooseList.add(new DayChoose(i,year,monthDay,week,format,yearMonthDay,true));    //当前天改红色
            }else {
                dayChooseList.add(new DayChoose(i,year,monthDay,week,format,yearMonthDay,false));
            }


            Log.e(TAG, "getDateList: " + format);
        }

        initDateData();
        dataView.setText(currentData);
    }

    private void initDateData() {
        dataItems.clear();

        for (int i = 0; i < dayChooseList.size(); i++) {
            dataItems.add(dayChooseList.get(i));
        }
        assertAllRegistered(dataAdapter,dataItems);
        dataAdapter.notifyDataSetChanged();
    }


    private void initView() {
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("写日报");
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
        jinriEdit = (EditText) findViewById(R.id.jinri_edit);
        mingtiEdit = (EditText) findViewById(R.id.mingri_edit);
        zhongdianEdit = (EditText) findViewById(R.id.zhongdian_edit);
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
        DayChooseViewBinder dayChooseViewBinder = new DayChooseViewBinder(this);
        dayChooseViewBinder.setListener(this);
        dataAdapter.register(DayChoose.class, dayChooseViewBinder);
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

        if (jinriEdit.getText().toString().equals("")){
            Toast.makeText(this, "请输入今日工作汇报", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mingtiEdit.getText().toString().equals("")){
            Toast.makeText(this, "请输入明日工作汇报", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId",new DbConfig(this).getUser().getId());
            jsonObject.put("dailyReportTime",currentData_day);
            jsonObject.put("reportType",1); //类型有1=每日 和 2=每周
            jsonObject.put("todaySummary",jinriEdit.getText().toString());  //今日工作总结
            jsonObject.put("tomorrowPlan",mingtiEdit.getText().toString());  //明日工作计划
            jsonObject.put("unfinishedWork",zhongdianEdit.getText().toString());  //明日工作计划
            jsonObject.put("commitReportTime", formatter.format(curDate).replace(" ","T"));



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

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

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
                            Matisse.from(WorkReportDayAddActivity.this)
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
            ImagPagerUtil imagPagerUtil = new ImagPagerUtil(WorkReportDayAddActivity.this, picList);
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

    @Override
    public void onDateChooseClickListener(int id) {
        for (int i = 0; i < dayChooseList.size(); i++) {
            if (dayChooseList.get(i).getId() == id) {
                dayChooseList.get(i).setChoose(true);
                dataView.setText(dayChooseList.get(i).getDayStr());
            }else {
                dayChooseList.get(i).setChoose(false);
            }
        }

        dataDialog.dismiss();
        initDateData();
    }
}
