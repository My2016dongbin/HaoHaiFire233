package com.skyline.ledge.videolibrary.videocatch.sourcestorage;


import com.skyline.ledge.videolibrary.videocatch.SourceInfo;

/**
 * @author Alexey Danilov (danikula@gmail.com).
 */
public interface SourceInfoStorage {

    SourceInfo get(String url);

    void put(String url, SourceInfo sourceInfo);

    void release();
}
