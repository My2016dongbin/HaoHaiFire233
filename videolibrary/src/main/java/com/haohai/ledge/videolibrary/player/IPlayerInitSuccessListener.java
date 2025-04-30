package com.haohai.ledge.videolibrary.player;

import com.haohai.ledge.videolibrary.model.GSYModel;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by geyang on 2020/1/8.
 */

public interface IPlayerInitSuccessListener {
    void onPlayerInitSuccess(IMediaPlayer player, GSYModel model);
}
