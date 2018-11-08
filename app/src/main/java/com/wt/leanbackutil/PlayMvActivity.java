package com.wt.leanbackutil;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wt.leanbackutil.player.PlayMvManager;
import com.wt.leanbackutil.player.PlayUiListener;
import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.view.MediaBufferView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author junyan
 *         视频播放界面
 */

public class PlayMvActivity extends Activity implements SurfaceHolder.Callback {

    @BindView(R.id.player_view)
    SurfaceView surfaceView;
    @BindView(R.id.loading_view)
    MediaBufferView mLoadingView;

    private PlayMvManager playMvManager;
    private boolean isLongPressKey = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mv);
        ButterKnife.bind(this);

        playMvManager = PlayMvManager.getInstance();
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(this);
        playMvManager.init();
        playMvManager.setPlayUiListener(new PlayUiListener() {
            @Override
            public void onUiMediaPlayerPrepared() {

            }
        });
        mLoadingView.show("正在缓冲", true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //创建surfaceCreated
        playMvManager.setDisplayHolder(holder);
        playMvManager.setSeekFinished(true);
        playMvManager.setDataSourceToEngine("");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        playMvManager.stopPlay();
        playMvManager.destory();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (event.getRepeatCount() == 0) {
                LogUtil.d("KEYCODE_DPAD_LEFT------------down");
                //这个必须的开启
                event.startTracking();
                isLongPressKey = false;
                return super.onKeyDown(keyCode, event);
            } else {
                isLongPressKey = true;
                LogUtil.d("KEYCODE_DPAD_LEFT------------" + event.getRepeatCount());
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            //这里就可以表示长按事件
            if (isLongPressKey) {
                LogUtil.d("KEYCODE_DPAD_LEFT------------up");
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
