package com.wt.leanbackutil.activity;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.open.leanback.widget.HorizontalGridView;
import com.open.leanback.widget.Presenter;
import com.open.leanback.widget.VerticalGridView;
import com.wt.leanbackutil.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author junyan
 *         播放器
 */

public class TextureActivity extends Activity {

    @BindView(R.id.vertical_grid_view)
    VerticalGridView verticalGridView;

    private String[] datas;
    private TextureAdapter textureAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture);
        ButterKnife.bind(this);
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutAllowed(true, true);
        verticalGridView.getBaseGridViewLayoutManager().setFocusOutSideAllowed(false, false);

        datas = new String[]{"1", "2", "3", "4", "5", "6"};
        textureAdapter = new TextureAdapter(datas);
        textureAdapter.setStatusCallBackListener(new StatusCallBack() {
            @Override
            public void programBar(ProgressBar mProgressBar) {

            }

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

            }
        });
        verticalGridView.setAdapter(textureAdapter);
    }

    public class TextureAdapter extends RecyclerView.Adapter implements TextureView.SurfaceTextureListener {

        private String[] datas;

        private StatusCallBack statusCallBack;

        public TextureAdapter(String[] datas) {
            super();
            this.datas = datas;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            if(viewType == 1){
                holder = new ViewHeaderHolder(View.inflate(parent.getContext(), R.layout.item_texture, null));
            }else{
                holder = new ViewHeaderHolder2(View.inflate(parent.getContext(), R.layout.item_texture_2, null));
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder.getItemViewType() == 1){
                ViewHeaderHolder viewHeaderHolder = (ViewHeaderHolder) holder;
                viewHeaderHolder.mSurfaceView.setSurfaceTextureListener(this);
                if(statusCallBack != null){
                    statusCallBack.programBar(viewHeaderHolder.mProgressBar);
                }
            }
        }

        @Override
        public int getItemCount() {
            return datas.length;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 1 : 2;
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            if(statusCallBack != null){
                statusCallBack.onSurfaceTextureAvailable(surface, width, height);
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }

        public void setStatusCallBackListener(StatusCallBack statusCallBackListener){
            this.statusCallBack = statusCallBackListener;
        }
    }

    public interface StatusCallBack{
        public void programBar(ProgressBar mProgressBar);
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height);
    }

    public  class ViewHeaderHolder extends RecyclerView.ViewHolder {
        public ImageView ivOne;
        public ImageView ivTwo;

        public TextureView mSurfaceView;
        public ProgressBar mProgressBar;
        public RelativeLayout media_container;
        public RelativeLayout rlOne;
        public RelativeLayout rlTwo;
        public View viewOne;
        public View viewTwo;


        public ViewHeaderHolder(View itemView) {
            super(itemView);
            mSurfaceView = itemView.findViewById(R.id.surface);
            mProgressBar = itemView.findViewById(R.id.progressBar);
            media_container = itemView.findViewById(R.id.media_container);
            ivOne = itemView.findViewById(R.id.ivOne);
            ivTwo = itemView.findViewById(R.id.ivTwo);
            rlOne = itemView.findViewById(R.id.rlOne);
            rlTwo = itemView.findViewById(R.id.rlTwo);
            viewOne = itemView.findViewById(R.id.viewOne);
            viewTwo = itemView.findViewById(R.id.viewTwo);
            // mSurfaceView.setSurfaceTextureListener(this);
        }
    }

    public  class ViewHeaderHolder2 extends RecyclerView.ViewHolder {
        public ImageView ivOne;
        public ImageView ivTwo;

        public ProgressBar mProgressBar;
        public RelativeLayout media_container;
        public RelativeLayout rlOne;
        public RelativeLayout rlTwo;
        public View viewOne;
        public View viewTwo;


        public ViewHeaderHolder2(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.progressBar);
            media_container = itemView.findViewById(R.id.media_container);
            ivOne = itemView.findViewById(R.id.ivOne);
            ivTwo = itemView.findViewById(R.id.ivTwo);
            rlOne = itemView.findViewById(R.id.rlOne);
            rlTwo = itemView.findViewById(R.id.rlTwo);
            viewOne = itemView.findViewById(R.id.viewOne);
            viewTwo = itemView.findViewById(R.id.viewTwo);
            // mSurfaceView.setSurfaceTextureListener(this);
        }
    }
}
