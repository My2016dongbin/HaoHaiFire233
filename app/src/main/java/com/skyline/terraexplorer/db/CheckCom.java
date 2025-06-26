package com.skyline.terraexplorer.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2020/3/24.
 */

@Table(name = "checkcom")
public class CheckCom {

    @Column(name = "id",isId = true,autoGen = false)
    private String id;


    @Column(name = "checkname")
    private String checkName;

    @Column(name = "zonggti")
    private String zongti;

    @Column(name = "yinhuan")
    private String yinhuan;

    @Column(name = "zhenggai")
    private String zhenggai;

    @Column(name = "checkmen")
    private String checkmen;

    @Column(name = "quname")
    private String quName;

    @Column(name = "jiedaoname")
    private String jiedaoName;


    @Column(name = "checknum")
    public int checkNum;

    @Column(name = "yinhuannum")
    public int yinhuanNum;

    public CheckCom() {
    }


    public CheckCom(String id, String checkName, String zongti, String yinhuan, String zhenggai, String checkmen, int checkNum, int yinhuanNum,String quName,String jiedaoName) {
        this.id = id;
        this.checkName = checkName;
        this.zongti = zongti;
        this.yinhuan = yinhuan;
        this.zhenggai = zhenggai;
        this.checkmen = checkmen;
        this.checkNum = checkNum;
        this.yinhuanNum = yinhuanNum;
        this.quName = quName;
        this.jiedaoName = jiedaoName;
    }

    public int getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }

    public int getYinhuanNum() {
        return yinhuanNum;
    }

    public void setYinhuanNum(int yinhuanNum) {
        this.yinhuanNum = yinhuanNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getZongti() {
        return zongti;
    }

    public void setZongti(String zongti) {
        this.zongti = zongti;
    }

    public String getYinhuan() {
        return yinhuan;
    }

    public void setYinhuan(String yinhuan) {
        this.yinhuan = yinhuan;
    }

    public String getZhenggai() {
        return zhenggai;
    }

    public void setZhenggai(String zhenggai) {
        this.zhenggai = zhenggai;
    }

    public String getCheckmen() {
        return checkmen;
    }

    public void setCheckmen(String checkmen) {
        this.checkmen = checkmen;
    }

    public String getQuName() {
        return quName;
    }

    public void setQuName(String quName) {
        this.quName = quName;
    }

    public String getJiedaoName() {
        return jiedaoName;
    }

    public void setJiedaoName(String jiedaoName) {
        this.jiedaoName = jiedaoName;
    }
}
