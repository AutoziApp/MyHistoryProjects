
package cn.com.mapuni.meshing.base.controls.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * FileName: MyScrollView.java 
 * Description: ������ScrollView�ؼ���ͻ�Ľ����
 * ��Ҫͨ���Զ���һ��ScrollView,����д��onTouchEvent��dispatchTouchEvent�����������ͻ���⡣
 * @author ���t��
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-7 ����03:26:10
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
