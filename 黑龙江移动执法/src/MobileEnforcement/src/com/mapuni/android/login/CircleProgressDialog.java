package com.mapuni.android.login;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/*
 * 圆形带有刻度的进度条
 * create by frimmon
 * 2013.09.18
 * */
public class CircleProgressDialog
{
	private Dialog dialog = null;
	private Context context;
	private CircleProgressBar bar;

	public CircleProgressDialog( Context context )
	{
		this.context = context;

		bar = new CircleProgressBar( context );
		LayoutParams params = new LayoutParams( ( int ) ( context.getResources().getDisplayMetrics().widthPixels * 0.8f ), 150 );
		params.gravity = Gravity.CENTER;
		bar.setLayoutParams( params );
	}

	public void Show()
	{
		Show( true );
	}

	public void Show( boolean isCancelable )
	{
		if ( dialog == null || !dialog.isShowing() )
		{
			dialog = null;

			dialog = new Dialog( context, android.R.style.Theme_Dialog );
			dialog.getWindow().setBackgroundDrawable( new ColorDrawable( android.R.color.transparent ) );
			dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
			dialog.setCancelable( isCancelable );
			dialog.setContentView( CreateView() );

			dialog.show();
		}
	}

	public void Dismiss()
	{
		if ( dialog != null && dialog.isShowing() )
		{
			dialog.dismiss();
			dialog = null;
		}
	}

	private View CreateView()
	{
		LinearLayout layout = new LinearLayout( context );
		LayoutParams params = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
		layout.setLayoutParams( params );
		layout.setOrientation( LinearLayout.VERTICAL );
		layout.setGravity( Gravity.CENTER );
		layout.setBackgroundColor( Color.parseColor( "#77000000" ) );
		layout.addView( bar );
		return layout;
	}

	public void setBgcolor( int color )
	{
		bar.setBgcolor( color );
	}

	public void setProgressColor( int color )
	{
		bar.setProgressColor( color );
	}

	public void setProgress( int value )
	{
		bar.setProgress( value );
	}

	public int getProgress()
	{
		return bar.getProgress();
	}

	public int getMax()
	{
		return bar.getMaxProgress();
	}

	public void setMax( int max )
	{
		bar.setMaxProgress( max );
	}

	public void postProgress( int value )
	{
		bar.setProgressNotInUiThread( value );
	}

	private class CircleProgressBar extends View
	{
		private int maxProgress = 100;
		private int progress = 0;
		private int progressStrokeWidth = 10;
		private int bgcolor, precolor;

		// 画圆所在的距形区域
		private RectF oval;
		private Paint paint;

		public CircleProgressBar( Context context )
		{
			super( context );
			oval = new RectF();
			paint = new Paint();
			paint.setFlags( Paint.ANTI_ALIAS_FLAG );// 消除锯齿
			paint.setAntiAlias( true ); // 设置画笔为抗锯齿
			bgcolor = Color.parseColor( "#AAAAAA" );
			precolor = Color.BLUE;
		}

		public void setBgcolor( int color )
		{
			bgcolor = color;
		}

		public void setProgressColor( int color )
		{
			precolor = color;
		}

		public int getMaxProgress()
		{
			return maxProgress;
		}

		public int getProgress()
		{
			return progress;
		}

		public void setMaxProgress( int maxProgress )
		{
			this.maxProgress = maxProgress;
		}

		public void setProgress( int progress )
		{
			this.progress = progress;
			this.invalidate();
		}

		// 非UI线程调用
		public void setProgressNotInUiThread( int progress )
		{
			this.progress = progress;
			this.postInvalidate();
		}

		@Override
		protected void onDraw( Canvas canvas )
		{
			super.onDraw( canvas );

			int width = getWidth();
			int height = getHeight();
			int arcSize = 0;

			arcSize = Math.min( width, height ) - ( progressStrokeWidth << 2 );// 圆的直径

			paint.setColor( bgcolor ); // 设置画笔颜色
			canvas.drawColor( android.R.color.transparent ); // 白色背景
			paint.setStrokeWidth( progressStrokeWidth ); // 线宽
			paint.setStyle( Style.STROKE );

			oval.left = ( width - arcSize ) >> 1; // 左上角x
			oval.top = ( height - arcSize ) >> 1; // 左上角y
			oval.right = oval.left + arcSize; // 左下角x
			oval.bottom = oval.top + arcSize; // 右下角y

			canvas.drawArc( oval, -90, 360, false, paint ); // 绘制白色圆圈，即进度条背景
			paint.setColor( precolor );

			paint.setStrokeWidth( progressStrokeWidth + 2 ); // 线宽
			canvas.drawArc( oval, -90, ( ( float ) progress / maxProgress ) * 360, false, paint ); // 绘制进度圆弧，这里是蓝色

			paint.setStrokeWidth( 1 );
			String text = progress + "%";
			int textHeight = arcSize >> 2;
			paint.setTextSize( textHeight );
			int textWidth = ( int ) paint.measureText( text, 0, text.length() );
			paint.setStyle( Style.FILL );

			canvas.drawText( text, ( width - textWidth ) >> 1, ( height + textHeight ) >> 1, paint );
		}
	}
}
