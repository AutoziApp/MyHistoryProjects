package com.jy.environment.invitefriends;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.invitefriends.SideBar.OnTouchingLetterChangedListener;
import com.jy.environment.util.FileUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class RequestActivity extends ActivityBase implements OnClickListener {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	ProgressDialog prDialog;
	ImageView activity_main_suggest;
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	private PinyinComparator pinyinComparator;
	private ProgressBar sms_main_bar;
	RelativeLayout loading;
	private Button invitefriend_button1, invitefriend_button2;
	boolean flag = false;
	// 存取邀请的联系人电话号码
	SharedPreferences sharedPreference;
	private SharedPreferencesUtil mSpUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.invitefriends_request_activity);
		// LocationManager
		mSpUtil = SharedPreferencesUtil.getInstance(RequestActivity.this);

		sms_main_bar = (ProgressBar) findViewById(R.id.sms_main_bar);
		invitefriend_button1 = (Button) findViewById(R.id.invitefriend_button1);
		invitefriend_button2 = (Button) findViewById(R.id.invitefriend_button2);
		invitefriend_button1.setOnClickListener(this);
		invitefriend_button2.setOnClickListener(this);
		sharedPreference = getSharedPreferences("phone", Context.MODE_PRIVATE);
		activity_main_suggest = (ImageView) findViewById(R.id.activity_main_suggest);
		activity_main_suggest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		if(!mSpUtil.getSmsNotify())
		{
			sms_main_bar.setVisibility(View.GONE);
		}
		if (mSpUtil.getSmsNotify()) {
			new com.jy.environment.controls.AsyncTask<Void, Void, Void>() {

				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					// prDialog = new ProgressDialog(RequestActivity.this);
					// //
					// prDialog.setIcon(android.R.drawable.ic_menu_info_details);
					// // prDialog.setTitle("");
					// prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					// prDialog.setMessage("读取手机联系人，请稍候……");
					// prDialog.show();
					characterParser = CharacterParser.getInstance();

					pinyinComparator = new PinyinComparator();

					sideBar = (SideBar) findViewById(R.id.sidrbar);
					dialog = (TextView) findViewById(R.id.dialog);
					sideBar.setTextView(dialog);

					sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

						@Override
						public void onTouchingLetterChanged(String s) {
							int position = adapter.getPositionForSection(s
									.charAt(0));
							if (position != -1) {
								sortListView.setSelection(position);
							}

						}
					});
					sortListView = (ListView) findViewById(R.id.country_lvcountry);
					// sortListView.setOnItemClickListener(new
					// OnItemClickListener()
					// {
					//
					// @Override
					// public void onItemClick(AdapterView<?> parent, View view,
					// int position, long id) {
					// // TODO Auto-generated method stub
					// String content =
					// "微保可以实时查看所在城市空气质量，推荐你用一下。下载地址:www.weibao.net/download";
					// // Intent mIntent = new Intent(Intent.ACTION_VIEW);
					// // mIntent.putExtra("address", list.get(position)
					// // .getStrPhoneNumber());
					// // mIntent.putExtra("sms_body", content);
					// // mIntent.setType("vnd.android-dir/mms-sms");
					// // mContext.startActivity(mIntent);
					// Intent intent = new Intent(RequestActivity.this,
					// SmsActivity.class);
					// SortModel s1 = (SortModel) adapter.getItem(position);
					// intent.putExtra("phoneNumber",
					// s1.getName() + "<" + s1.getStrPhoneNumber()
					// + ">");
					// intent.putExtra("phoneContent", content);
					// RequestActivity.this.startActivity(intent);
					// }
					// });
				}

				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					if (WeiBaoApplication.getInstance().smsList.size() != 0) {
						SourceDateList = filledData(WeiBaoApplication
								.getInstance().smsList);

					} else {
						SourceDateList = filledData(ContactsUtils
								.searchContact(RequestActivity.this));
						WeiBaoApplication.getInstance().smsList = SourceDateList;
					}

					Collections.sort(SourceDateList, pinyinComparator);
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					// prDialog.dismiss();
					sms_main_bar.setVisibility(View.GONE);
					adapter = new SortAdapter(RequestActivity.this,
							SourceDateList);
					adapter.wenjian = FileUtils.read(getApplicationContext(),
							"phone.txt");
					sortListView.setAdapter(adapter);
					// prDialog.dismiss();
					mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

					mClearEditText.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							filterData(s.toString());
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {

						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});
				}

			}.execute();

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (adapter == null) {
			return;
		} else {
			adapter.wenjian = FileUtils.read(getApplicationContext(),
					"phone.txt");
			adapter.notifyDataSetChanged();
		}

		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(List<SortModel> list) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < list.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(list.get(i).getName());
			sortModel.setStrPhoneNumber(list.get(i).getStrPhoneNumber());
			String pinyin = characterParser.getSelling(list.get(i).getName());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		// prDialog.dismiss();
		return mSortList;

	}

	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.invitefriend_button1:
			if (sms_main_bar.getVisibility() != View.GONE) {
				return;
			}
			if (invitefriend_button1.getText().toString().trim().equals("全选")) {
				adapter.configCheckMap(true);

				adapter.notifyDataSetChanged();

				invitefriend_button1.setText("全不选");
			} else {

				// 所有项目全部不选中
				adapter.configCheckMap(false);
				adapter.notifyDataSetChanged();
				invitefriend_button1.setText("全选");

			}
			break;
		case R.id.invitefriend_button2:
			if (sms_main_bar.getVisibility() != View.GONE
					||SourceDateList==null|| SourceDateList.size() == 0||!mSpUtil.getSmsNotify()) {
				return;
			}
			Intent intent = new Intent(RequestActivity.this, SmsActivity.class);
			Map<Integer, Boolean> map = adapter.getCheckMap();
			// 获取当前的数据数量
			int size = adapter.getCount();
			int count = 0;

			ArrayList<SortModel> modelsList = new ArrayList<SortModel>();
			for (int i = 0; i < size; i++) {

				// 因为List的特性,删除了2个item,则3变成2,所以这里要进行这样的换算,才能拿到删除后真正的position
				int position = i - (size - adapter.getCount());

				if (map.get(i) != null && map.get(i)) {

					SortModel model = (SortModel) adapter.getItem(i);
					modelsList.add(model);
					count++;
				}
			}
			MyLog.i(">>>>>>>>count" + count);
			if (count == 0) {
				ToastUtil.showShort(getApplicationContext(), "邀请人不能为空");
				return;
			}
			intent.putParcelableArrayListExtra("lianxi", modelsList);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
