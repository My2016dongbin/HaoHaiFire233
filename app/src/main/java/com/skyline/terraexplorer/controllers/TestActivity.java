package com.skyline.terraexplorer.controllers;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.skyline.terraexplorer.R;


public class TestActivity extends Activity {

    private TextView ceshi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ceshi = (TextView) findViewById(R.id.ceshi_view);

        ceshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //        startActivity(new Intent(getApplicationContext(),GPSNaviActivity.class));
            }
        });
    }
}
