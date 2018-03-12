/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jy.environment.widget;

import java.util.ArrayList;
import java.util.Random;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;

import com.jy.environment.R;

/**
 * This class is the custom view where all of the Droidflakes are drawn. This
 * class has all of the logic for adding, subtracting, and rendering
 * Droidflakes.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SnowView extends View {

	Bitmap droid; // The bitmap that all flakes use
	public int numFlakes = 0; // Current number of flakes
	ArrayList<Flake> flakes = new ArrayList<Flake>(); // List of current flakes
	int MAX_SPEED = 65;
	int MIN_SPEED = 30;
	// Animator used to drive all separate flake animations. Rather than have
	// potentially
	// hundreds of separate animators, we just use one and then update all
	// flakes for each
	// frame of that single animation.
	ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
	long startTime, prevTime; // Used to track elapsed time for animations and
								// fps
	int frames = 0; // Used to track frames per second
	Paint textPaint; // Used for rendering fps text
	float fps = 0; // frames per second
	Matrix m = new Matrix(); // Matrix used to translate/rotate each flake
								// during rendering
	String fpsString = "";
	String numFlakesString = "";
	private int snownumber = 80;
	// 随即生成器
	private static final Random random = new Random();

	private int snowsize;
	/**
	 * Constructor. Create objects used throughout the life of the View: the
	 * Paint and the animator
	 */
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public SnowView(Context context) {
		super(context);
		snowsize = (int) this.getResources().getDimension(R.dimen.snowsize);
		droid = BitmapFactory.decodeResource(getResources(),
				R.drawable.snowflake_s);
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(24);

		// This listener is where the action is for the flak animations. Every
		// frame of the
		// animation, we calculate the elapsed time and update every flake's
		// position and rotation
		// according to its speed.
		animator.addUpdateListener(new AnimatorUpdateListener() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void onAnimationUpdate(ValueAnimator arg0) {
				long nowTime = System.currentTimeMillis();
				float secs = (float) (nowTime - prevTime) / 1000f;
				prevTime = nowTime;
				for (int i = 0; i < numFlakes; ++i) {
					Flake flake = flakes.get(i);
					flake.y += 3 * (flake.speed * secs) / 5;
					if (flake.y > getHeight()) {
						flake.y = 0 - flake.height;
						flake.x = (float) Math.random()
								* (getWidth() - flake.width);
					}
					if (i % 3 == 0) {
						flake.x += 1 * (flake.speed - 50) * secs / 5
								* Math.random();
						if (flake.x > getWidth()) {
							flake.x = (float) Math.random()
									* (getWidth() - flake.width);
							flake.y = -400 + (float) Math.random() * 3000;
						}
					} else if (i % 3 == 1) {
						flake.x += (-1) * (flake.speed - 50) * secs / 5
								* Math.random();
						if (flake.x <= 0) {
							flake.x = (float) Math.random()
									* (getWidth() - flake.width);
							flake.y = -400 + (float) Math.random() * 3000;
						} else {

						}
					}
					flake.rotation = flake.rotation
							+ (flake.rotationSpeed * secs);
				}
				// Force a redraw to see the flakes in their new positions and
				// orientations
				invalidate();
			}
		});
		animator.setRepeatCount(ValueAnimator.INFINITE);
		animator.setDuration(2000);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public int getNumFlakes() {
		return numFlakes;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setNumFlakes(int quantity) {
		numFlakes = quantity;
		numFlakesString = "numFlakes: " + numFlakes;
	}

	/**
	 * Add the specified number of droidflakes.
	 */
	public void addFlakes(int quantity) {
		for (int i = 0; i < quantity; ++i) {
			flakes.add(Flake.createFlake(i, getWidth(), droid, snowsize));
		}
		setNumFlakes(numFlakes + quantity);
	}

	/**
	 * Subtract the specified number of droidflakes. We just take them off the
	 * end of the list, leaving the others unchanged.
	 */
	void subtractFlakes(int quantity) {
		for (int i = 0; i < quantity; ++i) {
			int index = numFlakes - i - 1;
			flakes.remove(index);
		}
		setNumFlakes(numFlakes - quantity);
	}

	public int getSnownumber() {
		return snownumber;
	}

	public void setSnownumber(int snownumber) {
		this.snownumber = snownumber;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// Reset list of droidflakes, then restart it with 8 flakes
		flakes.clear();
		numFlakes = 0;
		addFlakes(getSnownumber());
		// Cancel animator in case it was already running
		animator.cancel();
		// Set up fps tracking and start the animation
		startTime = System.currentTimeMillis();
		prevTime = startTime;
		frames = 0;
		animator.start();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// For each flake: back-translate by half its size (this allows it to
		// rotate around its center),
		// rotate by its current rotation, translate by its location, then draw
		// its bitmap
		for (int i = 0; i < numFlakes; ++i) {
			Flake flake = flakes.get(i);
			m.setTranslate(-flake.width / 2, -flake.height / 2);
			m.postRotate(flake.rotation);
			m.postTranslate(flake.width / 2 + flake.x, flake.height / 2
					+ flake.y);
			canvas.drawBitmap(flake.bitmap, m, null);
		}
		// fps counter: count how many frames we draw and once a second
		// calculate the
		// frames per second
		++frames;
		long nowTime = System.currentTimeMillis();
		long deltaTime = nowTime - startTime;
		if (deltaTime > 1000) {
			float secs = (float) deltaTime / 1000f;
			fps = (float) frames / secs;
			fpsString = "fps: " + fps;
			startTime = nowTime;
			frames = 0;
		}
		// canvas.drawText(numFlakesString, getWidth() - 200, getHeight() - 50,
		// textPaint);
		// canvas.drawText(fpsString, getWidth() - 200, getHeight() - 80,
		// textPaint);
	}

	public void pause() {
		// Make sure the animator's not spinning in the background when the
		// activity is paused.
		animator.cancel();
	}

	public void resume() {
		animator.start();
	}

}
