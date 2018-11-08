package com.wt.leanbackutil.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wt.leanbackutil.R;
import com.wt.leanbackutil.util.DateUtil;

import java.lang.ref.WeakReference;

/**
 * 快进快退弹窗
 */
public class FastForwardRewindView extends RelativeLayout {

    private final int WAIT_TIME = 5000;
    private final Context context;
    /**
     * 进度条
     */
    private CustomMediaSeekBar mediaSeekBar;
    /**
     * 当前播放的时间
     */
    private TextView currentTime;
    /**
     * 总时长
     */
    private TextView totalTime;
    private ImageView fastDirectionIcon;


    /**
     * 获取自动隐藏信号的handler
     */
    private MyHandler hideHandler;
    /**
     * 自动隐藏的线程
     */
    private Runnable hideTask;
    /**
     * 自动隐藏的动画
     */
    private Animation hideAnimation;

    /**
     * seek时间
     */
    private int t;
    /**
     * seek初速度
     */
    private final int v = 500;


    public FastForwardRewindView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initComponents();
    }

    public FastForwardRewindView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initComponents();
    }

    /**
     * 初始化各个控件
     */
    private void initComponents() {
        RelativeLayout.inflate(context, R.layout.fast_forward_rewind_view, this);
        mediaSeekBar = findViewById(R.id.fast_seek_bar);
        Drawable drawable = getResources().getDrawable(R.mipmap.fast_view_point);
        drawable.setBounds(0, 0, getResources().getDimensionPixelSize(R.dimen.w_15),
                getResources().getDimensionPixelSize(R.dimen.h_15));
        mediaSeekBar.setThumb(drawable);
        currentTime = findViewById(R.id.tv_fast_current_time);
        totalTime = findViewById(R.id.tv_fast_total_time);
        fastDirectionIcon = findViewById(R.id.iv_fast_forward_rewind);

        hideAnimation = AnimationUtils.loadAnimation(context, R.anim.video_play_control_bar_out);
        mediaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentTime.setText(DateUtil.getTimeDisplay(progress));
            }
        });

    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            super.setVisibility(visibility);
        } else {
            setAnimation(hideAnimation);
            startAnimation(hideAnimation);
            super.setVisibility(visibility);
        }
    }


    /**
     * 隐藏
     */
    public void hide() {
        setAnimation(hideAnimation);
        startAnimation(hideAnimation);
        stopAutoHide();
        setVisibility(INVISIBLE);
    }

    /**
     * 开始自动隐藏
     */
    private void startAutoHide() {
        stopAutoHide();
        if (null == hideHandler) {
            hideHandler = new MyHandler(this);
        }

        hideTask = new Runnable() {
            @Override
            public void run() {
                hideHandler.sendEmptyMessage(0);
            }
        };

        hideHandler.postDelayed(hideTask, WAIT_TIME);
    }


    private static class MyHandler extends Handler {
        private WeakReference<FastForwardRewindView> wf;

        private MyHandler(FastForwardRewindView playControlBar) {
            wf = new WeakReference<>(playControlBar);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FastForwardRewindView fastView = wf.get();
            if (fastView != null) {
                fastView.hide();
            }
        }
    }

    /**
     * 停止自动隐藏
     */
    private void stopAutoHide() {
        if (null != hideHandler) {
            hideHandler.removeMessages(0);
            if (null != hideTask) {
                hideHandler.removeCallbacks(hideTask);
            }
        }
    }

    /**
     * 快进操作
     */
    public void fastForward() {
        // 当前时间,毫秒
        int progress = mediaSeekBar.getProgress();
        // 总时长，毫秒
        int max = mediaSeekBar.getMax() - 10;// 保证不能seek到最后

        int st = getSeekTime();
        setSeekTime(++st);

        int perSeekProgress = v * getSeekTime();

        if (progress + perSeekProgress < max) {
            mediaSeekBar.setProgress(progress + perSeekProgress);
        } else {
            mediaSeekBar.setProgress(max);
        }
    }

    /**
     * 快退操作,每次快退总时长的百分之一
     */
    public void rewind() {
        // 当前时间，毫秒
        int progress = mediaSeekBar.getProgress();

        int st = getSeekTime();
        setSeekTime(++st);

        int perSeekProgress = v * getSeekTime();
        if (progress - perSeekProgress >= 0) {
            mediaSeekBar.setProgress(progress - perSeekProgress);
        } else {
            mediaSeekBar.setProgress(0);
        }
    }

    /**
     * 设置seek匀加速运动的时间
     *
     * @param time seek匀加速运动的时间
     */
    public void setSeekTime(int time) {
        t = time;
    }

    /**
     * 获取seek匀加速运动的时间
     *
     * @return seek匀加速运动的时间
     */
    private int getSeekTime() {
        return t;
    }

    /**
     * 更新总长度
     *
     * @param duration
     */
    public void updateDuration(int duration) {
        mediaSeekBar.setMax(duration);
        totalTime.setText(DateUtil.getTimeDisplay(duration));
    }

    /**
     * 返回进度条当前位置
     *
     * @return 进度条当前位置
     */
    public int getCurrentProgress() {
        return mediaSeekBar.getProgress();
    }

    /**
     * 更新进度条的进度
     *
     * @param progress       当前进度
     * @param secondProgress 缓冲进度
     */
    public void updateMediaSeekBar(int progress, int secondProgress) {
        mediaSeekBar.setProgress(progress);
        mediaSeekBar.setSecondaryProgress(progress);
        mediaSeekBar.postInvalidate();


    }


    public void setFastForwardIcon() {
        fastDirectionIcon.setImageResource(R.mipmap.fast_view_forward);
    }

    public void setFastRewindIcon() {
        fastDirectionIcon.setImageResource(R.mipmap.fast_view_rewind);
    }
}