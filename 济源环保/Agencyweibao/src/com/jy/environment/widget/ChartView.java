package com.jy.environment.widget;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class ChartView extends SurfaceView implements Callback{
	
	private final Handler mHandler = new Handler();
	private int [] mVizData = new int[128];
	private final Paint mPaint = new Paint();
	private int[] Audio_int;
	
	private static final int frequency = 1000 / 25;
	
	int mWidth = 0;
	int mCenterY = 0;
	
	private final Runnable mDrawCube = new Runnable() {
        public void run() {
            drawFrame();
        }
    };
    
    void drawFrame() {
        final SurfaceHolder holder = getHolder();
        final Rect frame = holder.getSurfaceFrame();
        final int width = frame.width();
        final int height = frame.height();

        Canvas c = null;
        try {
            c = holder.lockCanvas();
            if (c != null) {
                // draw something
                drawCube(c);
            }
        } finally {
            if (c != null) holder.unlockCanvasAndPost(c);
        }

        mHandler.removeCallbacks(mDrawCube);
        mHandler.postDelayed(mDrawCube, frequency);
    }

    public void refresh(int[] bigDialDegrees) {
    	mDrawCube.run();
    	this.Audio_int = bigDialDegrees;
        }
	public ChartView(Context context,AttributeSet attrs) {
		super(context);
		// TODO Auto-generated constructor stub
/*		mAudioCapture = new AudioCapture(AudioCapture.TYPE_PCM, 1024);
		mAudioCapture.start();*/
		mPaint.setColor(0xffffffff);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(2);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStyle(Paint.Style.STROKE);
		getHolder().addCallback(this);
	}
	
	void drawCube(Canvas c) {
        c.save();
        c.drawColor(0xff000000);

       // mVizData = Audio_int;

        for (int i = 0; i < 1024; i++) {
            c.drawPoint(i, mCenterY + Audio_int[i], mPaint);
            c.drawLine(i, mCenterY, i, mCenterY + Audio_int[i], mPaint);
        }
        c.restore();
    }


	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		mCenterY = height / 2;
		mWidth = width;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		drawFrame();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}