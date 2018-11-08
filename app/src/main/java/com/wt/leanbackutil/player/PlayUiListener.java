package com.wt.leanbackutil.player;

/**
 * 更新ui操作
 */
public abstract class PlayUiListener {

    public abstract void onUiMediaPlayerPrepared();


    public void onUiMediaPlayerStart() {
    }

    public void onUiMediaPlayerInfoBufferingStart() {
    }

    public void onUiMediaPlayerInfoBufferingEnd() {
    }

    public void onUiMediaPlayerSeekCompleted() {
    }

    public void onUiMediaPlayerIoError() {
    }

    public void onUiMediaPlayerError() {
    }

    public void onUiMediaPlayerPlayComplete() {
    }

    public void toLoginActivity(String message) {
    }

    public void toOrderActivity(boolean isRenew, String message) {
    }
}