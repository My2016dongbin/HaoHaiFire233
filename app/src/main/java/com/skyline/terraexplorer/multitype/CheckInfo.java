package com.skyline.terraexplorer.multitype;

import com.skyline.terraexplorer.db.CheckImages;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by geyang on 2020/3/24.
 */
@Table(name = "checkinfo")
public class CheckInfo {

    @Column(name = "id",isId = true,autoGen = false)
    public String checkId;

    @Column(name = "checkname")
    public String checkName;

    @Column(name = "checkinfo")
    public String checkInfo;

    @Column(name = "chooseimagelist")
    public List<ChooseImage> chooseImageList;

    @Column(name = "fullimagelist")
    public List<CheckImages> fullImageList;

    @Column(name = "issava")
    public boolean isSava;

    public CheckInfo() {
    }

    public CheckInfo(String checkId, String checkName, String checkInfo, List<ChooseImage> chooseImageList) {
        this.checkId = checkId;
        this.checkName = checkName;
        this.checkInfo = checkInfo;
        this.chooseImageList = chooseImageList;
        this.isSava = false;
    }

    public CheckInfo(String checkId, String checkName, String checkInfo, List<ChooseImage> chooseImageList, List<CheckImages> fullImageList, boolean isSava) {
        this.checkId = checkId;
        this.checkName = checkName;
        this.checkInfo = checkInfo;
        this.chooseImageList = chooseImageList;
        this.fullImageList = fullImageList;
        this.isSava = isSava;
    }

    public List<CheckImages> getFullImageList() {
        return fullImageList;
    }

    public void setFullImageList(List<CheckImages> fullImageList) {
        this.fullImageList = fullImageList;
    }

    public boolean isSava() {
        return isSava;
    }

    public void setSava(boolean sava) {
        isSava = sava;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }

    public List<ChooseImage> getChooseImageList() {
        return chooseImageList;
    }

    public void setChooseImageList(List<ChooseImage> chooseImageList) {
        this.chooseImageList = chooseImageList;
    }
}