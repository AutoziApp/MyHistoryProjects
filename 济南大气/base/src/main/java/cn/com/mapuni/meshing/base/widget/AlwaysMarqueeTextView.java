package cn.com.mapuni.meshing.base.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

/**
 * �Զ���AlwaysMarqueeTextViewʵ�ֲ���ģ�������
 * 
 * @author 
 * 
 */
public class AlwaysMarqueeTextView extends TextView {
	public AlwaysMarqueeTextView(Context context) {
		super(context);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
		return true;

	}
}