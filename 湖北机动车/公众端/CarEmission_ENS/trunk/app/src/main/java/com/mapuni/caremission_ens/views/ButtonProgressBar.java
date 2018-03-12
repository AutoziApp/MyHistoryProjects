package com.mapuni.caremission_ens.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.mapuni.caremission_ens.R;


public class ButtonProgressBar extends ProgressBar {


    private static final int DEFAULT_TEXT_COLOR = 0xff1abd9b;
    private static final int DEFAULT_COLOR_UNREACHED_COLOR = 0xffd3d6da;
    private static final int DEFAULT_HEIGHT_REACHED_PROGRESS_BAR = 4;
    private static final int DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR = 4;

    /**
     * 三角形的状态
     */
    private Status mStatus = Status.End;

    /**
     * 初始化画笔
     */
    protected Paint mPaint = new Paint();

    /**
     * 有进度 进度条的高度
     */
    protected int mReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_REACHED_PROGRESS_BAR);

    /**
     * 有进度的颜色
     */
    protected int mReachedBarColor = DEFAULT_TEXT_COLOR;

    /**
     * 无进度的颜色
     */
    protected int mUnReachedBarColor = DEFAULT_COLOR_UNREACHED_COLOR;

    /**
     * 无进度 圆形边框的高度
     */
    protected int mUnReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR);

    private int triangleLength;

    private Path mPath;

    private Bitmap arrowBitMap;

    RectF r1;

    /**
     * 圆的半径
     */
    private int mRadius = dp2px(15);


    public ButtonProgressBar(Context context) {
        this(context, null);
    }

    public ButtonProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 初始化自定义属性
         */
        final TypedArray attributes = getContext().obtainStyledAttributes(
                attrs, R.styleable.ButtonCircleProgressBar);

        mReachedBarColor = attributes
                .getColor(
                        R.styleable.ButtonCircleProgressBar_progress_reached_color,
                        getResources().getColor(R.color.myProgress));
        mUnReachedBarColor = attributes
                .getColor(
                        R.styleable.ButtonCircleProgressBar_progress_unreached_color,
                        DEFAULT_COLOR_UNREACHED_COLOR);
        mReachedProgressBarHeight = (int) attributes
                .getDimension(
                        R.styleable.ButtonCircleProgressBar_progress_reached_bar_height,
                        mReachedProgressBarHeight);
        mUnReachedProgressBarHeight = (int) attributes
                .getDimension(
                        R.styleable.ButtonCircleProgressBar_progress_unreached_bar_height,
                        mUnReachedProgressBarHeight);

        mRadius = (int) attributes.getDimension(
                R.styleable.ButtonCircleProgressBar_radius, mRadius);

        attributes.recycle();

        /**
         * 初始化画笔
         */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);




        mPath = new Path();
//        Resources r = getResources();
//        Drawable[] layers = new Drawable[2];
//        layers[0] = r.getDrawable(R.drawable.triangle);
//        Drawable layerDrawable = new LayerDrawable(layers);
//        Drawable drawable = getResources().getDrawable(R.drawable.triangle);
//        arrowBitMap  = drawableToBitmap(drawable);

        /**
         * 初始化三角形
         */
        triangleLength = mRadius;
//        float leftX = (float) ((2 * mRadius - Math.sqrt(3.0) / 2 * triangleLength) / 2);
//        float realX = (float) (leftX + leftX * 0.2);
//        mPath.moveTo(realX, mRadius - (triangleLength / 2));
//        mPath.lineTo(realX, mRadius + (triangleLength / 2));
//        mPath.lineTo((float) (realX + Math.sqrt(3.0) / 2 * triangleLength), mRadius);
//        mPath.lineTo(realX, mRadius - (triangleLength / 2));


        r1 = new RectF();
        r1.left = mRadius-dp2px(3);
        r1.right = mRadius+dp2px(3);
        r1.top = dp2px(7) ;
        r1.bottom = mRadius;


        float a = dp2px(2);
        mPath.moveTo(mRadius/2+a,mRadius);
        mPath.lineTo(mRadius+mRadius/2-a,mRadius);
        mPath.lineTo(mRadius+mRadius/2-a,mRadius+a);
        mPath.lineTo(mRadius+a/2,mRadius+dp2px(8));
        mPath.lineTo(mRadius-a/2,mRadius+dp2px(8));
        mPath.lineTo(mRadius/2+a,mRadius+a);
        mPath.lineTo(mRadius/2+a,mRadius);
    }


    public Status getStatus() {
        return mStatus;
    }

    /**
     * 设置内部三角形的状态
     * @param status
     */
    public void setStatus(Status status) {
        mStatus = status;
        invalidate();
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int paintWidth = Math.max(mReachedProgressBarHeight,
                mUnReachedProgressBarHeight);
        if (heightMode != MeasureSpec.EXACTLY) {

            int exceptHeight = (int) (getPaddingTop() + getPaddingBottom()
                    + mRadius * 2 + paintWidth);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(exceptHeight,
                    MeasureSpec.EXACTLY);
        }
        if (widthMode != MeasureSpec.EXACTLY) {
            int exceptWidth = (int) (getPaddingLeft() + getPaddingRight()
                    + mRadius * 2 + paintWidth);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(exceptWidth,
                    MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        mPaint.setStyle(Paint.Style.STROKE);

        /**
         * 画圆形边框
         */
        mPaint.setColor(mUnReachedBarColor);
        mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

        /**
         * 画进度条
         */
        mPaint.setColor(mReachedBarColor);
        mPaint.setStrokeWidth(mReachedProgressBarHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), -90,
                sweepAngle, false, mPaint);


        if (mStatus == Status.End) {
            /**
             * 三角形
             */
            mPaint.setStyle(Paint.Style.FILL);
//            canvas.draw
//            canvas.drawBitmap(arrowBitMap,0,0,null);

            canvas.drawPath(mPath, mPaint);
            float r = dp2px(1);
            float a = dp2px(2);
            canvas.drawCircle(mRadius/2+a,mRadius+r,r,mPaint);//zuo
            canvas.drawCircle(mRadius+mRadius/2-a,mRadius+r,r,mPaint);//you
            canvas.drawCircle(mRadius,mRadius+dp2px(8),r,mPaint);//xia
            canvas.drawRoundRect( r1,(float) 2.0,(float) 1.0,mPaint);
        } else {
            /**
             * 停止的两条竖线
             */
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(dp2px(4));

            canvas.drawLine(mRadius * 4 / 5, mRadius * 2 / 3, mRadius * 4 / 5, 2 * mRadius * 2 / 3, mPaint);
            canvas.drawLine(2 * mRadius - (mRadius * 4 / 5), mRadius * 2 / 3, 2 * mRadius - (mRadius * 4 / 5), 2 * mRadius * 2 / 3, mPaint);
        }
        canvas.restore();
    }

    /***
     * 进度条内部的状态
     */
    public enum Status {
        End,
        Start
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

}
