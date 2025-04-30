package com.skyline.terraexplorer.models;

import android.os.Build;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.tools.*;
import com.skyline.test.mytool.AnalyzeTool;
import com.skyline.test.mytool.PlottingTool;

public class MainButtonDragGestures {
    public static final MainButtonDragGestures instance = new MainButtonDragGestures();

   /* private static Action[] actions = new Action[]
            {
                    instance.new Action(SearchTool.class, R.string.title_activity_search),
                    instance.new Action(PlacesTool.class, R.string.title_activity_places),
                    instance.new Action(LayersTool.class, R.string.title_activity_layers),
                    instance.new Action(DistanceTool.class, R.string.mm_analyze_distance),
                    instance.new Action(AreaTool.class, R.string.mm_analyze_area),
                    instance.new Action(ViewshedTool.class, R.string.mm_analyze_viewshed),
                    instance.new Action(ProfileTool.class, R.string.mm_analyze_profile),
                    instance.new Action(ShadowTool.class, R.string.mm_analyze_shadow),
                    instance.new Action(ProjectsTool.class, R.string.title_activity_projects),
                    instance.new Action(GpsTool.class, R.string.mm_gps),
                    instance.new Action(WhiteboardTool.class, R.string.mm_whiteboard),
                    instance.new Action(SettingsTool.class, R.string.title_activity_settings),
                    instance.new Action(CaptureShareTool.class, R.string.mm_more_share),
            };*/

    //判断当系统版本在5.0及其以上时，可以使用阴影分析和视域功能，以及我的位置
    private static Action[] actions = getSystemVersion() > 20 ? new Action[] {
                    instance.new Action(PlacesTool.class, R.string.title_activity_places),
                    instance.new Action(LayersTool.class, R.string.title_activity_layers),
                    instance.new Action(DistanceTool.class, R.string.mm_analyze_distance),
                    instance.new Action(AreaTool.class, R.string.mm_analyze_area),
                    //instance.new Action(ViewshedTool.class, R.string.mm_analyze_viewshed),
            //2017-06-21 hht修改
//                    instance.new Action(ProfileTool.class, R.string.mm_analyze_profile),
                    //instance.new Action(ShadowTool.class, R.string.mm_analyze_shadow),
                    instance.new Action(ProjectsTool.class, R.string.title_activity_projects),
            //暂时去掉gps 定位功能
//                    instance.new Action(GpsTool.class, R.string.mm_gps),
                    instance.new Action(PlottingTool.class, R.string.mm_plottool),
                    instance.new Action(SettingsTool.class, R.string.title_activity_settings),
                    instance.new Action(CaptureShareTool.class, R.string.mm_more_share)
//                    instance.new Action(AnalyzeTool.class,R.string.analyze_tool)
            } : new Action[] {
                    instance.new Action(PlacesTool.class, R.string.title_activity_places),
                    instance.new Action(LayersTool.class, R.string.title_activity_layers),
                    instance.new Action(DistanceTool.class, R.string.mm_analyze_distance),
                    instance.new Action(AreaTool.class, R.string.mm_analyze_area),
//                    instance.new Action(ProfileTool.class, R.string.mm_analyze_profile),
                    instance.new Action(ProjectsTool.class, R.string.title_activity_projects),
                    instance.new Action(PlottingTool.class, R.string.mm_plottool),
                    instance.new Action(SettingsTool.class, R.string.title_activity_settings),
                    instance.new Action(CaptureShareTool.class, R.string.mm_more_share)
            };

    private class Action {
        String id;
        String displayName;

        public Action(Class<?> clazz, int stringId) {
            this.id = clazz.getName();
            this.displayName = TEApp.getAppContext().getString(stringId);
        }
    }

    private MainButtonDragGestures() {
    }

    public int defaultRight() {
        return 2;
    }

    public int defaultUp() {
        return 1;
    }

    public String[] getActionNames() {
        String[] names = new String[actions.length];
        for (int i = 0; i < actions.length; i++) {
            names[i] = actions[i].displayName;
        }
        return names;
    }

    public void preformAction(int index) {
        if (index < 0 || index >= actions.length)
            return;
        ToolManager.INSTANCE.openTool(actions[index].id);
    }

    public static int getSystemVersion() {
        return Build.VERSION.SDK_INT;
    }
}
