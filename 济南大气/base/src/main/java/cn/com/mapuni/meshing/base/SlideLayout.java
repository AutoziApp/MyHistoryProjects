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
 * @description ����������ҳ����ӻ���Ч��
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

	/** view�����ٶȿ��Ƽ����� */
	private LayoutOvershootInterpolator crollInterpolator;
	/** ����Ч�� */
	private GestureDetector gestureDector = null;

	public SlideLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		/** ���ʵ���� */
		initWorkspace();
	}

	public SlideLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		/** ʹ���ڴ�����ʱ��û�д��з������������ó����¼��ļ��� ,Ҳ���ǵ�ǰview�������¼� */
		setHapticFeedbackEnabled(false);
		/** ���ʵ���� */
		initWorkspace();
	}

	/** ���ʵ���� */
	private void initWorkspace() {
		Context context = getContext();
		crollInterpolator = new LayoutOvershootInterpolator();
		scroller = new Scroller(context, crollInterpolator);
		gestureDector = new GestureDetector(this.getContext(),
				new WorkspaceOnGestureListener());
	}

	/**
	 * �����ַ�TouchEvent, ��TouchEvent����ʱ������Activity��TouchEvent���ݸ�����View��
	 * TouchEvent���ȵ������ view �� dispatchTouchEvent ��Ȼ���� dispatchTouchEvent
	 * �������зַ������dispatchTouchEvent����true
	 * ���򽻸����view��onTouchEvent�������dispatchTouchEvent���� false ���򽻸���� view ��
	 * interceptTouchEvent �����������Ƿ�Ҫ��������¼������ interceptTouchEvent ���� true
	 * ��Ҳ�������ص��ˣ��򽻸����� onTouchEvent ��������� interceptTouchEvent ���� false ����ô�ʹ��ݸ���
	 * view ������ view �� dispatchTouchEvent ������ʼ����¼��ķַ�
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		/** ��ִ�л����¼� */
		boolean gesture = gestureDector.onTouchEvent(event);
		if (gesture && event.getAction() == MotionEvent.ACTION_UP) {
			return true;
		} else {
			return super.dispatchTouchEvent(event);
		}
	}

	/**
	 * onInterceptTouchEvent()��ViewGroup��һ������,��������TouchEvent,
	 * ��ϵͳ���ViewGroup�������childView����onTouchEvent()֮ǰ������¼�����һ������
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		/** �Ƿ������ƵĴ����¼� */
		boolean gesture = gestureDector.onTouchEvent(event);
		/** getChildAt(index:int)����λ��ָ������������Ԫ����ʾ����ʵ�� */
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

	/** ��ȡ������view����λ�� */
	@Override
	public void computeScroll() {
		/**
		 * ����Ҫ֪���µ�λ��ʱ�����ô˺���public boolean computeScrollOffset
		 * ()���������true����ʾ������û�н�����λ�øı����ṩһ���µ�λ�á�
		 */
		if (scroller.computeScrollOffset()) {
			/**
			 * scrollTo(int x, int y)�ڵ�ǰ��ͼ����ƫ����(x , y)���괦������ʾ(����)����λ��(x , y)���괦
			 * getCurrX(),���ص�ǰ����X�����ƫ��
			 */
			scrollTo(scroller.getCurrX(), 0);
			/** ʹ��postInvalidate()ˢ�½��� */
			postInvalidate();
		} else if (touchState == TOUCH_STATE_SCROLLING) {
			/** ʹ��postInvalidate()ˢ�½��� */
			postInvalidate();
		}
	}

	/**
	 * ����view�Ŀ�͸� ,
	 * onMeasure�����ڿؼ��ĸ�Ԫ����Ҫ���������ӿؼ�ʱ����.������һ�����⣬������Ҫ�ö��ط���������Ȼ����������������
	 * widthMeasureSpec��heightMeasureSpec,��������ģ
	 * UNSPECIFIED(δָ��),��Ԫ�ز�����Ԫ��ʩ���κ���������Ԫ�ؿ��Եõ�������Ҫ�Ĵ�С;
	 * EXACTLY(��ȫ)����Ԫ�ؾ�����Ԫ�ص�ȷ�д�С,��Ԫ�ؽ����޶��ڸ����ı߽���������������С��
	 * AT_MOST(����)����Ԫ������ﵽָ����С��ֵ;
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		/**
		 * һ��MeasureSpec��װ�˸����ִ��ݸ��Ӳ��ֵĲ���Ҫ��ÿ��MeasureSpec������һ���Ⱥ͸߶ȵ�Ҫ�� .
		 * getMode(int measureSpec)�����ṩ�Ĳ���ֵ(��ʽ)��ȡģʽ
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
			/** �����Ƿ��ܹ�ˮƽ���� */
			setHorizontalScrollBarEnabled(false);
			/** �ڵ�ǰ��ͼ����ƫ����(x , y)���괦������ʾ(����)����λ��(x , y)���괦�� */
			scrollTo(0, 0);
			setHorizontalScrollBarEnabled(true);
			firstLayout = false;
		}
	}

	/** ȷ�����ֺ�λ�� */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		/** ����ͼ����ʼλ�� */
		int childLeft = 0;
		/** �����view�ĸ��� */
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			/** ����λ��ָ������������Ԫ����ʾ����ʵ�� */
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				/** getMeasuredWidth�ǵõ�ĳview��Ҫ��parent view����ռ�Ĵ�С. */
				final int childWidth = child.getMeasuredWidth();
				child.layout(childLeft, 0, childLeft + childWidth,
						child.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}

	/** ������Ļ��ָ����λ�� */
	public void snapToScreen(int toScreen, boolean settle) {
		/** ����scroLler�Ƿ�����ɹ��� */
		if (!scroller.isFinished()) {
			/** ֹͣ��������forceFinished(boolean)�෴��scroLler����������x��yλ��ʱ��ֹ������ */
			scroller.abortAnimation();
		}
		if (settle) {
			crollInterpolator.setDistance(1);
		} else {
			crollInterpolator.disableSettle();
		}

		/** view������ʱ�� */
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

	/** view�����ٶȿ��Ƽ����� */
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

	/** ���Ƽ��� */
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