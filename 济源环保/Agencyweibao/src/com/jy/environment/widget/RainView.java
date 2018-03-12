package com.jy.environment.widget;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.jy.environment.R;
import com.jy.environment.activity.EnvironmentCurrentWeatherPullActivity;
import com.jy.environment.anime.Rain;

public class RainView extends View {
	// // int MAX_SNOW_COUNT_MAX = 100;
	// int MAX_SNOW_COUNT_NOR = 80;
	// // int MAX_SNOW_COUNT_LAW = 60;
	// int MAX_SNOW_COUNT = 10;
	// // 雨图片
	// Bitmap bitmap_rains = null;
	// // 画笔
	// private final Paint mPaint = new Paint();
	// // 随即生成器
	// private static final Random random = new Random();
	// // 雨的位置
	// private Rain[] rains = new Rain[MAX_SNOW_COUNT];
	// // 屏幕的高度和宽度
	// int view_height = 0;
	// int view_width = 0;
	// int MAX_SPEED = 65;
	//
	// /**
	// * 构造器
	// *
	// *
	// */
	// public RainView(Context context, AttributeSet attrs, int defStyle) {
	// super(context, attrs, defStyle);
	// }
	//
	// public RainView(Context context, AttributeSet attrs) {
	// super(context, attrs);
	//
	// }
	//
	// /**
	// * 加载天女散花的雨图片到内存中
	// *
	// */
	// public void LoadRainImage() {
	// Resources r = this.getContext().getResources();
	// bitmap_rains = ((BitmapDrawable) r.getDrawable(R.drawable.raindrop))
	// .getBitmap();
	// }
	//
	// /**
	// * 设置当前窗体的实际高度和宽度
	// *
	// */
	// public void SetView(int height, int width) {
	// view_height = height - 100;
	// view_width = width - 50;
	//
	// }
	//
	// /**
	// * 随机的生成雨的位置
	// *
	// */
	// public void addRandomRain() {
	// for(int i =0; i< MAX_SNOW_COUNT;i++){
	// rains[i] = new Rain(random.nextInt(view_width), -200
	// ,random.nextInt(MAX_SPEED));
	// }
	// }
	//
	// @Override
	// public void onDraw(Canvas canvas) {
	// super.onDraw(canvas);
	// if(EnvironmentCurrentWeatherPullActivity.is_rain=true){
	// for (int i = 0; i < MAX_SNOW_COUNT; i += 1) {
	// if (rains[i].coordinate.x >= view_width
	// || rains[i].coordinate.y >= view_height) {
	// rains[i].coordinate.y = 0;
	// rains[i].coordinate.x = random.nextInt(view_width);
	// }
	// // 雨下落的速度
	// // rains[i].coordinate.y += rains[i].speed + 20;
	// rains[i].coordinate.y += rains[i].speed + 25;
	// // 雨飘动的效果
	//
	// // // 随机产生一个数字，让雨有水平移动的效果
	// // int tmp = MAX_SPEED/2 - random.nextInt(MAX_SPEED);
	// // //为了动画的自然性，如果水平的速度大于雨的下落速度，那么水平的速度我们取下落的速度。
	// // rains[i].coordinate.x += rains[i].speed < tmp ? rains[i].speed :
	// // tmp;
	// canvas.drawBitmap(bitmap_rains, rains[i].coordinate.x,// ((float)
	// // rains[i].coordinate.x)
	// ((float) rains[i].coordinate.y) - 140, mPaint);
	// }
	// }
	//
	//
	// }

	int MAX_SNOW_COUNT = 100;
	// // 雪花图片
	// Bitmap bitmap_snows = null;
	// 雪花图片
	Bitmap bitmap_snows[] = new Bitmap[7];
	// 画笔
	private final Paint mPaint = new Paint();
	// 随即生成器
	private static final Random random = new Random();
	// 花的位置
	private Rain[] snows = new Rain[MAX_SNOW_COUNT];
	// 屏幕的高度和宽度
	int view_height = 0;
	int view_width = 0;
	int MAX_SPEED = 65;
	int MIN_SPEED = 30;

	/**
	 * 构造器
	 * 
	 * 
	 */
	public RainView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RainView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	/**
	 * 加载天女散花的花图片到内存中
	 * 
	 */
	public void LoadRainImage() {
		Resources r = this.getContext().getResources();
		bitmap_snows[0] = ((BitmapDrawable) r.getDrawable(R.drawable.raindrop))
				.getBitmap();
		bitmap_snows[1] = ((BitmapDrawable) r.getDrawable(R.drawable.raindrop1))
				.getBitmap();
		bitmap_snows[2] = ((BitmapDrawable) r.getDrawable(R.drawable.raindrop2))
				.getBitmap();
		bitmap_snows[3] = ((BitmapDrawable) r.getDrawable(R.drawable.raindrop3))
				.getBitmap();
		bitmap_snows[4] = ((BitmapDrawable) r.getDrawable(R.drawable.raindrop4))
				.getBitmap();
		bitmap_snows[5] = ((BitmapDrawable) r.getDrawable(R.drawable.raindrop5))
				.getBitmap();
		bitmap_snows[6] = ((BitmapDrawable) r.getDrawable(R.drawable.raindrop6))
				.getBitmap();
	}

	// /**
	// * 加载天女散花的花图片到内存中
	// *
	// */
	// public void LoadSnowImage() {
	// Resources r = this.getContext().getResources();
	// bitmap_snows = ((BitmapDrawable) r.getDrawable(R.drawable.raindrop_l))
	// .getBitmap();
	// }

	/**
	 * 设置当前窗体的实际高度和宽度
	 * 
	 */
	public void SetView(int height, int width) {
		view_height = height + 500;
		view_width = width + 50;
//		view_height = height*7/8;
//		view_width = width *9/10;
		MAX_SPEED = view_height/15;
		MIN_SPEED = view_height/30;

	}

	/**
	 * 随机的生成花朵的位置
	 * 
	 */
	public void addRandomRain() {
		for (int i = 0; i < MAX_SNOW_COUNT; i++) {
			int speed = MIN_SPEED + random.nextInt(MAX_SPEED-MIN_SPEED+1);
			snows[i] = new Rain(random.nextInt(view_width), -view_height,
					speed);
			snows[i].setBitmap(random.nextInt(6));
			
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (EnvironmentCurrentWeatherPullActivity.is_rain = true) {
			for (int i = 0; i < MAX_SNOW_COUNT; i += 1) {
				if (snows[i].coordinate.x >= view_width
						|| snows[i].coordinate.y >= view_height) {
					snows[i].coordinate.y = 0;
//					snows[i].coordinate.x += 20;
//					snows[i].coordinate.x = random.nextInt(view_width);
					if(snows[i].coordinate.x >= view_width){
						int needx = snows[i].coordinate.x - view_width;
						snows[i].coordinate.x = 0;
						snows[i].coordinate.x += needx;
					}
				}
				// 雪花下落的速度
				snows[i].coordinate.y += snows[i].speed + view_width/20;
				snows[i].coordinate.x += view_width/24;
				if(snows[i].coordinate.x >= view_width){
					int needx = snows[i].coordinate.x - view_width;
					snows[i].coordinate.x = 0;
					snows[i].coordinate.x += needx;
				}
				// 雪花飘动的效果

				// // 随机产生一个数字，让雪花有水平移动的效果
				// int tmp = MAX_SPEED/2 - random.nextInt(MAX_SPEED);
				// //为了动画的自然性，如果水平的速度大于雪花的下落速度，那么水平的速度我们取下落的速度。
				// snows[i].coordinate.x += snows[i].speed < tmp ?
				// snows[i].speed : tmp;
				canvas.drawBitmap(bitmap_snows[snows[i].bitmap],
						snows[i].coordinate.x,// ((float) snows[i].coordinate.x)
						((float) snows[i].coordinate.y) - 140, mPaint);
			}
		}
	}

}
