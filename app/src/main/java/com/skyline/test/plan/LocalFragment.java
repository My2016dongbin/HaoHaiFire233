package com.skyline.test.plan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.skyline.terraexplorer.R;
import com.skyline.test.net.HttpInfo;

import java.io.File;
import java.util.List;

/**
 * Created by miao on 2017/5/13 15:55.
 */

public class LocalFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefresh;
    private ProgressBar pb;
    private LinearLayout llNoData;
    private ListView lv;
    private static final String TAG = "LocalFragment";
    private LocalAdapter adapter;
    private OnLocalDeleteListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local,null);
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.local_srfl);
        pb = (ProgressBar) view.findViewById(R.id.local_pb);
        llNoData = (LinearLayout) view.findViewById(R.id.local_ll_nodata);
        lv = (ListView) view.findViewById(R.id.local_lv);
        initView();
        initSFRL();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }



    private void initSFRL() {
        mRefresh.setOnRefreshListener(this);
        mRefresh.setDistanceToTriggerSync(150);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mRefresh.setSize(SwipeRefreshLayout.DEFAULT);
    }

    public void initView() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(path);
        adapter = new LocalAdapter(R.layout.fragment_local_item,getContext());
        setViewVisible(pb);
        //请求权限
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
        }
        getWordFile(adapter.getList(),file);

        Log.d(TAG, "initView: list.size = " + adapter.getList().size());

        if(adapter.getList().size() == 0) {
            setViewVisible(llNoData);
        } else {
            setViewVisible(lv);
        }
    }


    public void initData() {
        //存进一个公共类，用于判断在在线中是否已经下载
        HttpInfo.localWord = adapter.getList();
        if(adapter.getList() != null) {
            lv.setAdapter(adapter);
        }
    }


    private void initListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WordInfo wordInfo = (WordInfo) adapter.getItem(position);
                //这个是本地直接查看 调用本地poi查看 二选一
                /*Intent intent = new Intent(getContext(),WordActivity.class);
                intent.putExtra("path",wordInfo.getPath());
                startActivity(intent);*/
                //这个是调用手机中的第三方程序
                Intent intent = getWordFileIntent(wordInfo.getPath());
                try {
                    getContext().startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(getContext(),"找不到可以打开该文件的程序",Toast.LENGTH_SHORT).show();
                }
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                WordInfo wordInfo = (WordInfo) adapter.getItem(position);
                showMyDialog(wordInfo.getPath());
                return true;
            }
        });
    }

    /**
     * 获得word文件
     * @param file
     */
    private void getWordFile(final List<WordInfo> list,File file) {
        try {
            if (file == null || file.exists() == false) return;

            File[] files = file.listFiles();
            if (files == null) return;

            for (File f : files) {
                if (f.isDirectory()) {
                    getWordFile(list,f);
                } else {
                    String name = f.getName();
                    int i = name.lastIndexOf('.');
                    if (i != -1) {
                        name = name.substring(i + 1);
                        if (name.equalsIgnoreCase("docx") || name.equalsIgnoreCase("doc")) {
                            String fileName = getFileNameNoEx(f.getName());
                            list.add(new WordInfo(fileName, f.getAbsolutePath()));
                            Log.d(TAG, "getWordFile: name = " + f.getName() + ",path = " + f.getAbsolutePath());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public void onRefresh() {
        initView();
        initData();
        mRefresh.setRefreshing(false);
    }

    private void setViewVisible(View view){
        lv.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.INVISIBLE);
        llNoData.setVisibility(View.INVISIBLE);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            default:
        }
    }

    /**
     * 获取去除后缀名的文件名
     * @param filename
     * @return
     */
    private String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    //android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param )
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /**
     * 是否删除图片的对话框
     */
    private void showMyDialog(final String path) {
		/* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getContext());
        normalDialog.setMessage("是否删除文件？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除图片
                        deleteWord(path);
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }


    /**
     * 删除指定文件
     * @param path
     */
    private void deleteWord(String path) {
        File file = new File(path);
        if(file.exists()){
            file.delete();
            Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
            initView();
            initData();
            listener.delete();
        }else{
            Toast.makeText(getContext(),"文件不存在",Toast.LENGTH_SHORT).show();
        }
    }

    public void setOnLocalDeleteListener(OnLocalDeleteListener listener) {
        this.listener = listener;
    }

    public interface OnLocalDeleteListener{
        void delete();
    }
}
