package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2020/3/24.
 */

@Table(name = "checkcomimage")
public class CheckComImage {

    @Column(name = "id",isId = true,autoGen = true)
    private String id;

    @Column(name = "checkcomid")
    private String  checkComId;


    @Column(name = "pic")
    private String  pic;

    @Column(name = "isadd")
    public boolean isAdd;

    public CheckComImage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CheckComImage(String checkComId, String pic, boolean isAdd) {
        this.checkComId = checkComId;
        this.pic = pic;
        this.isAdd = isAdd;
    }

    public String getCheckComId() {
        return checkComId;
    }

    public void setCheckComId(String checkComId) {
        this.checkComId = checkComId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
