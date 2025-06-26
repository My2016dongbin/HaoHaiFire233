package com.skyline.terraexplorer.wisdomgarden.ui.multitype;

/**
 * Created by geyang on 2020/12/8.
 */
public class QuData {
    public String quName;
    public String quCode;
    public boolean isCheck;

    public QuData() {
    }

    public QuData(String quName, String quCode, boolean isCheck) {
        this.quName = quName;
        this.quCode = quCode;
        this.isCheck = isCheck;
    }

    public String getQuName() {
        return quName;
    }

    public void setQuName(String quName) {
        this.quName = quName;
    }

    public String getQuCode() {
        return quCode;
    }

    public void setQuCode(String quCode) {
        this.quCode = quCode;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}