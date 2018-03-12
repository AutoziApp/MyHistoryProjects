package com.jy.environment.widget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.jy.environment.R;
import com.jy.environment.activity.EnvironmentWeatherRankkActivity;
import com.jy.environment.model.EnvironmentMonitorModel;
import com.jy.environment.model.WeatherInfoMonth;
import com.jy.environment.util.MyLog;

public class PathView_Monthh extends View {
	// 众多月份自定义view
	public static final int DAY_WEEK = 0;
	public static final int DAY_MONTH = 1;
	public static final int WEEK_MONTH = 2;
	public static final int MONTH_YEAR = 3;
	public static final int DAY_HOUR = 4;

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
	public int defaultType = DAY_MONTH;
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
	private List<EnvironmentMonitorModel> monitorModels;
	int[] colors = new int[18];

	public List<EnvironmentMonitorModel> getMonitorModels() {
		return monitorModels;
	}

	public void setMonitorModels(List<EnvironmentMonitorModel> monitorModels) {
		this.monitorModels = monitorModels;
		for (int i = 0; i < monitorModels.get(0).getInfoMonths().size(); i++) {
			defaultDay[i] = monitorModels.get(0).getInfoMonths().get(i)
					.getUpdate_time().substring(5, 10);
		}
	}

	public PathView_Monthh(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public PathView_Monthh(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

	}

	public PathView_Monthh(Context context) {
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
		// int[] aqi24_Lists = new int[24];
		// String[] hours = new String[24];
		// for (int i = 0; i < 24; i++) {
		// hours[i] = "10时";
		// aqi24_Lists[i] = 10;
		// }
		// setDate(new int[] { aqi24_Lists[0], aqi24_Lists[1],
		// aqi24_Lists[2], aqi24_Lists[3], aqi24_Lists[4], aqi24_Lists[5],
		// aqi24_Lists[6], aqi24_Lists[7], aqi24_Lists[8], aqi24_Lists[9],
		// aqi24_Lists[10], aqi24_Lists[11], aqi24_Lists[12],
		// aqi24_Lists[13], aqi24_Lists[14], aqi24_Lists[15],
		// aqi24_Lists[16], aqi24_Lists[17], aqi24_Lists[18],
		// aqi24_Lists[19], aqi24_Lists[20], aqi24_Lists[21],
		// aqi24_Lists[22], aqi24_Lists[23] });
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
		case DAY_MONTH:
			yLineCount = 31;
			MyLog.i(">>>>>>>>>>>slgehrgh" + data.length);
			for (int i = 0; i < 31; i++) {
				if (i % 4 == 0) {
					defaultDay[i] = defaultDay[i];
				} else {
					defaultDay[i] = "";
				}
			}

			break;
		case DAY_WEEK:
			yLineCount = 8;
			for (int i = 0; i < 7; i++) {
				defaultDay[i] = EnvironmentWeatherRankkActivity.days_data[i];
			}
			// defaultDay=days;
			break;
		case MONTH_YEAR:
			yLineCount = 13;
			defaultDay = mouths;
			break;
		case WEEK_MONTH:
			yLineCount = 5;
			defaultDay = weeks;
			break;
		case DAY_HOUR:
			yLineCount = 25;
			// defaultDay =hours;
			for (int i = 0; i < 24; i++) {
				defaultDay[i] = EnvironmentWeatherRankkActivity.hours[i];
			}
			break;

		}

		if (xMaxValue == 0)
			xMaxValue = this.getWidth() - left;

		if (xInterval == 0)
			xInterval = (this.getHeight() - bottom) / 10;

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
		if (monitorModels == null)
			return;
		MyLog.i(">>>>>>>>>xLineCount" + xLineCount + ">>>>>>>xInterval"
				+ xInterval);
		for (int j = 0; j < monitorModels.size(); j++) {
			int circlesize = 4;
			int circleSizeInner = 4;
			// int max = data[0];
			// for (int i = 0; i < data.length; i++) {
			// if (data[i] > max) {
			// max = data[i];
			// }
			// }
			// if (max < 200) {
			// xMaxValue = 200;
			// } else if (max < 300) {
			// xMaxValue = 300;
			// } else if (max < 400) {
			// xMaxValue = 400;
			// } else {
			// xMaxValue = 500;
			// }
			data = new int[31];
			EnvironmentMonitorModel model = monitorModels.get(j);
			List<WeatherInfoMonth> infoMonths = model.getInfoMonths();
			xMaxValue = 500;
			xLineCount =9;
			float sumHeight = xLineCount * xInterval;
			float tempInterval = xMaxValue / sumHeight;
			int color = colors[j];
			for (int i = 0; i < infoMonths.size(); i++) {
				try {
					float x = xPoint.get(i);
					float yPotion = sumHeight - (infoMonths.get(i).getAqi())
							/ tempInterval + xInterval;
					// linkPaint.setColor(getColorByIndex(data[i]));
					linkPaint.setColor(color);
					// linkPaint.setStrokeWidth(2);
					if (i != infoMonths.size() - 1) {
						float nextYPotion = sumHeight
								- (infoMonths.get(i + 1).getAqi())
								/ tempInterval + xInterval;
						// int[] colors = {
						// getColorByIntegerValue(infoMonths.get(i).getAqi()),
						// getColorByIntegerValue(infoMonths.get(i + 1)
						// .getAqi()) };
						// LinearGradient linearGradient = new LinearGradient(x
						// + 4,
						// yPotion, xPoint.get(i + 1), nextYPotion, colors,
						// null, TileMode.REPEAT);
						// linkPaint.setShader(linearGradient);
						linkPaint.setStrokeWidth(1.8f);
						// MyLog.i(">>>>>>>>>>>>ifghgshgshgs" + "true");
						canvas.drawLine(x + 4, yPotion, xPoint.get(i + 1),
								nextYPotion, linkPaint);
					}

					innerpaintPoint.setColor(color);
					innerpaintPoint.setStrokeWidth(2);
					// innerpaintPoint.setAlpha(55);
					// innerpaintPoint.setStrokeCap(Paint.Cap.ROUND);

					// canvas.drawCircle(x,yPotion,circlesize,paintPoint);
					// canvas.drawCircle(x,yPotion,circlesize-1,innerpaintPoint);
					paintPoint.setAlpha(255);

					if ((infoMonths.get(i).getAqi() > -1)
							&& (infoMonths.get(i).getAqi() < 51)) {
						paintPoint.setColor(Color.parseColor

						("#00FF00"));
					} else if (infoMonths.get(i).getAqi() < 101) {
						paintPoint.setColor(Color.parseColor("#FFFF00"));
					} else if (infoMonths.get(i).getAqi() < 151) {
						paintPoint.setColor(Color.parseColor("#FF7E00"));
					} else if (infoMonths.get(i).getAqi() < 201) {
						paintPoint.setColor(Color.parseColor("#FF0000"));
					} else if (infoMonths.get(i).getAqi() < 301) {
						paintPoint.setColor(Color.parseColor("#A0004C"));
					} else {
						paintPoint.setColor(Color.parseColor("#7D0125"));
					}
					paintPoint.setColor(color);
					canvas.drawCircle(x, yPotion, circlesize, paintPoint);
					canvas.drawCircle(x, yPotion, circleSizeInner,
							innerpaintPoint);
					paintPoint.setColor(color);
					// canvas.drawText(infoMonths.get(i).getAqi() + "", x - 10,
					// yPotion - 15, paintPoint);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
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
		xMaxValue = 500;
		// float startY =0.0f;
		// 绘制横线
		float m1 = 0.0f, m2 = 0.0f, m3 = 0.0f, m4 = 0.0f, m5 = 0.0f, m6 = 0.0f, m7 = 0.0f;
		float x1 = 0.0f;
		for (int i = 0; i <= 6; i++) {
			float startY = i * xInterval + xInterval;
			if (i == 1) {
				startY = 4 * xInterval;
			} else if (i == 2) {
				startY = 6 * xInterval;
			} else if (i == 3) {
				startY = 7*xInterval;
			} else if (i == 4) {
				startY =8*xInterval;
			} else if (i == 5) {
				startY =9*xInterval;
			} else if (i == 6) {
				startY =10*xInterval;
			}
			// startY = position[1]+150+startY;
			Path path_line = new Path();
			path_line.moveTo(left + 5, startY);
			path_line.lineTo(getWidth() - left - 5, startY);
			PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 },
					1);
			paintLine.setColor(Color.parseColor("#BFBFBF"));
			paintLine.setPathEffect(effects);
			paintLine.setStrokeWidth(3.0f);
			x1 = left + 48;
			canvas.drawLine(left + 48, startY, getWidth() - 28, startY,
					paintLine);
			// canvas.drawPath(path_line, paintLine);
			textPaint.setTextAlign(Align.RIGHT);
			textPaint.setColor(Color.parseColor("#ffffff"));
			if (i == 0) {
				bitmap_top = left + 28;
				bitmap_right = startY + bottom / 4;
			}
			if (i == 5) {
				bitmap_left = left + 28;
				bitmap_bottom = startY + bottom / 4;
			}
			MyLog.i(">>>>>>>>>>>bottom"
					+ Math.round(xMaxValue / xLineCount * (xLineCount - i)
							- 0.5) + "");
			if (i == 0) {
				m1 = startY;
				canvas.drawText(500 + "", left + 25, startY + bottom / 4,
						textPaint);
			} else if (i == 1) {
				m2 = startY;
				canvas.drawText(300 + "", left + 25, startY + bottom / 4,
						textPaint);
			} else if (i == 2) {
				m3 = startY;
				canvas.drawText(200 + "", left + 25, startY + bottom / 4,
						textPaint);
			} else if (i == 3) {
				m4 = startY;
				canvas.drawText(150 + "", left + 25, startY + bottom / 4,
						textPaint);
			} else if (i == 4) {
				m5 = startY;
				canvas.drawText(100 + "", left + 25, startY + bottom / 4,
						textPaint);
			} else if (i == 5) {
				m6 = startY;
				canvas.drawText(50 + "", left + 25, startY + bottom / 4,
						textPaint);
			} else if (i == 6) {
				m7 = startY;
				canvas.drawText("", left + 25, startY + bottom / 4,
						textPaint);
			}
		}
		MyLog.i(">>>>>>>>>>ylineCount" + yLineCount);
		for (int j = 0; j < yLineCount; j++) {
			float leftSpace = yInterval * j + left + 5;
			paintLine.setColor(Color.parseColor("#cccccc"));
			if (j != 0&&j%4==1) {
				canvas.drawLine(leftSpace, this.getHeight() - bottom -10,
						leftSpace, this.getHeight() - bottom, paintLine);
			}
			textPaint.setTextAlign(Align.CENTER);
			textPaint.setColor(Color.parseColor("#ffffff"));
			if (j == 0) {
				MyLog.i(">>>>>>>>>>>utils" + leftSpace + ">>>>>>>>>bottom"
						+ bottom + ">>>>>height" + (this.getHeight() - bottom));
				bitmap_top = leftSpace + 40;
				bitmap_right = bottom;
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
		paintLine.setColor(Color.parseColor("#7E0000"));
		paintLine.setStrokeWidth(15.0f);
		x1 = left + 48;
		MyLog.i(">>>>>>stroker" + m1 + ">>>>>>>>>>ker" + m2);
		canvas.drawLine(left + 40, m1, left + 40, m2, paintLine);
		paintLine.setColor(Color.parseColor("#99004C"));
		canvas.drawLine(left + 40, m2, left + 40, m3, paintLine);
		paintLine.setColor(Color.parseColor("#FF0000"));
		canvas.drawLine(left + 40, m3, left + 40, m4, paintLine);
		paintLine.setColor(Color.parseColor("#FF7E00"));
		canvas.drawLine(left + 40, m4, left + 40, m5, paintLine);
		paintLine.setColor(Color.parseColor("#FFD112"));
		canvas.drawLine(left + 40, m5, left + 40, m6, paintLine);
		paintLine.setColor(Color.parseColor("#39B711"));
		canvas.drawLine(left + 40, m6, left + 40, m7, paintLine);
		paintPoint.setColor(Color.parseColor("#BFBFBF"));
		canvas.drawText("六级,严重污染", left + 70, m1 + (m2-m1)/2, paintPoint);
		canvas.drawText("五级,重度污染", left + 70, m2 + xInterval, paintPoint);
		canvas.drawText("四级,中度污染", left + 70, m3 + xInterval / 2, paintPoint);
		canvas.drawText("三级,轻度污染", left + 70, m4 + xInterval / 2, paintPoint);
		canvas.drawText("二级,良", left + 70, m5 + xInterval / 2, paintPoint);
		canvas.drawText("一级,优", left + 70, m6 + xInterval / 2, paintPoint);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (monitorModels == null)
			return;
		drawFrame(canvas);
		doDraw(canvas);
		// Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(
		// R.drawable.tuli)).getBitmap();
		// drawImage(canvas, bitmap, (int) bitmap_top - 15, (int) bitmap_right,
		// 20, (int) (bitmap_bottom - bitmap_right), 0, 0);
		super.onDraw(canvas);
	}

	public static void drawImage(Canvas canvas, Bitmap blt, int x, int y,
			int w, int h, int bx, int by) { // x,y表示绘画的起点，
		Rect src = new Rect();// 图片
		Rect dst = new Rect();// 屏幕位置及尺寸
		// src 这个是表示绘画图片的大小
		src.left = bx; // 0,0
		src.top = by;
		src.right = bx + w;// mBitDestTop.getWidth();,这个是桌面图的宽度，
		src.bottom = by + h;// mBitDestTop.getHeight()/2;// 这个是桌面图的高度的一半
		// 下面的 dst 是表示 绘画这个图片的位置
		dst.left = x; // miDTX,//这个是可以改变的，也就是绘图的起点X位置
		dst.top = y; // mBitQQ.getHeight();//这个是QQ图片的高度。 也就相当于 桌面图片绘画起点的Y坐标
		dst.right = x + w; // miDTX + mBitDestTop.getWidth();// 表示需绘画的图片的右上角
		dst.bottom = y + h - 4; // mBitQQ.getHeight() +
								// mBitDestTop.getHeight();//表示需绘画的图片的右下角
		canvas.drawBitmap(blt, src, dst, null);// 这个方法 第一个参数是图片原来的大小，第二个参数是
												// 绘画该图片需显示多少。也就是说你想绘画该图片的某一些地方，而不是全部图片，第三个参数表示该图片绘画的位置
		src = null;
		dst = null;
	}
}