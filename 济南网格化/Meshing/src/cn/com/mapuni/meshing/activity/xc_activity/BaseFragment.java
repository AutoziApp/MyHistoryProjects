package cn.com.mapuni.meshing.activity.xc_activity;

import cn.com.mapuni.meshing.util.DateTimePickDialogUtil;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi") 
 public abstract class BaseFragment extends android.support.v4.app.Fragment{
	public Context context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View view=initView();
		initData();
		initListener();
		return view;
	}

	/**
     * ������ĳ��󷽷���ʼ��view
     *
     * @return
     */
    public abstract View initView();
    /**
     * ������ĳ��󷽷���ȡ����
     *
     * @return
     */
    public abstract void initData();	
    
    public abstract void initListener();	

}
