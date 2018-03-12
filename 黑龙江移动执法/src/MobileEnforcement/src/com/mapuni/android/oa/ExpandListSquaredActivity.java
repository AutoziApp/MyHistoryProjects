/**
 * 
 */
package com.mapuni.android.oa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.assessment.JCKHActivity;
import com.mapuni.android.base.BaseActivity;

/**
 * @author SS
 * 
 *         �����б�Ƕ�׾Ź���ҳ��
 * 
 */
public class ExpandListSquaredActivity extends BaseActivity {

	/** �������ݽṹList<String>, List<List<String>> �ֱ����ڴ�� Group / Children ��String */
	private List<String> groupdata;
	private ArrayList<ArrayList<HashMap<String, String>>> childMapData;

	private ExpandableListView expandListLiew;
	private ExpandableListBaseAdapter expandAdapter;// expandableListView������
	private GridBaseAdapter gridAdapter;// gridView������

	private ParserXml parserXml;// ����xmL������
	private ArrayList<HashMap<String, String>> oneLitData;// ����������ݼ���

	private ArrayList<HashMap<String, String>> oneData;
	private ArrayList<HashMap<String, String>> twoData;
	private ArrayList<HashMap<String, String>> threeData;

	private RelativeLayout two_list_tool_layout;// ���Ⲽ��

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.two_list_tool);
		two_list_tool_layout = (RelativeLayout) this
				.findViewById(R.id.two_list_tool_layout);
		SetBaseStyle(two_list_tool_layout, "��  ��");

		xmlConfiguration();

		expandListLiew = (ExpandableListView) this
				.findViewById(R.id.expandList);
		expandAdapter = new ExpandableListBaseAdapter(this);
		expandListLiew.setAdapter(expandAdapter);
		expandListLiew.setGroupIndicator(getResources().getDrawable(
				R.layout.expandablelistviewselector));
		int groupcount = expandListLiew.getCount();
		for (int i = 0; i < groupcount; i++) {
			expandListLiew.expandGroup(i);

		}

	}

	/** xmL���õķ��� */
	@SuppressWarnings("static-access")
	public void xmlConfiguration() {
		parserXml = new ParserXml();
		oneLitData = new ArrayList<HashMap<String, String>>();
		childMapData = new ArrayList<ArrayList<HashMap<String, String>>>();
		try {
			oneLitData = parserXml.parserXml(getResources().getAssets().open(
					"expandable_listview_config.xml"));
			groupdata = new ArrayList<String>();
			String str = null;
			for (int i = 0; i < oneLitData.size(); i++) {
				str = oneLitData.get(i).get("name").toString();
				groupdata.add(str);
			}
			oneData = new ArrayList<HashMap<String, String>>();
			oneData = parserXml.parserXml(getResources().getAssets().open(
					"grid_one_expand_config.xml"));
			childMapData.add(oneData);

			twoData = new ArrayList<HashMap<String, String>>();
			twoData = parserXml.parserXml(getResources().getAssets().open(
					"grid_two_expand_config.xml"));
			childMapData.add(twoData);

			threeData = new ArrayList<HashMap<String, String>>();
			threeData = parserXml.parserXml(getResources().getAssets().open(
					"grid_three_expand_config.xml"));
			childMapData.add(threeData);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class ExpandableListBaseAdapter extends BaseExpandableListAdapter {

		private final Context context;

		public ExpandableListBaseAdapter(Context context) {
			this.context = context;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childMapData.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			return getGridView(childMapData.get(groupPosition));
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupdata.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return groupdata.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			String string = groupdata.get(groupPosition);
			return getGenericView(string);
		}

		public TextView getGenericView(String str) {
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 64);
			TextView text = new TextView(context);
			text.setLayoutParams(lp);
			text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			text.setPadding(56, 0, 0, 0);
			text.setTextSize(18);
			text.setTextColor(Color.BLACK);
			text.setText(str);
			return text;

		}

		public GridView getGridView(ArrayList<HashMap<String, String>> childMap) {
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 1024);
			GridView gridview = new GridView(context);
			gridview.setLayoutParams(lp);
			gridview.setGravity(Gravity.CENTER_VERTICAL);
			gridview.setNumColumns(3);
			gridview.setColumnWidth(55);
			/** �����м�� */
			gridview.setVerticalSpacing(55);
			gridAdapter = new GridBaseAdapter(ExpandListSquaredActivity.this,
					childMap);
			gridview.setAdapter(gridAdapter);
			return gridview;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	private class GridBaseAdapter extends BaseAdapter {

		private final LayoutInflater inflater;
		ArrayList<HashMap<String, String>> childMap;

		private GridBaseAdapter(Context context,
				ArrayList<HashMap<String, String>> childMap) {
			inflater = LayoutInflater.from(context);
			this.childMap = childMap;
		}

		@Override
		public int getCount() {
			return childMap.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.grid_item_tool, null);
				viewHolder.grid_name_image = (ImageView) convertView
						.findViewById(R.id.grid_name_image);
				viewHolder.grid_name_tv = (TextView) convertView
						.findViewById(R.id.grid_name_tv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.grid_name_image.setImageBitmap(geBitmaptRes(childMap
					.get(position).get("image").toString()));
			viewHolder.grid_name_tv.setText(childMap.get(position).get("name")
					.toString());

			viewHolder.grid_name_image
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							switch (position) {
							// ���鿼��
							case 0:
								Intent check_intent = new Intent(
										ExpandListSquaredActivity.this,
										JCKHActivity.class);
								startActivity(check_intent);
								break;
							// ��Ч����
							case 1:
								Intent perfor_intent = new Intent(
										ExpandListSquaredActivity.this,
										JCKHActivity.class);
								startActivity(perfor_intent);
								break;
							// ���к˲�
							case 2:
								Intent listing_intent = new Intent(
										ExpandListSquaredActivity.this,
										JCKHActivity.class);
								startActivity(listing_intent);
								break;
							// ����ִ��
							case 3:
								Intent urgent_intent = new Intent(
										ExpandListSquaredActivity.this,
										JCKHActivity.class);

								startActivity(urgent_intent);
								break;
							// OA�칫
							case 4:
								Uri uri = Uri.parse("http://www.tjhb.gov.cn/");
								Intent office_intent = new Intent(
										Intent.ACTION_VIEW, uri);

								startActivity(office_intent);
								break;
							}
						}
					});
			return convertView;
		}
	}

	public static class ViewHolder {
		TextView grid_name_tv;
		ImageView grid_name_image;
	}

	/**
	 * Description: ��ȡ�б��ͼƬ
	 * 
	 * @param name
	 *            ��Ƭ������
	 * @return ����������Ƭ Bitmap
	 * @author xgf Create at: 2012-11-30 ����11:30:37
	 */
	@Override
	public Bitmap geBitmaptRes(String name) {
		ApplicationInfo appInfo = this.getApplicationInfo();
		int resID = this.getResources().getIdentifier(name, "drawable",
				appInfo.packageName);
		return BitmapFactory.decodeResource(this.getResources(), resID);
	}

}
