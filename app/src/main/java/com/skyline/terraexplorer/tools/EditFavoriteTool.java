package com.skyline.terraexplorer.tools;

import android.content.Intent;

import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.models.FavoriteItem;
import com.skyline.test.myview.EditPlaceActivity;

public class EditFavoriteTool extends BaseTool {
	private static final String TAG = "EditFavoriteTool";
	public void open(Object param) {
//		Intent in = new Intent(TEApp.getCurrentActivityContext(),EditFavoriteActivity.class);
		Intent in=new Intent(TEApp.getCurrentActivityContext(), EditPlaceActivity.class);
		if(param != null)
			in.putExtra(FavoriteItem.FAVORITE_ID, (String)param);
		TEApp.getCurrentActivityContext().startActivity(in);
	}
}
