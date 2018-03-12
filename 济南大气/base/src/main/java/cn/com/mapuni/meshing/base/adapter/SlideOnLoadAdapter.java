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

	public boolean isDone;// �ж�OnLoad�Ƿ�ִ�й�

	public abstract void OnLoad();
}
