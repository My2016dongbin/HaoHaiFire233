package com.skyline.terraexplorer.models;

import java.util.List;

/**
 * Created by geyang on 2020/3/25.
 */

public class CheckSaveData {
    public CheckDto dto;
    public List<CheckDetail> detailList;

    public CheckSaveData(CheckDto dto, List<CheckDetail> detailList) {
        this.dto = dto;
        this.detailList = detailList;
    }

    public CheckSaveData() {
    }

    public CheckDto getDto() {
        return dto;
    }

    public void setDto(CheckDto dto) {
        this.dto = dto;
    }

    public List<CheckDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<CheckDetail> detailList) {
        this.detailList = detailList;
    }
}
