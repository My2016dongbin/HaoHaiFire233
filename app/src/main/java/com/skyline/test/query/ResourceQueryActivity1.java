package com.skyline.test.query;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.skyline.terraexplorer.R;
import com.skyline.teapi.IPosition;
import com.skyline.teapi.ISGWorld;
import com.skyline.terraexplorer.controllers.MatchParentActivity;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.views.ModalDialog;
import com.skyline.terraexplorer.views.ModalDialogDelegateBase;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;

public class ResourceQueryActivity1 extends MatchParentActivity implements View.OnClickListener {
    private ArrayList<ResourceInfo> resourceInfos;
    private ResourceBaseAdapter baseAdapter;
    private SlideAndDragListView slideAndDragListView;
    private RelativeLayout back;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.RelativeLayout_query_back:
                finish();
                break;
        }
    }

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
        setContentView(R.layout.activity_resource1);
        initView1();
    }
    private  void initView1()
    {
        back = (RelativeLayout) findViewById(R.id.RelativeLayout_query_back);
        back.setOnClickListener(this);
        slideAndDragListView=(SlideAndDragListView)findViewById(R.id.slideAndDragListView);
        Menu menu=new Menu(true,0);
        menu.addItem(new MenuItem.Builder().setWidth(90).setBackground(new ColorDrawable(Color.RED)).setText("定位").setTextColor(Color.WHITE).setDirection(MenuItem.DIRECTION_RIGHT).build());
        menu.addItem(new MenuItem.Builder().setWidth(90).setBackground(new ColorDrawable(Color.BLUE)).setText("详情").setTextColor(Color.WHITE).setDirection(MenuItem.DIRECTION_RIGHT).build());

        slideAndDragListView.setMenu(menu);
        resourceInfos = ResourceData.infos;
        baseAdapter = new ResourceBaseAdapter(this, resourceInfos);
        slideAndDragListView.setAdapter(baseAdapter);
        slideAndDragListView.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View view, final int itemPosition, int buttonPosition, int direction) {
                switch (direction)
                {
                    case MenuItem.DIRECTION_RIGHT:
                        switch (buttonPosition)
                        {
                            case 0:

                                UI.runOnRenderThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        IPosition pt=resourceInfos.get(itemPosition).getPosition();
                                        ISGWorld.getInstance().getNavigate().FlyTo(pt);
                                        finish();
                                    }
                                });
//                                ToastUtils.shortShow(ResourceQueryActivity1.this,"111");
                                return Menu.ITEM_SCROLL_BACK;



                            case 1:
                                //显示详情
                                ResourceInfo resourceInfo = resourceInfos.get(itemPosition);
                                StringBuffer stringBuffer = new StringBuffer();
                                stringBuffer.append("名称：" + resourceInfo.getName().toString());
                                stringBuffer.append("\n");
                                stringBuffer.append("详情：" + resourceInfo.getDesc() + "\n");
                                ModalDialog modalDialog = new ModalDialog(R.string.query_place_content, modalDialogShowDelete);
                                modalDialog.setContentMessage(stringBuffer.toString());
                                modalDialog.show();
                                return Menu.ITEM_SCROLL_BACK;
                        }
                        break;
                }
                return 0;
            }
        });
    }
}
