package com.ruyiruyi.rylibrary.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2019/8/22.
 */

@Table(name = "setting")
public class Setting {
    @Column(name = "id",isId = true,autoGen = false)
    private int id;

    @Column(name = "weixing")
    private String weixing;        //卫星监测

    @Column(name = "tiankong")
    private String tiankong;        //天空监测

    @Column(name = "dimian")
    private String dimian;        //地面监测

    @Column(name = "dimao")
    private String dimao;        //地貌类型

    @Column(name = "number")
    private String number;        //火警数量

    @Column(name = "jingwai")
    private String jingwai;        //查询境外热源

    @Column(name = "huanchong")
    private String huanchong;        //是否包含缓冲区  0不包含  1包含

    public Setting() {
    }

    public Setting(int id, String weixing, String tiankong, String dimian, String dimao, String number, String jingwai, String huanchong) {
        this.id = id;
        this.weixing = weixing;
        this.tiankong = tiankong;
        this.dimian = dimian;
        this.dimao = dimao;
        this.number = number;
        this.jingwai = jingwai;
        this.huanchong = huanchong;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeixing() {
        return weixing;
    }

    public void setWeixing(String weixing) {
        this.weixing = weixing;
    }

    public String getTiankong() {
        return tiankong;
    }

    public void setTiankong(String tiankong) {
        this.tiankong = tiankong;
    }

    public String getDimian() {
        return dimian;
    }

    public void setDimian(String dimian) {
        this.dimian = dimian;
    }

    public String getDimao() {
        return dimao;
    }

    public void setDimao(String dimao) {
        this.dimao = dimao;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getJingwai() {
        return jingwai;
    }

    public void setJingwai(String jingwai) {
        this.jingwai = jingwai;
    }

    public String getHuanchong() {
        return huanchong;
    }

    public void setHuanchong(String huanchong) {
        this.huanchong = huanchong;
    }
}
