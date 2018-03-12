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
 * ����ģ�����ѡ��ͼƬ
 * 
 * @author xuhaiguang
 * 
 */
public class DiscoverAlbumSelectActivity extends Activity {
	private List<ImageBucket> dataList;
	private GridView gridView;
	private DiscoverImageBucketAdapter adapter;// �Զ����������
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
	 * ��ʼ������
	 */
	private void initData() {
		// /**
		// * ������Ǽ����Ѿ���������߱��ؽ����������ݣ�����ֱ��������ģ����10��ʵ���ֱ࣬��װ���б���
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
	 * ��ʼ��view��ͼ
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
				 * ����position���������Ի�ø�GridView����View��󶨵�ʵ���࣬Ȼ���������isSelected״̬��
				 * ���ж��Ƿ���ʾѡ��Ч���� ����ѡ��Ч���Ĺ��������������Ĵ����л���˵��
				 */
				/**
				 * ֪ͨ���������󶨵����ݷ����˸ı䣬Ӧ��ˢ����ͼ
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
