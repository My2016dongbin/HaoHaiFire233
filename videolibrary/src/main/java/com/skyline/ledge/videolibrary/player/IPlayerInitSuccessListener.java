package com.skyline.ledge.videolibrary.player;

import com.skyline.ledge.videolibrary.model.GSYModel;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by geyang on 2020/1/8.
 */

public interface IPlayerInitSuccessListener {
    void onPlayerInitSuccess(IMediaPlayer player, GSYModel model);
}
