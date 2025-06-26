package com.skyline.terraexplorer.bus;

/**
 * Created by qc
 * on 2024/5/10.
 * Copyright © 2018 青岛浩海网络科技股份有限公司 版权所有
 */
public class PlanPush {
    String id ;

    public PlanPush(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
