package com.mapuni.android.base.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;
/**
 * 自定义AutoCompleteTextView实现不用输入文字自动展示数据列表
 * @author wanglg
 *
 */
public class CustomAutoCompleteTextView extends AutoCompleteTextView {
	
  public CustomAutoCompleteTextView(Context context) {
    super(context);
  }
  public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }
  @Override
  public boolean enoughToFilter() {
    return true;
  }
  @Override
  protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
    super.onFocusChanged(focused, direction, previouslyFocusedRect);
    performFiltering(getText(), KeyEvent.KEYCODE_UNKNOWN);
  }
  @Override
  public boolean isFocused() {
	  return true;
  }


  
}
