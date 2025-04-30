package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2019/11/26.
 */

@Table(name = "checkrecord")
public class CheckRecord {
    @Column(name = "id",isId = true,autoGen = false)
    public String id;


    @Column(name = "checkstr")
    public String checkStr;

    /**
     *
     */
    @Column(name = "pic1")
    public String pic1;

    /**
     *
     */
    @Column(name = "pic2")
    public String pic2;

    /**
     *
     */
    @Column(name = "qianmingpic")
    public String qianMingPic;

    /**
     *
     */
    @Column(name = "qianmingonepic")
    public String qianMingOnePic;

    /**
     *
     */
    @Column(name = "checktime")
    public String checkTime;
    /**
     *
     */
    @Column(name = "resourceid")
    public String resourceId;

    /**
     *
     */
    @Column(name = "name")
    public String name;

    /**
     *
     */
    @Column(name = "description")
    public String description;

    /**
     *
     */
    @Column(name = "apiurl")
    public String apiUrl;

    @Column(name = "gridid")
    public String gridId;

    @Column(name = "gridname")
    public String gridName;

    @Column(name = "gridno")
    public String gridNo;

    public CheckRecord() {
    }

    public CheckRecord(String checkStr, String pic1, String pic2, String qianMingPic, String checkTime, String resourceId, String name, String description) {

        this.checkStr = checkStr;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.qianMingPic = qianMingPic;
        this.checkTime = checkTime;
        this.resourceId = resourceId;
        this.name = name;
        this.description = description;
    }

    public CheckRecord(String id,String checkStr, String pic1, String pic2, String qianMingPic,String qianMingOnePic, String checkTime, String resourceId, String name, String description, String apiUrl, String gridId, String gridName, String gridNo) {
        this.id = id;
        this.checkStr = checkStr;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.qianMingPic = qianMingPic;
        this.qianMingOnePic = qianMingOnePic;
        this.checkTime = checkTime;
        this.resourceId = resourceId;
        this.name = name;
        this.description = description;
        this.apiUrl = apiUrl;
        this.gridId = gridId;
        this.gridName = gridName;
        this.gridNo = gridNo;
    }

    public String getQianMingOnePic() {
        return qianMingOnePic;
    }

    public void setQianMingOnePic(String qianMingOnePic) {
        this.qianMingOnePic = qianMingOnePic;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public String getGridNo() {
        return gridNo;
    }

    public void setGridNo(String gridNo) {
        this.gridNo = gridNo;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCheckStr() {
        return checkStr;
    }

    public void setCheckStr(String checkStr) {
        this.checkStr = checkStr;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getQianMingPic() {
        return qianMingPic;
    }

    public void setQianMingPic(String qianMingPic) {
        this.qianMingPic = qianMingPic;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
