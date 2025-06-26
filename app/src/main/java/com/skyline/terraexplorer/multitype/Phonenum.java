package com.skyline.terraexplorer.multitype;

public class Phonenum {
    public String fullName;
    public String phone;
    public String id;
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Phonenum(String fullName, String phone, String id) {
        this.fullName = fullName;
        this.phone = phone;
        this.id = id;
    }
}
