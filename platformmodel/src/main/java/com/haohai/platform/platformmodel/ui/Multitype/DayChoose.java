package com.haohai.platform.platformmodel.ui.Multitype;

/**
 * Created by geyang on 2020/6/5.
 */
public class DayChoose {
    public int id;
    public String year;
    public String monthDay;
    public String week;
    public String dayStr;
    public String year_money_day;
    public boolean isChoose;

    public DayChoose(int id, String year, String monthDay, String week, String dayStr,String year_money_day,boolean isChoose) {
        this.id = id;
        this.year = year;
        this.monthDay = monthDay;
        this.week = week;
        this.dayStr = dayStr;
        this.year_money_day = year_money_day;
        this.isChoose = isChoose;
    }

    public String getYear_money_day() {
        return year_money_day;
    }

    public void setYear_money_day(String year_money_day) {
        this.year_money_day = year_money_day;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(String monthDay) {
        this.monthDay = monthDay;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDayStr() {
        return dayStr;
    }

    public void setDayStr(String dayStr) {
        this.dayStr = dayStr;
    }
}