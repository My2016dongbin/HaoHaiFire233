package com.skyline.test.plan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.skyline.terraexplorer.R;
import com.skyline.terraexplorer.controllers.MatchParentActivity;
import com.skyline.test.word.WordUtil;

/**
 * Created by miao on 2017/5/18 14:56.
 */

public class WordActivity extends MatchParentActivity {

    private static final String TAG = "WordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        WebView webView = (WebView) findViewById(R.id.word_web);
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);// 设置WebView可触摸放大缩小
        settings.setUseWideViewPort(true);

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        Log.d(TAG, "onCreate: path = " + path);

        // tm-extractors-0.4.jar与poi的包在编译时会冲突，二者只能同时导入一个
        WordUtil wu = new WordUtil(path);
        webView.loadUrl("file:///" + wu.htmlPath);

    }
}
