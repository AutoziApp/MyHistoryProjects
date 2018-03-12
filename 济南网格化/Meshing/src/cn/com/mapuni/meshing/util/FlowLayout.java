package cn.com.mapuni.meshing.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
//流式布局
public class FlowLayout extends ViewGroup
{

	private static final String TAG = "FlowLayout";


	public FlowLayout(Context context)
	{
		super(context, null);
	}
	public FlowLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(
			ViewGroup.LayoutParams p)
	{
		return new MarginLayoutParams(p);
	}

	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
	{
		return new MarginLayoutParams(getContext(), attrs);
	}

	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams()
	{
		return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}

	/**
	 * 鐠愮喕鐭楃拋鍓х枂鐎涙劖甯舵禒鍓佹畱濞村鍣哄Ο鈥崇础閸滃苯銇囩亸锟� 閺嶈宓侀幍锟介張澶婄摍閹貉傛鐠佸墽鐤嗛懛顏勭箒閻ㄥ嫬顔旈崪宀勭彯
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 閼惧嘲绶辩�瑰啰娈戦悥璺侯啇閸ｃ劋璐熺�瑰啳顔曠純顔炬畱濞村鍣哄Ο鈥崇础閸滃苯銇囩亸锟�
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

		Log.e(TAG, sizeWidth + "," + sizeHeight);

		// 婵″倹鐏夐弰鐥篴rp_content閹懎鍠屾稉瀣剁礉鐠佹澘缍嶇�硅棄鎷版锟�
		int width = 0;
		int height = 0;
		/**
		 * 鐠佹澘缍嶅В蹇庣鐞涘瞼娈戠�硅棄瀹抽敍瀵僫dth娑撳秵鏌囬崣鏍ㄦ付婢堆冾啍鎼达拷
		 */
		int lineWidth = 0;
		/**
		 * 濮ｅ繋绔寸悰宀�娈戞妯哄閿涘瞼鐤崝鐘哄殾height
		 */
		int lineHeight = 0;

		int cCount = getChildCount();

		// 闁秴宸诲В蹇庨嚋鐎涙劕鍘撶槐锟�
		for (int i = 0; i < cCount; i++)
		{
			View child = getChildAt(i);
			// 濞村鍣哄В蹇庣娑撶寶hild閻ㄥ嫬顔旈崪宀勭彯
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			// 瀵版鍩宑hild閻ㄥ埐p
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();
			// 瑜版挸澧犵�涙劗鈹栭梻鏉戠杽闂勫懎宕伴幑顔炬畱鐎硅棄瀹�
			int childWidth = child.getMeasuredWidth() + lp.leftMargin
					+ lp.rightMargin;
			// 瑜版挸澧犵�涙劗鈹栭梻鏉戠杽闂勫懎宕伴幑顔炬畱妤傛ê瀹�
			int childHeight = child.getMeasuredHeight() + lp.topMargin
					+ lp.bottomMargin;
			/**
			 * 婵″倹鐏夐崝鐘插弳瑜版挸澧燾hild閿涘苯鍨搾鍛毉閺堬拷婢堆冾啍鎼达讣绱濋崚娆戞畱閸掓壆娲伴崜宥嗘付婢堆冾啍鎼达妇绮皐idth閿涘瞼琚崝鐖僥ight 閻掕泛鎮楀锟介崥顖涙煀鐞涳拷
			 */
			if (lineWidth + childWidth > sizeWidth)
			{
				width = Math.max(lineWidth, childWidth);// 閸欐牗娓舵径褏娈�
				lineWidth = childWidth; // 闁插秵鏌婂锟介崥顖涙煀鐞涘矉绱濆锟芥慨瀣唶瑜帮拷
				// 閸欑姴濮炶ぐ鎾冲妤傛ê瀹抽敍锟�
				height += lineHeight;
				// 瀵拷閸氼垵顔囪ぐ鏇氱瑓娑擄拷鐞涘瞼娈戞妯哄
				lineHeight = childHeight;
			} else
			// 閸氾箑鍨槐顖氬閸婄辰ineWidth,lineHeight閸欐牗娓舵径褔鐝惔锟�
			{
				lineWidth += childWidth;
				lineHeight = Math.max(lineHeight, childHeight);
			}
			// 婵″倹鐏夐弰顖涙付閸氬簼绔存稉顏庣礉閸掓瑥鐨㈣ぐ鎾冲鐠佹澘缍嶉惃鍕付婢堆冾啍鎼达箑鎷拌ぐ鎾冲lineWidth閸嬫碍鐦潏锟�
			if (i == cCount - 1)
			{
				width = Math.max(width, lineWidth);
				height += lineHeight;
			}

		}
		setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth
				: width, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight
				: height);

	}
	/**
	 * 鐎涙ê鍋嶉幍锟介張澶屾畱View閿涘本瀵滅悰宀冾唶瑜帮拷
	 */
	private List<List<View>> mAllViews = new ArrayList<List<View>>();
	/**
	 * 鐠佹澘缍嶅В蹇庣鐞涘瞼娈戦張锟芥径褔鐝惔锟�
	 */
	private List<Integer> mLineHeight = new ArrayList<Integer>();
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		mAllViews.clear();
		mLineHeight.clear();

		int width = getWidth();

		int lineWidth = 0;
		int lineHeight = 0;
		// 鐎涙ê鍋嶅В蹇庣鐞涘本澧嶉張澶屾畱childView
		List<View> lineViews = new ArrayList<View>();
		int cCount = getChildCount();
		// 闁秴宸婚幍锟介張澶屾畱鐎涒晛鐡�
		for (int i = 0; i < cCount; i++)
		{
			View child = getChildAt(i);
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			// 婵″倹鐏夊鑼病闂囷拷鐟曚焦宕茬悰锟�
			if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width)
			{
				// 鐠佹澘缍嶆潻娆庣鐞涘本澧嶉張澶屾畱View娴犮儱寮烽張锟芥径褔鐝惔锟�
				mLineHeight.add(lineHeight);
				// 鐏忓棗缍嬮崜宥堫攽閻ㄥ垻hildView娣囨繂鐡ㄩ敍宀�鍔ч崥搴＄磻閸氼垱鏌婇惃鍑檙rayList娣囨繂鐡ㄦ稉瀣╃鐞涘瞼娈慶hildView
				mAllViews.add(lineViews);
				lineWidth = 0;// 闁插秶鐤嗙悰灞筋啍
				lineViews = new ArrayList<View>();
			}
			/**
			 * 婵″倹鐏夋稉宥夋付鐟曚焦宕茬悰宀嬬礉閸掓瑧鐤崝锟�
			 */
			lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
			lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
					+ lp.bottomMargin);
			lineViews.add(child);
		}
		// 鐠佹澘缍嶉張锟介崥搴濈鐞涳拷
		mLineHeight.add(lineHeight);
		mAllViews.add(lineViews);

		int left = 0;
		int top = 0;
		// 瀵版鍩岄幀鏄忣攽閺侊拷
		int lineNums = mAllViews.size();
		for (int i = 0; i < lineNums; i++)
		{
			// 濮ｅ繋绔寸悰宀�娈戦幍锟介張澶屾畱views
			lineViews = mAllViews.get(i);
			// 瑜版挸澧犵悰宀�娈戦張锟芥径褔鐝惔锟�
			lineHeight = mLineHeight.get(i);

			Log.e(TAG, "缁楋拷" + i + "鐞涳拷 閿涳拷" + lineViews.size() + " , " + lineViews);
			Log.e(TAG, "缁楋拷" + i + "鐞涘矉绱� 閿涳拷" + lineHeight);

			// 闁秴宸昏ぐ鎾冲鐞涘本澧嶉張澶屾畱View
			for (int j = 0; j < lineViews.size(); j++)
			{
				View child = lineViews.get(j);
				if (child.getVisibility() == View.GONE)
				{
					continue;
				}
				MarginLayoutParams lp = (MarginLayoutParams) child
						.getLayoutParams();

				//鐠侊紕鐣籧hildView閻ㄥ埐eft,top,right,bottom
				int lc = left + lp.leftMargin;
				int tc = top + lp.topMargin;
				int rc =lc + child.getMeasuredWidth();
				int bc = tc + child.getMeasuredHeight();

				Log.e(TAG, child + " , l = " + lc + " , t = " + t + " , r ="
						+ rc + " , b = " + bc);

				child.layout(lc, tc, rc, bc);
				
				left += child.getMeasuredWidth() + lp.rightMargin
						+ lp.leftMargin;
			}
			left = 0;
			top += lineHeight;
		}

	}
}
