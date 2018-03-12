package com.mapuni.android.photograph;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.mapuni.android.base.R;



/**
 * 发现模块相册选择图片
 * 
 * @author xuhaiguang
 * 
 */
public class DiscoverAlbumSelectActivity extends Activity {
	private List<ImageBucket> dataList;
	private GridView gridView;
	private DiscoverImageBucketAdapter adapter;// 自定义的适配器
	private AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discover_album_select);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		initData();
		initView();
	}

	public void postxcqx(View view) {
		this.finish();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// /**
		// * 这里，我们假设已经从网络或者本地解析好了数据，所以直接在这里模拟了10个实体类，直接装进列表中
		// */
		try {
			dataList = helper.getImagesBucketList(false);
			bimap = BitmapFactory.decodeResource(getResources(),
					R.drawable.paizhao_con);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new DiscoverImageBucketAdapter(
				DiscoverAlbumSelectActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 * 根据position参数，可以获得跟GridView的子View相绑定的实体类，然后根据它的isSelected状态，
				 * 来判断是否显示选中效果。 至于选中效果的规则，下面适配器的代码中会有说明
				 */
				/**
				 * 通知适配器，绑定的数据发生了改变，应当刷新视图
				 */
				Intent intent = new Intent(DiscoverAlbumSelectActivity.this,
						DiscoverAlbumSelectCompleteActivity.class);
//				intent.putExtra(DiscoverAlbumSelectActivity.EXTRA_IMAGE_LIST,
//						(Serializable) dataList.get(position).imageList);
				dataForIntent = dataList.get(position).imageList;
				startActivity(intent);
				finish();
			}
		});
	}

	public static List<ImageItem> dataForIntent = new ArrayList<ImageItem>();
}
