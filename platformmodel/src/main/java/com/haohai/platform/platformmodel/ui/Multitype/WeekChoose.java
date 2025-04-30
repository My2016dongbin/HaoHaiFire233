package com.haohai.platform.platformmodel.ui.Multitype;

/**
 * Created by geyang on 2020/6/6.
 */
public class WeekChoose {
    public int id;
    public String week;
    public boolean isChosoe;

    public WeekChoose() {
    }

    public WeekChoose(int id, String week, boolean isChosoe) {
        this.id = id;
        this.week = week;
        this.isChosoe = isChosoe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public boolean isChosoe() {
        return isChosoe;
    }

    public void setChosoe(boolean chosoe) {
        isChosoe = chosoe;
    }
}