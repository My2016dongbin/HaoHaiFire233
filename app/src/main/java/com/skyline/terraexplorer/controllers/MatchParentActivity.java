package com.skyline.terraexplorer.controllers;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


public class MatchParentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
	
	@Override
	public void setContentView (int layoutResID) 
	{
		super.setContentView(layoutResID);
		getWindow().setBackgroundDrawable(null);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
	
	@Override
	public void setContentView (View view) 
	{
		super.setContentView(view);
		getWindow().setBackgroundDrawable(null);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
	
	@Override
	public void setContentView (View view, LayoutParams layoutParams) 
	{
		super.setContentView(view,layoutParams);
		getWindow().setBackgroundDrawable(null);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
}
