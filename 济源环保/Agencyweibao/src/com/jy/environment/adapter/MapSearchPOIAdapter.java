package com.jy.environment.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.jy.environment.R;

public class MapSearchPOIAdapter extends BaseAdapter {
	private List<PoiInfo> lstPoiInfo;
	private LayoutInflater inflater;
	private Context mcontext;
	
	public MapSearchPOIAdapter(Context context, List<PoiInfo> PoiInfo
			) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		lstPoiInfo=PoiInfo;
		mcontext=context;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lstPoiInfo.size();
	}

	@Override
	public PoiInfo getItem(int position) {
		// TODO Auto-generated method stub
		return lstPoiInfo.get(position);
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
					convertView = inflater.inflate(R.layout.map_search_lst, null);
				}
				TextView tx=(TextView) convertView.findViewById(R.id.txt_poi_content);
				tx.setText(lstPoiInfo.get(position).name);
				return convertView;
	}

}
