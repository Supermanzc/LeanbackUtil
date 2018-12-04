package com.wt.leanbackutil.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.SurfaceHolder;

import com.wt.leanbackutil.App;

import java.io.IOException;

import hk.reco.baselib.util.Logger;

/**
 * 播放引擎控制模块
 */
public class PlayEngine implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnVideoSizeChangedListener, AudioManager.OnAudioFocusChangeListener {
    private static final String TAG = PlayEngine.class.getName();
    private boolean isMv = true;

    private MediaPlayer mediaPlayer;
    private PlayEngineListener playEngineListener;

    private PlayState playState = PlayState.IDLE;

    /**
     * 是否处于缓冲状态
     */
    private boolean isBuffering = false;
    /**
     * 预缓冲
     */
    private int bufferProgress;

    private SurfaceHolder holder;

    public PlayEngine() {
        init();
    }

    private void init() {
        mediaPlayer = new MediaPlayer();
        if (isMv) {
            mediaPlayer.setDisplay(holder);
        }
        mediaPlayer.setWakeMode(App.getInstance().getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setScreenOnWhilePlaying(true);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnVideoSizeChangedListener(this);
        setPlayState(PlayState.IDLE);
    }

    /**
     * 设置播放url
     *
     * @param path 播放url
     */
    public synchronized void setDataSource(String path) {
        if (TextUtils.isEmpty(path)) {
            Logger.d(TAG, "path is null:" + path);
            return;
        }
        try {
            Logger.d(TAG, "setDataSource:" + getPlayState());
            if (isMv) {
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    mediaPlayer.release();
                }
                init();
            }
            Logger.d(TAG, "setDataSource:" + path);
            if (mediaPlayer == null) {
                init();
            }
            mediaPlayer.reset();
            setPlayState(PlayState.IDLE);
            mediaPlayer.setDataSource(path);
            setPlayState(PlayState.INITIALIZED);
            mediaPlayer.prepareAsync();
            setPlayState(PlayState.PREPARING);
        } catch (IOException e) {
            Logger.e(TAG, "prepare IOException error" + e.toString());
            e.printStackTrace();
            if (null != playEngineListener) {
                playEngineListener.onMediaPlayerIoError();
            }
        } catch (Exception e) {
            Logger.e(TAG, "prepare Exception error" + e.toString());
            e.printStackTrace();
            if (null != playEngineListener) {
                playEngineListener.onMediaPlayerError();
            }
        }
    }

    /**
     * 释放播放器
     */
    public synchronized void release() {
        if (mediaPlayer != null) {
            Logger.d(TAG, "release");
            this.holder = null;
            mediaPlayer.setDisplay(null);
            mediaPlayer.release();
            mediaPlayer = null;
            setPlayState(PlayState.END);
        }
    }

    /**
     * 开始播放
     */
    synchronized public void start() {
        if (playState == PlayState.STARTED || playState == PlayState.PAUSED
                || playState == PlayState.PREPARED || playState == PlayState.PLAYBACK_COMPLETED) {
            Logger.d(TAG, "start");
            requestAudioFocus();
            mediaPlayer.start();
            setPlayState(PlayState.STARTED);
        }
    }

    /**
     * 暂停播放
     */
    synchronized public void pause() {
        if (playState == PlayState.STARTED) {
            Logger.d(TAG, "pause");
            mediaPlayer.pause();
            setPlayState(PlayState.PAUSED);
        }
    }

    /**
     * 停止播放
     */
    synchronized public void stop() {
        if (playState == PlayState.PREPARED || playState == PlayState.STARTED
                || playState == PlayState.PAUSED || playState == PlayState.STOPPED) {
            if (null != mediaPlayer) {
                Logger.d(TAG, "stop");
                this.holder = null;
                mediaPlayer.setDisplay(null);
                mediaPlayer.stop();
            }
            setPlayState(PlayState.STOPPED);
        }
    }

    /**
     * 拖动进度条时更新播放进度
     *
     * @param progress 播放进度
     * @return
     */
    synchronized public boolean seekTo(int progress) {
        if (playState == PlayState.PREPARED || playState == PlayState.STARTED
                || playState == PlayState.PAUSED || playState == PlayState.PLAYBACK_COMPLETED) {
            if (mediaPlayer != null) {
                int duration = getDuration();
                // 如果拖动到最后，就把进度设置为倒数第三秒
                if (progress >= duration - 1000 && duration > 1000) {
                    mediaPlayer.seekTo(duration - 1000);
                    return true;
                } else {
                    mediaPlayer.seekTo(progress);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 注册绘制句柄
     *
     * @param holder 绘制句柄
     */
    synchronized public void setDisplay(SurfaceHolder holder) {
        mediaPlayer.setDisplay(holder);
        this.holder = holder;
    }

    /**
     * 准备就绪
     */
    @Override
    synchronized public void onPrepared(MediaPlayer mp) {
        Logger.d(TAG, "====onPrepared");
        setPlayState(PlayState.PREPARED);
        if (playEngineListener != null) {
            playEngineListener.onMediaPlayerPrepared();
        }

        if (playState == PlayState.STARTED || playState == PlayState.PAUSED
                || playState == PlayState.PREPARED || playState == PlayState.PLAYBACK_COMPLETED) {
            if (mp != null) {
                requestAudioFocus();
                mp.start();
                setPlayState(PlayState.STARTED);

                //荣耀bug，荣耀机顶盒需要这样后，得出的当前播放时间才是正确的。
//                if (BuildConfig.FLAVOR.equals(Cst.CHANNEL_HONOR)) {
//                    if (isMv) {
//                        mp.seekTo(0);
//                    }
//                }
                if (playEngineListener != null) {
                    playEngineListener.onMediaPlayerStart();
                }

            }
        }

    }

    /**
     * 播放完成并通知界面
     */
    @Override
    synchronized public void onCompletion(MediaPlayer mp) {
        Logger.d(TAG, "===onCompletion");
        // 避免有两次回调
        if (PlayState.PLAYBACK_COMPLETED == getPlayState()) {
            return;
        }
        setPlayState(PlayState.PLAYBACK_COMPLETED);
        if (playEngineListener != null) {
            playEngineListener.onMediaPlayerPlayComplete();
        }
    }


    /**
     * 文件Seek完成
     */
    @Override
    synchronized public void onSeekComplete(MediaPlayer mp) {
        if (mp != null) {
            mp.start();
            setPlayState(PlayState.STARTED);
            if (playEngineListener != null) {
                playEngineListener.onMediaPlayerSeekCompleted();
            }
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    }

    /**
     * 获取一般播放信息并发送给界面
     */
    @Override
    synchronized public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Logger.d(TAG, "====onInfo, what:" + what + "  extra:" + extra);
        switch (what) {
            case 32773:
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                setBuffering(true);
                if (playEngineListener != null) {
                    playEngineListener.onMediaPlayerInfoBufferingStart();
                }
                break;
            case 32774:
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                setBuffering(false);
                if (playEngineListener != null) {
                    playEngineListener.onMediaPlayerInfoBufferingEnd();
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    synchronized public void onBufferingUpdate(MediaPlayer mp, int percent) {
        setBuffer(percent);
    }

    /**
     * 获取播放错误信息并发送给界面
     */
    @Override
    synchronized public boolean onError(MediaPlayer mp, int what, int extra) {
        setPlayState(PlayState.ERROR);
        if (MediaPlayer.MEDIA_ERROR_UNKNOWN == what) {
        } else if (MediaPlayer.MEDIA_ERROR_SERVER_DIED == what) {
        } else {
        }
        Logger.d(TAG, "onError, what:" + what + "  extra:" + extra);
        if (playEngineListener != null) {
            playEngineListener.onMediaPlayerError();
        }
        mp.reset();
        setPlayState(PlayState.IDLE);
        return true;
    }


    /**
     * 获取总时长
     *
     * @return 总时长
     */
    synchronized public int getDuration() {
        int duration = 0;
        if (playState == PlayState.PREPARED || playState == PlayState.STARTED
                || playState == PlayState.PAUSED) {
            duration = mediaPlayer.getDuration();
        }
        return duration;
    }

    /**
     * 获取当前进度
     *
     * @return 当前进度
     */
    synchronized public int getCurrentPosition() {
        int position = 0;
        if (playState == PlayState.PREPARED || playState == PlayState.STARTED || playState == PlayState.PAUSED) {
            position = mediaPlayer.getCurrentPosition();
        } else if (playState == PlayState.PLAYBACK_COMPLETED) {
//            position = getDuration();
        }
        return position;
    }

    /**
     * 设置当前播放器状态
     *
     * @param state 播放状态
     */
    synchronized private void setPlayState(PlayState state) {
        playState = state;
    }

    /**
     * 获取播放状态
     *
     * @return 播放状态
     */
    synchronized public PlayState getPlayState() {
        return playState;
    }

    /**
     * @param playEngineListener the playEngineListener to set
     */
    public void setPlayEngineListener(PlayEngineListener playEngineListener) {
        this.playEngineListener = playEngineListener;
    }

    /**
     * 获取缓冲数据
     *
     * @return 缓冲
     */
    public int getBuffer() {
        return bufferProgress;
    }

    /**
     * 设置缓冲数据
     *
     * @param buffer 缓冲
     */
    private void setBuffer(int buffer) {
        bufferProgress = buffer;
    }


    /**
     * 是否处于缓冲状态
     *
     * @return
     */
    public boolean isBuffering() {
        return isBuffering;
    }

    /**
     * 设置缓冲状态
     *
     * @param is 是否缓冲
     */
    private void setBuffering(boolean is) {
        isBuffering = is;
    }

    public boolean isMv() {
        return isMv;
    }

    public void setMv(boolean mv) {
        isMv = mv;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                Logger.d(TAG, "onAudioFocusChange:AudioManager.AUDIOFOCUS_GAIN");
//                start();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                Logger.d(TAG, "onAudioFocusChange:AudioManager.AUDIOFOCUS_LOSS");
//                release();
                pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                Logger.d(TAG, "onAudioFocusChange:AudioManager.AUDIOFOCUS_LOSS_TRANSIENT");
                pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                Logger.d(TAG, "onAudioFocusChange:AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                break;
            default:
                break;

        }
    }

    private boolean requestAudioFocus() {
        AudioManager audioManager = (AudioManager) App.getInstance().getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // could not get audio focus.
            Logger.d(TAG, "could not get audio focus");
            return false;
        } else {
            Logger.d(TAG, "get audio focus");
            return true;
        }
    }
}