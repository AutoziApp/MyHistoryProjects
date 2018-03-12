package com.jy.environment.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Custom view allowing an image to be displayed with a "top crop" scale type
 * 
 * @author Nicolas POMEPUY
 * 
 */
public class TopCenterImageView extends ImageView {

	public TopCenterImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setScaleType(ScaleType.FIT_XY);
	}

	public TopCenterImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setScaleType(ScaleType.FIT_XY);
	}

	public TopCenterImageView(Context context) {
		super(context);
		setScaleType(ScaleType.FIT_XY);
	}

	/**
	 * Top crop scale type
	 */
	@Override
	protected boolean setFrame(int l, int t, int r, int b) {
		if (getDrawable() == null) {
			return super.setFrame(l, t, r, b);
		}
		Matrix matrix = getImageMatrix();
		float scaleFactor = getWidth() / (float) getDrawable().getIntrinsicWidth();
		matrix.setScale(scaleFactor, scaleFactor, 0, 0);
		setImageMatrix(matrix);
		return super.setFrame(l, t, r, b);
	}

}
