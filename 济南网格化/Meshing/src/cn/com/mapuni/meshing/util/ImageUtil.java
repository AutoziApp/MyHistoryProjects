package cn.com.mapuni.meshing.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;


/**
 * ͼƬ���ˮӡ������
 *
 */
public class ImageUtil {

    /**
     * ����ˮӡͼƬ�����Ͻ�
     * @param Context
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    public static Bitmap createWaterMaskLeftTop(
            Context context, Bitmap src, Bitmap watermark,
            int paddingLeft, int paddingTop) {
        return createWaterMaskBitmap(src, watermark, 
                dp2px(context, paddingLeft), dp2px(context, paddingTop));
    }

    private static Bitmap createWaterMaskBitmap(Bitmap src, Bitmap watermark,
            int paddingLeft, int paddingTop) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        //����һ��bitmap
        Bitmap newb = Bitmap.createBitmap(width, height, Config.ARGB_8888);// ����һ���µĺ�SRC���ȿ��һ����λͼ
        //����ͼƬ��Ϊ����
        Canvas canvas = new Canvas(newb);
        //�ڻ��� 0��0�����Ͽ�ʼ����ԭʼͼƬ
        canvas.drawBitmap(src, 0, 0, null);
        //�ڻ����ϻ���ˮӡͼƬ
        canvas.drawBitmap(watermark, paddingLeft, paddingTop, null);
        // ����
        canvas.save(Canvas.ALL_SAVE_FLAG);
        // �洢
        canvas.restore();
        return newb;
    }

    /**
     * ����ˮӡͼƬ�����½�
     * @param Context
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingBottom
     * @return
     */
    public static Bitmap createWaterMaskRightBottom(
            Context context, Bitmap src, Bitmap watermark,
            int paddingRight, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark, 
                src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight), 
                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * ����ˮӡͼƬ�����Ͻ�
     * @param Context
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    public static Bitmap createWaterMaskRightTop(
            Context context, Bitmap src, Bitmap watermark,
            int paddingRight, int paddingTop) {
        return createWaterMaskBitmap( src, watermark, 
                src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight), 
                dp2px(context, paddingTop));
    }

    /**
     * ����ˮӡͼƬ�����½�
     * @param Context
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    public static Bitmap createWaterMaskLeftBottom(
            Context context, Bitmap src, Bitmap watermark,
            int paddingLeft, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark, dp2px(context, paddingLeft), 
                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * ����ˮӡͼƬ���м�
     * @param Context
     * @param src
     * @param watermark
     * @return
     */
    public static Bitmap createWaterMaskCenter(Bitmap src, Bitmap watermark) {
        return createWaterMaskBitmap(src, watermark, 
                (src.getWidth() - watermark.getWidth()) / 2,
                (src.getHeight() - watermark.getHeight()) / 2);
    }

    /**
     * ��ͼƬ������ֵ����Ͻ�
     * @param context
     * @param bitmap
     * @param text
     * @return
     */
    public static Bitmap drawTextToLeftTop(Context context, Bitmap bitmap, String text,
            int size, int color, int paddingLeft, int paddingTop) {
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, 
                dp2px(context, paddingLeft),  
                dp2px(context, paddingTop) + bounds.height());
    }

    /**
     * �������ֵ����½�
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    public static Bitmap drawTextToRightBottom(Context context, Bitmap bitmap, String text,
            int size, int color, int paddingRight, int paddingBottom) {
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, 
                bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight), 
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * �������ֵ����Ϸ�
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    public static Bitmap drawTextToRightTop(Context context, Bitmap bitmap, String text,
            int size, int color, int paddingRight, int paddingTop) {
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, 
                bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight), 
                dp2px(context, paddingTop) + bounds.height());
    }

    /**
     * �������ֵ����·�
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    public static Bitmap drawTextToLeftBottom(Context context, Bitmap bitmap, String text,
            int size, int color, int paddingLeft, int paddingBottom) {
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, 
                dp2px(context, paddingLeft),  
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }
    
    public static Bitmap drawTextToLeftBottom2(Context context, Bitmap bitmap, String text,
            int size, int color, int paddingLeft, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap2(context, bitmap, text, paint, bounds, 
                dp2px(context, paddingLeft),  
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }

    //ͼƬ�ϻ�������
    private static Bitmap drawTextToBitmap2(Context context, Bitmap bitmap, String text,
            Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // ��ȡ��������ͼ�����
        paint.setFilterBitmap(true);// ����һЩ
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

    /**
     * �������ֵ��м�
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static Bitmap drawTextToCenter(Context context, Bitmap bitmap, String text,
            int size, int color) {
    	TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds, 
                (bitmap.getWidth() - bounds.width()) / 2,  
                (bitmap.getHeight() + bounds.height()) / 2);
    }

    //ͼƬ�ϻ�������
    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text,
    		TextPaint paint, Rect bounds, int paddingLeft, int paddingTop) {
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // ��ȡ��������ͼ�����
        paint.setFilterBitmap(true);// ����һЩ
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

       // canvas.drawText(text, paddingLeft, paddingTop, paint);
        StaticLayout myStaticLayout = new StaticLayout(text,paint,800,Alignment.ALIGN_NORMAL,1.5F,0,false);
        myStaticLayout.draw(canvas);
        return bitmap;
    }

    /**
     * ����ͼƬ
     * @param src
     * @param w
     * @param h
     * @return
     */
    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // ��¼src�Ŀ��
            int width = src.getWidth();
            int height = src.getHeight();
            // ����һ��matrix����
            Matrix matrix = new Matrix();
            // �������ű���
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // ��ʼ����
            matrix.postScale(scaleWidth, scaleHeight);
            // �������ź��ͼƬ
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }

    /**
     * dipתpix
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) { 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int) (dp * scale + 0.5f); 
    } 
    
    public static Bitmap getThumbnail(Context context,Uri uri,int size) throws FileNotFoundException, IOException{
        InputStream input = context.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;
        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
        double ratio = (originalSize > size) ? (originalSize / size) : 1.0;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither=true;//optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }
    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }
    
    //��ȡϵͳʱ��
    public static String getNowTime(){
    	SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy��MM��dd��    HH:mm:ss     ");       
    	Date    curDate    =   new    Date(System.currentTimeMillis());//��ȡ��ǰʱ��       
    	String    str    =    formatter.format(curDate); 
    	return str;
    }
 
}
