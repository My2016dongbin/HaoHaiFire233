package com.haohai.ledge.videolibrary.listener;


import java.io.File;

/**
 * 截屏保存结果
 * Created by gy
 */

public interface GSYVideoShotSaveListener {
    void result(boolean success, File file);
}
