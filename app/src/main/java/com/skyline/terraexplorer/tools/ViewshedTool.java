package com.skyline.terraexplorer.tools;

import android.content.Context;
import android.text.InputType;

import com.skyline.teapi.AccuracyLevel;
import com.skyline.teapi.I3DViewshed;
import com.skyline.teapi.IPosition;
import com.skyline.teapi.ISGWorld;
import com.skyline.teapi.ISGWorld.OnLButtonClickedListener;
import com.skyline.teapi.ITerrainImageLabel;
import com.skyline.teapi.IWorldPointInfo;
import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.models.MenuEntry;
import com.skyline.terraexplorer.models.TEImageHelper;
import com.skyline.terraexplorer.models.TEUnits;
import com.skyline.terraexplorer.models.UI;
import com.skyline.terraexplorer.views.ModalDialog;
import com.skyline.terraexplorer.views.ModalDialogDelegateBase;
import com.skyline.terraexplorer.views.ToolContainer.CloseReason;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class ViewshedTool extends BaseToolWithContainer {
    private ITerrainImageLabel firstPointMarker;
    private I3DViewshed viewshed;
    private double viewerAltitudeOffset;
    private OnLButtonClickedListener listener = new OnLButtonClickedListener() {
        @Override
        public boolean OnLButtonClicked(int Flags, int X, int Y) {
            ViewshedTool.this.OnLButtonClicked(Flags, X, Y);
            return false;
        }

    };
    private ModalDialogDelegateBase modalDelegate = new ModalDialogDelegateBase() {
        public void modalDialogDidDismissWithOk(ModalDialog dlg) {
            ViewshedTool.this.modalDialogDidDismissWithOk(dlg);
        }

        ;
    };

    @Override
    public MenuEntry getMenuEntry() {
        return MenuEntry.createFor(this, R.string.mm_analyze_viewshed, R.drawable.viewshed, MenuEntry.MenuEntryAnalyze(), 30);
    }

    @Override
    public boolean onBeforeOpenToolContainer() {
        Boolean isViewshedSupported = UI.runOnRenderThread(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                // 获取状态
                Short isShadowSupported = (Short) ISGWorld.getInstance().GetParam(8380);
                if (isShadowSupported == 0)
                    return false;
                // 添加监听
                ISGWorld.getInstance().addOnLButtonClickedListener(listener);
                return true;
            }
        });

        if (isViewshedSupported == false) { //这个设备不支持视域分析
            ModalDialog dlg = new ModalDialog(R.string.viewshed_tool_shadow_not_supported_title, new ModalDialogDelegateBase());
            dlg.setContentMessage(R.string.viewshed_tool_shadow_not_supported);
            dlg.show();
            return false;
        }

        toolContainer.addButton(1, R.drawable.delete);
        toolContainer.addButton(2, R.drawable.fov_horizontal);
        toolContainer.addButton(3, R.drawable.fov_vertical);
        toolContainer.addButton(4, R.drawable.los_height);
        updateText();
        return true;
    }

    @Override
    public boolean onBeforeCloseToolContainer(CloseReason closeReason) {
        UI.runOnRenderThread(new Runnable() {
            @Override
            public void run() {
                //取消监听
                ISGWorld.getInstance().removeOnLButtonClickedListener(listener);
            }
        });
        //delete stuff on terrain //删除地形上的东西
        onButtonClick(1);
        return true;
    }

    @Override
    public void onButtonClick(int tag) {
        switch (tag) {
            case 1: {
                UI.runOnRenderThread(new Runnable() {
                    @Override
                    public void run() {
                        if (firstPointMarker != null)
                            ISGWorld.getInstance().getCreator().DeleteObject(firstPointMarker.getID());
                        if (viewshed != null)
                            ISGWorld.getInstance().getCreator().DeleteObject(viewshed.getID());
                    }
                });
                viewshed = null;
                firstPointMarker = null;
                break;
            }
            case 2: // fov horizontal 垂直视场角
            {
                showEditorForNumber("setFovHorizontal", getFovHorizontal(), R.string.viewshed_tool_fov_horizontal);
                break;
            }
            case 3:  // fov vertical 水平视场角
            {
                showEditorForNumber("setFovVertical", getFovVertical(), R.string.viewshed_tool_fov_vertical);
                break;
            }
            case 4: // viewer altitude 观察高度
            {
                showEditorForNumber("setViewerAltitude", getViewerAltitude(), R.string.viewshed_tool_viewer_altitude);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 编辑显示角度或数字
     *
     * @param setterMethodName
     * @param value
     * @param titleId
     */
    private void showEditorForNumber(String setterMethodName, float value, int titleId) {
        ModalDialog editorDialog = new ModalDialog(titleId, modalDelegate);
        //                                          数字                              小数                                  带符号的数字              保留到小数点后2位
        editorDialog.setContentTextField(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED, String.format("%.2f", value));
        editorDialog.show();
        //放到TAG里
        editorDialog.setTag(setterMethodName);
    }

    /**
     * 当点击了确认（OK）对话框消失时
     *
     * @param dlg
     */
    private void modalDialogDidDismissWithOk(ModalDialog dlg) {
        //从TAG中取出
        String setterName = (String) dlg.getTag();
        try {
            Method setter = this.getClass().getDeclaredMethod(setterName, new Class[]{float.class});
            setter.setAccessible(true);
            float newValue = 0;
            try {
                newValue = Float.parseFloat(dlg.getTextField().getText().toString());
            } catch (Exception ignore) {
                // ignore 忽视
            }
            setter.invoke(this, newValue);  // 重新设置
        } catch (Exception ignore) {
            // ignore
        }

        UI.runOnRenderThread(new Runnable() {   //渲染线程
            @Override
            public void run() {

                // if user inteface is in feet, convert altitude from feet to meters 如果用户界面为英尺，则将海拔从英尺转换为米
                float coefficient = 1;
                if (TEUnits.instance.getUnitsType() == TEUnits.UNITS_FEET)
                    coefficient = 1 / 3.28084f;

                double altitude = viewerAltitudeOffset + getViewerAltitude() * coefficient;
                if (viewshed != null) {
                    viewshed.setFieldOfViewX(getFovHorizontal());
                    viewshed.setFieldOfViewY(getFovVertical());
                    viewshed.getPosition().setAltitude(altitude);
                }
                if (firstPointMarker != null) {
                    firstPointMarker.getPosition().setAltitude(altitude);
                }
            }
        });
        updateText();
    }

    private void updateText() {
        //水平视场角 1~120，垂直视场角 1~90,观察高度 -999 999 984 306 749 000~999 999 984 306 749 000 超过或低于置为最大值或最小值
        String text = String.format(TEApp.getAppContext().getString(R.string.viewshed_tool_fov), getFovHorizontal(), getFovVertical());
        toolContainer.setText(text);
    }

    private void OnLButtonClicked(int flags, int x, int y) {
        IWorldPointInfo pointInfo = ISGWorld.getInstance().getWindow().PixelToWorld(x, y);
        pointInfo = ISGWorld.getInstance().getTerrain().GetGroundHeightInfo(pointInfo.getPosition().getX(), pointInfo.getPosition().getY(), AccuracyLevel.ACCURACY_BEST_FROM_MEMORY, true);

        if (firstPointMarker == null) {
            // if user inteface is in feet, convert altitude from feet to meters 如果用户界面为英尺，则将海拔从英尺转换为米
            float coefficient = 1;
            if (TEUnits.instance.getUnitsType() == TEUnits.UNITS_FEET)
                coefficient = 1 / 3.28084f;
            viewerAltitudeOffset = pointInfo.getPosition().getAltitude();
            pointInfo.getPosition().setAltitude(viewerAltitudeOffset + getViewerAltitude() * coefficient);
            // create first label point 创建第一个标签点
            firstPointMarker = ISGWorld.getInstance().getCreator().CreateImageLabel(pointInfo.getPosition(), TEImageHelper.prepareImageForTE(R.drawable.star_red), null, ISGWorld.getInstance().getProjectTree().getHiddenGroupID());
            firstPointMarker.getStyle().setLineToGround(true);
        } else if (viewshed == null) {
            // create viewshed 创建视域
            double distance = firstPointMarker.getPosition().DistanceTo(pointInfo.getPosition());
            IPosition startPoint = firstPointMarker.getPosition().AimTo(pointInfo.getPosition());
            viewshed = ISGWorld.getInstance().getAnalysis().Create3DViewshed(startPoint, getFovHorizontal(), getFovVertical(), distance, ISGWorld.getInstance().getProjectTree().getHiddenGroupID());
            ISGWorld.getInstance().getProjectTree().SetVisibility(firstPointMarker.getID(), false);
        }
    }

    /**
     * 水平视场角
     * @return
     */
    private float getFovHorizontal() {
        return readFromPrefs("viewshed_tool_fov_horizontal", 1, 120, 120);
    }

    @SuppressWarnings("unused")
    private void setFovHorizontal(float value) {
        saveInPrefs("viewshed_tool_fov_horizontal", value);
    }

    /**
     * 垂直视场角
     * @return
     */
    private float getFovVertical() {
        return readFromPrefs("viewshed_tool_fov_vertical", 1, 90, 90);
    }

    @SuppressWarnings("unused") //忽略未使用的警告信息
    private void setFovVertical(float value) {
        saveInPrefs("viewshed_tool_fov_vertical", value);
    }

    /**
     * 观察高度
     * @return
     */
    private float getViewerAltitude() {
        return readFromPrefs("viewshed_tool_viewer_altitude", 0, 0, 2);
    }

    @SuppressWarnings("unused")
    private void setViewerAltitude(float value) {
        saveInPrefs("viewshed_tool_viewer_altitude", value);
    }

    private void saveInPrefs(String key, float value) {
        TEApp.getAppContext()
                .getSharedPreferences(SettingsTool.PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .putFloat(key, value)
                .apply();
    }

    private float readFromPrefs(String key, float minValue, float maxValue, float defaultValue) {
        float value = TEApp.getAppContext()
                .getSharedPreferences(SettingsTool.PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getFloat(key, defaultValue);
        if (minValue == 0 && maxValue == 0)
            return value;
        return Math.max(minValue, Math.min(value, maxValue));
    }
}
