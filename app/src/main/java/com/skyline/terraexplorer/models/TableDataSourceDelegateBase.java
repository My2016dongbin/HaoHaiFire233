package com.skyline.terraexplorer.models;


public class TableDataSourceDelegateBase implements TableDataSource.TableDataSourceDelegate
{
	@Override
	public ContextMenuEntry[] contextMenuForPath(long packedPosition) {
		return null;
	}

	@Override
	public void contextMenuItemTapped(int menuId, long packedPosition) {
	}

	@Override
	public void accessoryButtonTappedForRowWithIndexPath(long packedPosition) {
		
	}

	@Override
	public void didSelectRowAtIndexPath(long packedPosition) {
		
	}

	@Override
	public boolean accessoryButtonTappableForRowWithIndexPath(
			long packedPosition) {
		return false;
	}
	
}
