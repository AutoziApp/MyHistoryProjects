package com.mapuni.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class MyDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int mOrientation;

    private boolean isSkipHead = false;

    private boolean isSkipFoot = false;

    private View headView;

    private View footView;

    public MyDecoration(Context context, int orientation) {
//        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = new ColorDrawable(Color.parseColor("#359cc7"));
        setOrientation(orientation);
    }
    public MyDecoration(Context context, int orientation,boolean isSkipHead) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        setOrientation(orientation);
        this.isSkipHead = isSkipHead;
    }
    public MyDecoration(Context context, int orientation,boolean isSkipHead,boolean isSkipFoot) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        setOrientation(orientation);
        this.isSkipHead = isSkipHead;
        this.isSkipFoot = isSkipFoot;
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }
    /**
     * 设置分割线的显示样式
     *
     * @param resId drawable资源,可以使自定义的shape文件
     */
    public void setDivider(Context mContext,int resId)
    {
        mDivider = mContext.getResources().getDrawable(resId);
    }
    @Override
    public void onDraw(Canvas c, RecyclerView parent) {

        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }

    }


    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
//        final int childCount = ((AssemblyRecyclerAdapter)(parent.getAdapter())).getItemFactoryCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if(isSkipHead&&child==headView){
                continue;
            }
            if(isSkipFoot&&child==footView){
                continue;
            }
            RecyclerView v = new RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
//        final int childCount = ((AssemblyRecyclerAdapter)(parent.getAdapter())).getItemFactoryCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if(isSkipHead&&child==headView){
                continue;
            }
            if(isSkipFoot&&child==footView){
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if(isSkipHead){
            headView = parent.getChildAt(0);
        }
        if(isSkipFoot){
            footView = parent.getChildAt(parent.getChildCount()-1);
        }
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}