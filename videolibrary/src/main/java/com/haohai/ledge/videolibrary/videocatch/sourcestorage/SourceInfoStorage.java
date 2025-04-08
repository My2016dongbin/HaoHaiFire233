package com.haohai.ledge.videolibrary.videocatch.sourcestorage;


import com.haohai.ledge.videolibrary.videocatch.SourceInfo;

/**
 * @author Alexey Danilov (danikula@gmail.com).
 */
public interface SourceInfoStorage {

    SourceInfo get(String url);

    void put(String url, SourceInfo sourceInfo);

    void release();
}
