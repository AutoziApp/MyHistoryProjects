package com.jy.environment.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.jy.environment.R;
import com.jy.environment.util.FileUtils;
import com.jy.environment.util.MyLog;

public class DialView extends SurfaceView implements Callback {

    private SurfaceHolder holder;
    // private Thread thread;
    private Paint paint;
    private Paint texPaint;
    private Paint textPaint1;
    private Canvas canvas;
    private int screenW;
    private String[] decibel_msg;
    private Bitmap bigDialBmp, bigPointerBmp,
    // smallDialBmp, smallPointerBmp,
	    bgBmp;
    private int bigDialX, bigDialY, bigPointerX, bigPointerY, textBgX, textBgY;
    // smallDialX,smallDialY, smallPointerX, smallPointerY, , textBgY;
    private Rect bgRect;
    private Bitmap textBg;
    public int bigDialDegrees;
    private String DBtext;
    // , smallDialDegrees;
    private String percentageText = "";

    // private int percentageX, percentageY;

    public DialView(Context context, AttributeSet attrs) {
	super(context, attrs);
	holder = getHolder();
	holder.addCallback(this);
	paint = new Paint();
	paint.setAntiAlias(true);
	paint.setColor(Color.BLACK);
	paint.setColor(Color.argb(255, 207, 60, 11));
	int textsize1 = (int) this.getResources().getDimension(R.dimen.paint_zaosheng);
	paint.setTextSize(textsize1);
	texPaint = new Paint();
	texPaint.setAntiAlias(true);
	texPaint.setColor(Color.BLACK);
	int textsize2 = (int) this.getResources().getDimension(R.dimen.textpaint_zaosheng);
	texPaint.setTextSize(textsize2);
	textPaint1 = new Paint();
	textPaint1.setColor(Color.RED);
	textPaint1.setTextSize(60);
	setFocusable(true);
	setFocusableInTouchMode(true);
    }

    public void myDraw() {
	try {
	    canvas = holder.lockCanvas(bgRect);
	    canvas.drawColor(Color.WHITE);
	    drawBigDial();
	    // drawSmallDial();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		holder.unlockCanvasAndPost(canvas);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    public void drawBigDial() {
    int size1 = (int) this.getResources().getDimension(R.dimen.leftsize1);
    int size2 = (int) this.getResources().getDimension(R.dimen.downsize1);
	canvas.drawBitmap(bigDialBmp, bigDialX, bigDialY, paint);
	canvas.drawText(DBtext + "  dB", screenW / 2 - size1, getHeight() / 4
		+ bigDialBmp.getHeight() / 4, paint);
	for (int i = 0; i <= 12; i++) {
	    int DBint = Integer.parseInt(DBtext);
	    int data;
	    if (i <= 4) {
		data = Integer.parseInt(decibel_msg[i].substring(0, 3));
	    } else {
		data = Integer.parseInt(decibel_msg[i].substring(0, 2));
	    }
	    if (DBint / 10 == data / 10) {
		texPaint.setColor(Color.RED);
	    } else {
		texPaint.setColor(Color.BLACK);
	    }
	    canvas.drawText(decibel_msg[i], 100, bigDialBmp.getHeight() + size2
		    + size2 * i, texPaint);
	}
	canvas.save();
	canvas.rotate(bigDialDegrees, bigPointerX + bigPointerBmp.getWidth()
		/ 2, bigPointerY + bigPointerBmp.getHeight() / 2);
	// canvas.rotate(bigDialDegrees, bigPointerX + bigPointerBmp.getWidth()
	// / 2, bigPointerY + bigPointerBmp.getHeight() / 2);
	canvas.drawBitmap(bigPointerBmp, bigPointerX, bigPointerY, paint);
	canvas.restore();
    }

    // public void drawSmallDial() {
    // canvas.drawBitmap(smallDialBmp, smallDialX, smallDialY, paint);
    // canvas.save();
    // canvas.rotate(smallDialDegrees,
    // smallPointerX + smallPointerBmp.getWidth() / 2, smallPointerY
    // + smallPointerBmp.getHeight() / 2);
    // canvas.drawBitmap(smallPointerBmp, smallPointerX, smallPointerY, paint);
    // canvas.restore();
    // }

    public void refresh(int bigDialDegrees, String DBtext) {
	myDraw();
	this.DBtext = DBtext;
	this.bigDialDegrees = bigDialDegrees;
    }

    // public void logic(){
    // bigDialDegrees++;
    // smallDialDegrees--;
    // // percentageText.
    // }
    // public void run() {
    // while(flag){
    // long start = System.currentTimeMillis();
    // myDraw();
    // logic();
    // long end = System.currentTimeMillis();
    // try {
    // if (end - start < 50)
    // Thread.sleep(50 - (end - start));
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }
    public void surfaceCreated(SurfaceHolder holder) {
	// 获取屏幕的宽、高
	BitmapFactory.decodeResource(getResources(), R.drawable.signsec_dj_iv);
	textBg = BitmapFactory.decodeResource(getResources(),
		R.drawable.black_box);
	bigDialBmp = BitmapFactory.decodeResource(getResources(),
		R.drawable.signsec_dashboard_01);
	bigPointerBmp = BitmapFactory.decodeResource(getResources(),
		R.drawable.signsec_pointer_01);
	// smallDialBmp = BitmapFactory.decodeResource(getResources(),
	// R.drawable.signsec_dashboard_small_bg);
	// smallPointerBmp = BitmapFactory.decodeResource(getResources(),
	// R.drawable.signsec_pointer_02);
	bgBmp = BitmapFactory.decodeResource(getResources(),
		R.drawable.signsec_dj_ll_blue);
	decibel_msg = new String[13];
	decibel_msg[0] = "180dB:卫星发射升空的噪音";
	decibel_msg[1] = "130dB:飞机喷气机发动时的噪音";
	decibel_msg[2] = "120dB:令人痛苦的噪音";
	decibel_msg[3] = "110dB:摇滚乐,汽车喇叭";
	decibel_msg[4] = "100dB:当火车经过的噪音";
	decibel_msg[5] = "90dB:嘈杂的工厂,挖掘机";
	decibel_msg[6] = "80dB:嘈杂的大街,闹钟声";
	decibel_msg[7] = "70dB:嘈杂的街道,电话铃声";
	decibel_msg[8] = "60dB:正在开会的会议室";
	decibel_msg[9] = "50dB:安静的办公室,下雨声";
	decibel_msg[10] = "40dB:安静的住宅区与公园";
	decibel_msg[11] = "30dB:安静的图书馆,安静的剧场";
	decibel_msg[12] = "20dB:非常安静";
	getHeight();
	screenW = getWidth();
	bgRect = new Rect(0, 0, screenW, bgBmp.getHeight());
	bigDialX = screenW / 2 - bigDialBmp.getWidth() / 2;
	bigDialY = getHeight() / 4 - bigDialBmp.getHeight() / 2;
	/*
	 * bigPointerX = 20 / 2 + bigDialBmp.getWidth() / 2 -
	 * bigPointerBmp.getWidth() / 2 + 10;
	 */
	bigPointerX = screenW / 2 - bigPointerBmp.getWidth() / 2;
	bigPointerY = getHeight() / 4 - bigPointerBmp.getHeight() / 2;

	textBgX = bigDialX + bigDialBmp.getWidth() / 2 - textBg.getWidth() / 2;
	textBgY = bigDialY + bigDialBmp.getHeight() / 2 - textBg.getHeight()
		/ 2 - 50;

	// smallDialX = bigDialX + bigDialBmp.getWidth();
	// smallDialY = 30;
	// smallPointerX = smallDialX + smallDialBmp.getWidth() / 2
	// - smallPointerBmp.getWidth() / 2 - 15;
	// smallPointerY = 53;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
	    int height) {

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public int getBigDialDegrees() {
	return bigDialDegrees;
    }

    public void setBigDialDegrees(int bigDialDegrees) {
	this.bigDialDegrees = bigDialDegrees;
    }

    // public int getSmallDialDegrees() {
    // return smallDialDegrees;
    // }
    //
    // public void setSmallDialDegrees(int smallDialDegrees) {
    // this.smallDialDegrees = smallDialDegrees;
    // }

    public String getPercentageText() {
	return percentageText;
    }

    public void setPercentageText(String percentageText) {
	this.percentageText = percentageText;
    }
    
    public Bitmap saveScreenshot(String dBtextString) {
	if(null != canvas){
            Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas bitCanvas = new Canvas(bitmap);
            bitCanvas.drawColor(Color.WHITE);
            int size1 = (int) this.getResources().getDimension(R.dimen.leftsize1);
            int size2 = (int) this.getResources().getDimension(R.dimen.downsize1);
            bitCanvas.drawBitmap(bigDialBmp, bigDialX, bigDialY, paint);
            bitCanvas.drawText(dBtextString + "  dB", screenW / 2 - size1, getHeight() / 4
        		+ bigDialBmp.getHeight() / 4, paint);
        	for (int i = 0; i <= 12; i++) {
        	    int DBint = Integer.parseInt(dBtextString);
        	    int data;
        	    if (i <= 4) {
        		data = Integer.parseInt(decibel_msg[i].substring(0, 3));
        	    } else {
        		data = Integer.parseInt(decibel_msg[i].substring(0, 2));
        	    }
        	    if (DBint / 10 == data / 10) {
        		texPaint.setColor(Color.RED);
        	    } else {
        		texPaint.setColor(Color.BLACK);
        	    }
        	    bitCanvas.drawText(decibel_msg[i], 100, bigDialBmp.getHeight() + size2
        		    + size2 * i, texPaint);
        	}
        	bitCanvas.save();
        	bitCanvas.rotate(bigDialDegrees, bigPointerX + bigPointerBmp.getWidth()
        		/ 2, bigPointerY + bigPointerBmp.getHeight() / 2);
        	bitCanvas.drawBitmap(bigPointerBmp, bigPointerX, bigPointerY, paint);
        	bitCanvas.restore();
        	 String newStr = System.currentTimeMillis()+"_pic.jpg";
                 boolean t = FileUtils.saveBitmap2file(bitmap, "" + newStr);
                 MyLog.i("读取屏幕截图成功："+ t);
            return bitmap;
	}
	return null;
    }
}
