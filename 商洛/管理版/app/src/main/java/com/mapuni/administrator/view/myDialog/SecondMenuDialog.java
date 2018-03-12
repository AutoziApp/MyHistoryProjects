package com.mapuni.administrator.view.myDialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapuni.administrator.R;

/**
 * 二级菜单
 * Created by LaiYingtang on 2016/5/25.
 */
public class SecondMenuDialog extends Dialog{
    public Context mContext;
    public LinearLayout containerViewGroup;
    public View mContentView;
    public TextView titleView;
    Window window = null;

    //构造器
    public SecondMenuDialog(Context context) {
        super(context, R.style.dialog_change_card);//样式
        mContext = context;
        containerViewGroup = (LinearLayout) getLayoutInflater().inflate(R.layout.second_menu_dialog, null);
        titleView = (TextView) containerViewGroup.findViewById(R.id.dictdialog_title_tv);
    }
    public View findViewById(int id) {
        return mContentView.findViewById(id);
    }

    /**
     * 设置窗口显示
     */
    public void windowDeploy() {
        window = getWindow(); // 得到对话框
        window.setWindowAnimations(R.style.RegDialogAnimation); // 设置窗口弹出动画效果
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.x = 0; // x小于0左移，大于0右移
        windowAttributes.y = 0; // y小于0上移，大于0下移
        windowAttributes.height = 1 * mContext.getResources().getDisplayMetrics().heightPixels / 2;
        windowAttributes.width = LinearLayout.LayoutParams.FILL_PARENT;
        windowAttributes.alpha = 1f; //设置透明度
        windowAttributes.gravity = Gravity.BOTTOM; // 设置重力，对齐方式
        window.setAttributes(windowAttributes);
    }
    //显示到layout里面
    @Override
    public void show() {
        if (mContentView != null) {
            containerViewGroup.addView(mContentView);
        }
        setContentView(containerViewGroup);
        setCanceledOnTouchOutside(true);
        windowDeploy();
        super.show();
    }
    //选中的title设置为title
    @Override
    public void setTitle(CharSequence title) {
        if (titleView != null)
            titleView.setText(title);
    }
}
