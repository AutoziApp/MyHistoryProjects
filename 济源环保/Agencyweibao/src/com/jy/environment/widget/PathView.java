package com.jy.environment.widget;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

import com.jy.environment.R;
import com.jy.environment.activity.EnvironmentWeatherRankActivity;
import com.jy.environment.activity.EnvironmentWeatherRankkActivity;

public class PathView extends View {

	public static final int DAY_WEEK = 0;
	public static final int DAY_MONTH = 1;
	public static final int WEEK_MONTH = 2;
	public static final int MONTH_YEAR = 3;
	public static final int DAY_OLDWEEK = 5;
	public static final int DAY_OLDHOUR = 6;

	public String[] days = { "3月21", "3月22", "3月23", "3月24", "3月25", "3月26",
			"3月27" };
	// public static String[] days=new String[7];
	public String[] weeks = { "第一周", "第二周", "第三周", "第四周" };
	public String[] mouths = { "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月",
			"九月", "十月", "十一月", "十二月", };
	public String[] days_month = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
			"21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
	// public String[]
	// days_month={"3月18","3月19","3月20","3月21","3月22","3月23","3月24"};

	public String[] hours = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" };
	public ArrayList<Float> xPoint = new ArrayList<Float>();

	public int defaultType = DAY_WEEK;
	public String[] defaultDay = new String[7];

	// x,y轴的线条数量
	private int xLineCount = 7;
	private int yLineCount = 7;
	private Paint paintLine, paintPoint, innerpaintPoint, textPaint, linkPaint;
	private int[] data;

	// 靠左侧，底部的距离
	private float left;
	private float bottom;

	// x,y轴上显示的值
	private float xMaxValue, yMaxValue;
	// 间距
	private float xInterval, yInterval;

	public PathView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public PathView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PathView(Context context) {
		super(context);
		init(context);
	}

	public void setType(int type) {
		defaultType = type;
	}

	private void init(Context cont) {
		int tsize = (int) this.getResources().getDimension(R.dimen.textPaint) - 6;
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
		paintPoint.setStrokeWidth(55);
		paintPoint.setAntiAlias(true);

		innerpaintPoint = new Paint();
		innerpaintPoint.setColor(Color.WHITE);
		innerpaintPoint.setFakeBoldText(true);
		innerpaintPoint.setStrokeWidth(52);
		innerpaintPoint.setAntiAlias(true);

		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setFakeBoldText(true);
		textPaint.setTextSize(tsize);
		textPaint.setAntiAlias(true);

		linkPaint = new Paint();
		linkPaint.setColor(Color.RED);
		linkPaint.setFakeBoldText(true);
		linkPaint.setTextSize(25);
		linkPaint.setStrokeWidth(linesize - 5);
		linkPaint.setAntiAlias(true);

	}

	public int getColorByIndex(int defStyle) {
		if ((defStyle > -1) && (defStyle < 51)) {
			return Color.parseColor("#00FF00");
		} else if (defStyle < 101) {
			return Color.parseColor("#FFFF00");
		} else if (defStyle < 151) {
			return Color.parseColor("#FF0000");
		} else if (defStyle < 201) {
			return Color.parseColor("#A0004C");
		} else if (defStyle < 301) {
			return Color.parseColor("#7D0125");
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
		invalidate();
	}

	private void calculateLeft() {
		if (data.length > 0) {
			for (int value : data) {

				float tempLeft = textPaint.measureText(value + "");
				if (tempLeft > left) {
					left = tempLeft + 15;
				}
			}
		} else {
			for (int i = 0; i < 7; i++) {
				data[i] = 0;
			}
		}

		bottom = textPaint.getFontMetrics().descent
				- textPaint.getFontMetrics().ascent;

		switch (defaultType) {
		case DAY_MONTH:
			yLineCount = getCurrentMonthLastDay() + 1;
			defaultDay = days_month;
			break;
		case DAY_WEEK:
			yLineCount = 8;
			// defaultDay=days;
			for (int i = 0; i < 7; i++) {
				defaultDay[i] = EnvironmentWeatherRankkActivity.days_data[i];
			}
			break;
		case DAY_OLDWEEK:
			yLineCount = 8;
			// defaultDay=days;
			for (int i = 0; i < 7; i++) {
				defaultDay[i] = EnvironmentWeatherRankActivity.days_data[i];
			}
			break;
		case MONTH_YEAR:
			yLineCount = 13;
			defaultDay = mouths;
			break;
		case WEEK_MONTH:
			yLineCount = 5;
			defaultDay = weeks;
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

	public void setXCount(int maxValue, int count) {
		xLineCount = count;
		xMaxValue = maxValue;
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
		int circlesize = (int) this.getResources().getDimension(R.dimen.circle);
		int circleSizeInner = (int) this.getResources().getDimension(
				R.dimen.circle_inner);
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
		float sumHeight = xLineCount * xInterval;
		float tempInterval = xMaxValue / sumHeight;
		for (int i = 0; i < data.length; i++) {
			float x = xPoint.get(i);
			float yPotion = sumHeight - data[i] / tempInterval + xInterval;

			// linkPaint.setColor(getColorByIndex(data[i]));
			linkPaint.setColor(Color.parseColor("#FFFFFF"));
			if (i != data.length - 1) {
				float nextYPotion = sumHeight - data[i + 1] / tempInterval
						+ xInterval;
				int[] colors = { getColorByIntegerValue(data[i]),
						getColorByIntegerValue(data[i + 1]) };
				LinearGradient linearGradient = new LinearGradient(x + 4,
						yPotion, xPoint.get(i + 1), nextYPotion, colors, null,
						TileMode.REPEAT);
				linkPaint.setShader(linearGradient);
				canvas.drawLine(x, yPotion, xPoint.get(i + 1), nextYPotion,
						linkPaint);
			}

			// canvas.drawCircle(x, yPotion, circlesize, paintPoint);
			// canvas.drawCircle(x, yPotion, circlesize - 1, innerpaintPoint);

			innerpaintPoint.setColor(paintPoint.getColor());
			innerpaintPoint.setStrokeWidth(2);
			innerpaintPoint.setAlpha(55);
			innerpaintPoint.setStrokeCap(Paint.Cap.ROUND);

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
			paintPoint.setStrokeWidth(3);
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
			// canvas.drawLine(left+5, startY,getWidth(),startY,paintLine);
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

			/*
			 * canvas.drawLine(leftSpace, bottom, leftSpace, this.getHeight() -
			 * bottom, paintLine);
			 */
			textPaint.setTextAlign(Align.CENTER);

			if (j == 0) {
				continue;
			}
			xPoint.add(leftSpace);
			canvas.drawText(defaultDay[j - 1], leftSpace, this.getHeight(),
					textPaint);

		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawFrame(canvas);
		doDraw(canvas);
		super.onDraw(canvas);
	}
}