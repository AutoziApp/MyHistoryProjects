package com.jy.environment.widget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Shader.TileMode;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.jy.environment.R;
import com.jy.environment.util.MyLog;

public class PathView_PM extends View {
	// 众多月份自定义view
	public static final int Pm25_Month = 0;
	public static final int Pm10_Month = 1;
	public static final int Pmo3_Month = 2;
	public static final int Pmso2_Month = 3;
	public static final int Pmno2_Month = 4;
	public static final int Pmco_Month = 5;

	public String[] days = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" };// DAY_WEEK
	public String[] weeks = { "第一周", "第二周", "第三周", "第四周" }; // WEEK_MONTH
	public String[] mouths = { "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月",
			"九月", "十月", "十一月", "十二月", }; // MONTH_YEAR
	public String[] days_month = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
			"21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };// DAY_MONTH
	// public String[] hours = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
	// "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
	// "21", "22", "23", "24"};
	public String[] hour;
	public ArrayList<Float> xPoint = new ArrayList<Float>();
	public int defaultType = Pm25_Month;
	public String[] defaultDay = new String[31];
	// x,y轴的线条数量
	private int xLineCount = 10;
	private int yLineCount = 24;
	private Paint paintLine, paintPoint, innerpaintPoint, textPaint, linkPaint;
	private int[] data = new int[24];

	// 靠左侧，底部的距离
	private float left;
	private float bottom;
	private float bitmap_top, bitmap_right, bitmap_left, bitmap_bottom;

	// x,y轴上显示的值
	private float xMaxValue, yMaxValue;
	// 间距
	private float xInterval, yInterval;
	private String[] dayMonths;
	int[] colors = new int[31];

	public String[] getDayMonths() {
		return dayMonths;
	}

	public void setDayMonths(String[] dayMonths) {
		this.dayMonths = dayMonths;
		for (int i = 0; i < dayMonths.length; i++) {
			defaultDay[i] = dayMonths[i];
		}
	}

	public PathView_PM(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public PathView_PM(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

	}

	public PathView_PM(Context context) {
		super(context);
		init(context);
	}

	public void setType(int type) {
		defaultType = type;
	}

	private void init(Context cont) {
		// this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		int tsize = (int) this.getResources().getDimension(R.dimen.textPaint);
		int linesize = (int) this.getResources().getDimension(R.dimen.line);
		paintLine = new Paint();
		paintLine.setAntiAlias(true);
		paintLine.setStyle(Paint.Style.STROKE);
		paintLine.setColor(Color.WHITE);
		paintLine.setStrokeWidth(1.5f);

		paintPoint = new Paint();
		paintPoint.setColor(Color.WHITE);
		paintPoint.setFakeBoldText(true);
		paintPoint.setTextSize(tsize);
		paintPoint.setStrokeWidth(7);
		paintPoint.setAntiAlias(true);

		innerpaintPoint = new Paint();
		innerpaintPoint.setColor(Color.WHITE);
		innerpaintPoint.setFakeBoldText(true);
		innerpaintPoint.setStrokeWidth(4);
		innerpaintPoint.setAntiAlias(true);
		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setFakeBoldText(true);

		textPaint.setTextSize(tsize);
		textPaint.setAntiAlias(true);

		linkPaint = new Paint();
		linkPaint.setColor(Color.GREEN);
		linkPaint.setFakeBoldText(true);
		linkPaint.setTextSize(tsize);
		linkPaint.setStrokeWidth(linesize);
		linkPaint.setAntiAlias(true);
		colors[0] = Color.parseColor("#e851ff");
		colors[1] = Color.parseColor("#006cff");
		colors[2] = Color.parseColor("#32b16c");
		colors[3] = Color.parseColor("#f39700");
		colors[4] = Color.parseColor("#e23696");
		colors[5] = Color.parseColor("#84ccc9");
		colors[6] = Color.parseColor("#8fc31f");
		colors[7] = Color.parseColor("#00b7ee");
		colors[8] = Color.parseColor("#ff8593");
		colors[9] = Color.parseColor("#fff45c");
		colors[10] = Color.parseColor("#8f82bc");
		colors[11] = Color.parseColor("#217e00");
		colors[12] = Color.parseColor("#f19149");
		colors[13] = Color.parseColor("#e8375d");
		colors[14] = Color.parseColor("#3dfff4");
		colors[15] = Color.parseColor("#008B8B");
		colors[16] = Color.parseColor("#00CED1");
		colors[17] = Color.parseColor("#00FA9A");
	}

	public int getColorByIndex(int defStyle) {
		if ((defStyle > -1) && (defStyle < 51)) {
			return Color.parseColor("#00FF00");
		} else if (defStyle < 101) {
			return Color.parseColor("#FFFF00");
		} else if (defStyle < 151) {
			return Color.parseColor("#FF7E00");
		} else if (defStyle < 201) {
			return Color.parseColor("#FF0000");
		} else if (defStyle < 301) {
			return Color.parseColor("#A0004C");
		} else {
			return Color.parseColor("#7D0125");
		}
	}

	public void setTextSize(int size) {
		textPaint.setTextSize(size);
	}

	public void setDate(int[] x) {
		data = new int[x.length];
		data = x;
		MyLog.i("setDate");
		// this.postInvalidate();
		invalidate();
	}

	private void calculateLeft() {
		if (null != data && data.length > 0) {
			for (int value : data) {
				float tempLeft = textPaint.measureText(value + "");
				if (tempLeft > left) {
					left = tempLeft + 15;
				}
			}
		} else {
			for (int i = 0; i < 24; i++) {
				data[i] = 0;
			}
		}

		bottom = textPaint.getFontMetrics().descent
				- textPaint.getFontMetrics().ascent;

		switch (defaultType) {
		case Pm25_Month:
		case Pm10_Month:
		case Pmo3_Month:
		case Pmso2_Month:
		case Pmno2_Month:
		case Pmco_Month:
			yLineCount = 31;
			for (int i = 0; i < 31; i++) {
				if (i % 2 == 0) {
					defaultDay[i] = defaultDay[i];
				} else {
					defaultDay[i] = "";
				}
			}
			break;
		}

		if (xMaxValue == 0)
			xMaxValue = this.getWidth() - left;

		if (xInterval == 0)
			xInterval = (this.getHeight() - bottom) / (xLineCount + 1);

		if (yMaxValue == 0)
			yMaxValue = this.getHeight() - bottom;

		if (yInterval == 0)
			yInterval = (this.getWidth() - left) / yLineCount;

	}

	private int getCurrentMonthLastDay() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public void setMaxX(int maxValue, int width) {
		xLineCount = maxValue / width + 1;
		xMaxValue = maxValue;
	}

	public void setMaxY(int maxValue, int height) {
		yLineCount = maxValue / height + 1;
		yMaxValue = maxValue;
	}

	// 设置x方向
	public void setXCount(int maxValue, int count) {
		xMaxValue = maxValue;
		xLineCount = count;// 5
	}

	public void setYCount(int maxValue, int count) {
		yLineCount = count;
		yMaxValue = maxValue;
	}

	/**
	 * 绘制曲线
	 * 
	 * @param canvas
	 */
	private void doDraw(Canvas canvas) {
		MyLog.i("doDraw");

		int circlesize = (int) this.getResources().getDimension(R.dimen.circle);
		int circleSizeInner = (int) this.getResources().getDimension(
				R.dimen.circle_inner);
		int max = data[0];
		for (int i = 0; i < data.length; i++) {
			if (data[i] > max) {
				max = data[i];
			}
		}
		if (max < 1) {
			xMaxValue = 1;
		} else if (max < 2) {
			xMaxValue = 2;
		}
		if (max < 200) {
			xMaxValue = 200;
		} else if (max < 300) {
			xMaxValue = 300;
		} else if (max < 400) {
			xMaxValue = 400;
		} else {
			xMaxValue = 500;
		}
		float sumHeight = xLineCount * xInterval;
		float tempInterval = xMaxValue / sumHeight;
		for (int i = 0; i < data.length; i++) {
			float x = xPoint.get(i);
			float yPotion = sumHeight - data[i] / tempInterval + xInterval;
			// linkPaint.setColor(getColorByIndex(data[i]));
			linkPaint.setColor(Color.parseColor("#FFFFFF"));
			// linkPaint.setStrokeWidth(2);
			if (i != data.length - 1) {
				float nextYPotion = sumHeight - data[i + 1] / tempInterval
						+ xInterval;
				int[] colors = { getColorByIntegerValue(data[i]),
						getColorByIntegerValue(data[i + 1]) };
				LinearGradient linearGradient = new LinearGradient(x + 4,
						yPotion, xPoint.get(i + 1), nextYPotion, colors, null,
						TileMode.REPEAT);
				linkPaint.setShader(linearGradient);
				canvas.drawLine(x + 4, yPotion, xPoint.get(i + 1), nextYPotion,
						linkPaint);
			}

			innerpaintPoint.setColor(paintPoint.getColor());
			innerpaintPoint.setStrokeWidth(10);
			innerpaintPoint.setAlpha(55);
			innerpaintPoint.setStrokeCap(Paint.Cap.ROUND);

			// canvas.drawCircle(x,yPotion,circlesize,paintPoint);
			// canvas.drawCircle(x,yPotion,circlesize-1,innerpaintPoint);
			paintPoint.setAlpha(255);
			// paintPoint.setColor(Color.YELLOW);
			if ((data[i] > -1) && (data[i] < 51)) {
				paintPoint.setColor(Color.parseColor

				("#00FF00"));
			} else if (data[i] < 101) {
				paintPoint.setColor(Color.parseColor("#FFFF00"));
			} else if (data[i] < 151) {
				paintPoint.setColor(Color.parseColor("#FF7E00"));
			} else if (data[i] < 201) {
				paintPoint.setColor(Color.parseColor("#FF0000"));
			} else if (data[i] < 301) {
				paintPoint.setColor(Color.parseColor("#A0004C"));
			} else {
				paintPoint.setColor(Color.parseColor("#7D0125"));
			}
			canvas.drawCircle(x, yPotion, circlesize, paintPoint);
			canvas.drawCircle(x, yPotion, circleSizeInner, innerpaintPoint);

			paintPoint.setColor(Color.parseColor("#FFFFFF"));
			canvas.drawText(data[i] + "", x - 10, yPotion - 15, paintPoint);
		}
	}

	private int getColorByIntegerValue(int value) {
		if ((value > -1) && (value < 51)) {
			return (Color.parseColor("#00FF00"));
		} else if (value < 101) {
			return (Color.parseColor("#FFFF00"));
		} else if (value < 151) {
			return (Color.parseColor("#FF7E00"));
		} else if (value < 201) {
			return (Color.parseColor("#FF0000"));
		} else if (value < 301) {
			return (Color.parseColor("#A0004C"));
		} else {
			return (Color.parseColor("#7D0125"));
		}
	}

	/**
	 * 绘制连框
	 */
	private void drawFrame(Canvas canvas) {
		calculateLeft();
		int max = data[0];
		for (int i = 0; i < data.length; i++) {
			if (data[i] > max) {
				max = data[i];
			}
		}
		if (max < 200) {
			xMaxValue = 200;
		} else if (max < 300) {
			xMaxValue = 300;
		} else if (max < 400) {
			xMaxValue = 400;
		} else {
			xMaxValue = 500;
		}

		// 绘制横线
		for (int i = 0; i <= xLineCount; i++) {
			float startY = i * xInterval + xInterval;
			Path path_line = new Path();
			path_line.moveTo(left + 5, startY);
			path_line.lineTo(getWidth() - left - 5, startY);
			PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 },
					1);
			paintLine.setPathEffect(effects);
			canvas.drawPath(path_line, paintLine);
			textPaint.setTextAlign(Align.RIGHT);
			canvas.drawText(
					Math.round(xMaxValue / xLineCount * (xLineCount - i) - 0.5)
							+ "", left, startY + bottom / 4, textPaint);
		}

		for (int j = 0; j < yLineCount; j++) {
			float leftSpace = yInterval * j + left + 5;

			// canvas.drawLine(leftSpace,
			// bottom,leftSpace,this.getHeight()-bottom,paintLine);
			textPaint.setTextAlign(Align.CENTER);
			if (j == 0) {
				continue;
			}
			xPoint.add(leftSpace);
			try {
				canvas.drawText(defaultDay[j - 1], leftSpace,
						this.getHeight() - 3, textPaint);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawFrame(canvas);
		doDraw(canvas);
		super.onDraw(canvas);
	}

}