package cn.com.mapuni.meshing.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @author SS
 * @description 用来给更多页面添加滑动效果
 * @param
 * 
 */
public class SlideLayout extends ViewGroup {

	private final  int TOUCH_STATE_REST = 0;
	private final  int TOUCH_STATE_SCROLLING = 1;

	private final int touchState = TOUCH_STATE_REST;

	private boolean firstLayout = true;
	private Scroller scroller;
	private int currentScreen;

	/** view布局速度控制加速器 */
	private LayoutOvershootInterpolator crollInterpolator;
	/** 手势效果 */
	private GestureDetector gestureDector = null;

	public SlideLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		/** 类的实例化 */
		initWorkspace();
	}

	public SlideLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		/** 使其在触摸的时候没有触感反馈。接着设置长按事件的监听 ,也就是当前view处理触摸事件 */
		setHapticFeedbackEnabled(false);
		/** 类的实例化 */
		initWorkspace();
	}

	/** 类的实例化 */
	private void initWorkspace() {
		Context context = getContext();
		crollInterpolator = new LayoutOvershootInterpolator();
		scroller = new Scroller(context, crollInterpolator);
		gestureDector = new GestureDetector(this.getContext(),
				new WorkspaceOnGestureListener());
	}

	/**
	 * 用来分发TouchEvent, 当TouchEvent发生时，首先Activity将TouchEvent传递给最顶层的View，
	 * TouchEvent最先到达最顶层 view 的 dispatchTouchEvent ，然后由 dispatchTouchEvent
	 * 方法进行分发，如果dispatchTouchEvent返回true
	 * ，则交给这个view的onTouchEvent处理，如果dispatchTouchEvent返回 false ，则交给这个 view 的
	 * interceptTouchEvent 方法来决定是否要拦截这个事件，如果 interceptTouchEvent 返回 true
	 * ，也就是拦截掉了，则交给它的 onTouchEvent 来处理，如果 interceptTouchEvent 返回 false ，那么就传递给子
	 * view ，由子 view 的 dispatchTouchEvent 再来开始这个事件的分发
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		/** 先执行滑屏事件 */
		boolean gesture = gestureDector.onTouchEvent(event);
		if (gesture && event.getAction() == MotionEvent.ACTION_UP) {
			return true;
		} else {
			return super.dispatchTouchEvent(event);
		}
	}

	/**
	 * onInterceptTouchEvent()是ViewGroup的一个方法,用来拦截TouchEvent,
	 * 在系统向该ViewGroup及其各个childView触发onTouchEvent()之前对相关事件进行一次拦截
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		/** 是否获得手势的触摸事件 */
		boolean gesture = gestureDector.onTouchEvent(event);
		/** getChildAt(index:int)返回位于指定索引处的子元素显示对象实例 */
		if (currentScreen == 1
				&& event.getX() + getChildAt(1).getWidth() < getWidth()
				&& event.getAction() == MotionEvent.ACTION_DOWN) {
			snapToScreen(0, false);
			return true;
		}
		if (gesture && event.getAction() == MotionEvent.ACTION_UP) {
			return true;
		} else {
			return super.onInterceptTouchEvent(event);
		}
	}

	/** 获取滚动后view的新位置 */
	@Override
	public void computeScroll() {
		/**
		 * 当想要知道新的位置时，调用此函数public boolean computeScrollOffset
		 * ()。如果返回true，表示动画还没有结束。位置改变以提供一个新的位置。
		 */
		if (scroller.computeScrollOffset()) {
			/**
			 * scrollTo(int x, int y)在当前视图内容偏移至(x , y)坐标处，即显示(可视)区域位于(x , y)坐标处
			 * getCurrX(),返回当前滚动X方向的偏移
			 */
			scrollTo(scroller.getCurrX(), 0);
			/** 使用postInvalidate()刷新界面 */
			postInvalidate();
		} else if (touchState == TOUCH_STATE_SCROLLING) {
			/** 使用postInvalidate()刷新界面 */
			postInvalidate();
		}
	}

	/**
	 * 计算view的宽和高 ,
	 * onMeasure方法在控件的父元素正要放置它的子控件时调用.它会问一个问题，“你想要用多大地方啊？”，然后传入两个参数——
	 * widthMeasureSpec和heightMeasureSpec,它有三种模
	 * UNSPECIFIED(未指定),父元素不对子元素施加任何束缚，子元素可以得到任意想要的大小;
	 * EXACTLY(完全)，父元素决定子元素的确切大小,子元素将被限定在给定的边界里而忽略它本身大小；
	 * AT_MOST(至多)，子元素至多达到指定大小的值;
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		/**
		 * 一个MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求 .
		 * getMode(int measureSpec)根据提供的测量值(格式)提取模式
		 */
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"Workspace can only be used in EXACTLY mode.");
		}

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"Workspace can only be used in EXACTLY mode.");
		}

		getChildAt(0).measure(widthMeasureSpec, heightMeasureSpec);
		getChildAt(1).measure(0, heightMeasureSpec);

		if (firstLayout) {
			/** 设置是否能够水平滚动 */
			setHorizontalScrollBarEnabled(false);
			/** 在当前视图内容偏移至(x , y)坐标处，即显示(可视)区域位于(x , y)坐标处。 */
			scrollTo(0, 0);
			setHorizontalScrollBarEnabled(true);
			firstLayout = false;
		}
	}

	/** 确定布局和位置 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		/** 子视图的起始位置 */
		int childLeft = 0;
		/** 获得子view的个数 */
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			/** 返回位于指定索引处的子元素显示对象实例 */
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				/** getMeasuredWidth是得到某view想要在parent view里面占的大小. */
				final int childWidth = child.getMeasuredWidth();
				child.layout(childLeft, 0, childLeft + childWidth,
						child.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}

	/** 滚动屏幕到指定的位置 */
	public void snapToScreen(int toScreen, boolean settle) {
		/** 返回scroLler是否已完成滚动 */
		if (!scroller.isFinished()) {
			/** 停止动画。与forceFinished(boolean)相反，scroLler滚动到最终x与y位置时中止动画。 */
			scroller.abortAnimation();
		}
		if (settle) {
			crollInterpolator.setDistance(1);
		} else {
			crollInterpolator.disableSettle();
		}

		/** view滚动的时间 */
		int duration = 300;
		if (toScreen == 1) {

			View focusedChild = getFocusedChild();
			if (focusedChild != null /* && focusedChild == getChildAt(1) */) {
				focusedChild.clearFocus();
			}
			currentScreen = 1;
			scroller.startScroll(0, 0, getChildAt(1).getWidth(), 0, duration);

		} else if (toScreen == 0) {

			View focusedChild = getFocusedChild();
			if (focusedChild != null /* && focusedChild == getChildAt(0) */) {
				focusedChild.clearFocus();
			}
			currentScreen = 0;
			scroller.startScroll(getChildAt(1).getWidth(), 0, -getChildAt(1)
					.getWidth(), 0, duration);
		}
		invalidate();
	}

	/** view布局速度控制加速器 */
	private static class LayoutOvershootInterpolator implements Interpolator {
		private static final float DEFAULT_TENSION = 1.3f;
		private float mTension;

		public LayoutOvershootInterpolator() {
			mTension = DEFAULT_TENSION;
		}

		public void setDistance(int distance) {
			mTension = distance > 0 ? DEFAULT_TENSION / distance
					: DEFAULT_TENSION;
		}

		public void disableSettle() {
			mTension = 0.f;
		}

		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * ((mTension + 1) * t + mTension) + 1.0f;
		}
	}

	/** 手势监听 */
	private class WorkspaceOnGestureListener extends
			GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if (currentScreen == 1
					&& e.getX() + getChildAt(1).getWidth() < getWidth()) {
				snapToScreen(0, true);
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// float delty = e2.getY() - e1.getY();
			// float deltx = e2.getX() - e1.getX();
			// if ((velocityX < -100 || deltx < -150) && Math.abs(delty) < 100
			// && currentScreen == 0) {
			// snapToScreen(1, true);
			// return true;
			// } else if ((velocityX > 100 || deltx > 150)
			// && Math.abs(delty) < 100 && currentScreen == 1) {
			// snapToScreen(0, true);
			// return true;
			// }
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {

			return true;
		}
	}
}