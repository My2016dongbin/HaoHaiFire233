package com.ruyiruyi.rylibrary.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2020/6/11.
 */
@Table(name = "attendance")
public class Attendance {
    @Column(name = "id",isId = true,autoGen = false)
    private String id;

    @Column(name = "riqi")
    private String riqi;

    @Column(name = "qiandaoshijian")
    private String qiandaoShijian;

    @Column(name = "qiantuishijian")
    private String qiantuiShijian;

    @Column(name = "is1iandao")
    private boolean isQiandao;

    @Column(name = "is1iantui")
    private boolean isQiantui;

    public Attendance() {
    }

    public Attendance(String id, String riqi, String qiandaoShijian, String qiantuiShijian, boolean isQiandao, boolean isQiantui) {
        this.id = id;
        this.riqi = riqi;
        this.qiandaoShijian = qiandaoShijian;
        this.qiantuiShijian = qiantuiShijian;
        this.isQiandao = isQiandao;
        this.isQiantui = isQiantui;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRiqi() {
        return riqi;
    }

    public void setRiqi(String riqi) {
        this.riqi = riqi;
    }

    public String getQiandaoShijian() {
        return qiandaoShijian;
    }

    public void setQiandaoShijian(String qiandaoShijian) {
        this.qiandaoShijian = qiandaoShijian;
    }

    public String getQiantuiShijian() {
        return qiantuiShijian;
    }

    public void setQiantuiShijian(String qiantuiShijian) {
        this.qiantuiShijian = qiantuiShijian;
    }

    public boolean isQiandao() {
        return isQiandao;
    }

    public void setQiandao(boolean qiandao) {
        isQiandao = qiandao;
    }

    public boolean isQiantui() {
        return isQiantui;
    }

    public void setQiantui(boolean qiantui) {
        isQiantui = qiantui;
    }
}

