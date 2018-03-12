package com.viewpagerindicator;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public final class WelcomeFragment extends Fragment {
	private static final String KEY_CONTENT = "TestFragment:Content";
	
	int height=0,width=0;
	static ImageLoader imageloader;
	static WelcomePageItem mItem;
	static DisplayImageOptions options;
	private static Handler mHandler;
	private int mPosition = -1;
	
	String id = "";
	
	public static WelcomeFragment newInstance(WelcomePageItem item,
			ImageLoader loader,DisplayImageOptions opt,Handler handler) {
		WelcomeFragment fragment = new WelcomeFragment();
		mItem  = item;
		imageloader = loader;
		options = opt;
		mHandler = handler;
		return fragment;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			id = savedInstanceState.getString(KEY_CONTENT);
		}
		
		WindowManager manager = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
		height = manager.getDefaultDisplay().getHeight();
		width = manager.getDefaultDisplay().getWidth();
		
	}
	
	public void setPosition(int position){
		this.mPosition = position;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		RelativeLayout layout = new RelativeLayout(getActivity());

		ImageView view = new ImageView(getActivity());
		imageloader.displayImage(mItem.getImgurl(), view,options);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(mPosition);
			}
		});
		view.setScaleType(ScaleType.FIT_XY);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1,-1);

		TextView tv = new TextView(getActivity());
		tv.setText(mItem.title);
		tv.setEllipsize(TextUtils.TruncateAt.END);
		tv.setBackgroundColor(Color.argb(100,222,222,222));
		layout.addView(view,params);
		layout.addView(tv);

		return layout;
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mItem.getImgurl());
	}
	

}
