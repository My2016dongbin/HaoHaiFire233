package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2020/3/23.
 */
@Table(name = "imagesurl")
public class ImagesUrl {
    @Column(name = "id",isId = true,autoGen = true)
    public int id;

    @Column(name = "resourceid")
    public String resourceId;

    @Column(name = "pic")
    public String pic;

    @Column(name = "fullimagepath")
    public String fullImagePath;

    public ImagesUrl() {
    }

    public ImagesUrl( String resourceId, String pic) {
        this.resourceId = resourceId;
        this.pic = pic;
    }

    public String getFullImagePath() {
        return fullImagePath;
    }

    public void setFullImagePath(String fullImagePath) {
        this.fullImagePath = fullImagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImagesUrl(int id, String resourceId, String pic, String fullImagePath) {
        this.id = id;
        this.resourceId = resourceId;
        this.pic = pic;
        this.fullImagePath = fullImagePath;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
