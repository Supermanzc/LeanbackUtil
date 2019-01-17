package com.wt.leanbackutil;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.wt.leanbackutil.util.LogUtil;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaDiscoverer;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.media.VideoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author junyan
 *         vlc 播放视频
 */

public class PlayVlcActivity extends Activity implements SurfaceHolder.Callback {

    @BindView(R.id.player_view)
    SurfaceView surfaceView;

    private MediaPlayer mediaPlayer = null;
    private LibVLC libVLC;

    private SurfaceHolder holder = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlc_play);
        ButterKnife.bind(this);

        // 让屏幕保持高亮状态.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SurfaceHolder holder = surfaceView.getHolder();
        holder.setFormat(PixelFormat.RGBX_8888);
        holder.addCallback(this);
        surfaceView.setKeepScreenOn(true);

        initMediaPlayer();
    }

    private void initMediaPlayer() {
        ArrayList<String> options = new ArrayList<>();
        libVLC = new LibVLC(getApplicationContext(), options);
    }

    private void playUrl(String url) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer(libVLC);
//        mediaPlayer.getVLCVout().setVideoSurface(surfaceView.getHolder().getSurface(), holder);
        mediaPlayer.getVLCVout().setVideoView(surfaceView);
        mediaPlayer.getVLCVout().attachViews();

        Media media = new Media(libVLC, Uri.parse(url));

        mediaPlayer.setMedia(media);
        mediaPlayer.play();

        mediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
//                LogUtil.e("event -----------" + event.getEsChangedType());
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
        playUrl("http://110.185.117.22/vcloud1049.tc.qq.com/1049_M0120000003SKN5L3mMzOH1001599820.f20.mp4?vkey=5C461A836A6114DA73ECDDB40F8262623AC15B6F0E80EE49B45DAA47F09E30507F5A0381CBC5BCB91C741AF8359BFA54570073B2B0ACF021FCE40FC395B6C800F4A95E77FB45B8300CC6ABFB74F2E64D1A9EB77C3DD3F24D&stdfrom=1");
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
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
