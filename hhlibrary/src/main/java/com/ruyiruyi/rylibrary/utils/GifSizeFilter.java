package com.ruyiruyi.rylibrary.utils;


import android.content.Context;
import android.graphics.Point;

import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.entity.UncapableCause;
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils;

import java.util.HashSet;
import java.util.Set;


public class GifSizeFilter extends Filter {
    public int mMinWidth;
    public int mMinHeight;
    public int mMaxSize;

    public GifSizeFilter(int minWidth, int minHeight, int maxSizeInBytes) {
        this.mMinWidth = minWidth;
        this.mMinHeight = minHeight;
        this.mMaxSize = maxSizeInBytes;
    }

    public Set<MimeType> constraintTypes() {
        return new HashSet() {
            {
                this.add(MimeType.GIF);
            }
        };
    }

    public UncapableCause filter(Context context, Item item) {
        if(!this.needFiltering(context, item)) {
            return null;
        } else {
            Point size = PhotoMetadataUtils.getBitmapBound(context.getContentResolver(), item.getContentUri());
            return size.x >= this.mMinWidth && size.y >= this.mMinHeight && item.size <= (long)this.mMaxSize?null:new UncapableCause(1, context.getString(2131296636, new Object[]{Integer.valueOf(this.mMinWidth), String.valueOf(PhotoMetadataUtils.getSizeInMB((long)this.mMaxSize))}));
        }
    }
}