package com.wt.leanbackutil.player;

interface PlayEngineListener {

    void onMediaPlayerPrepared();

    void onMediaPlayerStart();

    void onMediaPlayerSeekCompleted();

    void onMediaPlayerInfoBufferingStart();

    void onMediaPlayerInfoBufferingEnd();

    void onMediaPlayerIoError();

    void onMediaPlayerError();

    void onMediaPlayerPlayComplete();
}