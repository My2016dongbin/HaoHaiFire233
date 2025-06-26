package com.skyline.terraexplorer.wisdomgarden.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.wisdomgarden.ui.activity.base.HhBaseActivity;
import com.ruyiruyi.rylibrary.android.searchview.ICallBack;
import com.ruyiruyi.rylibrary.android.searchview.ItemCallBack;
import com.ruyiruyi.rylibrary.android.searchview.SearchView;
import com.ruyiruyi.rylibrary.android.searchview.bCallBack;
import com.ruyiruyi.rylibrary.cell.ActionBar;

public class SearchActivity extends HhBaseActivity {
    private static final String TAG = SearchActivity.class.getSimpleName();
    private SearchView searchView;
    public static String TYPE = "TYPE";
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitle("公园查询");
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

        searchView = (SearchView) findViewById(R.id.search_view);

        // 4. 设置点击搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                Log.e(TAG, "SearchAciton: " + string);
                    Log.e(TAG, "SearchAciton: ----1");
                    Intent intent = new Intent();
                    intent.putExtra("SEARCH_STR",string);
                    setResult(ParkListActivity.SEARCH_CODE,intent);
                    finish();


            }
        });

        // 5. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });

        searchView.setOnItemClickBack(new ItemCallBack() {
            @Override
            public void ItemClick(String string) {
                Log.e(TAG, "SearchAciton: " + string);


                    Intent intent = new Intent();
                    intent.putExtra("SEARCH_STR",string);
                    setResult(ParkListActivity.SEARCH_CODE,intent);
                    finish();

            }
        });
    }
}
