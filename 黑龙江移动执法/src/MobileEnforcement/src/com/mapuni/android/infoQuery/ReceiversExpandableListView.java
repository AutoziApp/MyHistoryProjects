package com.mapuni.android.infoQuery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class ReceiversExpandableListView extends ExpandableListView {  

    public ReceiversExpandableListView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
        MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);  
    }  
}