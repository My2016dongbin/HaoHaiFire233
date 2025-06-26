package com.skyline.terraexplorer.wisdomgarden.ui.model;

/**
 * Created by geyang on 2020/11/4.
 */

public class ParkType {
    public String id;
    public String parentId;
    public String typeName;

    public ParkType() {
    }

    public ParkType(String id, String parentId, String typeName) {
        this.id = id;
        this.parentId = parentId;
        this.typeName = typeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
