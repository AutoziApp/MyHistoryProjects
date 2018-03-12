package cn.com.mapuni.meshing.base.controls.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class TaskListview extends ListView {

	public TaskListview(Context context) {
		super(context);
	}

	public TaskListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TaskListview(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
	            MeasureSpec.AT_MOST);  
	    super.onMeasure(widthMeasureSpec, expandSpec);  
	}

}
