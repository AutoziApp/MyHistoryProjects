package com.mapuni.android.base.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.base.DataSyncActivity;
import com.mapuni.android.base.ListActivity;
import com.mapuni.android.base.ListActivity.LeftImageOnCliclkListener;
import com.mapuni.android.base.ListActivity.rightImageOnClickListener;
import com.mapuni.android.base.R;
import com.mapuni.android.base.business.BaseUsers;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SqliteUtil;

/**
 * FileName: ListActivityAdapter.java Description: ListActivity的数据载体 extends
 * BaseAdapter
 * 
 * @author 王留庚
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-11-30 上午10:39:46
 */
public class ListActivityAdapter extends BaseAdapter {

	/*************** 以下是列表显示样式的xml节点名称 ******************/

	/** 对应数据的主键 */
	public final String TAGNAME_ID = "id";
	/** 列表显示的第一行数据 */
	public final String TAGNAME_TITLE = "title";
	/** 列表显示的第二行数据 */
	public final String TAGNAME_CONTENT = "content";
	/** 列表显示的第二行靠后的数据 */
	public final String TAGNAME_DATE = "date";
	/** 列表左边的图片 */
	public final String TAGNAME_LEFTICON = "lefticon";
	/** 列表右边的图片 */
	public final String TAGNAME_RIGHTICON = "righticon";
	/** 列表右侧的复选框checkbox */
	public final String TAGNAME_CHKCHOICE = "chkchoice ";

	/*************** 以下是控制列表样式是否显示所传递的参数名称 ******************/

	/** 是否显示左边的图片 */
	public final String IS_LEFTICON_VISIBLE = "isShowLefticon";
	/** 是否显示第一行数据 */
	public final String IS_TITLE_VISIBLE = "isShowTitle";
	/** 是否显示第二行数据 */
	public final String IS_CONTENT_VISIBLE = "isShowContent";
	/** 是否显示第二行靠后的数据 */
	public final String IS_DATE_VISIBLE = "isShowDate";
	/** 是否显示右边的图片 */
	public final String IS_RIGHTICON_VISIBLE = "isShowRighticon";
	/** 是否显示复选框 */
	public final String IS_CHECKBOX_VISIABLE = "isShowCheckBox";

	/** 上下文初始值 */
	private Context ctx = null;

	/** 初始化列表显示值 */
	private boolean isLefticonVisiable = true;
	private boolean isTitleVisiable = true;
	private boolean isContentVisiable = true;
	private boolean isDateVisiable = true;
	private boolean isRighticonVisiable = true;
	private boolean isCheckBoxVisiable = false;

	/** 所有复选框是否勾选了 */
	public boolean IS_CHECKED = false;

	/** 处理前的原始数据 */
	public ArrayList<HashMap<String, Object>> data = null;
	/** 显示的规范 */
	private HashMap<String, Object> style = null;
	/** 保存勾选的复选框 */
	protected LinkedList<String> chkChoice = null;
	/** 存放列表数据的Bundle */
	protected Bundle bundle = null;
	/** 用于log输出，显示getview调用次数 */
	private int tag = 0;

	private int textSize = 20;

	private LeftImageOnCliclkListener imgListener;

	private rightImageOnClickListener imgListener2;

	protected Map<Integer, View> listView = new HashMap<Integer, View>();

	/** 打电话数据 */
	public ArrayList<HashMap<String, Object>> phoneData;
	/** 初始化SqLite工具 */
	private final SqliteUtil sqliteutil = SqliteUtil.getInstance();

	/**
	 * 构造函数初始化参数
	 * 
	 * @param context
	 *            上下文
	 * @param widgetVisiable
	 *            单行中控件的显示与隐藏
	 * @param data
	 *            要展示的数据
	 * @param style
	 *            展示数据的规范
	 */

	public ListActivityAdapter(Context context, Bundle widgetVisiable,
			ArrayList<HashMap<String, Object>> data,
			HashMap<String, Object> style) {

		ctx = context;
		this.data = data;
		this.style = style;
		this.bundle = widgetVisiable;
		this.textSize = Integer.parseInt(String.valueOf(DisplayUitl
				.getSettingValue(context, DisplayUitl.TEXTSIZE, 26)));

		/** 控件的显示和隐藏初始化 */
		if (widgetVisiable.containsKey(IS_LEFTICON_VISIBLE)) {
			isLefticonVisiable = widgetVisiable.getBoolean(IS_LEFTICON_VISIBLE);
		}

		if (widgetVisiable.containsKey(IS_TITLE_VISIBLE)) {
			isTitleVisiable = widgetVisiable.getBoolean(IS_TITLE_VISIBLE);
		}

		if (widgetVisiable.containsKey(IS_CONTENT_VISIBLE)) {
			isContentVisiable = widgetVisiable.getBoolean(IS_CONTENT_VISIBLE);
		}

		if (widgetVisiable.containsKey(IS_DATE_VISIBLE)) {
			isDateVisiable = widgetVisiable.getBoolean(IS_DATE_VISIBLE);
		}

		if (widgetVisiable.containsKey(IS_RIGHTICON_VISIBLE)) {
			isRighticonVisiable = widgetVisiable
					.getBoolean(IS_RIGHTICON_VISIBLE);
		}

		if (widgetVisiable.containsKey(IS_CHECKBOX_VISIABLE)) {
			isCheckBoxVisiable = widgetVisiable
					.getBoolean(IS_CHECKBOX_VISIABLE);

			chkChoice = new LinkedList<String>();
		}
	}

	/**
	 * 构造函数初始化参数
	 * 
	 * @param context
	 *            上下文
	 * @param widgetVisiable
	 *            单行中控件的显示与隐藏
	 * @param data
	 *            要展示的数据
	 * @param style
	 *            展示数据的规范
	 */

	public ListActivityAdapter(Context context, Bundle widgetVisiable,
			ArrayList<HashMap<String, Object>> data,
			HashMap<String, Object> style,
			LeftImageOnCliclkListener imageOnCliclkListener,
			rightImageOnClickListener imageOnCliclkListener2) {

		ctx = context;
		this.data = data;
		this.style = style;
		this.bundle = widgetVisiable;
		this.textSize = Integer.parseInt(String.valueOf(DisplayUitl
				.getSettingValue(context, DisplayUitl.TEXTSIZE, 26)));
		this.imgListener = imageOnCliclkListener;
		this.imgListener2 = imageOnCliclkListener2;

		/** 控件的显示和隐藏初始化 */
		if (widgetVisiable.containsKey(IS_LEFTICON_VISIBLE)) {
			isLefticonVisiable = widgetVisiable.getBoolean(IS_LEFTICON_VISIBLE);
		}

		if (widgetVisiable.containsKey(IS_TITLE_VISIBLE)) {
			isTitleVisiable = widgetVisiable.getBoolean(IS_TITLE_VISIBLE);
		}

		if (widgetVisiable.containsKey(IS_CONTENT_VISIBLE)) {
			isContentVisiable = widgetVisiable.getBoolean(IS_CONTENT_VISIBLE);
		}

		if (widgetVisiable.containsKey(IS_DATE_VISIBLE)) {
			isDateVisiable = widgetVisiable.getBoolean(IS_DATE_VISIBLE);
		}

		if (widgetVisiable.containsKey(IS_RIGHTICON_VISIBLE)) {
			isRighticonVisiable = widgetVisiable
					.getBoolean(IS_RIGHTICON_VISIBLE);
		}

		if (widgetVisiable.containsKey(IS_CHECKBOX_VISIABLE)) {
			isCheckBoxVisiable = widgetVisiable
					.getBoolean(IS_CHECKBOX_VISIABLE);

			chkChoice = new LinkedList<String>();
		}
	}

	/**
	 * Description: 获取列表的图片
	 * 
	 * @param name
	 *            照片的名字
	 * @return 返回所需照片 Bitmap
	 * @author xgf Create at: 2012-11-30 上午11:30:37
	 */
	public Bitmap getRes(String name) {
		ApplicationInfo appInfo = ctx.getApplicationInfo();
		int resID = ctx.getResources().getIdentifier(name, "drawable",
				appInfo.packageName);
		return BitmapFactory.decodeResource(ctx.getResources(), resID);
	}

	@Override
	public int getCount() {
		if (data.size() == 0) {
			return 1;
		}
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (data.size() == 0) {
			/** 没有数据，替换布局 */
			YutuLoading loading = new YutuLoading(ctx);
			loading.setLoadMsg("加载中", "对不起，暂无数据！", Color.BLACK);
			loading.setFocusable(false);
			loading.setClickable(false);
			loading.setEnabled(false);
			loading.setFailed();
			loading.setLayoutParams(new android.widget.AbsListView.LayoutParams(
					android.widget.AbsListView.LayoutParams.FILL_PARENT,
					android.widget.AbsListView.LayoutParams.FILL_PARENT));
			return loading;
		}
		/*
		 * if(listView.get(position)!=null){ return listView.get(position); }
		 */
		ViewHolder holder = null;

		if (convertView == null) {

			holder = new ViewHolder();
			convertView = uiListCell(ctx, holder, position);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, Object> dataRow = data.get(position);

		/** 获取样式布局的规范 */
		String idKey = style.get(TAGNAME_ID).toString();
		String titleKey = style.get(TAGNAME_TITLE).toString();
		String contentKey = style.get(TAGNAME_CONTENT).toString();
		String dateKey = style.get(TAGNAME_DATE).toString();
		String cellValue = "";

		/** 绑定数据 */
		if (dataRow.containsKey(idKey) && dataRow.get(idKey) != null) {
			cellValue = dataRow.get(idKey).toString();
			holder.id.setTag(cellValue);
			if (isCheckBoxVisiable) {
				holder.chkchoice.setTag(cellValue);
				holder.chkchoice.setOnCheckedChangeListener(chkChoiceListener);
				String boxid = (String) dataRow.get("id");
				if (chkChoice.contains(boxid)) {
					holder.chkchoice.setChecked(true);
				} else {
					holder.chkchoice.setChecked(IS_CHECKED);
				}
			}
		}

		if (isTitleVisiable && dataRow.containsKey(titleKey)
				&& dataRow.get(titleKey) != null) {
			cellValue = dataRow.get(titleKey).toString();
			if (titleKey.equals("wastename") && cellValue != null && !cellValue.equals("")) {
				// 危废-危险废物名称
				String sql = "select name from WXFWMC where CODE = '"+cellValue+"'";
				cellValue = SqliteUtil.getInstance().getDepidByDepName(sql);
			}
			
			
			// 安徽的demo
			if (bundle.containsKey("cbsj") ? true : false) {
				@SuppressWarnings("unchecked")
				ArrayList<HashMap<String, Object>> data = (ArrayList<HashMap<String, Object>>) bundle
						.getSerializable("cbqy");
				for (HashMap<String, Object> map : data) {
					if (cellValue.equals(map.get("qymc"))) {
						holder.title.setTextColor(Color.RED);
					}
				}
			}
			cellValue = FileHelper.formatTextLength(cellValue, 24);
			holder.title.setTextSize(textSize);
			holder.title.setText(cellValue);
		}

		if (isContentVisiable && dataRow.containsKey(contentKey)
				&& dataRow.get(contentKey) != null) {
			cellValue = dataRow.get(contentKey).toString();
			holder.content.setText(cellValue);
		}

		if (isDateVisiable && dataRow.containsKey(dateKey)
				&& dataRow.get(dateKey) != null) {
			cellValue = dataRow.get(dateKey).toString();
			// 从后台同步过来的数据往往不规范，需要处理。
			if (cellValue.contains("T")) {
				String values = (String) cellValue.subSequence(0,
						cellValue.length() - 6);
				cellValue = values.replace("T", "  ");
			}
			holder.date.setText(cellValue);
		}

		/** 暂时不用type处理 */
		if (isLefticonVisiable) {
			if (null == data.get(position).get(TAGNAME_LEFTICON)) {
				holder.lefticon.setImageBitmap(getRes(style.get(
						TAGNAME_LEFTICON).toString()));
			} else
				holder.lefticon.setImageBitmap(getRes(data.get(position)
						.get(TAGNAME_LEFTICON).toString()));
		}

		if (isRighticonVisiable) {
			holder.righticon.setImageBitmap(getRes(style.get(TAGNAME_RIGHTICON)
					.toString()));
		}
		// }
		// convertView.setMinimumHeight(50);
		// listView.put(position, convertView);
		return convertView;
	}

	/**
	 * Description: ListView单行数据样式布局
	 * 
	 * LinearLayout(father) RelativeLayout(son) ImageView(lefticon)
	 * LinearLayout(1) TextView(title) LinearLayout(2) TextView(content)
	 * TextView(date) LinearLayout(2) LinearLayout(1) ImageView(righticon)
	 * CheckBox(choice) RelativeLayout(son) LinearLayout(father)
	 * 
	 * @param context
	 *            上下文
	 * @param holder
	 *            控件绑定的实体类
	 * @param position
	 *            listview的位置
	 * @return 返回所需视图 View
	 * @author 王留庚 Create at: 2012-11-30 上午11:32:43
	 */
	private View uiListCell(final Context context, final ViewHolder holder,
			final int position) {

		int lefticonId = 1111;
		int righticonId = 1112;
		int chkChoiceId = 1113;

		LinearLayout linearLayoutFather = new LinearLayout(context);
		linearLayoutFather.setOrientation(LinearLayout.HORIZONTAL);
		linearLayoutFather.setGravity(Gravity.CENTER_VERTICAL);

		RelativeLayout relativeLayoutSon = new RelativeLayout(context);
		relativeLayoutSon.setPadding(5,5,5,5);
		RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		relativeLayoutSon.setLayoutParams(relativeLayoutParams);
		
		
		//relativeLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		
	
		if (isLefticonVisiable) {
			final ImageView imgLefticon = new ImageView(context);
			relativeLayoutParams = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			relativeLayoutParams.setMargins(1, 1, 1, 1);
			relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			relativeLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
			imgLefticon.setId(lefticonId);
		  //  imgLefticon.setImageBitmap(getRes("icon"));
			holder.lefticon = imgLefticon;
			imgLefticon.setFocusable(false);
			// 给imgLefticon绑定监听器

			imgLefticon.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (imgListener != null) {
						imgListener
								.bindLeftImage(imgLefticon, holder, position);
					}

				}
			});

			relativeLayoutSon.addView(imgLefticon, relativeLayoutParams);
		}

		if (isRighticonVisiable) {
			
	
			final ImageView imgRighticon = new ImageView(context);
			relativeLayoutParams = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			relativeLayoutParams.setMargins(1, 1, 1, 1);
			relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			relativeLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

			if (isCheckBoxVisiable) {
				relativeLayoutParams.addRule(RelativeLayout.LEFT_OF,
						chkChoiceId);
			}
			imgRighticon.setVisibility(View.GONE);
			imgRighticon.setId(righticonId);
			// imgRighticon.setImageBitmap(getRes("icon"));
			holder.righticon = imgRighticon;
			if (BaseUsers.flag = true) {
				imgRighticon.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//								String string = data.toString();
//								
//								int i=position;
//						String name = data.get(position).get("u_realname")
//								.toString();
//						/** 打电话数据 */
//						ArrayList<HashMap<String, Object>> phoneData = sqliteutil
//								.queryBySqlReturnArrayListHashMap("select u_officetel,u_unicomtel,u_hometel from pc_users where u_realname ='"
//										+ name + "'");
//
//						LayoutInflater inflater = LayoutInflater.from(context);
//						View view = inflater.inflate(R.layout.phone_layout,
//								null);
//						final TextView textView1 = (TextView) view
//								.findViewById(R.id.phone_text);
//						final TextView textView2 = (TextView) view
//								.findViewById(R.id.phone_text2);
//						final TextView textView3 = (TextView) view
//								.findViewById(R.id.phone_text3);
//						
//						textView1.setText(phoneData.get(0).get("u_officetel").toString());
//						textView2.setText(phoneData.get(0).get("u_unicomtel").toString());
//						textView3.setText(phoneData.get(0).get("u_hometel").toString());
//					    
//						RelativeLayout layout1=(RelativeLayout)view.findViewById(R.id.rrr1);
//						RelativeLayout layout2=(RelativeLayout)view.findViewById(R.id.rrr2);
//						RelativeLayout layout3=(RelativeLayout)view.findViewById(R.id.rrr3);
//					
//						Builder mDialog = new AlertDialog.Builder(context)
//								.setTitle("拨打电话")
//								.setIcon(R.drawable.bodadianhua).setView(view)
//								.setNegativeButton("取消", null);
//						mDialog.show();
//					
//                       
//						layout1.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								String number = textView1.getText().toString();
//								if (number != null &&  !number.equals("")) {
//									Intent intent = new Intent(
//											Intent.ACTION_CALL, Uri
//													.parse("tel:" + number));
//									context.startActivity(intent);
//								} else {
//								Toast.makeText(context, "暂无电话", 5000).show();
//								}
//							}
//						});
//						layout2.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								String number = textView2.getText().toString();
//								if (number != null && !number.equals("")) {
//									Intent intent = new Intent(
//											Intent.ACTION_CALL, Uri
//													.parse("tel:" + number));
//									context.startActivity(intent);
//								} else {
//									Toast.makeText(context, "暂无电话", 5000).show();
//								}
//							}
//						});
//						layout3.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								String number = textView3.getText().toString();
//								if (number != null && !number.equals("")) {
//									Intent intent = new Intent(
//											Intent.ACTION_CALL, Uri
//													.parse("tel:" + number));
//									context.startActivity(intent);
//								} else {
//									Toast.makeText(context, "暂无电话", 5000).show();
//								}
//							}
//						});

					}
				});

			}

			relativeLayoutSon.addView(imgRighticon, relativeLayoutParams);
		}

		if (isCheckBoxVisiable) {
			CheckBox chkChoice = new CheckBox(context);
			chkChoice.setId(chkChoiceId);
			relativeLayoutParams = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			relativeLayoutParams.setMargins(1, 1, 1, 1);
			relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			relativeLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

			if (isRighticonVisiable) {
				relativeLayoutParams.addRule(RelativeLayout.RIGHT_OF,
						righticonId);
			}

			holder.chkchoice = chkChoice;
			relativeLayoutSon.addView(chkChoice, relativeLayoutParams);
		}

		LinearLayout linearLayout1 = new LinearLayout(context);
		relativeLayoutParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		linearLayout1.setOrientation(LinearLayout.VERTICAL);
		linearLayout1.setGravity(Gravity.CENTER_VERTICAL);

		if (isLefticonVisiable) {
			relativeLayoutParams.addRule(RelativeLayout.RIGHT_OF, lefticonId);
		}

		if (isRighticonVisiable) {
			relativeLayoutParams.addRule(RelativeLayout.LEFT_OF, righticonId);
		}

		relativeLayoutSon.addView(linearLayout1, relativeLayoutParams);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.FILL_PARENT);

		int dataSize = data.size();
		if (isTitleVisiable || dataSize == 0) {
			TextView txtTitle = new TextView(context);

			if (isContentVisiable || isDateVisiable) {
				layoutParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
			} else {
				layoutParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.FILL_PARENT);
			}

			txtTitle.setTextSize(textSize);
		//	txtTitle.setPadding(120, 0, 0, 0);
			txtTitle.setTextColor(Color.BLACK);
			// txtTitle.getPaint().setFakeBoldText(true);//加粗

			if (dataSize == 0) {
				txtTitle.setText("暂无数据");
			}

			txtTitle.setMinWidth(200);
			holder.title = txtTitle;

			linearLayout1.addView(txtTitle, layoutParams);
		}

		LinearLayout linearLayout2 = null;
		if (isContentVisiable || isDateVisiable) {
			linearLayout2 = new LinearLayout(context);
			layoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
			linearLayout2.setLayoutParams(layoutParams);
		}

		if (isContentVisiable) {
			TextView txtContent = new TextView(context);
		//	txtContent.setPadding(120, 0, 0, 0);
			layoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.weight = 1;
			txtContent.setGravity(Gravity.LEFT);
			txtContent.setTextSize(14);
			txtContent.setTextColor(Color.BLACK);
			// txtContent.getPaint().setFakeBoldText(true);//加粗
			// txtContent.setText(txt[1]);
			txtContent.setGravity(Gravity.CENTER_VERTICAL);
			holder.content = txtContent;

			linearLayout2.addView(txtContent, layoutParams);
		}

		if (isDateVisiable) {

			TextView txtDate = new TextView(context);
		//	txtDate.setPadding(120, 0, 0, 0);
			layoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.weight = 1;
			txtDate.setGravity(Gravity.RIGHT);
			txtDate.setTextColor(Color.BLACK);
			txtDate.setTextSize(14);
			txtDate.setWidth(50);
			// txtDate.getPaint().setFakeBoldText(true);//加粗
			// txtDate.setText(txt[2]);
			// txtDate.setGravity(Gravity.CENTER_VERTICAL);
			holder.date = txtDate;

			linearLayout2.addView(txtDate, layoutParams);
		}

		if (isContentVisiable || isDateVisiable) {

			linearLayout1.addView(linearLayout2);
		}

		linearLayoutFather.addView(relativeLayoutSon);

		holder.id = new TextView(context);
		holder.id.setVisibility(View.GONE);
		linearLayoutFather.addView(holder.id);

		return linearLayoutFather;
	}

	/**
	 * 复选框的单击事件
	 */
	private final OnCheckedChangeListener chkChoiceListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			String id = buttonView.getTag().toString();
			if (isChecked && !chkChoice.contains(id)) {
				chkChoice.add(id);
			}
			if (!isChecked) {
				chkChoice.remove(id);
			}
			if (chkChoice.size() == data.size()) {
				// 全选按钮被选中
				DataSyncActivity.chkChoiceAll.setChecked(true);
			} else {
				// 全选按钮取消选中
				DataSyncActivity.chkChoiceAll.setChecked(false);
			}

		}
	};

	/**
	 * FileName: ListActivityAdapter.java Description: 用来绑定数据的实体类
	 * 
	 * @author 王留庚
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-11-30 上午11:36:08
	 */
	public class ViewHolder {

		public TextView id = null;
		public TextView title = null;
		public TextView content = null;
		TextView date = null;
		ImageView lefticon = null;
		ImageView righticon = null;
		CheckBox chkchoice = null;

	}

	/**
	 * Description:
	 * 
	 * @return LinkedList<String>
	 * @author Administrator Create at: 2013-1-10 下午01:34:38
	 */
	public LinkedList<String> getchkchoice() {
		return chkChoice;
	}

}
