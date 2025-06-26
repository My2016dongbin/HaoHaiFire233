package com.skyline.terraexplorer.tools;

import android.content.Intent;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.models.MenuEntry;
import com.skyline.terraexplorer.models.ToolProtocol;
import com.skyline.test.places1.Place2Activity;

/**
 *
 */
public class PlacesTool extends BaseTool implements ToolProtocol {
	@Override
	public MenuEntry getMenuEntry() {
		return MenuEntry.createFor(this, R.string.title_activity_places, R.drawable.places, 20);
	}
	
	@Override
	public void open(Object param) {
		Intent in = new Intent(TEApp.getCurrentActivityContext(), Place2Activity.class);
//		Intent in = new Intent(TEApp.getCurrentActivityContext(),PlacesActivity1.class);
		TEApp.getCurrentActivityContext().startActivity(in);
	}

}
