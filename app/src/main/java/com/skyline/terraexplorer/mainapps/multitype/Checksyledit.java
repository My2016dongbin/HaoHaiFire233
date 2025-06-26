package com.skyline.terraexplorer.mainapps.multitype;

import java.io.Serializable;

/**
 * Created by geyang on 2019/11/22.
 */

public class Checksyledit implements Serializable{

    public String id;
    /**
     *检查字段名称 ,
     */
    public String name;
    /**
     * 选中状态  0未选中  1选中
     */
    public String editmeg;

    public String code;
    public Checksyledit() {
    }

    public Checksyledit(String id, String name, String code, String editmeg) {
        this.id = id;
        this.name = name;
        this.editmeg = editmeg;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public String toString() {
        return "CheckField{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", editmeg=" + editmeg +
                '}';
    }
}
