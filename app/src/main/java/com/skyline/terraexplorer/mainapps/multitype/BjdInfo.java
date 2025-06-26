package com.skyline.terraexplorer.mainapps.multitype;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by geyang on 2020/3/24.
 */
@Table(name = "bjdinfo")
public class BjdInfo {
    @Override
    public String toString() {
        return "BjdInfo{" +
                "id='" + bjdid + '\'' +
                ", plantname='" + plantname + '\'' +
                ", variety='" + variety + '\'' +
                ", isSava=" + isSava +
                ", count=" + count +
                ", createUser='" + createUser + '\'' +
                ", groupId='" + groupId + '\'' +
                ", packaging='" + packaging + '\'' +
                ", remark='" + remark + '\'' +
                ", spec='" + spec + '\'' +
                ", transportationQuarantineId='" + transportationQuarantineId + '\'' +
                ", unit='" + unit + '\'' +
                ", unitPrice=" + unitPrice +
                '}';
    }

    @Column(name = "id",isId = true,autoGen = false)
    public String bjdid;

    @Column(name = "plantname")
    public String plantname;

    @Column(name = "variety")
    public String variety;

    @Column(name = "issava")
    public boolean isSava;
    @Column(name = "count")
    public int count;
    @Column(name = "creatuser")
    public String createUser;
    @Column(name = "groupid")
    public String groupId;
    @Column(name = "packaging")
    public String packaging;
    @Column(name = "remark")
    public String remark;
    @Column(name = "spec")
    public String spec;
    @Column(name = "transportationquarantineid")
    public String transportationQuarantineId;
    @Column(name = "unit")
    public String unit;
    @Column(name = "unitprice")
    public int unitPrice;

    public BjdInfo() {
    }

    public BjdInfo(String id, String plantname, String variety, int count, String createUser, String groupId, String packaging, String remark, String spec, String transportationQuarantineId, String unit, int unitPrice) {
        this.bjdid = id;
        this.plantname = plantname;
        this.variety = variety;
        this.isSava = false;
        this.count = count;
        this.createUser = createUser;
        this.groupId = groupId;
        this.packaging = packaging;
        this.remark = remark;
        this.spec = spec;
        this.transportationQuarantineId = transportationQuarantineId;
        this.unit = unit;
        this.unitPrice = unitPrice;
    }

    public BjdInfo(String id, String plantname, String variety, boolean isSava) {
        this.bjdid = id;
        this.plantname = plantname;
        this.variety = variety;
        this.isSava = isSava;
    }

    public boolean isSava() {
        return isSava;
    }

    public void setSava(boolean sava) {
        isSava = sava;
    }
    public String getBjdid() {
        return bjdid;
    }

    public void setBjdid(String bjdid) {
        this.bjdid = bjdid;
    }

    public String getPlantname() {
        return plantname;
    }

    public void setPlantname(String plantname) {
        this.plantname = plantname;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getTransportationQuarantineId() {
        return transportationQuarantineId;
    }

    public void setTransportationQuarantineId(String transportationQuarantineId) {
        this.transportationQuarantineId = transportationQuarantineId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

}