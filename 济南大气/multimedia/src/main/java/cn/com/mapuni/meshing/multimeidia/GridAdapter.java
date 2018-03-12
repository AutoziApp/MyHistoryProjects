package cn.com.mapuni.meshing.multimeidia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import cn.com.mapuni.meshing.base.R;

public class GridAdapter extends BaseAdapter {
	private LayoutInflater inflater; // ��ͼ����
	private int selectedPosition = -1;// ѡ�е�λ��
	private boolean shape;
	private Context mcontext;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public GridAdapter(Context context) {
		this.mcontext = context;
		inflater = LayoutInflater.from(context);
	}

	public void update() {
		loading();
	}

	public int getCount() {
		if (BimpHelper.bmp.size() <= 8) {
			return (BimpHelper.bmp.size() + 1);
		} else {
			return 9;
		}
	}

	public Object getItem(int arg0) {

		return null;
	}

	public long getItemId(int arg0) {

		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * ListView Item����
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		final int coord = position;
		ViewHolder holder = null;
		try {
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.photo_img_item, parent,
						false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == BimpHelper.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						mcontext.getResources(), R.drawable.paizhao_con));
				if (position == 9 || position > 9) {
					holder.image.setVisibility(View.INVISIBLE);
				}
			} else {
				holder.image.setImageBitmap(BimpHelper.bmp.get(position));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				GridAdapter.this.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void loading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (BimpHelper.max == BimpHelper.drr.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						try {
							String path = BimpHelper.drr.get(BimpHelper.max);
							/**
							 * // * ��ȡͼƬ����ת�Ƕȣ���Щϵͳ�����յ�ͼƬ��ת�ˣ��е�û����ת //
							 */
							Bitmap bm = BimpHelper.revitionImageSize(path);
							BimpHelper.bmp.add(bm);
							String newStr = path.substring(
									path.lastIndexOf("/") + 1,
									path.lastIndexOf("."));
							FileUtils.saveBitmap(bm, "" + newStr);
							BimpHelper.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						} catch (OutOfMemoryError e) {
						} catch (Exception e) {
							// e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
}
