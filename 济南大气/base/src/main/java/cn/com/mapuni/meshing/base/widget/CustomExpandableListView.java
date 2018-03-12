package cn.com.mapuni.meshing.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
/**
 * 自定义ExpandableListView实现Scroview包裹ExpandableListView显示问题
 * @author wanglg
 *
 */
public class CustomExpandableListView extends ExpandableListView {

	public CustomExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}

}
