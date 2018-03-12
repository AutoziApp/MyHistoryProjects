package com.goldnut.app.sdk.view;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * @author wkt 
 * 侧滑Item的ListView，点击按钮删除。
 * 并增加回到原位置的动画过程
 * @Desc:  2015-1-7 增加阻尼效果
 */
public class SideslipListView extends ListView {
	
	/** 阻尼系数 */
	private static final float OFFSET_RADIO = 2.5f;
	/**
	 * 当前滑动的ListView　position
	 */
	private int slidePosition;
	/**
	 * 手指按下X的坐标
	 */
	private int downY;
	/**
	 * 手指按下Y的坐标
	 */
	private int downX;
	/**
	 * 向左滑出的最大距离
	 */
	private int maxDistance;
	/**
	 * 获取按下时，item的位置
	 */
	private int downViewX;
	/**
	 * ListView的item
	 */
	private View itemView;
	/**
	 * 滑动类
	 */
	private Scroller scroller;
	/**
	 * 最小速度
	 */
	private static final int SNAP_VELOCITY = 200;
	/**
	 * 最小距离
	 */
	private static final int minDestance = 30;
	/**
	 * 速度追踪对象
	 */
	private VelocityTracker velocityTracker;
	/**
	 * 是否响应滑动，默认为不响应
	 */
	private boolean isSlide = false;

	/**
	 * 是否已滑出
	 */
	private boolean slided = false;

	/**
	 * 是否可滑动
	 */
	private boolean enable = true;
	/**
	 * 是否可侧滑
	 */
	private boolean mSideslip = true;
	/**
	 * 认为是用户滑动的最小距离
	 */
	private int mTouchSlop;

	/**
	 * 上次滑出的item的下标值
	 */
	private int lastPosition = -1;
	/**
	 * 删除按钮的id
	 */
	private int deleteId = 0;
	
	// 滑动删除方向的枚举值
	public enum RemoveDirection {
		RIGHT, LEFT, NONE;
	}

	public SideslipListView(Context context) {
		this(context, null);
	}

	public SideslipListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SideslipListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		scroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}
	
	/**
	 * @Desc  设置是否可侧滑
	 * @author wkt 
	 * @param sideable
	 */
	public void setSideslipAble(boolean sideslipAble){
		mSideslip = sideslipAble;
	}
	
	/**
	 * @Desc   设置右侧隐藏控件的id
	 * @author wkt 
	 * @param deleteId
	 */
	public void setHideViewId(int  deleteId){
		this.deleteId = deleteId;
	}
		
	
	/**
	 * 分发事件，主要做的是判断点击的是那个item, 以及通过postDelayed来设置响应左右滑动事件
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// 假如scroller滚动还没有结束，我们直接返回
			if (!scroller.isFinished()) {
				return false;
			}
			addVelocityTracker(event);

			downX = (int) event.getX();
			downY = (int) event.getY();

			slidePosition = pointToPosition(downX, downY);

			// 无效的position, 不做任何处理
			if (slidePosition == AdapterView.INVALID_POSITION) {
				ItemViewRestore();
				return super.dispatchTouchEvent(event);
			}
			if(lastPosition!=-1 && lastPosition != slidePosition){
				ItemViewRestore();
				return super.dispatchTouchEvent(event);
			}else
				enable = true;
			// 获取我们点击的item view
			itemView = getChildAt(slidePosition - getFirstVisiblePosition());
			View deleteView = itemView.findViewById(deleteId);
			if(deleteView != null){
				maxDistance = deleteView.getMeasuredWidth();
			}
			//获取按下时，item的位置
			downViewX =itemView.getScrollX();
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			
			if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY
					|| (Math.abs(event.getX() - downX) > mTouchSlop && Math
							.abs(event.getY() - downY) < mTouchSlop)) {
				isSlide = mSideslip;
			}
			break;
		}
		case MotionEvent.ACTION_UP:
			recycleVelocityTracker();
			break;
		}

		return super.dispatchTouchEvent(event);
	}

	/**
	 * 手指滑动距离太小，滑回去
	 */
	private void scrollBack() {
		if(downViewX>0){
			scrollOut();
		}else if(downViewX==0){
			scrollInto();
		}
		postInvalidate(); // 刷新itemView
	}

	/**
	 * 向左滑出到目标点
	 */
	private void scrollOut() {

		slided=true;
		lastPosition = slidePosition;
		final int delta = (maxDistance - itemView.getScrollX());
		// 调用startScroll方法来设置一些滚动的参数，我们在 ()方法中调用scrollTo来滚动item
		scroller.startScroll(itemView.getScrollX(), 0, delta, 0,+
				Math.abs(delta));
		postInvalidate(); // 刷新itemView
	}

	/**
	 *  向右滑回到原点
	 */
	private void scrollInto(){
		slided=false;
		scroller.startScroll(itemView.getScrollX(), 0, -itemView.getScrollX(), 0,
				Math.abs(itemView.getScrollX()));
		postInvalidate(); // 刷新itemView
	}

	/**
	 * 将已滑出的item复位
	 * @param position
	 */
	public void ItemViewRestore(){
		if(lastPosition != -1){
			View slideView = getChildAt(lastPosition - getFirstVisiblePosition());
			if(slideView!=null){
				slideView.scrollTo(0, 0);
			}
			slided = false;
			enable=false;
			lastPosition = -1;
		}
	}
	
	/**
	 * 根据手指滚动itemView的距离来判断是滚动到开始位置还是向左或者向右滚动
	 */
	private void scrollByDistanceX(int x) {
		int destanceX= downX - x;
		if(Math.abs(destanceX)<=minDestance){
			scrollBack();
		}else if (Math.abs(destanceX)>minDestance) {
			if(destanceX > 0){
				scrollOut();
			}else{
				scrollInto();
			}
		}
	}

	/**
	 * 处理我们拖动ListView item的逻辑
	 */
	@SuppressLint("Recycle")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isSlide && slidePosition != AdapterView.INVALID_POSITION) {
			requestDisallowInterceptTouchEvent(true);
			addVelocityTracker(ev);
			final int action = ev.getAction();
			int x = (int) ev.getX();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				MotionEvent cancelEvent = MotionEvent.obtain(ev);
				cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
						(ev.getActionIndex()<< MotionEvent.ACTION_POINTER_INDEX_SHIFT));
				onTouchEvent(cancelEvent);

				if(enable){
					int deltaX = downX - x;
					deltaX = (int) (deltaX/OFFSET_RADIO);
					if(deltaX>0 && !slided){
						if(deltaX > maxDistance){
							itemView.scrollTo(maxDistance, 0);
							lastPosition = slidePosition;
							slided=true;
						}else{
							itemView.scrollTo(deltaX, 0);
						}
					}
					if(deltaX<0&&slided){
						if(Math.abs(deltaX)>maxDistance){
							itemView.scrollTo(0, 0);
							lastPosition =-1;
							enable = true;
							slided=false;
						}else{
							itemView.scrollTo(maxDistance+deltaX, 0);
						}

					}
				}
				// 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚
				return true;  //拖动的时候ListView不滚动
				
			case MotionEvent.ACTION_UP:
				// 手指离开的时候就不响应左右滚动
				isSlide = false;
				if(enable){
					int velocityX = getScrollVelocity();
					if (velocityX > SNAP_VELOCITY) {
						scrollInto();
					} else if (velocityX < -SNAP_VELOCITY) {
						scrollOut();
					} else {
						scrollByDistanceX(x);
					}
					recycleVelocityTracker();
				}
				break;
			}
		}
		//否则直接交给ListView来处理onTouchEvent事件
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		// 调用startScroll的时候scroller.computeScrollOffset()返回true，
		if (scroller.computeScrollOffset()) {
			// 让ListView item根据当前的滚动偏移量进行滚动
			itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
		}
	}

	/**
	 * 添加用户的速度跟踪器
	 * 
	 * @param event
	 */
	private void addVelocityTracker(MotionEvent event) {
		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();
		}
		velocityTracker.addMovement(event);
	}

	/**
	 * 移除用户速度跟踪器
	 */
	private void recycleVelocityTracker() {
		if (velocityTracker != null) {
			velocityTracker.recycle();
			velocityTracker = null;
		}
	}

	/**
	 * 获取X方向的滑动速度,大于0向右滑动，反之向左
	 * 
	 * @return
	 */
	private int getScrollVelocity() {
		velocityTracker.computeCurrentVelocity(100);
		int velocity = (int) velocityTracker.getXVelocity();
		return velocity;
	}

}