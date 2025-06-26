package com.skyline.test.net;

/**
 * Created by miao on 2017/5/16 14:18.
 */

public interface DownloadListener1 {
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
