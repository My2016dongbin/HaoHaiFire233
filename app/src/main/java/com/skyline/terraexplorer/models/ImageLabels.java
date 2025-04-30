package com.skyline.terraexplorer.models;

import java.io.Serializable;
import java.util.List;

/**
 * 地图点位图标集合
 */
public class ImageLabels  implements Serializable {

    public String id;

    //组id
    public String groupId;

    //图标类型
    public String type;

    //图片id
    public int imageId;

    //是否选中
    public boolean isChecked;

    //点位集合
    public List<ImageLabel> labelList;

}
