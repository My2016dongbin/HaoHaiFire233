package com.skyline.test.query;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.skyline.teapi.IPosition;
import com.skyline.teapi.ISGWorld;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.MatchParentActivity;
import com.skyline.terraexplorer.models.UI;

import java.util.ArrayList;

/**
 * Created by miao on 2017/5/31 16:47.
 */

public class ResourceListActivity extends MatchParentActivity implements View.OnClickListener {

    private RelativeLayout back;
    private ListView list;
    private ArrayList<ResourceInfo> infos;
    private ResourceAdapter resourceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_list);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.query_back);
        list = (ListView) findViewById(R.id.query_list);
    }

    private void initData() {
        infos = ResourceData.infos;
        resourceAdapter = new ResourceAdapter(this, infos);
        list.setAdapter(resourceAdapter);
    }

    private void initListener() {
        back.setOnClickListener(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                UI.runOnRenderThread(new Runnable() {
                    @Override
                    public void run() {
                        ResourceInfo info = (ResourceInfo) resourceAdapter.getItem(position);
                        IPosition position1 = info.getPosition();
//                        IPosition pos = ISGWorld.getInstance().getCreator().CreatePosition(position1.getX(), position1.getY());
                        ISGWorld.getInstance().getNavigate().FlyTo(position1);
                        finish();
                    }
                });

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.query_back:
                finish();
                break;
        }
    }
}
