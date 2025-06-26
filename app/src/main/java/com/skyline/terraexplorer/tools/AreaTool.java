package com.skyline.terraexplorer.tools;

import android.os.Handler;
import android.os.Looper;

import com.skyline.terraexplorer.R;
import com.skyline.teapi.AltitudeTypeCode;
import com.skyline.teapi.IPolygon;
import com.skyline.teapi.ISGWorld;
import com.skyline.teapi.ITerrainPolygon;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.models.MenuEntry;
import com.skyline.terraexplorer.models.TEUnits;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.views.ToolContainer;

public class AreaTool extends ProgressTool implements ISGWorld.OnLButtonUpListener, ISGWorld.OnAnalysisProgressListener {

    private double groundArea;
    private String toolContainerText;
    private static final String TAG = "AreaTool";

    @Override
    public MenuEntry getMenuEntry() {
        return MenuEntry.createFor(this, R.string.mm_analyze_area, R.drawable.area, MenuEntry.MenuEntryAnalyze(), 20);
    }

    /**
     * 开始绘制多边形
     */
    private void startDrawPolygon() {
        // start draw polygon 开始绘制多边形
        ISGWorld.getInstance().getCommand().Execute(1012, 5);

        // get object 获取对象
        String objectId = (String) ISGWorld.getInstance().GetParam(7200);
        ITerrainPolygon areaPolygon = ISGWorld.getInstance().getCreator().GetObject(objectId).CastTo(ITerrainPolygon.class);
        if (areaPolygon != null) {
            areaPolygon.getPosition().setAltitudeType(AltitudeTypeCode.ATC_TERRAIN_RELATIVE);
            areaPolygon.SetParam(5440, null);    // Give the polygon X-Ray look 给出了多边形x射线
            areaPolygon.SetParam(5441, null);    // // Make sure we do not see the red "edit vertex helper polyline" 确保我们看不到红色的“编辑顶点帮助器折线”
            areaPolygon.getLineStyle().setWidth(-2.0);  // Make the polygon a bit wider 使多边形更宽一点
        }
    }

    @Override
    public boolean onBeforeOpenToolContainer() {
        super.onBeforeOpenToolContainer();
        //初始化按钮
        showNormalButtons();
        //更新数据
        updateArea(0, 0);
        groundArea = 0;
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                // start draw polygon 开始绘制多边形
                startDrawPolygon();
                // subscribe to lButtonUp as an event that causes the polygon to change 订阅lButtonUp作为导致多边形改变的事件
                ISGWorld.getInstance().addOnLButtonUpListener(AreaTool.this);
            }
        });
        return true;
    }

    @Override
    public boolean onBeforeCloseToolContainer(ToolContainer.CloseReason closeReason) {
        super.onBeforeCloseToolContainer(closeReason);
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                String objectId = (String) ISGWorld.getInstance().GetParam(7200);
                // simulate right click to end drawing 模拟右键单击结束绘图
                ISGWorld.getInstance().SetParam(8044, 0);
                ISGWorld.getInstance().getCreator().DeleteObject(objectId); //删除线
                ISGWorld.getInstance().removeOnLButtonUpListener(AreaTool.this);    //取消绘制事件
            }
        });
        return true;
    }

    /**
     * 更新容器中的文字
     * @param area
     * @param perimeter
     */
    private void updateArea(final double area, final double perimeter) {
        UI.runOnUiThreadAsync(new Runnable() {
            @Override
            public void run() {
                if (area == 0 || perimeter == 0) {
                    toolContainerText = "";
                } else {
                    String aerialText = String.format(TEApp.getAppContext().getString(R.string.measure_area_area), TEUnits.instance.formatArea(area));
                    String verticalText = String.format(TEApp.getAppContext().getString(R.string.measure_area_perimeter), TEUnits.instance.formatDistance(perimeter));
                    toolContainerText = String.format("%s\r\n%s", aerialText, verticalText);
                }
                toolContainer.setText(toolContainerText);
            }
        });
    }

    @Override
    public void onButtonClick(int tag) {
        super.onButtonClick(tag);
        switch (tag) {
            case 1: // delete all points 删除所有点
            {
                // bug fix 18295 错误修复18295
                groundArea = 0;

                updateArea(0, 0);
                UI.runOnRenderThread(new Runnable() {
                    @Override
                    public void run() {
                        String objectId = (String) ISGWorld.getInstance().GetParam(7200);
                        // simulate right click to end drawing 模拟右键单击结束绘图
                        ISGWorld.getInstance().SetParam(8044, 0);
                        // delete object    删除对象
                        ISGWorld.getInstance().getCreator().DeleteObject(objectId);
                        // and start adding again 并重新开始绘制
                        startDrawPolygon();
                    }
                });
                // get object   获取对象
                break;
            }
            case 3: // calculate ground area 计算地面面积
                doWorkAsync();
                break;
            default:
                break;
        }

    }

    @Override
    protected void doWork() {
        if (groundArea <= 0) {
            //添加进度监听，求出地面面积
            ISGWorld.getInstance().addOnAnalysisProgressListener(this);
            groundArea = calculateGroundArea();
            ISGWorld.getInstance().removeOnAnalysisProgressListener(this);
        }
    }

    @Override
    public boolean OnAnalysisProgress(int CurrPos, int Range) {
        setProgress(CurrPos, Range);
        return workCanceled;
    }

    @Override
    protected void workCompleted() {
        if (workCanceled == false) {
            String groundText = String.format(TEApp.getAppContext().getString(R.string.measure_area_ground), TEUnits.instance.formatArea(groundArea));
            String text = String.format("%s\r\n%s", groundText, toolContainerText);
            toolContainer.setText(text);
        }
    }

    /**
     * 求地面面积
     * @return
     */
    private double calculateGroundArea() {
        String objectId = (String) ISGWorld.getInstance().GetParam(7200);
        ITerrainPolygon areaPolygon = ISGWorld.getInstance().getCreator().GetObject(objectId).CastTo(ITerrainPolygon.class);
        if (areaPolygon != null) {  //如果划线了就return面积，如果没划线就return 0
            double area = ISGWorld.getInstance().getAnalysis().MeasureTerrainSurface(areaPolygon.getGeometry(), 0);
            return area;
        }
        return 0;
    }

    @Override
    protected void showNormalButtons() {
        toolContainer.removeButtons();
        toolContainer.addButton(1, R.drawable.delete);
        //toolContainer.addButton(2, R.drawable.delete_last_point);	//删除上一段效果，为什么删除了？？
        toolContainer.addButton(3, R.drawable.calc_area);
    }

    @Override
    public boolean OnLButtonUp(int Flags, int X, int Y) {   //不光是计算求面积，每次抬起也是在求面积
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {//延时执行
            @Override
            public void run() {
                UI.runOnRenderThread(new Runnable() {   //渲染线程
                    @Override
                    public void run() {
                        String objectId = (String) ISGWorld.getInstance().GetParam(7200);
                        ITerrainPolygon areaPolygon = ISGWorld.getInstance().getCreator().GetObject(objectId).CastTo(ITerrainPolygon.class);
                        IPolygon poly = areaPolygon.getGeometry().CastTo(IPolygon.class);
                        if (poly != null) {
                            double area = (Double) areaPolygon.GetParam(5430);
                            double perimeter = poly.getExteriorRing().getLength();
                            updateArea(area, perimeter);
                            groundArea = 0;
                        }
                    }
                });
            }
        }, 10);
        return false;
    }

}
