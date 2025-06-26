package com.skyline.test.places1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.skyline.terraexplorer.R;
import com.skyline.teapi.IPosition;
import com.skyline.teapi.ISGWorld;
import com.skyline.terraexplorer.controllers.MatchParentActivity;
import com.skyline.terraexplorer.models.FavoriteItem;
import com.skyline.terraexplorer.models.FavoritesStorage;
import com.skyline.terraexplorer.models.ToolManager;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.tools.EditFavoriteTool;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Created by miao on 2017/6/2 8:51.
 */

public class PlacesActivity1 extends MatchParentActivity {

    private ListView lv;
    private Places1Info infoDelete = new Places1Info();
    private Places1Adapter places1Adapter;
    private ArrayList<Places1Info> infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places1);
        // add header 添加标题
        UI.addHeader(R.string.title_activity_places, R.drawable.places, this, EnumSet.of(UI.HeaderOptions.SearchButton));
        initView();
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.places1_lv);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
       /* UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                if(infos != null) {
                    infos.clear();
                }
                infos = new ArrayList<Places1Info>();
                for (FavoriteItem fav : FavoritesStorage.defaultStorage.getAll()) {
                    IPosition position = fav.position;
                    String name = fav.name;
                    String id = fav.id;
                    infos.add(new Places1Info(position, id, name));
                }
                runOnUiThread(new Runnable() {


                    @Override
                    public void run() {
                        places1Adapter = new Places1Adapter(PlacesActivity1.this, infos);
                        lv.setAdapter(places1Adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final Places1Info info = (Places1Info) places1Adapter.getItem(position);
                                UI.runOnRenderThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ISGWorld.getInstance().getNavigate().FlyTo(info.getPosition());
                                        finish();
                                    }
                                });
                            }
                        });
                        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                Places1Info info = (Places1Info) places1Adapter.getItem(position);
                                dialogShow(info);
                                return true;
                            }
                        });
                    }
                });
            }
        });*/
        new DownloadTask().execute();

    }

    class DownloadTask extends AsyncTask<Void,Integer,Object> {

        @Override
        protected Object doInBackground(Void... params) {
            if(infos != null) {
                infos.clear();
            }
            infos = new ArrayList<Places1Info>();
            for (FavoriteItem fav : FavoritesStorage.defaultStorage.getAll()) {
                IPosition position = fav.position;
                String name = fav.name;
                String id = fav.id;
                infos.add(new Places1Info(position, id, name));
            }
            return infos;
        }

        @Override
        protected void onPostExecute(Object o) {
            ArrayList<Places1Info> infos = (ArrayList<Places1Info>)o;
            places1Adapter = new Places1Adapter(PlacesActivity1.this, infos);
            lv.setAdapter(places1Adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Places1Info info = (Places1Info) places1Adapter.getItem(position);
                    UI.runOnRenderThread(new Runnable() {
                        @Override
                        public void run() {
                            ISGWorld.getInstance().getNavigate().FlyTo(info.getPosition());
                            finish();
                        }
                    });
                }
            });
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Places1Info info = (Places1Info) places1Adapter.getItem(position);
                    dialogShow(info);
                    return true;
                }
            });
        }
    }

    private Dialog mCameraDialog = null;

    /**
     * 展示dialog
     */
    private void dialogShow(Places1Info info) {

        if (mCameraDialog == null) {
            mCameraDialog = new Dialog(PlacesActivity1.this, R.style.my_dialog);
            LinearLayout root = (LinearLayout) LayoutInflater.from(PlacesActivity1.this).inflate(
                    R.layout.place1_dialog, null);
            root.findViewById(R.id.place_bt_edit).setOnClickListener(new MyDialogClick(info));
            root.findViewById(R.id.place_bt_exit).setOnClickListener(new MyDialogClick(info));
            root.findViewById(R.id.place_bt_delete).setOnClickListener(new MyDialogClick(info));
            mCameraDialog.setContentView(root);
            Window dialogWindow = mCameraDialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.x = 0; // 新位置X坐标
            lp.y = -20; // 新位置Y坐标
            lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
            root.measure(0, 0);
            lp.height = root.getMeasuredHeight();
            lp.alpha = 9f; // 透明度
            dialogWindow.setAttributes(lp);
            mCameraDialog.setCanceledOnTouchOutside(true);
        }
        mCameraDialog.show();
    }

    /**
     * 自定义onClick类用于擦传入相机参数
     */
    private class MyDialogClick implements View.OnClickListener {

        private Places1Info info;

        public MyDialogClick(Places1Info info) {
            this.info = info;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.place_bt_edit: // 编辑
                    ToolManager.INSTANCE.openTool(EditFavoriteTool.class.getName(), info.getId());
                    dialogDissmiss(mCameraDialog);
                    mCameraDialog=null;

                    break;
                case R.id.place_bt_delete:   //删除
                    showMyDialog(info);
                    mCameraDialog=null;

                    break;
                case R.id.place_bt_exit:   //退出
                    dialogDissmiss(mCameraDialog);
                    mCameraDialog=null;
                    break;
            }
        }
    }

    /**
     * 是否删除图片的对话框
     */
    private void showMyDialog(Places1Info info1) {
        infoDelete = info1;
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(PlacesActivity1.this);
        normalDialog.setMessage("是否删除文件？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FavoritesStorage.defaultStorage.deleteItem(infoDelete.getId());
                        initView();
                        initData();
                        dialogDissmiss(mCameraDialog);
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogDissmiss(mCameraDialog);
                    }
                });
        // 显示
        normalDialog.show();
    }

    /**
     * 让dialog消失
     *
     * @param
     */
    private void dialogDissmiss(Dialog dialog) {
//        if (mCameraDialog != null) {
//            mCameraDialog.dismiss();
//            mCameraDialog=null;
//        }
        if (dialog!=null)
        {
            dialog.dismiss();
        }
    }


}
