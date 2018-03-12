package cn.com.mapuni.meshingtotal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Yang on 2016/10/20.
 */
public class ListViewForScrollView extends ListView {
    public ListViewForScrollView(Context context) {
        super(context);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    /**
          * 重写该方法，达到使ListView适应ScrollView的效果
          */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

//    /**
//     * 屏蔽android4.4 setAdapter时View抢焦点的BUG
//     */
//    @Override
//    public boolean isInTouchMode() {
//        if(Build.VERSION.SDK_INT>=19){
//            return !(hasFocus() && !super.isInTouchMode());
//        }else{
//            return super.isInTouchMode();
//        }
//    }
}
