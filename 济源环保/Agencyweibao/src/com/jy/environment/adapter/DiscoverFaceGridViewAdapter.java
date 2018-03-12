package com.jy.environment.adapter;



import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.jy.environment.R;

/**
 * 用户表情Adapter类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-8-9
 */
public class DiscoverFaceGridViewAdapter extends BaseAdapter
{
	// 定义Context
	private Context	mContext;
	// 定义整型数组 即图片源
	private static int[] mImageIds = new int[]{R.drawable.f001,R.drawable.f002,R.drawable.f003,R.drawable.f004,R.drawable.f005,R.drawable.f006,
			R.drawable.f007,R.drawable.f008,R.drawable.f009,R.drawable.f010,R.drawable.f011,R.drawable.f012,
			R.drawable.f013,R.drawable.f014,R.drawable.f015,R.drawable.f016,R.drawable.f017,R.drawable.f018,
			R.drawable.f019,R.drawable.f020
		};

	public static int[] getImageIds()
	{
		return mImageIds;
	}
	
	public DiscoverFaceGridViewAdapter(Context c)
	{
		mContext = c;
	}
	
	// 获取图片的个数
	public int getCount()
	{
		return mImageIds.length;
	}

	// 获取图片在库中的位置
	public Object getItem(int position)
	{
		return position;
	}


	// 获取图片ID
	public long getItemId(int position)
	{
		return mImageIds[position];
	}


	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		if (convertView == null)
		{
			imageView = new ImageView(mContext);
			// 设置图片n×n显示
			imageView.setLayoutParams(new GridView.LayoutParams(100, 110));
			// 设置显示比例类型
			imageView.setScaleType(ImageView.ScaleType.CENTER);
		}
		else
		{
			imageView = (ImageView) convertView;
		}
		
		imageView.setImageResource(mImageIds[position]);
		if(position < 65)
			imageView.setTag("["+position+"]");
		else if(position < 100)
			imageView.setTag("["+(position+1)+"]");
		else
			imageView.setTag("["+(position+2)+"]");
		
		return imageView;
	}
}