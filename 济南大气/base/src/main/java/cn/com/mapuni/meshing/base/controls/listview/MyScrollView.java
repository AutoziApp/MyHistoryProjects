
package cn.com.mapuni.meshing.base.controls.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * FileName: MyScrollView.java 
 * Description: 手势与ScrollView控件冲突的解决：
 * 主要通过自定义一个ScrollView,并重写其onTouchEvent和dispatchTouchEvent方法来解决冲突问题。
 * @author 王紅娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-7 下午03:26:10
 */
public class MyScrollView extends ScrollView {
	
	GestureDetector gestureDetector;
	
	public MyScrollView(Context context) {
		super(context);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean result = super.onTouchEvent(ev);
		if(gestureDetector != null){
			result = gestureDetector.onTouchEvent(ev);
		}
		
		return result;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(gestureDetector != null)
			gestureDetector.onTouchEvent(ev);
		super.dispatchTouchEvent(ev);
		return true;
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}
	
}
