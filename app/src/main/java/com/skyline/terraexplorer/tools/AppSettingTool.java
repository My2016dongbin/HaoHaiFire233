package com.skyline.terraexplorer.tools;

import android.content.Intent;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.models.MenuEntry;

/**
 * Created by geyang on 2019/11/23.
 */

public class AppSettingTool extends BaseTool {
    @Override
    public MenuEntry getMenuEntry() {
        return MenuEntry.createFor(this, R.string.app_setting, R.drawable.places, 20);
    }

    @Override
    public void open(Object param) {
        Intent in = new Intent(TEApp.getCurrentActivityContext(),SettingsTool.class);
//		Intent in = new Intent(TEApp.getCurrentActivityContext(),PlacesActivity1.class);
        TEApp.getCurrentActivityContext().startActivity(in);
    }
}
