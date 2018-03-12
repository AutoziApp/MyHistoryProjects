package cn.com.mapuni.meshing.multimeidia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
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

import cn.com.mapuni.meshing.base.R;

public class DiscoverAlbumSelectCompleteActivity extends Activity {
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
				Toast.makeText(DiscoverAlbumSelectCompleteActivity.this,
						"���ѡ��9��ͼƬ", Toast.LENGTH_LONG).show();
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

			// dataList = (List<ImageItem>) getIntent().getSerializableExtra(
			// EXTRA_IMAGE_LIST);
			dataList = DiscoverAlbumSelectActivity.dataForIntent;

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
					DiscoverAlbumSelectActivity.dataForIntent = new ArrayList<ImageItem>();
				}

			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new DiscoverImageGridAdapter(
				DiscoverAlbumSelectCompleteActivity.this, dataList, mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new cn.com.mapuni.meshing.multimeidia.DiscoverImageGridAdapter.TextCallback() {
			public void onListen(int count) {
				bt.setText("���" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				adapter.notifyDataSetChanged();
			}

		});

	}
}
