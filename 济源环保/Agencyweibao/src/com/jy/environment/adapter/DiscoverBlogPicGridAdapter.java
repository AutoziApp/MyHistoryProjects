package com.jy.environment.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jy.environment.R;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.PicPath;
import com.jy.environment.util.BimpHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class DiscoverBlogPicGridAdapter extends BaseAdapter {
	private String[] files;
	private List<PicPath> picPaths;
	private Context context;

	DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.newsloading)
			.showImageOnFail(R.drawable.newsloading).cacheInMemory(true)
			.cacheOnDisc(true).build();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private LayoutInflater mLayoutInflater;

	public DiscoverBlogPicGridAdapter(String[] files, Context context) {
		this.files = files;
		this.context = context;
		mLayoutInflater = LayoutInflater.from(context);
		picPaths = new ArrayList<PicPath>();
	}

	public DiscoverBlogPicGridAdapter(String[] files, Context context,
			List<PicPath> picPaths) {
		this.files = files;
		this.context = context;
		mLayoutInflater = LayoutInflater.from(context);
		this.picPaths = picPaths;
	}

	@Override
	public int getCount() {
		return files == null ? 0 : files.length;
	}

	@Override
	public String getItem(int position) {
		return files[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyGridViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new MyGridViewHolder();
			convertView = mLayoutInflater.inflate(
					R.layout.discover_blog_pic_grid_item, parent, false);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.album_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (MyGridViewHolder) convertView.getTag();
		}
		String url = getItem(position);

		if (!ImageLoader.getInstance().isInited()) {
			WeiBaoApplication.initImageLoader(WeiBaoApplication.getInstance());
		}

		if (picPaths.size() > 0 && null != picPaths.get(position)
				&& !picPaths.get(position).isNeedNet()) {
			try {
				Bitmap bm = BimpHelper.revitionImageSize(picPaths.get(position)
						.getPath());
				viewHolder.imageView.setImageBitmap(bm);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}

		} else {
			ImageLoader.getInstance().displayImage(url, viewHolder.imageView,
					defaultOptions, animateFirstListener);
		}

		return convertView;
	}

	private static class MyGridViewHolder {
		ImageView imageView;
	}

	private class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;

				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					displayedImages.add(imageUri);
				}
			}
		}

	}

}
