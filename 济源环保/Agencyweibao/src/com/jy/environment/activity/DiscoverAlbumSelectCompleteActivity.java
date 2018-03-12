package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.jy.environment.R;
import com.jy.environment.adapter.DiscoverImageGridAdapter;
import com.jy.environment.adapter.DiscoverImageGridAdapter.TextCallback;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.model.ImageItem;
import com.jy.environment.util.AlbumHelper;
import com.jy.environment.util.BimpHelper;
import com.jy.environment.util.MyLog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

/**
 * 发现选择图片二级页面
 * 
 * @author baiyuchuan
 * 
 */
public class DiscoverAlbumSelectCompleteActivity extends ActivityBase {
	public static final String EXTRA_IMAGE_LIST = "imagelist";

	List<ImageItem> dataList;
	GridView gridView;
	DiscoverImageGridAdapter adapter;
	AlbumHelper helper;
	Button bt;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(DiscoverAlbumSelectCompleteActivity.this, "最多选择9张图片", 400).show();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.discove_album_select_complete);
		try {
			helper = AlbumHelper.getHelper();
			helper.init(getApplicationContext());

			dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);

			initView();
			bt = (Button) findViewById(R.id.bt);
			bt.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					ArrayList<String> list = new ArrayList<String>();
					Collection<String> c = adapter.map.values();
					Iterator<String> it = c.iterator();
					for (; it.hasNext();) {
						list.add(it.next());
					}

					if (BimpHelper.act_bool) {
						BimpHelper.act_bool = false;
					}
					for (int i = 0; i < list.size(); i++) {
						if (BimpHelper.drr.size() < 9) {
							BimpHelper.drr.add(list.get(i));
						}
					}
					finish();
				}

			});
		} catch (Exception e) {
			// TODO: handle exception
			MyLog.e("weibao Exception" + e);
		}
	}

	/**
	 * 鍒濆鍖杤iew瑙嗗浘
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new DiscoverImageGridAdapter(DiscoverAlbumSelectCompleteActivity.this, dataList, mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				/**
				 * 鏍规嵁position鍙傛暟锛屽彲浠ヨ幏寰楄窡GridView鐨勫瓙View鐩哥粦瀹氱殑瀹炰綋绫伙紝鐒跺悗鏍规嵁瀹冪殑isSelected鐘舵
				 * �锛� 鏉ュ垽鏂槸鍚︽樉绀洪�涓晥鏋溿� 鑷充簬閫変腑鏁堟灉鐨勮鍒欙紝涓嬮潰閫傞厤鍣ㄧ殑浠ｇ爜涓細鏈夎鏄�
				 */
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				/**
				 * 閫氱煡閫傞厤鍣紝缁戝畾鐨勬暟鎹彂鐢熶簡鏀瑰彉锛屽簲褰撳埛鏂拌鍥�
				 */
				adapter.notifyDataSetChanged();
			}

		});

	}
}
