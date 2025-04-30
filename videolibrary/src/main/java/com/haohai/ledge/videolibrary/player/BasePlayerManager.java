package com.haohai.ledge.videolibrary.player;

import com.haohai.ledge.videolibrary.model.GSYModel;

/**
 * Created by geyang on 2020/1/8.
 */


public abstract class BasePlayerManager implements IPlayerManager {

    protected IPlayerInitSuccessListener mPlayerInitSuccessListener;

    public IPlayerInitSuccessListener getPlayerPreparedSuccessListener() {
        return mPlayerInitSuccessListener;
    }

    public void setPlayerInitSuccessListener(IPlayerInitSuccessListener listener) {
        this.mPlayerInitSuccessListener = listener;
    }

    protected void initSuccess(GSYModel gsyModel) {
        if (mPlayerInitSuccessListener != null) {
            mPlayerInitSuccessListener.onPlayerInitSuccess(getMediaPlayer(), gsyModel);
        }
    }
}