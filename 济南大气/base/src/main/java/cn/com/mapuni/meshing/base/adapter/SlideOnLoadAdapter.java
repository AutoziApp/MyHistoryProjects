package cn.com.mapuni.meshing.base.adapter;

import android.view.View;

public abstract class SlideOnLoadAdapter
{
	public SlideOnLoadAdapter(View view )
	{
		this.view = view;
		isDone = false;
	}

	public View view;

	public boolean isDone;// 判断OnLoad是否被执行过

	public abstract void OnLoad();
}
