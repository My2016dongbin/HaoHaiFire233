package com.skyline.terraexplorer.models;

/**
 * Created by geyang on 2020/1/4.
 */

public class MapFile {
    public String name;
    public String fileNameOne;
    public String fileNameTwo;
    public int rank;
    public int level;       //0s文件  1文件夹
    public String name_cn;       //0s文件  1文件夹

    public MapFile(String name, String fileNameOne, String fileNameTwo, int rank, int level, String name_cn) {
        this.name = name;
        this.fileNameOne = fileNameOne;
        this.fileNameTwo = fileNameTwo;
        this.rank = rank;
        this.level = level;
        this.name_cn = name_cn;
    }

    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileNameOne() {
        return fileNameOne;
    }

    public void setFileNameOne(String fileNameOne) {
        this.fileNameOne = fileNameOne;
    }

    public String getFileNameTwo() {
        return fileNameTwo;
    }

    public void setFileNameTwo(String fileNameTwo) {
        this.fileNameTwo = fileNameTwo;
    }
}
