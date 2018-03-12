package com.jy.environment.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 
 * @ClassName: RealTimeDayBaseFragment 
 * @Description: fragment基类
 * @author tianfy
 * @date 2017-11-30 下午5:02:13
 */
public abstract class RealTimeDayBaseFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutResources(), container, false);
		initView(view);
		initData();
		return view;
	}

	public abstract int getLayoutResources();

	public abstract void initView(View view);

	public abstract void initData() ;

}
