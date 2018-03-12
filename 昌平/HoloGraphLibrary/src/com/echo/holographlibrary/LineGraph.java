package com.echo.holographlibrary;

import java.util.ArrayList;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class LineGraph extends View {

	private ArrayList<Line> lines = new ArrayList<Line>();
	private Paint paint = new Paint();
	private Paint paintAlpha = new Paint();
	private Paint txtPaint = new Paint();
	private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private float minY = 0, minX = 0;
	private float maxY = 0, maxX = 0;
	private boolean isMaxYUserSet = false;
	private int lineToFill = -1;
	private int indexSelected = -1;
	private OnPointClickedListener listener;
	private Bitmap fullImage;
	private boolean shouldUpdate = false;
	private boolean showMinAndMax = false;
	private boolean showHorizontalGrid = false;
	private int gridColor = 0xffffffff;

	public LineGraph(Context context){
		this(context,null);
	}
	public LineGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		txtPaint.setColor(0xffffffff);
		txtPaint.setTextSize(20);
		txtPaint.setAntiAlias(true);
	}
	public void setGridColor(int color)
	{
		gridColor = color;
	}
	public void showHorizontalGrid(boolean show)
	{
		showHorizontalGrid = show;
	}
	public void showMinAndMaxValues(boolean show)
	{
		showMinAndMax = show;
	}
	public void setTextColor(int color)
	{
		txtPaint.setColor(color);
	}
	public void setTextSize(float s)
	{
		txtPaint.setTextSize(s);
	}
	public void setMinY(float minY){
		this.minY = minY;
	}

	public void update()
	{
		shouldUpdate = true;
		postInvalidate();
	}
	public void removeAllLines(){
		while (lines.size() > 0){
			lines.remove(0);
		}
		shouldUpdate = true;
		postInvalidate();
	}

	public void addLine(Line line) {
		lines.add(line);
		shouldUpdate = true;
		postInvalidate();
	}
	public ArrayList<Line> getLines() {
		return lines;
	}
	public void setLineToFill(int indexOfLine) {
		this.lineToFill = indexOfLine;
		shouldUpdate = true;
		postInvalidate();
	}
	public int getLineToFill(){
		return lineToFill;
	}
	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}
	public Line getLine(int index) {
		return lines.get(index);
	}
	public int getSize(){
		return lines.size();
	}

	public void setRangeY(float min, float max) {
		minY = min;
		maxY = max;
		isMaxYUserSet = true;
	}
	public float getMaxY(){
		if (isMaxYUserSet){
			return maxY;
		} else {
			maxY = lines.get(0).getPoint(0).getY();
			for (Line line : lines){
				for (LinePoint point : line.getPoints()){
					if (point.getY() > maxY){
						maxY = point.getY();
					}
				}
			}
			return maxY;
		}

	}
	public float getMinY(){
		if (isMaxYUserSet){
			return minY;
		} else {
			float min = lines.get(0).getPoint(0).getY();
			for (Line line : lines){
				for (LinePoint point : line.getPoints()){
					if (point.getY() < min) min = point.getY();
				}
			}
			minY = min;
			return minY;
		}
	}
	public float getMaxX(){
		float max = lines.get(0).getPoint(0).getX();
		for (Line line : lines){
			for (LinePoint point : line.getPoints()){
				if (point.getX() > max) max = point.getX();
			}
		}
		maxX = max;
		return maxX;

	}
	public float getMinX(){
		float max = lines.get(0).getPoint(0).getX();
		for (Line line : lines){
			for (LinePoint point : line.getPoints()){
				if (point.getX() < max) max = point.getX();
			}
		}
		maxX = max;
		return maxX;
	}
	//	private static boolean isYellow = false;
//	public void setPointYellow(boolean isYellow){
//		Log.i("bai", "setPointYellow :" + isYellow);
//		this.setYellow(isYellow);
//	}
	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas ca) {
		if (fullImage == null || shouldUpdate) {
			fullImage = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(fullImage);
			String max = (int)maxY+"";// used to display max
			String min = (int)minY+"";// used to display min
			paint.reset();
			paintAlpha.reset();
			Path path = new Path();


			float bottomPadding = 1, topPadding = 0;
			float sidePadding = 10;
			if (this.showMinAndMax)
				sidePadding = txtPaint.measureText(max);

			float usableHeight = getHeight() - bottomPadding - topPadding;
			float usableWidth = getWidth() - sidePadding - 80;
			float lineSpace = usableHeight/10;

			int lineCount = 0;
			for (Line line : lines){
				int count = 0;
				float lastXPixels = 0, newYPixels;
				float lastYPixels = 0, newXPixels;
				float maxY = getMaxY();
				float minY = getMinY();
				float maxX = getMaxX();
				float minX = getMinX();

				if (lineCount == lineToFill){
					paint.setColor(Color.BLACK);
					paint.setAlpha(30);
					paint.setStrokeWidth(2);
					for (int i = 10; i-getWidth() < getHeight(); i = i+20){
						canvas.drawLine(i, getHeight()-bottomPadding, 0, getHeight()-bottomPadding-i, paint);
					}

					paint.reset();

					paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
					for (LinePoint p : line.getPoints()){
						float yPercent = (p.getY()-minY)/(maxY - minY);
						float xPercent = (p.getX()-minX)/(maxX - minX);
						if (count == 0){
							lastXPixels = sidePadding + (xPercent*usableWidth);
							lastYPixels = getHeight() - bottomPadding - (usableHeight*yPercent);
							path.moveTo(lastXPixels, lastYPixels);
						} else {
							newXPixels = sidePadding + (xPercent*usableWidth);
							newYPixels = getHeight() - bottomPadding - (usableHeight*yPercent);
							path.lineTo(newXPixels, newYPixels);
							Path pa = new Path();
							pa.moveTo(lastXPixels, lastYPixels);
							pa.lineTo(newXPixels, newYPixels);
							pa.lineTo(newXPixels, 0);
							pa.lineTo(lastXPixels, 0);
							pa.close();
							canvas.drawPath(pa, paint);
							lastXPixels = newXPixels;
							lastYPixels = newYPixels;
						}
						count++;
					}

					path.reset();

					path.moveTo(0, getHeight()-bottomPadding);
					path.lineTo(sidePadding, getHeight()-bottomPadding);
					path.lineTo(sidePadding, 0);
					path.lineTo(0, 0);
					path.close();
					canvas.drawPath(path, paint);

					path.reset();

					path.moveTo(getWidth(), getHeight()-bottomPadding);
					path.lineTo(getWidth()-sidePadding, getHeight()-bottomPadding);
					path.lineTo(getWidth()-sidePadding, 0);
					path.lineTo(getWidth(), 0);
					path.close();

					canvas.drawPath(path, paint);

				}

				lineCount++;
			}

			paint.reset();

			paint.setColor(this.gridColor);
			paint.setAlpha(50);
			paint.setAntiAlias(true);
			// 底部的一条底线
//			canvas.drawLine(sidePadding, getHeight() - bottomPadding, getWidth(), getHeight()-bottomPadding, paint);
			if(this.showHorizontalGrid)
				for(int i=1;i<=10;i++)
				{
					canvas.drawLine(sidePadding, getHeight() - bottomPadding-(i*lineSpace), getWidth(), getHeight()-bottomPadding-(i*lineSpace), paint);
				}
			paint.setAlpha(255);
			int linesize1 = (int) this.getResources().getDimension(R.dimen.linesize1);
			for (Line line : lines) { // 画线
				int count = 0;
				float lastXPixels = 0, newYPixels;
				float lastYPixels = 0, newXPixels;
				float maxY = getMaxY();
				float minY = getMinY();
				float maxX = getMaxX();
				float minX = getMinX();

//				paint.setColor(line.getColor());
				if(line.isHigh()){
//					paint.setColor(Color.YELLOW);
					paint.setColor(Color.parseColor("#cce198"));
				}else{
//					paint.setColor(Color.WHITE);
					paint.setColor(Color.parseColor("#a9cde8"));
				}
				paint.setStrokeWidth(linesize1);

				for (LinePoint p : line.getPoints()){
					float yPercent = (p.getY()-minY)/(maxY - minY);
					float xPercent = (p.getX()-minX)/(maxX - minX);
					float x = p.getX();
					if (count == 0){
						//lastXPixels = sidePadding + (xPercent*usableWidth) + 30;
						lastXPixels = x;
						lastYPixels = getHeight() - bottomPadding - (usableHeight*yPercent) + 30;
					} else {
						newXPixels = x;
						newYPixels = getHeight() - bottomPadding - (usableHeight*yPercent) + 30;
						if(xPercent == 1.0f) {
							newXPixels -= 10;
						}
						canvas.drawLine(lastXPixels, lastYPixels, newXPixels, newYPixels, paint);
						lastXPixels = newXPixels;
						lastYPixels = newYPixels;
					}
					count++;
				}
			}


			int pointCount = 0;
			int pointsize1 = (int) this.getResources().getDimension(R.dimen.pointsize1);
			int circleAlphasize = (int) this.getResources().getDimension(R.dimen.circlealphasize);
			int upsize = (int) this.getResources().getDimension(R.dimen.upsize);
			int downsize = (int) this.getResources().getDimension(R.dimen.downsize);
			int j = 0;
			for (Line line : lines) { // 画点
				j++;
				float maxY = getMaxY();
				float minY = getMinY();
				float maxX = getMaxX();
				float minX = getMinX();

				paint.setColor(line.getColor());
				paint.setStrokeWidth(3);
				paint.setStrokeCap(Paint.Cap.ROUND);

				paintAlpha.setColor(line.getColor());
				paintAlpha.setStrokeWidth(10);
				paintAlpha.setAlpha(55);
				paintAlpha.setStrokeCap(Paint.Cap.ROUND);

				if (line.isShowingPoints()){
					for (LinePoint p : line.getPoints()){
						float yPercent = (p.getY()-minY)/(maxY - minY);
						float xPercent = (p.getX()-minX)/(maxX - minX);
						float xPixels = sidePadding + (xPercent*usableWidth) + 30;
						float yPixels = getHeight() - bottomPadding - (usableHeight*yPercent) + 30;
						float x = p.getX();
						if(xPercent == 1.0f) {
							xPixels -= 10;
						}
//						int yellow = 0xfffc00;
						if(line.isHigh()){
//							paint.setColor(Color.YELLOW);
							paint.setColor(Color.parseColor("#e4d930"));
						}else{
//							paint.setColor(Color.WHITE);
							paint.setColor(Color.parseColor("#b2c7d7"));
						}

//						paintAlpha.setColor(Color.WHITE);
						canvas.drawCircle(x, yPixels, pointsize1, paint);
						canvas.drawCircle(x, yPixels, circleAlphasize, paintAlpha);
//						paint.setColor(Color.WHITE);
//						canvas.drawCircle(xPixels, yPixels, 5, paint);

						Path path2 = new Path();
						path2.addCircle(x, yPixels, 5, Direction.CW);
						p.setPath(path2);
						p.setRegion(new Region((int)(x-30), (int)(yPixels-30), (int)(x+30), (int)(yPixels+30)));

						if (indexSelected == pointCount && listener != null){
							paint.setColor(Color.parseColor("#33B5E5"));
							paint.setAlpha(100);
							canvas.drawPath(p.getPath(), paint);
							paint.setAlpha(255);
						}

						paint.setColor(Color.WHITE);
//						paint.setTextSize(20);
						paint.setTextSize(this.getResources().getDimension(R.dimen.tem_text_size));
						if(j==2){
//						canvas.drawText((int)p.getY()+"°", x-18, yPixels+45, paint);
							canvas.drawText((int)p.getY()+"°", x-18, yPixels+upsize, paint);
						}else{
//							canvas.drawText((int)p.getY()+"°", x-18, yPixels-15, paint);
							canvas.drawText((int)p.getY()+"°", x-18, yPixels-downsize, paint);
						}
						pointCount++;
					}
				}
			}

			shouldUpdate = false;
			if (this.showMinAndMax) {
				ca.drawText(max, 0, txtPaint.getTextSize(), txtPaint);
				ca.drawText(min,0,this.getHeight(),txtPaint);
			}
			//画虚线
			WindowManager wm = (WindowManager) getContext()
					.getSystemService(Context.WINDOW_SERVICE);

			int width = wm.getDefaultDisplay().getWidth();
			int line_size1 = (int) this.getResources().getDimension(R.dimen.line_size1);
			for(int i=1;i<8;i++){
				linePaint.setStyle(Paint.Style.STROKE);
				linePaint.setColor(Color.WHITE);
				linePaint.setAlpha(70);
				linePaint.setStrokeWidth(1);
				Path path_line = new Path();
				path_line.moveTo(20, line_size1*i);
				path_line.lineTo(width-25, line_size1*i);
				PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);
				linePaint.setPathEffect(effects);
//            canvas.drawPath(path_line, linePaint); 
			}
		}

		ca.drawBitmap(fullImage, 0, 0, null);


	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		Point point = new Point();
		point.x = (int) event.getX();
		point.y = (int) event.getY();

		int count = 0;
		int lineCount = 0;
		int pointCount;

		Region r = new Region();
		for (Line line : lines){
			pointCount = 0;
			for (LinePoint p : line.getPoints()){

				if (p.getPath() != null && p.getRegion() != null){
					r.setPath(p.getPath(), p.getRegion());
					if (r.contains(point.x, point.y) && event.getAction() == MotionEvent.ACTION_DOWN) {
						indexSelected = count;
					} else if (event.getAction() == MotionEvent.ACTION_UP){
						if (r.contains(point.x, point.y) && listener != null) {
							listener.onClick(lineCount, pointCount);
						}
						indexSelected = -1;
					}
				}

				pointCount++;
				count++;
			}
			lineCount++;

		}

		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
			shouldUpdate = true;
			postInvalidate();
		}

		return true;
	}

	public void setOnPointClickedListener(OnPointClickedListener listener) {
		this.listener = listener;
	}

//	public static boolean isYellow() {
//		Log.i("bai", "isYellow :" + isYellow);
//		return isYellow;
//	}
//	public static void setYellow(boolean isYellow) {
//		LineGraph.isYellow = isYellow;
//		Log.i("bai", "LineGraph.isYellow  :" + LineGraph.isYellow );
//	}

	public interface OnPointClickedListener {
		abstract void onClick(int lineIndex, int pointIndex);
	}
}
