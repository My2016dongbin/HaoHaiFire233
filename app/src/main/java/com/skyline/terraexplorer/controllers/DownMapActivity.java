package com.skyline.terraexplorer.controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.base.HBaseActivity;
import com.skyline.terraexplorer.models.AppLinks;
import com.skyline.terraexplorer.models.MapFile;
import com.skyline.terraexplorer.utils.RxViewAction;
import com.skyline.terraexplorer.views.progress.LineProgress;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.functions.Action1;

import static com.ruyiruyi.rylibrary.request.RequestUtils.REQUEST_URL_BASE;

public class DownMapActivity extends HBaseActivity {

    private static final String TAG = DownMapActivity.class.getSimpleName();
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
//    private static final String RECENT_PROJECTS = "com.skyline.terraexplorer.RECENT_PROJECTS";
//    private static final String RECENT_PROJECTS_SEPARATOR = "x,x,x,x,x,x";
    private static int REQUEST_PERMISSION_CODE = 1;
    String filepath = "";
    public List<MapFile> mapFileList;
    private TextView sizeView;
    private TextView startButton;
    private ProgressDialog progressDialog;
    private int size = 0;
    private LineProgress mProgress;
    private LinearLayout startLayout;
    private android.app.AlertDialog.Builder successDialog;
    private android.app.AlertDialog.Builder faildDialog;
    private android.app.AlertDialog.Builder wifiDialog;
    private ImageView backButon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_map);

     /*  if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }*/
        mapFileList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        filepath =   getObbDir().getAbsolutePath() + "/" + "haohai/qingdao";
       // filepath = getObbDir().getAbsolutePath();
        Log.e(TAG, "onCreate: " + filepath);
        File file = new File(filepath);
        if (!file.exists()) {
            file.mkdirs();//创建文件夹
        }

        verifyStoragePermissions(this);
        initView();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }else {
            //    downFile();
            }
        }else {
          //  downFile();
        }

        //downMapFile();
        FileDownloader.setup(this);
     //   downFile();

        getFireDataFromService();

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_PERMISSION_CODE);
        }
    }

    private void initView() {
        backButon = (ImageView) findViewById(R.id.back_button);
        backButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sizeView = (TextView) findViewById(R.id.size_text);
        startButton = (TextView) findViewById(R.id.start_button);
        startLayout = (LinearLayout) findViewById(R.id.start_button_layout);
        mProgress = (LineProgress) findViewById(R.id.down_view);
        successDialog = new android.app.AlertDialog.Builder(this)
               .setTitle("下载成功")
               .setMessage("是否重新加载地图")
               .setPositiveButton("确定 ", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       startActivity(new Intent(getApplicationContext(), TEMainActivity.class));

                       Intent intent2 = new Intent();
                       intent2.setAction("out_login");
                       sendBroadcast(intent2);
                       finish();

                   }
               })
               .setNegativeButton("取消 ", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {


                   }
               });
        faildDialog = new android.app.AlertDialog.Builder(this)
                .setTitle("下载失败")
                .setMessage("是否重新下载")
                .setPositiveButton("确定 ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createMapFile();

                    }
                })
                .setNegativeButton("取消 ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
        wifiDialog = new android.app.AlertDialog.Builder(this)
               .setTitle("网络状态")
               .setMessage("当前网络不在WIFI状态，是否继续下载")
               .setPositiveButton("确定 ", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       startLayout.setVisibility(View.GONE);
                       mProgress.setVisibility(View.VISIBLE);

                       createMapFile();

                   }
               })
               .setNegativeButton("取消 ", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });

        RxViewAction.clickNoDouble(startButton)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        if (isWifi(getApplicationContext())){
                            startLayout.setVisibility(View.GONE);
                            mProgress.setVisibility(View.VISIBLE);

                            createMapFile();
                        }else {
                            wifiDialog.show();
                        }

                        //start();

                    }
                });
    }

    private void getFireDataFromService() {

        //showDialogProgress(progressDialog,"数据加载中....");
//        RequestParams params = new RequestParams("http://49.232.128.132:10172/app/publish_phone/config.json");
        RequestParams params = new RequestParams(REQUEST_URL_BASE + "qingdao/config.json");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
              //  Log.e(TAG, "onSuccess:json-- " + result);
                try {
                    mapFileList.clear();
                    size = 0;
                    JSONArray array = new JSONArray(result);
                    for (int i = 0; i < array.length() ; i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        int level = jsonObject.getInt("level");
                        int size0 = jsonObject.getInt("size");
                        size = size + size0;
                        if (level == 0){        //是文件
                            mapFileList.add(new MapFile(name,"","",1,0,""));

                        }else if ( level == 1){ //是文件夹
                            mapFileList.add(new MapFile(name,"","",1,1,""));
                            JSONArray childrenOne = jsonObject.getJSONArray("children");
                            for (int j = 0; j < childrenOne.length(); j++) {
                                String name1 = childrenOne.getJSONObject(j).getString("name");
                                String name_cn = childrenOne.getJSONObject(j).getString("name_cn");
                                int level1 = childrenOne.getJSONObject(j).getInt("level");
                                int size1 = jsonObject.getInt("size");
                                size = size + size1;
                                if (level1 == 0){   //是文件
                                    mapFileList.add(new MapFile(name,name1,"",2,0,name_cn));
                                }else if (level1 == 1){//是文件夹
                                    mapFileList.add(new MapFile(name,name1,"",2,1,name_cn));
                                    JSONArray childrenTwo = childrenOne.getJSONObject(j).getJSONArray("children");
                                    for (int k = 0; k < childrenTwo.length(); k++) {
                                        String name2 = childrenTwo.getJSONObject(k).getString("name");
                                        int size2 = jsonObject.getInt("size");
                                        mapFileList.add(new MapFile(name,name1,name2,3,0,name_cn));
                                        size = size + size2;
                                    }
                                }
                            }
                        }
                    }

                    sizeView.setText("大小:" + size / 1024 /1024 + "M");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                //progressDialog.dismiss();
            }
        });
    }

    /**
     * 创建地图文件夹
     */
    private void createMapFile() {
        for (int i = 0; i < mapFileList.size(); i++) {
            if (mapFileList.get(i).getLevel() == 1) {
                if (mapFileList.get(i).getRank() == 1){
                    File file = new File(filepath + "/" + mapFileList.get(i).getName());
                    if (!file.exists()) {
                        file.mkdirs();//创建文件夹
                    }
                }else if (mapFileList.get(i).getRank() == 2){
                    File file = new File(filepath + "/" + mapFileList.get(i).getName() + "/" + mapFileList.get(i).getFileNameOne());
                    if (!file.exists()) {
                        file.mkdirs();//创建文件夹
                    }
                }

            }
        }

        /*//https://hhandroid.oss-cn-hangzhou.aliyuncs.com/qdm.mpt
        mapFileList.add(new MapFile("qdm.mpt","","",1,0,""));//TODO to测试本地
        //https://hhandroid.oss-cn-hangzhou.aliyuncs.com/qdmf.fly
        mapFileList.add(new MapFile("qdmf.fly","","",1,0,""));//TODO to测试本地
*/
        createMap();
    }


    private void addProjectAsRecent(String path) {
        ArrayList<String> lruProjects = getRecentProjects();
        // if selected project already was in lru list, remove it from the list 如果所选项目已经在lru列表中，请从列表中删除它
        lruProjects.remove(path);
        lruProjects.add(0, path);
        // limit to 20 recent projects 限于20个最近的项目
        while (lruProjects.size() > 20) {
            lruProjects.remove(lruProjects.size() - 1);
        }
        Log.e(TAG, "addProjectAsRecent: " + lruProjects );
        Log.e(TAG, "addProjectAsRecent: TextUtils.join(RECENT_PROJECTS_SEPARATOR, lruProjects) " + TextUtils.join(TEMainActivity.RECENT_PROJECTS_SEPARATOR, lruProjects) );
        boolean commit = getPreferences(MODE_PRIVATE).edit().putString(TEMainActivity.RECENT_PROJECTS, TextUtils.join(TEMainActivity.RECENT_PROJECTS_SEPARATOR, lruProjects)).commit();
        Log.e(TAG, "addProjectAsRecent: commit " + commit );
        String[] recentProjects = getPreferences(MODE_PRIVATE).getString(TEMainActivity.RECENT_PROJECTS, "").split(TEMainActivity.RECENT_PROJECTS_SEPARATOR);
        Log.e(TAG, "getRecentProjects: getPreferences(MODE_PRIVATE).getString(RECENT_PROJECTS, ) = " + getPreferences(MODE_PRIVATE).getString(TEMainActivity.RECENT_PROJECTS, "") );
        Log.e(TAG, "getRecentProjects: recentProjects = " + Arrays.asList(recentProjects).toString() );


    }

    private ArrayList<String> getRecentProjects() {
        String[] recentProjects = getPreferences(MODE_PRIVATE).getString(TEMainActivity.RECENT_PROJECTS, "").split(TEMainActivity.RECENT_PROJECTS_SEPARATOR);
        if (TextUtils.isEmpty(recentProjects[0])) {
            recentProjects[0] = AppLinks.getDefaultFlyFile();
            return new ArrayList<String>(Arrays.asList(recentProjects));
        }

        ArrayList<String> userProjects = new ArrayList<String>(Arrays.asList(recentProjects));
        // remove all disk projects that are not on the disk anymore form recent list 删除最近列出的不在磁盘上的所有磁盘项目
        for (int i = 0; i < userProjects.size(); i++) {
            if (userProjects.get(i).startsWith("/") && new File(userProjects.get(i)).exists() == false) {
                userProjects.remove(i);
                i--;
            }
        }
        return userProjects;
    }

    /**
     * 下载地图资源文件
     */
    private void createMap() {
        //downFile("http://49.232.128.132:10172/app/publish_phone/data/防火通道/防火通道.dbf","/storage/emulated/0/haohai/qingdao/data/防火通道");
        for (int i = 0; i < mapFileList.size(); i++) {
//            String url = "http://49.232.128.132:10172/app/publish_phone/";
            String url = REQUEST_URL_BASE + "qingdao/qingdao/";
//            String url = "https://hhandroid.oss-cn-hangzhou.aliyuncs.com/"+mapFileList.get(i).getName();
            String name = "";
//            String name = mapFileList.get(i).getName();
            String filepath = getObbDir().getAbsolutePath() + "/" + "haohai/qingdao";
            if (mapFileList.get(i).getLevel() == 0) {
                if (mapFileList.get(i).getRank() == 1){ //TODO to测试本地 old
                    url = url + mapFileList.get(i).getName();
                    name = mapFileList.get(i).getName();
                }else if (mapFileList.get(i).getRank() == 2){
                    filepath = filepath + "/" + mapFileList.get(i).getName() ;
                    url = url + mapFileList.get(i).getName() + "/" + mapFileList.get(i).getFileNameOne();
                    name = mapFileList.get(i).getFileNameOne();
                }else if (mapFileList.get(i).getRank() == 3){
                 //   filepath = filepath + "/" + mapFileList.get(i).getName() + "/" + mapFileList.get(i).getName_cn(); //中文名
                    filepath = filepath + "/" + mapFileList.get(i).getName() + "/" + mapFileList.get(i).getFileNameOne();
                    url = url + mapFileList.get(i).getName() + "/" + mapFileList.get(i).getFileNameOne() + "/" + mapFileList.get(i).getFileNameTwo();
                    name = mapFileList.get(i).getFileNameTwo();
                }

                Log.e(TAG, "createMap: url--  " + url );
                Log.e(TAG, "createMap: filepath--  " + filepath );
                downFile(url,filepath,name);
               /* FileDownloader.getImpl().create(url)
                        .setPath(filepath)
                        //.setCallbackProgressTimes(0) // 由于是队列任务, 这里是我们假设了现在不需要每个任务都回调`FileDownloadListener#progress`, 我们只关系每个任务是否完成, 所以这里这样设置可以很有效的减少ipc.
                        .setListener(queueTarget)
                        .asInQueueTask()
                        .enqueue();*/

            }


        }
        //FileDownloader.getImpl().start(queueTarget, false);
    }

    final FileDownloadListener queueTarget = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Log.e(TAG, "pending: " );
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            Log.e(TAG, "connected: " );
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            Log.e(TAG, "progress:soFarBytes "  + soFarBytes);
            Log.e(TAG, "progress:totalBytes " + totalBytes);
            Log.e(TAG, "progress: " + (100 - totalBytes / soFarBytes));
        }

        @Override
        protected void blockComplete(BaseDownloadTask task) {
        }

        @Override
        protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            Log.e(TAG, "completed: " );
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            Log.e(TAG, "error: "+ task.getUrl() );
        }

        @Override
        protected void warn(BaseDownloadTask task) {
        }
    };




    private void downMapFile() {

        String url = "http://49.232.128.132:10172/app/publish_phone/data/xzqh/xzqh.dbf";
        String url1 = "http://49.232.128.132:10172/app/publish_phone/L15.mpt";
        //String url = "http://27.223.18.10:10172/ftp/wanggehua.apk";
        final DownloadTask downloadTask = new DownloadTask(
                DownMapActivity.this);
        downloadTask.execute(url,url1);
    }

    class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error
                // report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File("/storage/emulated/0/haohai/qingdao/data/防火通道","防火通道.dbf");

                    if (!file.exists()) {
                        // 判断父文件夹是否存在
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                    }

                } else {
                    Toast.makeText(DownMapActivity.this, "sd卡未挂载",
                            Toast.LENGTH_LONG).show();
                }
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                return e.toString();

            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
           // mBar.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
           /* mBar.setIndeterminate(false);
            mBar.setMax(100);
            mBar.setProgress(progress[0]);*/
            Log.e(TAG, "onProgressUpdate: jindu --" + progress[0] );
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
           // mBar.dismiss();
            Log.e(TAG, "onPostExecute: 完成" );
            if (result != null) {

//                // 申请多个权限。大神的界面
//                AndPermission.with(MainActivity.this)
//                        .requestCode(REQUEST_CODE_PERMISSION_OTHER)
//                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                        .rationale(new RationaleListener() {
//                                       @Override
//                                       public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
//                                           // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
//                                           AndPermission.rationaleDialog(MainActivity.this, rationale).show();
//                                       }
//                                   }
//                        )
//                        .send();
                // 申请多个权限。
               /* AndPermission.with(MainActivity.this)
                        .requestCode(REQUEST_CODE_PERMISSION_SD)
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                        .rationale(rationaleListener
                        )
                        .send();*/


                Toast.makeText(context, "您未打开SD卡权限" + result, Toast.LENGTH_LONG).show();
                Log.e(TAG, "onPostExecute: " + result);
            } else {
                // Toast.makeText(context, "File downloaded",
                // Toast.LENGTH_SHORT)
                // .show();


            }

        }
    }

    private void downFile(String url , final String path, final String fileName) {
      //  String url = "http://49.232.128.132:10172/app/publish_phone/DefaultMobile.fly";
        //String url1 = "http://49.232.128.132:10172/app/publish_phone/L15.mpt";
        Log.e(TAG, "downFile: 文件下载" );


        FileDownloader.getImpl().create(url)
                .setPath(path,true)
                //.setForceReDownload(true)
                .setListener(new FileDownloadListener() {
                    //等待
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }


                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }
                    //下载进度回调
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e(TAG, "progress: 下载进度--");
                        if (fileName.contains("mpt")){
                            int yixia = soFarBytes / 1024 / 1024;
                            int zongshu = totalBytes / 1024 / 1024;
                            mProgress.setProgress(yixia * 100 / zongshu);
                        }
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    //完成下载
                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.e(TAG, "completed: " );
                        if (fileName.contains("mpt")){
                            mProgress.setProgress(100);
                            Log.e(TAG, "completed: " + path+"/"+fileName );
                            successDialog.show();

                        }/*else if (fileName.contains("fhtd")){
                            new File(path + "/" + fileName).renameTo(new File(path + "/" + fileName.replace("fhtd","防火通道")));
                        }else if (fileName.contains("gld")){
                            new File(path + "/" + fileName).renameTo(new File(path + "/" + fileName.replace("gld","隔离带")));
                        }else if (fileName.contains("qdqx")){
                            new File(path + "/" + fileName).renameTo(new File(path + "/" + fileName.replace("qdqx","青岛区县")));
                        }else if (fileName.contains("qdxz")){
                            new File(path + "/" + fileName).renameTo(new File(path + "/" + fileName
                            .replace("qdxz","青岛乡镇")));
                        }*/
                    }

                    //暂停
                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e(TAG, "paused: " );
                    }

                    //下载出错
                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.e(TAG, "error: " +task.toString());
                        Log.e(TAG, "error: " +e.toString());
                        if (fileName.contains("mpt")){
                           faildDialog.show();
                        }
                    }
                    //已存在相同下载
                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Log.e(TAG, "warn: " );

                    }
                }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
        downMapFile();
    }

    private int max = 100;
    private int current = 0;
    private String speed = "1";

    public void start() {
        if (current <= max) {

           /* mProgress.setMaxValue(max);
            mProgress.setCurrentValue(current);*/
            Log.e(TAG, "start: " + current* 100 / max );

            handler.postDelayed(runnable, 100);
        } else {
            handler.removeCallbacks(runnable);
        }

    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            current = current + 1;
            start();
        }
    };

    private static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}
