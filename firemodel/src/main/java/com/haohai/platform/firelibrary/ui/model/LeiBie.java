package com.haohai.platform.firelibrary.ui.model;

import java.util.List;

/**
 * Created by geyang on 2020/7/13.
 */

public class LeiBie {
    public String name;
    public List<Leixing> list;

    public LeiBie(String name, List<Leixing> list) {
        this.name = name;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Leixing> getList() {
        return list;
    }

    public void setList(List<Leixing> list) {
        this.list = list;
    }
}
