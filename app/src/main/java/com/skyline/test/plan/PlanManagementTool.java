package com.skyline.test.plan;

import android.content.Intent;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.models.MenuEntry;
import com.skyline.terraexplorer.tools.BaseTool;

/**
 * Created by miao on 2017/4/25 16:46.
 */

public class PlanManagementTool extends BaseTool{

    @Override
    public MenuEntry getMenuEntry() {
        return MenuEntry.createFor(this, R.string.plan_management,R.drawable.plan_management,95);
    }

    @Override
    public void open(Object param) {
        TEApp.getCurrentActivityContext().startActivity(new Intent(TEApp.getCurrentActivityContext(),PlanManagementActivity.class));
    }
}
