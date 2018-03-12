package com.jy.environment.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.jy.environment.R;
import com.jy.environment.activity.KongqizhiliangyubaoActivity;
import com.jy.environment.util.MyLog;

public class HistogramView extends View {

	private Paint xLinePaint;// 坐标轴 轴线 画笔：
	private Paint hLinePaint;// 坐标轴水平内部 虚线画笔
	private Paint titlePaint;// 绘制文本的画笔
	private Paint paint;// 矩形画笔 柱状图的样式信息
	private List<Integer> progress = new ArrayList<Integer>();// 7 条
	private List<Integer> aniProgress = new ArrayList<Integer>();// 实现动画的值
	private final int TRUE = 1;// 在柱状图上显示数字
	// int width = getWidth();
	// int height = getHeight() - 200;
	// int heightEver = height / 8;

	// private int[] text;
	// 坐标轴左侧的数标
	// private String[] ySteps;
	// 坐标轴底部的星期数
	private List<String> xWeeks = new ArrayList<String>();

	private HistogramAnimation ani;
	private int width, height, heightEver;
	private String[] dengji = new String[] { "优", "良", "轻度", "中度", "重度", "严重" };

	public HistogramView(Context context) {
		super(context);
		init(context, null);
	}

	public HistogramView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		height = View.MeasureSpec.getSize(heightMeasureSpec);
		width = View.MeasureSpec.getSize(widthMeasureSpec);
		MyLog.i(">>>>>>>>>>>>>>>>>>shhgghghg"+KongqizhiliangyubaoActivity.size);
		if (KongqizhiliangyubaoActivity.size == 4) {
			width = 1000;
			heightEver = height / 8;
			setMeasuredDimension(width, height);
		} else if (KongqizhiliangyubaoActivity.size == 5) {
			width = 1100;
			heightEver = height / 8;
			setMeasuredDimension(width, height);
		} else if (KongqizhiliangyubaoActivity.size == 6) {
			width = 1500;
			heightEver = height / 8;
			setMeasuredDimension(width, height);
		} else if (KongqizhiliangyubaoActivity.size == 7) {
			width = 2000;
			heightEver = height / 8;
			setMeasuredDimension(width, height);
		}
		// 这里面是原始的大小，需要重新计算可以修改本行
	}

	private void init(Context context, AttributeSet attrs) {

		// ySteps = new String[] { "10k", "7.5k", "5k", "2.5k", "0" };
		// xWeeks = new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
		// text = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		// aniProgress = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		// aniProgress= new ArrayList<Integer>();
		if (ani == null) {
			try {
				ani = new HistogramAnimation();
				ani.setDuration(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		xLinePaint = new Paint();
		hLinePaint = new Paint();
		titlePaint = new Paint();
		paint = new Paint();

		xLinePaint.setColor(Color.DKGRAY);
		hLinePaint.setColor(Color.LTGRAY);
		titlePaint.setColor(Color.BLACK);
	}

	public void setText(int[] text) {
		this.postInvalidate();// 可以子线程 更新视图的方法调用。
	}

	public void setProgress(List<Integer> progress) {
		this.progress = progress;
		for (int i = 0; i < progress.size(); i++) {
			aniProgress.add(progress.get(i) * 1000);
		}
		if (ani == null) {
			try {
				ani = new HistogramAnimation();
				ani.setDuration(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// this.invalidate(); //失效的意思。
		// this.postInvalidate(); // 可以子线程 更新视图的方法调用。
		this.startAnimation(ani);
		this.postInvalidate();// 可以子线程 更新视图的方法调用。

	}

	public void setWeeks(List<String> setWeeks) {
		this.xWeeks = setWeeks;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		int width = getWidth();
		int height = getHeight() - 200;

		// 1 绘制坐标线：startX, startY, stopX, stopY, paint
		int startX = dip2px(getContext(), 50);
		int startY = dip2px(getContext(), 10);
		int stopX = dip2px(getContext(), 50);
		int stopY = dip2px(getContext(), 320);
		titlePaint.setTextAlign(Align.LEFT);
		titlePaint.setTextSize(20);
		titlePaint.setAntiAlias(true);
		titlePaint.setStyle(Paint.Style.FILL);
		canvas.drawLine(60, 10, 60, height, xLinePaint);

		canvas.drawLine(60, height, width - 60, height, xLinePaint);

		// 2 绘制坐标内部的水平线

		int leftHeight = height - 20;// 左侧外周的 需要划分的高度：

		int hPerHeight = leftHeight / 8;// 分成四部分

		hLinePaint.setTextAlign(Align.CENTER);
		for (int i = 0; i < 8; i++) {
			canvas.drawLine(70, 20 + i * hPerHeight, width - 10, 20 + i
					* hPerHeight, hLinePaint);
			if (i <= 5) {
				canvas.drawText(dengji[i], 10, 20 + (7 - i) * hPerHeight,
						titlePaint);
			}
		}

		// 3 绘制 Y 周坐标

		// titlePaint.setTextAlign(Align.RIGHT);

		// for (int i = 0; i < ySteps.length; i++) {
		// canvas.drawText(ySteps[i], 40, 20 + i * hPerHeight, titlePaint);
		// }

		// 4 绘制 X 周 做坐标
		int xAxisLength = width - 30;
		int columCount = xWeeks.size() + 1;
		int step = xAxisLength / columCount;
		MyLog.i("step1:" + step);
		// if(step < 150){
		// step = 150;
		// }

		MyLog.i("step" + step);
		for (int i = 0; i < columCount - 1; i++) {
			// text, baseX, baseY, textPaint
			if (!"".equals(xWeeks.get(i))) {
				// canvas.drawText(xWeeks.get(i), 55 + step * (i + 1),
				// height+30, titlePaint);
				canvas.drawText(xWeeks.get(i), 30 + step * (i + 1) - 30,
						height + 30, titlePaint);
			}
		}

		// 5 绘制矩形

		try {
			if (aniProgress != null && aniProgress.size() > 0) {
				for (int i = 0; i < aniProgress.size(); i++) {// 循环遍历将7条柱状图形画出来
					int value = aniProgress.get(i);
					paint.setAntiAlias(true);// 抗锯齿效果
					paint.setStyle(Paint.Style.FILL);
					paint.setTextSize(20);// 字体大小
					// paint.setColor(Color.parseColor("#6DCAEC"));// 字体颜色
					paint.setColor(Color.parseColor("#6DCAEC"));// 字体颜色
					Rect rect = new Rect();// 柱状图的形状

					rect.left = 30 + step * (i + 1) - 30;
					rect.right = 30 + step * (i + 1) + 30;
					// int rh = (int) (leftHeight - leftHeight * (value /
					// 10000.0));
					int rh = (int) (leftHeight - (progress.get(i) * hPerHeight));
					rect.top = rh + 20;
					rect.bottom = height;
					Bitmap bitmap;
					switch (progress.get(i)) {
					case 1:
						// bitmap = BitmapFactory.decodeResource(getResources(),
						// R.drawable.zhuzt_air1);
						bitmap = decodeResource(getResources(),
								R.drawable.zhuzt_air1);
						break;
					case 2:
						bitmap = decodeResource(getResources(),
								R.drawable.zhuzt_air2);
						break;
					case 3:
						bitmap = decodeResource(getResources(),
								R.drawable.zhuzt_air3);
						break;
					case 4:
						bitmap = decodeResource(getResources(),
								R.drawable.zhuzt_air4);
						break;
					case 5:
						bitmap = decodeResource(getResources(),
								R.drawable.zhuzt_air5);
						break;
					case 6:
						bitmap = decodeResource(getResources(),
								R.drawable.zhuzt_air6);
						break;

					default:
						bitmap = decodeResource(getResources(),
								R.drawable.zhuzt_air6);
						break;
					}
					canvas.drawBitmap(bitmap, null, rect, paint);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 集成animation的一个动画类
	 * 
	 * @author 李垭超
	 * 
	 */
	private class HistogramAnimation extends Animation {
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			try {
				if (interpolatedTime < 1.0f) {
					for (int i = 0; i < aniProgress.size(); i++) {
						aniProgress
								.set(i,
										(int) ((progress.get(i) * 1000) * interpolatedTime));
					}
				} else {
					for (int i = 0; i < aniProgress.size(); i++) {
						aniProgress.set(i, (int) (progress.get(i) * 1000));
					}
				}
				postInvalidate();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private Bitmap decodeResource(Resources resources, int id) {
		TypedValue value = new TypedValue();
		resources.openRawResource(id, value);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inTargetDensity = value.density;
		return BitmapFactory.decodeResource(resources, id, opts);
	}
}