package com.jy.environment.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.model.BiaoGeModel;
import com.jy.environment.model.GradeModel;
import com.jy.environment.util.MyLog;

public class SettingDengJIActivity extends ActivityBase implements
		OnClickListener {
	private TextView dengji_tv1, dengji_tv2, dengji_tv3, dengji_weibaobi;
	private ListView dengji_lv1;
	private List<BiaoGeModel> bModels;
	private MyAdapter adapter;
	private ImageView dengji_back;
	private GradeModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.biaoge_dengji);
		bModels = new ArrayList<BiaoGeModel>();
		model = getIntent().getParcelableExtra("model");
		try {
		init();
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
	}

	private void init() {
		dengji_tv1 = (TextView) findViewById(R.id.dengji_tv1);
		dengji_tv1.setText("L" + model.getLevel());
		dengji_tv2 = (TextView) findViewById(R.id.dengji_tv2);
		dengji_tv2.setText(model.getExp());
		dengji_tv3 = (TextView) findViewById(R.id.dengji_tv3);
		dengji_tv3.setText("/" + model.getFull());
		dengji_lv1 = (ListView) findViewById(R.id.dengji_lv1);
		dengji_back = (ImageView) findViewById(R.id.dengji_back);
		dengji_back.setOnClickListener(this);
		dengji_weibaobi = (TextView) findViewById(R.id.dengji_weibaobi);
		SpannableString msp = new SpannableString("(什么是微保币?)");
		msp.setSpan(new UnderlineSpan(), 1, 8,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ColorStateList csllink = null;
		ColorStateList csl = null;
		XmlResourceParser xppcolor = getResources().getXml(R.drawable.bt_link);
		try {
			csl = ColorStateList.createFromXml(getResources(), xppcolor);
		} catch (XmlPullParserException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		XmlResourceParser xpplinkcolor = getResources().getXml(
				R.drawable.bt_link);
		try {
			csllink = ColorStateList
					.createFromXml(getResources(), xpplinkcolor);
		} catch (XmlPullParserException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		msp.setSpan(new TextAppearanceSpan("monospace",
				android.graphics.Typeface.BOLD_ITALIC, 30, csl, csllink), 1,
				8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		dengji_weibaobi.setText(msp);
		dengji_weibaobi.setOnClickListener(this);
		bModels = DataSupport.findAll(BiaoGeModel.class);
		if (bModels == null || bModels.size() != 0) {
			MyLog.i(">>>>>>>>>>tv1" + "1" + bModels.size());
			adapter = new MyAdapter();
			adapter.bindData(bModels);
			dengji_lv1.setAdapter(adapter);
		} else {
			MyLog.i(">>>>>>>>>>tv1" + "2");
			BiaoGeModel model = new BiaoGeModel();
			model.setGrade("等级");
			model.setMoney("所需经验");
			model.setAward("升级奖励");
			bModels.add(model);
			xunhuan();
			DataSupport.saveAll(bModels);
			adapter = new MyAdapter();
			adapter.bindData(bModels);
			dengji_lv1.setAdapter(adapter);
		}

	}

	void xunhuan() {
		for (int i = 0; i <= 60; i++) {
			BiaoGeModel model2 = new BiaoGeModel();
			if (i == 0) {
				model2.setGrade("L0");
				model2.setMoney("0");
				model2.setAward("");
			} else if (i == 1) {
				model2.setGrade("L1");
				model2.setMoney("100");
				model2.setAward("");
			} else if (i == 2) {
				model2.setGrade("L2");
				model2.setMoney("340");
				model2.setAward("");
			} else if (i == 3) {
				model2.setGrade("L3");
				model2.setMoney("760");
				model2.setAward("");
			} else if (i == 4) {
				model2.setGrade("L4");
				model2.setMoney("1400");
				model2.setAward("20个微保币");
			} else if (i == 5) {
				model2.setGrade("L5");
				model2.setMoney("2300");
				model2.setAward("30个微保币");
			} else if (i == 6) {
				model2.setGrade("L6");
				model2.setMoney("3500");
				model2.setAward("34个微保币");
			} else if (i == 7) {
				model2.setGrade("L7");
				model2.setMoney("5040");
				model2.setAward("");
			} else if (i == 8) {
				model2.setGrade("L8");
				model2.setMoney("6960");
				model2.setAward("40个微保币");
			} else if (i == 9) {
				model2.setGrade("L9");
				model2.setMoney("9300");
				model2.setAward("56个微保币");
			} else if (i == 10) {
				model2.setGrade("L10");
				model2.setMoney("12100");
				model2.setAward("");
			} else if (i == 11) {
				model2.setGrade("L11");
				model2.setMoney("15400");
				model2.setAward("");
			} else if (i == 12) {
				model2.setGrade("L12");
				model2.setMoney("19240");
				model2.setAward("80个微保币");
			} else if (i == 13) {
				model2.setGrade("L13");
				model2.setMoney("23660");
				model2.setAward("120个微保币");
			} else if (i == 14) {
				model2.setGrade("L14");
				model2.setMoney("28700");
				model2.setAward("");
			} else if (i == 15) {
				model2.setGrade("L15");
				model2.setMoney("34400");
				model2.setAward("");
			} else if (i == 16) {
				model2.setGrade("L16");
				model2.setMoney("40800");
				model2.setAward("");
			} else if (i == 17) {
				model2.setGrade("L17");
				model2.setMoney("47940");
				model2.setAward("158个微保币");
			} else if (i == 18) {
				model2.setGrade("L18");
				model2.setMoney("55860");
				model2.setAward("184个微保币");
			} else if (i == 19) {
				model2.setGrade("L19");
				model2.setMoney("64600");
				model2.setAward("");
			} else if (i == 20) {
				model2.setGrade("L20");
				model2.setMoney("74200");
				model2.setAward("");
			} else if (i == 21) {
				model2.setGrade("L21");
				model2.setMoney("84700");
				model2.setAward("");
			} else if (i == 22) {
				model2.setGrade("L22");
				model2.setMoney("96140");
				model2.setAward("");
			} else if (i == 23) {
				model2.setGrade("L23");
				model2.setMoney("108560");
				model2.setAward("");
			} else if (i == 24) {
				model2.setGrade("L24");
				model2.setMoney("122000");
				model2.setAward("");
			} else if (i == 25) {
				model2.setGrade("L25");
				model2.setMoney("136500");
				model2.setAward("微保定制版口罩一个，500个微保币");
			} else if (i == 26) {
				model2.setGrade("L26");
				model2.setMoney("152100");
				model2.setAward("");
			} else if (i == 27) {
				model2.setGrade("L27");
				model2.setMoney("168840");
				model2.setAward("");
			} else if (i == 28) {
				model2.setGrade("L28");
				model2.setMoney("186760");
				model2.setAward("");
			} else if (i == 29) {
				model2.setGrade("L29");
				model2.setMoney("205900");
				model2.setAward("");
			} else if (i == 30) {
				model2.setGrade("L30");
				model2.setMoney("226300");
				model2.setAward("800个微保币");
			} else if (i == 31) {
				model2.setGrade("L31");
				model2.setMoney("248000");
				model2.setAward("");
			} else if (i == 32) {
				model2.setGrade("L32");
				model2.setMoney("271040");
				model2.setAward("");
			} else if (i == 33) {
				model2.setGrade("L33");
				model2.setMoney("295460");
				model2.setAward("");
			} else if (i == 34) {
				model2.setGrade("L34");
				model2.setMoney("321300");
				model2.setAward("");
			} else if (i == 35) {
				model2.setGrade("L35");
				model2.setMoney("348600");
				model2.setAward("微保定制版口罩一个，1000个微保币");
			} else if (i == 36) {
				model2.setGrade("L36");
				model2.setMoney("377400");
				model2.setAward("");
			} else if (i == 37) {
				model2.setGrade("L37");
				model2.setMoney("407740");
				model2.setAward("");
			} else if (i == 38) {
				model2.setGrade("L38");
				model2.setMoney("439660");
				model2.setAward("");
			} else if (i == 39) {
				model2.setGrade("L39");
				model2.setMoney("473200");
				model2.setAward("");
			} else if (i == 40) {
				model2.setGrade("L40");
				model2.setMoney("508400");
				model2.setAward("1200个微保币");
			} else if (i == 41) {
				model2.setGrade("L41");
				model2.setMoney("545300");
				model2.setAward("");
			} else if (i == 42) {
				model2.setGrade("L42");
				model2.setMoney("583940");
				model2.setAward("");
			} else if (i == 43) {
				model2.setGrade("L43");
				model2.setMoney("624360");
				model2.setAward("");
			} else if (i == 44) {
				model2.setGrade("L44");
				model2.setMoney("666600");
				model2.setAward("");
			} else if (i == 45) {
				model2.setGrade("L45");
				model2.setMoney("710700");
				model2.setAward("1400个微保币");
			} else if (i == 46) {
				model2.setGrade("L46");
				model2.setMoney("756700");
				model2.setAward("");
			} else if (i == 47) {
				model2.setGrade("L47");
				model2.setMoney("804640");
				model2.setAward("");
			} else if (i == 48) {
				model2.setGrade("L48");
				model2.setMoney("854560");
				model2.setAward("");
			} else if (i == 49) {
				model2.setGrade("L49");
				model2.setMoney("906500");
				model2.setAward("");
			} else if (i == 50) {
				model2.setGrade("L50");
				model2.setMoney("960500");
				model2.setAward("1600个微保币");
			} else if (i == 51) {
				model2.setGrade("L51");
				model2.setMoney("1016600");
				model2.setAward("");
			} else if (i == 52) {
				model2.setGrade("L52");
				model2.setMoney("1074840");
				model2.setAward("");
			} else if (i == 53) {
				model2.setGrade("L53");
				model2.setMoney("1135260");
				model2.setAward("");
			} else if (i == 54) {
				model2.setGrade("L54");
				model2.setMoney("1197900");
				model2.setAward("");
			} else if (i == 55) {
				model2.setGrade("L55");
				model2.setMoney("1262800");
				model2.setAward("微保定制版口罩一个，2000个微保币");
			} else if (i == 56) {
				model2.setGrade("L56");
				model2.setMoney("1330000");
				model2.setAward("");
			} else if (i == 57) {
				model2.setGrade("L57");
				model2.setMoney("1399540");
				model2.setAward("");
			} else if (i == 58) {
				model2.setGrade("L58");
				model2.setMoney("1471460");
				model2.setAward("");
			} else if (i == 59) {
				model2.setGrade("L59");
				model2.setMoney("1545800");
				model2.setAward("");
			} else {
				model2.setGrade("L60");
				model2.setMoney("1622600");
				model2.setAward("微保神秘大礼包一份，2200个微保币");
			}
			bModels.add(model2);
		}
	}

	class MyAdapter extends BaseAdapter {
		private List<BiaoGeModel> bModels;

		void bindData(List<BiaoGeModel> bModels) {
			this.bModels = bModels;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bModels.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return bModels.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			 try {
					MyLog.i("weibao result:" + bModels.get(position));
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(SettingDengJIActivity.this)
						.inflate(R.layout.biaoge_item, null);
				holder = new ViewHolder();
				holder.item_tv1 = (TextView) convertView
						.findViewById(R.id.item_tv1);
				holder.item_tv2 = (TextView) convertView
						.findViewById(R.id.item_tv2);
				holder.item_tv3 = (TextView) convertView
						.findViewById(R.id.item_tv3);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 120);
			convertView.setLayoutParams(lp);
			BiaoGeModel model = bModels.get(position);

			holder.item_tv1.setText(model.getGrade());
			holder.item_tv2.setText(model.getMoney());
			holder.item_tv3.setText(model.getAward());
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
			return convertView;
		}
	}

	static class ViewHolder {
		TextView item_tv1;
		TextView item_tv2;
		TextView item_tv3;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dengji_back:
			finish();
			break;
		case R.id.dengji_weibaobi:
			Intent intent = new Intent(SettingDengJIActivity.this,
					SettingCaifuActivity.class);
			intent.putExtra("model", SettingDengJIActivity.this.model);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
