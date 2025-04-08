package com.haohai.platform.fireforestplatform.ui.acticity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.haohai.platform.fireforestplatform.R;
import com.haohai.platform.platformmodel.ui.acticity.base.HhBaseActivity;

@Route(path = "/test/activity")
public class Test1Activity extends HhBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
    }
}
