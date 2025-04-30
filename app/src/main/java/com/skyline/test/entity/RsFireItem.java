package com.skyline.test.entity;

import android.content.IntentFilter;

import com.bean.RsFireBuf;
import com.skyline.teapi.IPosition;

import java.util.UUID;

public class RsFireItem {
    public static final String FAVORITE_ID = "com.skyline.terraexplorer.RSFire_ID";
    public static final String FAVORITE_ICON = "com.skyline.terraexplorer.RSFire_ICON";
    public static final IntentFilter FavoriteChanged = new IntentFilter("com.skyline.terraexplorer.RsFireChanged");
    public String id;
    public String name;
    public String desc;
    public boolean showOn3D;
    public int icon;
    public IPosition position;
    public RsFireItem()
    {
        id = UUID.randomUUID().toString();  //自动生成主键
        name = "";
        icon = 0;
        desc = "";
    }
}
