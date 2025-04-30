package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2020/3/24.
 */

@Table(name = "checkimages")
public class CheckImages {
    /**
     * 账号
     */
    @Column(name = "id",isId = true,autoGen = true)
    private String id;

    @Column(name = "checkid")
    private String checkId;

    @Column(name = "fullstr")
    private String fullStr;


    public CheckImages() {
    }

    public CheckImages(String checkId, String fullStr) {
        this.checkId = checkId;
        this.fullStr = fullStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getFullStr() {
        return fullStr;
    }

    public void setFullStr(String fullStr) {
        this.fullStr = fullStr;
    }
}
