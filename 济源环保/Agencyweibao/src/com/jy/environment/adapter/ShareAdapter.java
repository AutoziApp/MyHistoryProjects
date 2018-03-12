package com.jy.environment.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.model.ShareInfo;
/**
 * map部分，不知是否可用
 * @author baiyuchuan
 *
 */
public class ShareAdapter extends BaseAdapter {
	private List<ShareInfo> lstShareinfo;
	private LayoutInflater inflater;
	private Context mcontext;
	
	public ShareAdapter(Context context, List<ShareInfo> shareinfo
			) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		lstShareinfo=shareinfo;
		mcontext=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lstShareinfo.size();
	}

	@Override
	public ShareInfo getItem(int position) {
		// TODO Auto-generated method stub
		ShareInfo section = lstShareinfo.get(position);
		return section;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.share_content_item, null);
		}
		RelativeLayout rl=(RelativeLayout)convertView;
		TextView txt_user_name = (TextView) convertView.findViewById(R.id.txt_user_name);
		TextView txt_content = (TextView) convertView.findViewById(R.id.txt_content);
		ImageView img_tx= (ImageView) convertView.findViewById(R.id.img_tx);
		ImageView img_content= (ImageView) convertView.findViewById(R.id.img_content);
		txt_user_name.setText(lstShareinfo.get(position).get_user_name());
		txt_content.setText(lstShareinfo.get(position).get_content());
		img_tx.setImageBitmap(lstShareinfo.get(position).get_headimg());
		Bitmap bm=lstShareinfo.get(position).get_contentimg();
//		LinearLayout l=(LinearLayout)convertView.findViewById(R.id.share_content);
		if(bm==null){
			img_content.setVisibility(View.GONE);
		}else{
			img_content.setImageBitmap(bm);
//			img_content.setImageResource(R.id.aqi);
//			img_content.setImageDrawable(new BitmapDrawable(bm));
//			img_content.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.aqi));
		}
		

		return rl;
	}

}
