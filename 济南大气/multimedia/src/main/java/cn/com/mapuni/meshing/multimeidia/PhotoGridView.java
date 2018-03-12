package cn.com.mapuni.meshing.multimeidia;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * ÅÄÕÕ¾Å¹¬¸ñ²¼¾Ö
 * @author Administrator
 *
 */
public class PhotoGridView extends GridView {
	 
    public PhotoGridView(Context context, AttributeSet attrs) { 
           super(context, attrs); 
       } 

       public PhotoGridView(Context context) { 
           super(context); 
       } 

       public PhotoGridView(Context context, AttributeSet attrs, int defStyle) { 
           super(context, attrs, defStyle); 
       }

       @Override
       public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

           int expandSpec = MeasureSpec.makeMeasureSpec( 
                   Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
           super.onMeasure(widthMeasureSpec, expandSpec); 
       } 
}
