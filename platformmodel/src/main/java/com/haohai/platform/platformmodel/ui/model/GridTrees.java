package com.haohai.platform.platformmodel.ui.model;

import org.json.JSONArray;

import java.util.List;

public class GridTrees {
    String id;
    String parentId;
    List<GridTrees> children;
    String name;
    String no;
    String count;

    boolean status = false;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<GridTrees> getChildren() {
        return children;
    }

    public void setChildren(List<GridTrees> children) {
        this.children = children;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return "GridTrees{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", children=" + children +
                ", name='" + name + '\'' +
                ", no='" + no + '\'' +
                ", count='" + count + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
