package com.skyline.terraexplorer.models;


import com.skyline.terraexplorer.views.ToolContainer;

public interface ToolContainerDelegate {
	public void setToolContainer(ToolContainer toolContainer);
	public boolean onBeforeCloseToolContainer(ToolContainer.CloseReason closeReason);
	public void onClosedToolContainer();
	public boolean onBeforeOpenToolContainer();
	public void onOpenedToolContainer();
	public void onButtonClick(int tag);
}
