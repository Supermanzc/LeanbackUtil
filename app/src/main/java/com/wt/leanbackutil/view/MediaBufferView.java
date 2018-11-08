package com.wt.leanbackutil.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.TrafficStats;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wt.leanbackutil.R;

/**
 * 视频播放缓冲样式
 */
public class MediaBufferView extends RelativeLayout implements Runnable {

    /**
     * 圆的半径
     */
    private int radius;

    /**
     *  圆周宽度
     */
    private int ringWidth;

    /**
     *  圆周颜色
     */
    private int circleColor;

    /**
     * 圆周颜色
     */
    private int ringColor;

    /**
     * 当前速度文字大小
     */
    private int speedTextSize;

    /**
     * 当前速度文字颜色
     */
    private int speedTextColor;

    /**
     * 进度缓冲颜色
     */
    private int progressColor;

    /**
     * 当前加载提示文字颜色
     */
    private int loadLabelTextColor;

    /**
     * 进度缓冲文字大小
     */
    private int loadLabelTextSize;

    /**
     * 当前速度文字颜色
     */
    private int loadTextColor;

    /**
     * 进度缓冲文字大小
     */
    private int loadTextSize;

    private Circle circle;

    private TextView loadText;

    /**
     * 当前总流量信息
     */
    private long flowBytes;

    public MediaBufferView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.buffer);
        try {
            circleColor = a.getColor(R.styleable.buffer_circleColor, Color.TRANSPARENT);
            radius = a.getDimensionPixelSize(R.styleable.buffer_circleRadius, getResources().getDimensionPixelSize(R.dimen.play_loading_radius));
            ringWidth = a.getDimensionPixelSize(R.styleable.buffer_ringWidth, getResources().getDimensionPixelSize(R.dimen.play_loading_ring_width));
            ringColor = a.getColor(R.styleable.buffer_ringColor, getResources().getColor(R.color.color_green));
            progressColor = a.getColor(R.styleable.buffer_innerColor, getResources().getColor(R.color.color_green));

            speedTextSize = a.getDimensionPixelSize(R.styleable.buffer_speedTextSize, getResources().getDimensionPixelSize(R.dimen.play_loading_label_text_size));
            speedTextColor = a.getColor(R.styleable.buffer_speedTextColor, getResources().getColor(R.color.white));
            loadLabelTextColor = a.getColor(R.styleable.buffer_loadLabelTextColor, getResources().getColor(R.color.white));
            loadLabelTextSize = a.getDimensionPixelSize(R.styleable.buffer_loadLabelTextSize, getResources().getDimensionPixelSize(R.dimen.play_loading_label_text_size));
            loadTextColor = a.getColor(R.styleable.buffer_loadTextColor, getResources().getColor(R.color.white));
            loadTextSize = a.getDimensionPixelSize(R.styleable.buffer_loadTextSize, getResources().getDimensionPixelSize(R.dimen.play_loading_label_text_size));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }
        initComponent();
    }

    public MediaBufferView(Context context) {
        super(context);
    }

    /**
     * 获取当前网络总流量
     *
     * @return 速度
     */
    private long getByte() {
        return TrafficStats.getTotalRxBytes() / 1024;
    }

    @Override
    public void run() {
        long bytes = getByte();
        if (flowBytes != 0) {
            long speed = bytes - flowBytes;
            circle.setText(speed);
        }
        // 剔除异常网速
        if (flowBytes != 0) {
            postDelayed(this, 1000);
        }
        flowBytes = bytes;
    }

    /**
     * 初始化组件
     */
    @SuppressLint("ResourceType")
    private void initComponent() {
        // 添加圆
        circle = new Circle(getContext());
        LayoutParams p = new LayoutParams((radius * 2 + 10), (radius * 2 + 10));
        p.addRule(CENTER_HORIZONTAL);
        circle.setLayoutParams(p);
        circle.setId(0x11);
        addView(circle);

        // 添加文字提示
        LayoutParams labelP = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        labelP.addRule(BELOW, 0x11);

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(labelP);
        addView(linearLayout);

        TextView textLabel = new TextView(getContext());
        textLabel.setText("正在加载：");
        textLabel.setTextColor(loadLabelTextColor);
        textLabel.setTextSize(loadLabelTextSize);
        textLabel.setVisibility(View.GONE);
        linearLayout.addView(textLabel);

        loadText = new TextView(getContext());
        loadText.setTextColor(loadTextColor);
        loadText.setTextSize(loadTextSize);
        linearLayout.addView(loadText);

    }

    /**
     * 设置正在加载的信息
     *
     * @param str 加载信息
     */
    private void setLoadText(String str) {
        loadText.setText(str);
    }

    /**
     * 显示
     *
     * @param isShowCircle 显示进度
     * @param info         显示信息
     */
    public void show(String info, boolean isShowCircle) {
        circle.reset();
        if (isShowCircle) {
            //初始化，避免显示的时候第一次更新的网速为nM/s
            flowBytes = getByte();
            circle.setVisibility(View.VISIBLE);
        } else {
            circle.setVisibility(View.GONE);
        }

        setLoadText(info);
        this.setVisibility(View.VISIBLE);
        post(this);
    }

    /**
     * 隐藏
     */
    public void hide() {
        circle.reset();
        removeCallbacks(this);
        this.setVisibility(View.GONE);
    }

    /**
     * 缓冲圆
     */
    private class Circle extends View {

        private Paint circlePaint;

        private Paint ringPaint;

        private Paint bufferPaint;

        private Paint textPaint;

        private RectF rect;

        private String text = "0kb/s";

        private float progress = 0f;

        public Circle(Context context) {
            super(context);
            init();

        }

        private void init() {
            // 画圆
            circlePaint = new Paint();
            circlePaint.setColor(circleColor);
            circlePaint.setAntiAlias(true);
            circlePaint.setStyle(Paint.Style.FILL);

            // 画圆周
            ringPaint = new Paint();
            ringPaint.setColor(ringColor);
            ringPaint.setAntiAlias(true);
            ringPaint.setStyle(Paint.Style.STROKE);
            ringPaint.setStrokeWidth(ringWidth);

            // 缓冲进度
            bufferPaint = new Paint();
            bufferPaint.setColor(progressColor);
            bufferPaint.setAlpha(180);
            bufferPaint.setAntiAlias(true);
            bufferPaint.setStyle(Paint.Style.FILL);


            rect = new RectF();

            textPaint = new Paint();
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setColor(speedTextColor);
            textPaint.setTextSize(speedTextSize);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            float centerX = getWidth() / 2.0f;
            float centerY = getHeight() / 2.0f;

            canvas.drawCircle(centerX, centerY, radius, circlePaint);
            canvas.drawCircle(centerX, centerY, radius, ringPaint);

            rect.left = centerX - radius + ringWidth / 2.0f;
            rect.top = centerY - radius + ringWidth / 2.0f;
            rect.right = centerX + radius - ringWidth / 2.0f;
            rect.bottom = centerY + radius - ringWidth / 2.0f;

            float progressAngle = 360 * (progress / 100.0f);
            canvas.drawArc(rect, -90, progressAngle, true, bufferPaint);

            float textWidth = textPaint.measureText(text);
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float textHeight = metrics.descent - metrics.ascent;
            canvas.drawText(text, centerX - textWidth / 2, centerY + textHeight / 4, textPaint);

        }

        /**
         * 设置当前速度
         *
         * @param speed 加载速度
         */
        public void setText(long speed) {
            if (speed > 1024) {
                String speedStr = speed / 1024.0f + "";
                text = speedStr.substring(0, speedStr.indexOf(".")) + speedStr.substring(speedStr.indexOf("."), speedStr.indexOf(".") + 2) + " mb/s";
            } else {
                text = speed + " kb/s";
            }

            // 速度大于0
            if (speed > 0) {
                float increment;
                if (progress <= 25) {
                    increment = speed / 100.0f;
                } else if (progress <= 50) {
                    increment = speed / 200.0f;
                } else {
                    increment = speed / 400.0f;
                }
                progress += increment;
                if (progress >= 100) {
                    progress = 99.5f;
                }
            }

            invalidate();
        }

        public void reset() {
            this.text = "0kb/s";
            this.progress = 0.0f;
            this.invalidate();
        }

    }

}