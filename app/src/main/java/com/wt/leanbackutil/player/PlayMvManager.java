package com.wt.leanbackutil.player;

import android.os.Handler;
import android.os.Message;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.wt.leanbackutil.model.MvInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hk.reco.baselib.util.Logger;

/**
 * 播放界面控制模块
 */
public class PlayMvManager {
    private final static String TAG = PlayMvManager.class.getName();

    /**
     * 每次用户手动seek以后都延迟一秒再执行，防止频繁seek引擎出错
     */
    private static final int DELAY_SEEK_TIME = 1000;

    /**
     * 封装播放引擎的类
     */
    private PlayEngine playEngine;
    private PlayUiListener playUiListener;

    /**
     * 用于区分是异常触发播放引擎的onCompletion还是正常退出触发的onCompletion
     */
    private boolean isPlayError = false;

    /**
     * 延迟一秒钟再seek防止频繁seek引擎出错
     */
    private Handler seekHandler;

    private boolean initState;

    private boolean seekFinished = true;

    private List<MvInfo> mvs = new ArrayList<>();
    private int currentPlayIndex = 0;
    /**
     * 当前视频的总时长
     */
    private int duration;
    private int willPlayIndex = 0;

    private static PlayMvManager instance;

    private PlayMvManager() {

    }

    public static PlayMvManager getInstance() {
        if (instance == null) {
            synchronized (PlayMvManager.class) {
                instance = new PlayMvManager();
            }
        }
        return instance;
    }

    public void init() {
        playEngine = new PlayEngine();
        playEngine.setMv(true);
        playEngine.setPlayEngineListener(playEngineListener);
        initState = true;
    }


    /**
     * 处理网络异常
     */
    public boolean needFinishOnNetworkOff() {
        Logger.d(TAG, "needFinishOnNetworkOff");
        return null != playEngine;
    }

    /**
     * 向播放引擎设置解析得到的播放url
     *
     * @param url 解析得到的播放url
     */
    public void setDataSourceToEngine(final String url) {
        Logger.d(TAG, "播放地址： " + url);
        playEngine.setDataSource(url);
        initState = true;
    }

    /**
     * 控制播放引擎暂停或播放
     */
    public boolean setPlayOrPause() {
        boolean ret = true;
        // 已经是暂停状态
        if (playEngine.getPlayState() == PlayState.PAUSED) {
            playEngine.start();
            ret = true;
            // 已经是播放状态
        } else if (playEngine.getPlayState() == PlayState.STARTED) {
            playEngine.pause();
            ret = false;
        }
        return ret;
    }

    /**
     * 暂停
     */
    public void pause(){
        if(playEngine.getPlayState() == PlayState.STARTED){
            playEngine.pause();
        }
    }

    public boolean isSeekAble() {
        return playEngine.getPlayState() == PlayState.PAUSED
                || playEngine.getPlayState() == PlayState.STARTED
                || playEngine.getPlayState() == PlayState.PREPARED;
    }


    /**
     * 从播放引擎传出的总时长，用于更新时长显示和设置进度条的总长度
     */
    public int updateDuration() {
        int duration = playEngine.getDuration();
        return duration;
    }


    /**
     * 手动拖动进度时，延迟执行seek
     */
    public void delaySeekProgress(final int progress) {
        seekProgress(progress);
    }

    /**
     * 拖动进度条给播放引擎设置进度
     */
    public void seekProgress(final int progress) {
        setSeekFinished(false);
        playEngine.seekTo(progress);
    }

    /**
     * 获取当前的播放进度，返回0表示失败。
     *
     * @return
     */
    public int getCurrentPosition() {
        return playEngine.getCurrentPosition();
    }

    public boolean isPlaying() {
        return playEngine.getPlayState() == PlayState.STARTED;
    }

    /**
     * 设置绘制回调
     *
     * @param holder
     */
    public void setDisplayHolder(SurfaceHolder holder) {
        playEngine.setDisplay(holder);
    }

    public void setSurface(Surface surface){
        playEngine.setSurface(surface);
    }

    /**
     * 获得播放引擎是否已经出错，用于区分是异常触发播放引擎的onCompletion还是正常退出触发的onCompletion
     *
     * @return 是否已经出错
     */
    public boolean isPlayError() {
        return isPlayError;
    }

    /**
     * 设置播放引擎是否已经出错，用于区分是异常触发播放引擎的onCompletion还是正常退出触发的onCompletion
     *
     * @param isPlayError 是否已经出错
     */
    private void setPlayError(boolean isPlayError) {
        this.isPlayError = isPlayError;
    }

    /**
     * 处理停止播放事件,注意：切换上下集或分辨率时会调用stop，但是不会调用destory
     */
    public void stopPlay() {
        if (seekHandler != null) {
            seekHandler.removeCallbacksAndMessages(null);
        }
        if (playEngine != null) {
            this.playUiListener = null;
            setPlayUiListener(null);
            playEngine.stop();
        }
        setPlayError(false);
        initState = false;
    }

    public void destory() {
        if (null != playEngine) {
            playEngine.release();
        }
        handler.removeCallbacks(r);
        setPlayError(false);
    }


    synchronized public boolean isInitState() {
        return initState;
    }

    synchronized public void setInitState(boolean initState) {
        this.initState = initState;
    }

    private Runnable r;
    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<PlayMvManager> wf;

        private MyHandler(PlayMvManager playMvManager) {
            wf = new WeakReference<PlayMvManager>(playMvManager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MvInfo mvInfo = (MvInfo) msg.obj;
            PlayMvManager playMvManager = wf.get();
            MvInfo currentMvInfo = playMvManager.getCurrentPlayMv();
            if (mvInfo == null || currentMvInfo == null) {
                return;
            }
            //保存当前的播放进度
//            RecentPlayMvManager.getInstance().save(mvInfo, currentMvInfo);
        }
    }

    private final PlayEngineListener playEngineListener = new PlayEngineListener() {

        @Override
        public void onMediaPlayerStart() {
            setSeekFinished(true);
            if (playUiListener != null) {
                playUiListener.onUiMediaPlayerStart();
            }
        }

        @Override
        public void onMediaPlayerSeekCompleted() {
            setSeekFinished(true);
            playEngine.start();
            if (playUiListener != null) {
                playUiListener.onUiMediaPlayerSeekCompleted();
            }
        }

        @Override
        public void onMediaPlayerPrepared() {
            final MvInfo mvInfo = getCurrentPlayMv();
            //从播放引擎传出的总时长，用于更新时长显示和设置进度条的总长度
            if (playUiListener != null) {
                playUiListener.onUiMediaPlayerPrepared();
            }

            if (r != null) {
                handler.removeCallbacks(r);
            }

            r = new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.obj = mvInfo;
                    handler.sendMessage(msg);
                }
            };
            handler.postDelayed(r, 15 * 1000);
        }

        @Override
        public void onMediaPlayerPlayComplete() {
            // 正常情况下的播放完成
            setSeekFinished(true);
//            网络链接或者播放下一首
//            if (Cache.getInstance().isNetworkConnected()) {
//                if (playUiListener != null) {
//                    playUiListener.onUiMediaPlayerPlayComplete();
//                }
//                String url;
//
//                int playSequence = PreferencesUtil.getIntValue(App.getInstance(),
//                        PreferencesUtil.KEY_PLAY_MV_SEQUENCE, Cst.PLAY_SEQUENCE_ALL);
//                if (playSequence == Cst.PLAY_SEQUENCE_ALL) {
//                    url = next();
//                } else if (playSequence == Cst.PLAY_SEQUENCE_RANDOM) {
//                    url = random();
//                } else {
//                    url = getCurrentPlayMvUrl();
//                }
//
//                setDataSourceToEngine(url);
//            } else {
            if (playUiListener != null) {
                playUiListener.onUiMediaPlayerPlayComplete();
            }
//            }
        }

        @Override
        public void onMediaPlayerIoError() {
            if (playUiListener != null) {
                playUiListener.onUiMediaPlayerIoError();
            }
        }

        @Override
        public void onMediaPlayerInfoBufferingStart() {
            if (playUiListener != null) {
                playUiListener.onUiMediaPlayerInfoBufferingStart();
            }
        }

        @Override
        public void onMediaPlayerInfoBufferingEnd() {
//            setSeekFinished(true);
//            playEngine.start();
            if (playUiListener != null) {
                playUiListener.onUiMediaPlayerInfoBufferingEnd();
            }
        }

        @Override
        public void onMediaPlayerError() {
//            setPlayError(true);
            if (playUiListener != null) {
                playUiListener.onUiMediaPlayerError();
            }
        }
    };

    public void setPlayUiListener(PlayUiListener playUiListener) {
        this.playUiListener = playUiListener;
    }


    /**
     * 是否手动seek缓冲完成
     *
     * @return the manualSeekfinished
     */
    synchronized public boolean isSeekFinished() {
        return seekFinished;
    }

    /**
     * 设置手动seek缓冲完成
     *
     * @param seekFinished the manualSeekfinished to set
     */
    synchronized public void setSeekFinished(boolean seekFinished) {
        this.seekFinished = seekFinished;
    }


    /* =============数据处理区=============*/
    public boolean isLastSong() {
        return mvs != null && currentPlayIndex == mvs.size() - 1;
    }

    public boolean isFirstSong() {
        return currentPlayIndex == 0;
    }

    public String next() {
        if (mvs == null || mvs.isEmpty()) {
            return "";
        }
        if ((currentPlayIndex + 1) >= 0 && (currentPlayIndex + 1) < mvs.size()) {
            currentPlayIndex++;
        } else {
            currentPlayIndex = 0;
        }
        return getPlayUrl(mvs.get(currentPlayIndex));
    }

    public String previous() {
        if (mvs == null || mvs.isEmpty()) {
            return "";
        }
        if ((currentPlayIndex - 1) >= 0 && (currentPlayIndex - 1) < mvs.size()) {
            currentPlayIndex--;
        } else {
            currentPlayIndex = mvs.size() - 1;
        }
        return getPlayUrl(mvs.get(currentPlayIndex));
    }

    public String random() {
        if (mvs == null) {
            return "";
        }
        Random random = new Random();
        currentPlayIndex = random.nextInt(mvs.size());
        return getPlayUrl(mvs.get(currentPlayIndex));
    }

    public MvInfo getCurrentPlayMv() {
        if (mvs != null && currentPlayIndex < mvs.size()) {
            return mvs.get(currentPlayIndex);
        } else {
            return null;
        }
    }

    public String getCurrentPlayMvUrl() {
        if (mvs != null && currentPlayIndex < mvs.size()) {
            return getPlayUrl(mvs.get(currentPlayIndex));
        } else {
            return "";
        }
    }


    public String getPlayUrl(MvInfo mvInfo) {
        String url = mvInfo.getPlayUrl();
        return url;
    }


    public List<MvInfo> getMvs() {
        return mvs;
    }

    public void setMvs(List<MvInfo> mvs) {
        this.mvs = mvs;
    }

    public int getCurrentPlayIndex() {
        return currentPlayIndex;
    }

    public void setCurrentPlayIndex(int currentPlayIndex) {
        this.currentPlayIndex = currentPlayIndex;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWillPlayIndex() {
        return willPlayIndex;
    }

    public void setWillPlayIndex(int willPlayIndex) {
        this.willPlayIndex = willPlayIndex;
    }

}