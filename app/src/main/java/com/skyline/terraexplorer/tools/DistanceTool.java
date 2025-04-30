package com.skyline.terraexplorer.tools;

import android.text.Html;
import android.view.Gravity;
import android.widget.Toast;

import com.skyline.teapi.*;
import com.skyline.teapi.ISGWorld.OnAnalysisDistancePointAddedListener;
import com.skyline.teapi.ISGWorld.OnAnalysisProgressListener;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.models.MenuEntry;
import com.skyline.terraexplorer.models.TEUnits;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.views.ToolContainer.CloseReason;

public class DistanceTool extends ProgressTool implements OnAnalysisDistancePointAddedListener, OnAnalysisProgressListener {

    private String toolContainerText;
    private double groundDistance;
    protected IGeometry currentGeometry;
    protected int commandId;

    @Override
    public MenuEntry getMenuEntry() {
        return MenuEntry.createFor(this, R.string.mm_analyze_distance, R.drawable.distance, MenuEntry.MenuEntryAnalyze(), 10);
    }

    public DistanceTool() {
        commandId = 1035;
    }

    @Override
    public boolean onBeforeOpenToolContainer() {
        super.onBeforeOpenToolContainer();
        //显示删除所有，删除一条线，计算三个按钮
        showNormalButtons();
        //更新距离
        updateDistance(0, 0, 0, 0);
        //渲染线程，即子线程
        UI.runOnRenderThreadAsync(new Runnable() {
            @Override
            public void run() {
                //开始测量，第一次发是开始测量，再次发就是停止测量；
                if (ISGWorld.getInstance().getCommand().IsChecked(commandId, 0) == false)
                    ISGWorld.getInstance().getCommand().Execute(commandId, 0);
                //添加距离监听
                ISGWorld.getInstance().addOnAnalysisDistancePointAddedListener(DistanceTool.this);
            }
        });
        return true;
    }

    @Override
    public boolean onBeforeCloseToolContainer(CloseReason closeReason) {
        super.onBeforeCloseToolContainer(closeReason);
        groundDistance = 0;
        currentGeometry = null;
        UI.runOnRenderThreadAsync(new Runnable() {
            @Override
            public void run() {
                //取消监听
                ISGWorld.getInstance().removeOnAnalysisDistancePointAddedListener(DistanceTool.this);
                //停止测量
                if (ISGWorld.getInstance().getCommand().IsChecked(commandId, 0))
                    ISGWorld.getInstance().getCommand().Execute(commandId, 0);
            }
        });
        return true;
    }

    @Override
    protected void showNormalButtons() {
        toolContainer.removeButtons();
        toolContainer.addButton(1, R.drawable.delete);
        toolContainer.addButton(2, R.drawable.delete_last_point);
        toolContainer.addButton(3, R.drawable.calc_distance);
    }


    private void updateDistance(double aerial, double horizontal, double slope, double elevDiff) {
        if (aerial == 0 && horizontal == 0 && slope == 0 && elevDiff == 0) {
            toolContainerText = "";
        } else {
            String aerialText = String.format(TEApp.getAppContext().getString(R.string.measure_distance_aerial), TEUnits.instance.formatDistance(aerial));
            String horizontalText = String.format(TEApp.getAppContext().getString(R.string.measure_distance_horizontal), TEUnits.instance.formatDistance(horizontal));
            String slopeText = String.format(TEApp.getAppContext().getString(R.string.measure_distance_slope), slope);
            String elevationText = String.format(TEApp.getAppContext().getString(R.string.measure_distance_elevation), TEUnits.instance.formatDistance(elevDiff));
            toolContainerText = String.format("%s\r\n%s\r\n%s\r\n%s", aerialText, horizontalText, elevationText, slopeText);
        }
        // tool container can be null, since this method is called asynchronously 工具容器可以为null，因为这种方法被异步调用
        if (toolContainer != null)
            toolContainer.setText(toolContainerText);
    }

    @Override
    public void onButtonClick(int tag) {
        super.onButtonClick(tag);
        switch (tag) {
            case 1: // reset is stop and start
                // reset state
                OnAnalysisDistancePointAdded(null, 0, 0, 0, 0);
                UI.runOnRenderThreadAsync(new Runnable() {
                    @Override
                    public void run() {
                        // stop measurement 停止测量
                        if (ISGWorld.getInstance().getCommand().IsChecked(commandId, 0))
                            ISGWorld.getInstance().getCommand().Execute(commandId, 0);
                        // start measurement 开始测量
                        ISGWorld.getInstance().getCommand().Execute(commandId, 0);
                    }
                });
                break;
            case 2: // delete last point 删除上一条划线
                UI.runOnRenderThreadAsync(new Runnable() {
                    @Override
                    public void run() {
                        ISGWorld.getInstance().SetParam(8335, 0);
                    }
                });
                break;
            case 3: // calculate area distance 计算面积距离
                if (currentGeometry != null)
                    doWorkAsync();  //这个方法里会按顺序执行doWork(),showNormalButtons(),workCompleted()方法
                break;
        }
    }

    @Override
    protected void doWork() {
        if (groundDistance <= 0) {
            ISGWorld.getInstance().addOnAnalysisProgressListener(this);
            // currentGeometry may be point 当前几何可能是点
            if (currentGeometry.CastTo(ILineString.class) != null) {
                // ignore api exception that is thrown on cancel 忽略被取消的api异常
                try {
                    //计算地面距离
                    groundDistance = ISGWorld.getInstance().getAnalysis().MeasureTerrainGroundDistance(currentGeometry, 0, true);
                } catch (ApiException e) {
                    if (this.workCanceled == false)
                        throw e;
                }
            }
            //取消进度监听
            ISGWorld.getInstance().removeOnAnalysisProgressListener(this);
        }

    }

    @Override
    protected void workCompleted() {
        if (workCanceled == false) {
            String groundText = String.format(TEApp.getAppContext().getString(R.string.measure_distance_ground), TEUnits.instance.formatDistance(groundDistance));
            String htmlText = String.format("<b>%s</b><br/>%s", groundText, toolContainerText.replace("\r\n", "<br/>"));
            toolContainer.setText(Html.fromHtml(htmlText));
            Toast toast = Toast.makeText(TEApp.getAppContext(), groundText, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @Override
    public boolean OnAnalysisProgress(int CurrPos, int Range) {
        setProgress(CurrPos, Range);
        return workCanceled;
    }

    @Override
    public void OnAnalysisDistancePointAdded(final IGeometry geom,
                                             final double aerial, final double horizontal, final double slope,
                                             final double elevDiff) {
        UI.runOnUiThreadAsync(new Runnable() {
            @Override
            public void run() {
                groundDistance = 0;
                currentGeometry = geom;
                updateDistance(aerial, horizontal, slope, elevDiff);
            }
        });
    }
}
