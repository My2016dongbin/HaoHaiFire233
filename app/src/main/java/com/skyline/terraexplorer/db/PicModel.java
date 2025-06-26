package com.skyline.terraexplorer.db;

public class PicModel {
    private String fileName;
    private String group;
    private String path;
    private String fullPath;
    private double msize;
    private String extName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public double getMsize() {
        return msize;
    }

    public void setMsize(double msize) {
        this.msize = msize;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    @Override
    public String toString() {
        return "PicModel{" +
                "fileName='" + fileName + '\'' +
                ", group='" + group + '\'' +
                ", path='" + path + '\'' +
                ", fullPath='" + fullPath + '\'' +
                ", msize=" + msize +
                ", extName='" + extName + '\'' +
                '}';
    }
}
