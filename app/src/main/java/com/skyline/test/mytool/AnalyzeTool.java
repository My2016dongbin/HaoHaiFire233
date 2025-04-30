package com.skyline.test.mytool;

import android.graphics.Point;
import android.view.MotionEvent;

import com.skyline.teapi.AltitudeTypeCode;
import com.skyline.teapi.I3DViewshed;
import com.skyline.teapi.IContourMap;
import com.skyline.teapi.IPosition;
import com.skyline.teapi.ISGWorld;
import com.skyline.teapi.ISlopeMap;
import com.skyline.teapi.IWorldPointInfo;
import com.skyline.test.utils.Constance;
import com.skyline.terraexplorer.R;
import com.skyline.test.entity.PointLatLng;
import com.skyline.terraexplorer.models.MenuEntry;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.tools.ProgressTool;
import com.skyline.terraexplorer.views.ModalDialog;
import com.skyline.terraexplorer.views.ModalDialogDelegateBase;
import com.skyline.terraexplorer.views.TEView;
import com.skyline.terraexplorer.views.ToolContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Yellow on 2017-06-05.
 */

public class AnalyzeTool extends ProgressTool implements ISGWorld.OnLButtonDownListener,TEView.OnLongPressListener {
    private  String operateType="";
    IContourMap contourMap=null;
    ISlopeMap slopeMap=null;
    I3DViewshed i3DViewshed=null;
    private List<PointLatLng> connPoints=new ArrayList<PointLatLng>();
    private List<PointLatLng>Points;
    public List<PointLatLng> getPoints() {
        return Points;
    }

    public void setPoints(List<PointLatLng> points) {
        Points = points;
        Generate();
    }

    @Override
    public MenuEntry getMenuEntry() {
        return MenuEntry.createFor(this, R.string.analyze_tool, R.drawable.viewshed, MenuEntry.MenuEntryAnalyze(),20);
    }

    @Override
    public boolean onBeforeOpenToolContainer() {
        super.onBeforeOpenToolContainer();
        showNormalButtons();
        UI.getTEView().addOnLongPressListener(AnalyzeTool.this);
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
              ISGWorld.getInstance().addOnLButtonDownListener(AnalyzeTool.this);
            }
        });
        return  true;
    }

    @Override
    public boolean onBeforeCloseToolContainer(ToolContainer.CloseReason closeReason) {
        super.onBeforeCloseToolContainer(closeReason);
        UI.getTEView().removeOnLongPressListener(AnalyzeTool.this);
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                ISGWorld.getInstance().removeOnLButtonDownListener(AnalyzeTool.this);
            }
        });
        return  true;
    }

    @Override
    protected void doWork() {

    }
    @Override
    protected void showNormalButtons() {
        toolContainer.removeButtons();
        toolContainer.setUpperViewHidden(true);
        toolContainer.addButton(1, R.drawable.analyze_coutourmap, R.string.analyze_tool_contourmap);
        toolContainer.addButton(2, R.drawable.analyze_slopemap, R.string.analyze_tool_slopemap);
        //判断手机版本号
        toolContainer.addButton(3, R.drawable.analyze_viewshed, R.string.analyze_tool_viewshed);
//        toolContainer.addButton(3,R.drawable.delete,R.string.analyze_tool_delete);
    }

    @Override
    public void onButtonClick(int tag) {
        switch (tag)
        {
            case 1:
                operateType=Constance.contourMap;
                break;
            case 2:
                operateType=Constance.slopeMap;
                break;
            case 3:
                if (checkIsViewshedSupport())
                {
                    operateType=Constance.viewshed3D;
                }
                else
                {
                    ModalDialog dlg = new ModalDialog(R.string.analyze_tool_title, new ModalDialogDelegateBase());
                    dlg.setContentMessage(R.string.analyze_tool_messagecontent);
                    dlg.show();
                }

                break;
        }
        super.onButtonClick(tag);
    }

    @Override
    protected void workCompleted() {

    }

    @Override
    public boolean OnLButtonDown(int Flags, int X, int Y) {
        IWorldPointInfo worldPointInfo = ISGWorld.getInstance().getWindow().PixelToWorld(X, Y);
        IPosition downPosition = worldPointInfo.getPosition();
        if (operateType.isEmpty())
        {
            return  false;
        }
        if (operateType== Constance.contourMap)
        {
            connPoints.add(new PointLatLng(downPosition.getY(), downPosition.getX()));
            this.setPoints(connPoints);
            if (connPoints.size()>1)
            {
                clearOperateType();
            }
        }
        if(operateType==Constance.slopeMap)
        {
            connPoints.add(new PointLatLng(downPosition.getY(), downPosition.getX()));
            this.setPoints(connPoints);
            if (connPoints.size()>1)
            {
                clearOperateType();
            }
        }
        if (operateType==Constance.viewshed3D)
        {
            connPoints.add(new PointLatLng(downPosition.getY(), downPosition.getX()));
            this.setPoints(connPoints);
            if (connPoints.size()>1)
            {
                clearOperateType();
            }
        }

        return false;
    }

    /**
     *
     */
    private  void Generate()
    {
        if (operateType==Constance.contourMap)
        {
            setShowContourMap(this.getPoints());
        }
        if (operateType==Constance.slopeMap)
        {
            setShowSlopeMap(this.getPoints());
        }
        if(operateType==Constance.viewshed3D)
        {
            show3DViewshed(this.getPoints());
        }
    }

    /**
     * 恢复初始化状态
     */
    private void clearOperateType()
    {
        contourMap=null;
        slopeMap=null;
        i3DViewshed=null;
        operateType="";
        connPoints.clear();

    }



    /**
     * 显示等高线分析
     * @param points
     */
    private  void setShowContourMap(final List<PointLatLng> points)
    {
        if (points.size() < 2) {
            return;
        }
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                if (contourMap!=null)
                {
                    ISGWorld.getInstance().getCreator().DeleteObject(contourMap.getID());
                }
                contourMap= ISGWorld.getInstance().getAnalysis().CreateContourMap(points.get(0).getLng(),points.get(0).getLat(),points.get(1).getLng(),points.get(1).getLat(),4);
                contourMap.setContourLinesInterval(3);

            }
        });
    }

    /**
     * 显示坡度图
     * @param points
     */
    private  void setShowSlopeMap( final List<PointLatLng> points)
    {
        if (points.size()<2)
        {
            return;
        }
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                if (slopeMap!=null)
                {
                    ISGWorld.getInstance().getCreator().DeleteObject(slopeMap.getID());
                }
                slopeMap= ISGWorld.getInstance().getAnalysis().CreateSlopeMap(points.get(0).getLng(),points.get(0).getLat(),points.get(1).getLng(),points.get(1).getLat(),4);
            }
        });
    }

    /***
     *显示视域分析
     * @param points
     */
    private  void show3DViewshed( final List<PointLatLng> points)
    {
        if (points.size()<2)
        {
            return;
        }
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                if (i3DViewshed!=null)
                {
                    ISGWorld.getInstance().getCreator().DeleteObject(i3DViewshed.getID());
                }
                IPosition p1 = ISGWorld.getInstance().getCreator().CreatePosition(points.get(0).getLng(), points.get(0).getLat());
                IPosition p2 = ISGWorld.getInstance().getCreator().CreatePosition(points.get(1).getLng(), points.get(1).getLat());
                double d = p1.DistanceTo(p2);
                IPosition temPosition = p1.AimTo(p2);
                IPosition newPosition = ISGWorld.getInstance().getCreator().CreatePosition(p1.getX(), p1.getY(), 0, AltitudeTypeCode.ATC_ON_TERRAIN, temPosition.getYaw(), temPosition.getPitch());
                i3DViewshed=ISGWorld.getInstance().getAnalysis().Create3DViewshed(newPosition,120,90,d);
            }
        });
    }
    @Override
    public void onLongPress(MotionEvent ev) {
        final Point coords = new Point((int)ev.getX(), (int)ev.getY());
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                IWorldPointInfo worldPointInfo= ISGWorld.getInstance().getWindow().PixelToWorld(coords.x,coords.y);
                if (worldPointInfo.getObjectID().isEmpty()||worldPointInfo.getObjectID()==null)
                {
                    return;
                }
                ISGWorld.getInstance().getCreator().DeleteObject(worldPointInfo.getObjectID());
            }
        });
    }

    /**
     *
     * @return
     */
    private  boolean  checkIsViewshedSupport()
    {
       boolean isViewshed= UI.runOnRenderThread(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Short isShadowSupported = (Short) ISGWorld.getInstance().GetParam(8380);
                if (isShadowSupported==0)
                {
                    return  false;
                }
                return true;
            }
        });
        return  isViewshed;
    }

}
