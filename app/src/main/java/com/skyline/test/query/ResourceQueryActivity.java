package com.skyline.test.query;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.MatchParentActivity;
import com.skyline.terraexplorer.views.ModalDialog;
import com.skyline.terraexplorer.views.ModalDialogDelegateBase;

import java.util.ArrayList;

public class ResourceQueryActivity extends MatchParentActivity implements View.OnClickListener {
    private RelativeLayout back;
    private SlideListView listSlideView;
    private ArrayList<ResourceInfo> resourceInfos;
    private ResourceBaseAdapter baseAdapter;

    //


    private class ModalDialogShowDelete extends ModalDialogDelegateBase {
        @Override
        public void modalDialogDidDismissWithOk(ModalDialog dlg) {
            super.modalDialogDidDismissWithOk(dlg);
        }

    }

    private ModalDialogShowDelete modalDialogShowDelete = new ModalDialogShowDelete();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        initView();
//        initData();

    }
    private void initView() {
        back = (RelativeLayout) findViewById(R.id.RelativeLayout_query_back);
        listSlideView = (SlideListView) findViewById(R.id.list_query_list);
        back.setOnClickListener(this);
    }

  /*  private void initData() {
        resourceInfos = ResourceData.infos;
        baseAdapter = new ResourceBaseAdapter(this, resourceInfos);
        listSlideView.setAdapter(baseAdapter);
        baseAdapter.setOnOperateListerner(new ResourceBaseAdapter.OnOperateListerner() {
            @Override
            public void GetLocation(final int position) {
                UI.runOnRenderThread(new Runnable() {
                    @Override
                    public void run() {
                        IPosition fPosition = resourceInfos.get(position).getPosition();
                        ISGWorld.getInstance().getNavigate().FlyTo(fPosition);
                        finish();
                    }
                });
            }

            @Override
            public void showContent(int position) {
                //显示详情
                ResourceInfo resourceInfo = resourceInfos.get(position);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("名称：" + resourceInfo.getName().toString());
                stringBuffer.append("\n");
                stringBuffer.append("详情：" + resourceInfo.getDesc() + "\n");
                ModalDialog modalDialog = new ModalDialog(R.string.query_place_content, modalDialogShowDelete);
                modalDialog.setContentMessage(stringBuffer.toString());
                modalDialog.show();
            }
        });
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.RelativeLayout_query_back:
                finish();
                break;
        }
    }
}
