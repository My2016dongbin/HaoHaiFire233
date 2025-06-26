package com.skyline.terraexplorer.models;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;


import com.blankj.utilcode.util.SPUtils;
import com.skyline.terraexplorer.TEAppException;
import com.skyline.terraexplorer.tools.AboutTool;
import com.skyline.terraexplorer.tools.AddFeatureTool;
import com.skyline.terraexplorer.tools.AreaTool;
import com.skyline.terraexplorer.tools.CaptureShareTool;
import com.skyline.terraexplorer.tools.DistanceTool;
import com.skyline.terraexplorer.tools.EditFavoriteTool;
import com.skyline.terraexplorer.tools.EditFeatureLayerTool;
import com.skyline.terraexplorer.tools.EditFeatureTool;
import com.skyline.terraexplorer.tools.GpsTool;
import com.skyline.terraexplorer.tools.LayersTool;
import com.skyline.terraexplorer.tools.PlacesTool;
import com.skyline.terraexplorer.tools.PresentationStepsTool;
import com.skyline.terraexplorer.tools.PresentationTool;
import com.skyline.terraexplorer.tools.QueryTool;
import com.skyline.terraexplorer.tools.ResourcesTool;
import com.skyline.terraexplorer.tools.SettingsTool;
import com.skyline.terraexplorer.views.ToolContainer;
import com.skyline.test.mytool.AnalyzeTool;
import com.skyline.test.mytool.PlottingTool;
import com.skyline.test.plan.PlanManagementTool;
import com.skyline.test.query.ResourceQueryTool;
import com.skyline.test.utils.Constance;

import java.util.ArrayList;
import java.util.HashMap;

public class ToolManager {
    public final static ToolManager INSTANCE = new ToolManager();
    private HashMap<String, ToolProtocol> tools = new HashMap<String, ToolProtocol>();
    private boolean toolsRegistered = false;
    private static final String TAG = "ToolManager";

    private ToolManager() {
    }

    public void registerTools() {
        if (toolsRegistered)
            return;
//	    registerTool(new SearchTool());		//取消注册搜索
//                registerTool(new ProjectsTool()); //取消项目
         registerTool(new GpsTool());  //取消注册我的位置
        //registerTool(new LosTool());
//	    registerTool(new ShadowTool());		//阴影分析 5.0以下不支持
        //registerTool(new ProfileTool());//注释掉剖面分析
//	    registerTool(new ViewshedTool());	//视域 5.0以下不支持
//        registerTool(new WhiteboardTool());
//        registerTool(new WhiteboardAddFeatureTool());
//        registerTool(new WhiteboardEditFeatureTool());
        int i=   SPUtils.getInstance(SettingsTool.PREFERENCES_NAME).getInt(Constance.USERREGISTER);
//        if (i==1)
//        {
        registerTool(new ResourcesTool());
            registerTool(new PlacesTool());
            registerTool(new LayersTool());
            registerTool(new PlanManagementTool());
            registerTool(new AnalyzeTool());
            registerTool(new ResourceQueryTool());
            registerTool(new PlottingTool());
   //        registerTool(new RsFireTool());
            registerTool(new CaptureShareTool());
            registerTool(new AboutTool());
            //设置第一次安装后教程
//        registerTool(new TutorialTool());
            registerTool(new AreaTool());
            registerTool(new DistanceTool());
            registerTool(new EditFavoriteTool());
            registerTool(new PresentationTool());
            registerTool(new PresentationStepsTool());
            registerTool(new QueryTool());
            registerTool(new EditFeatureLayerTool());
            registerTool(new EditFeatureTool());
            registerTool(new AddFeatureTool());
        //}

        if(getSystemVersion() > 20) {  //判断当系统版本在5.0及其以上时，可以使用视域功能，以及我的位置
//            registerTool(new ShadowTool());
//            registerTool(new ViewshedTool());
            //立项书无gps定位功能，暂时取消，取消后需修改配置界面侧滑
            //registerTool(new GpsTool());
        }
        registerTool(new SettingsTool());
        toolsRegistered = true;
    }

    public void registerTool(ToolProtocol tool) {
        String toolId = tool.getId();
        if (tools.get(toolId) != null)
            throw new TEAppException(String.format("Tool with id %s already registered", toolId));
        tools.put(toolId, tool);
    }

    public ArrayList<MenuEntry> getMenuEntries() {
        ArrayList<MenuEntry> menuEntries = new ArrayList<MenuEntry>();
        for (ToolProtocol tool : tools.values()) {
            MenuEntry me = tool.getMenuEntry();
            if (me != null)
                menuEntries.add(me);
        }
        return menuEntries;
    }

    public void openTool(String toolId) {
        openTool(toolId, null);
    }

    public void openTool(String toolId, Object param) {
        openTool(toolId, param, null, null);
    }

    public void openTool(String toolId, Object param, final String returnToTool, final Object returnToToolParam) {
        ToolProtocol tool = tools.get(toolId);
        tool.open(param);
        if (tool instanceof ToolContainerDelegate) {
            ToolContainer.INSTANCE.showWithDelegate((ToolContainerDelegate) tool, new ToolContainer.OnContainerStartCloseListener() {
                @Override
                public void OnCloseStart() {
                    if (returnToTool != null) {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                openTool(returnToTool, returnToToolParam);
                            }
                        }, 1);
                    }
                }
            });
        }
    }

    public static int getSystemVersion() {
//        return android.os.Build.VERSION.RELEASE;
        return Build.VERSION.SDK_INT;
    }

}
