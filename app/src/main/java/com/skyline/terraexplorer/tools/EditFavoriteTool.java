package com.skyline.terraexplorer.tools;

import android.content.Intent;

import com.skyline.terraexplorer.TEApp;
import com.skyline.terraexplorer.controllers.EditFavoriteActivity;
import com.skyline.terraexplorer.models.FavoriteItem;
import com.skyline.terraexplorer.models.FavoritesStorage;
import com.skyline.test.myview.EditPlaceActivity;
import com.skyline.test.others.LogUtils;

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
