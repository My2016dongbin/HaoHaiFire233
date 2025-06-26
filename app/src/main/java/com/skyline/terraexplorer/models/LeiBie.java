package com.skyline.terraexplorer.models;

import java.util.List;

/**
 * Created by geyang on 2019/10/25.
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
