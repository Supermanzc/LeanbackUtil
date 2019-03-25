package com.wt.leanbackutil.leankback.presenter;

import android.graphics.SurfaceTexture;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.open.leanback.widget.ArrayObjectAdapter;
import com.open.leanback.widget.ListRow;
import com.open.leanback.widget.RowPresenter;
import com.open.leanback.widget.VerticalGridView;
import com.wt.leanbackutil.R;
import com.wt.leanbackutil.model.SingItem;
import com.wt.leanbackutil.player.PlayMvManager;
import com.wt.leanbackutil.player.PlayUiListener;
import com.wt.leanbackutil.util.FrescoUtil;
import com.wt.leanbackutil.util.LogUtil;
import com.wt.leanbackutil.util.ViewUtils;
import com.wt.leanbackutil.view.MediaBufferView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * @author junyan
 *         视频播放框的页面
 */

public class ConcertTexturePresenter extends RowPresenter implements TextureView.SurfaceTextureListener {

    @BindViews({R.id.item_1, R.id.item_2, R.id.item_3, R.id.item_4})
    LinearLayout[] views;
    @BindView(R.id.surface_view)
    TextureView surfaceView;
    @BindView(R.id.loading_view)
    MediaBufferView mLoadingView;
    @BindView(R.id.surface_image_view)
    SimpleDraweeView surfaceImageView;

    private ViewHolder holder;
    private VerticalGridView verticalGridView;
    private PlayMvManager playMvManager;

    @Override
    protected ViewHolder createRowViewHolder(ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.concert_view_texture,
                parent, false);
        ButterKnife.bind(this, view);
        playMvManager = PlayMvManager.getInstance();
        playMvManager.init();
        playMvManager.setLooping(true);
        holder = new ViewHolder(view);

        surfaceView.setSurfaceTextureListener(this);
        verticalGridView = (VerticalGridView) parent;
        surfaceImageView.setVisibility(View.VISIBLE);
        verticalGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LogUtil.e("onScrollStateChanged-------------" + surfaceView.isShown()
                            + "   verticalGridView.getSelectedPosition()" + verticalGridView.getSelectedPosition());
                    if (surfaceView.isShown() && verticalGridView.getSelectedPosition() == 3) {
//                        surfaceImageView.setVisibility(View.GONE);
                        surfaceImageView.setImageResource(0);
                        //开始处理播放器数据
                        playMvManager.setDataSourceToEngine("http://kltvvod.clouddianshi.com/otv/yfy/F/F0/BB/00001881113/index.m3u8?k=8c96b63aa3ce45e908fdce483a624489&t=5C98815D&UID=5295eb13-8307-44be-b83f-bd0be1be78f2");
                        playMvManager.setPlayUiListener(new PlayUiListener() {
                            @Override
                            public void onUiMediaPlayerPrepared() {
                                mLoadingView.hide();
                            }

                            @Override
                            public void onUiMediaPlayerInfoBufferingStart() {
                                mLoadingView.show("正在缓冲", true);
                            }

                            @Override
                            public void onUiMediaPlayerInfoBufferingEnd() {
                                mLoadingView.hide();
                            }

                            @Override
                            public void onUiMediaPlayerError() {
                                mLoadingView.hide();
                            }

                            @Override
                            public void onUiMediaPlayerPlayComplete() {
                            }
                        });
                        mLoadingView.show("正在缓冲", true);
                    } else {
                        playMvManager.pause();
                        surfaceImageView.setVisibility(View.VISIBLE);
//                        FrescoUtil.getInstance().loadImage(surfaceImageView, singItems.get(0).getAlbum_pic(), FrescoUtil.TYPE_ONE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return this.holder;
    }

    @Override
    protected void onBindRowViewHolder(ViewHolder vh, Object item) {
        super.onBindRowViewHolder(vh, item);
        ListRow listRow = (ListRow) item;
        ArrayObjectAdapter arrayObjectAdapter = (ArrayObjectAdapter) listRow.getAdapter();
        int size = arrayObjectAdapter.size();
        List<SingItem> singItems = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            SingItem singItem = (SingItem) arrayObjectAdapter.get(i);
            singItems.add(singItem);
        }
        initData(singItems);
    }

    @Override
    protected void onUnbindRowViewHolder(ViewHolder vh) {
        super.onUnbindRowViewHolder(vh);
        LogUtil.e("onUnbindRowViewHolder--------------vh=" + vh);
    }

    private void initData(List<SingItem> singItems) {
        for (int i = 0; i < views.length; i++) {
            if (i > singItems.size() - 2) {
                views[i].setVisibility(View.GONE);
            } else {
                SingItem singItem = singItems.get(i + 1);
                views[i].setVisibility(View.VISIBLE);
                TextView titleView = views[i].findViewById(R.id.title_view);
                TextView descriptionView = views[i].findViewById(R.id.description_view);
                SimpleDraweeView imageView = views[i].findViewById(R.id.img_view);
                ViewUtils.onFocus(imageView);
                views[i].setFocusable(true);
                titleView.setText(singItem.getSong_name());
                descriptionView.setText(singItem.getSinger_name());
                FrescoUtil.getInstance().loadImage(imageView, singItem.getAlbum_pic(), FrescoUtil.TYPE_ONE);
                views[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            v.bringToFront();
                        }
                        ViewUtils.scaleView(v, hasFocus);
                    }
                });
            }
        }
        FrescoUtil.getInstance().loadImage(surfaceImageView, singItems.get(0).getAlbum_pic(), FrescoUtil.TYPE_ONE);


        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) surfaceImageView.getLayoutParams();
        layoutParams.leftMargin = -surfaceImageView.getPaddingLeft();
        layoutParams.rightMargin = -surfaceImageView.getPaddingRight();
        layoutParams.bottomMargin = -surfaceImageView.getPaddingBottom();
        layoutParams.topMargin = -surfaceImageView.getPaddingTop();
        surfaceImageView.setLayoutParams(layoutParams);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        LogUtil.d("onSurfaceTextureAvailable-------------width=" + width + " height=" + height);
        playMvManager.setSurface(new Surface(surface));
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        LogUtil.d("onSurfaceTextureSizeChanged-------------width=" + width + " height=" + height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        LogUtil.d("onSurfaceTextureDestroyed-------------");
        playMvManager.pause();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        LogUtil.d("onSurfaceTextureUpdated-------------");
    }
}
