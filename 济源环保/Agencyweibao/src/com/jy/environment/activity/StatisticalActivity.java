package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.fragment.DayTimeFragment;
import com.jy.environment.fragment.DefaultTimeFragment;
import com.jy.environment.fragment.MonthTimeFragment;
import com.jy.environment.fragment.RealTimeFragment;
import com.jy.environment.fragment.YearTimeFragment;

/**
 * 统计分析Activity
 * 
 * @ClassName: StatisticalActivity
 * @Description: TODO
 * @author tianfy
 * @date 2017-11-29 上午10:27:16
 */
public class StatisticalActivity extends ActivityBase implements
		OnCheckedChangeListener {
	//把五个fragment放入HashMap用自定义的索引进行维护
	private Map<Integer,Fragment> mFragments = new HashMap<Integer, Fragment>();
	//同时将自定义的索引放入List集合进行维护
	private List<Integer> index=new ArrayList<Integer>();
	public static final Integer FIRST = 0;
	public static final Integer SECOND = 1;
	public static final Integer THIRD = 2;
	public static final Integer FOURTH = 3;
	public static final Integer FIVE = 4;
	private FragmentManager supportFragmentManager;
	private RealTimeFragment realTimeFragment;
	private DayTimeFragment dayTimeFragment;
	private DefaultTimeFragment defaultTimeFragment;
	private MonthTimeFragment monthTimeFragment;
	private YearTimeFragment yearTimeFragment;
	private static final String SELECT_INDEX="SELECT_INDEX";
	private RadioGroup rgStatistical;
	private RadioButton rbRealTime;
	private RadioButton rbDayTime;
	private RadioButton rbDefaultTime;
	private RadioButton rbMonthTime;
	private RadioButton rbYearTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistical);
		rgStatistical = (RadioGroup) findViewById(R.id.rg_statistical);
		rbRealTime = (RadioButton) findViewById(R.id.rb_realTime);
		rbDayTime = (RadioButton) findViewById(R.id.rb_dayTime);
		rbDefaultTime = (RadioButton) findViewById(R.id.rb_defaultTime);
		rbMonthTime = (RadioButton) findViewById(R.id.rb_mothTime);
		rbYearTime = (RadioButton) findViewById(R.id.rb_yearTime);
		
		rgStatistical.setOnCheckedChangeListener(this);
		initFragment(savedInstanceState);

	}
	/**
	 *展示第一个fragment
	 * 同时将fangment放入HashMap
	 * 索引放入List集合
	 * @author tianfy
	 */
	private int selindex;
	private void initFragment(Bundle savedInstanceState) {
		supportFragmentManager = getSupportFragmentManager();
		//解决app进入后台，因内存不够，重新回到前台fragment覆盖显示的bug
		if (savedInstanceState!=null) {
			try {
				selindex = savedInstanceState.getInt(SELECT_INDEX);
				realTimeFragment = (RealTimeFragment) supportFragmentManager.findFragmentByTag(RealTimeFragment.class.getSimpleName());
				dayTimeFragment = (DayTimeFragment) supportFragmentManager.findFragmentByTag(DayTimeFragment.class.getSimpleName());
				defaultTimeFragment = (DefaultTimeFragment) supportFragmentManager.findFragmentByTag(DefaultTimeFragment.class.getSimpleName());
				monthTimeFragment = (MonthTimeFragment) supportFragmentManager.findFragmentByTag(MonthTimeFragment.class.getSimpleName());
				yearTimeFragment = (YearTimeFragment) supportFragmentManager.findFragmentByTag(YearTimeFragment.class.getSimpleName());
				checkedRadioButton(selindex);
			} catch (Exception e) {
				// TODO: handle exception
				checkedRadioButton(selindex);
			}
		}else {
			realTimeFragment = RealTimeFragment.newInstance();
			mFragments.put(FIRST, realTimeFragment);
			index.add(FIRST);
			rbRealTime.setChecked(true);
		}
	}
	private void checkedRadioButton(int checkId){
		switch (checkId) {
		case R.id.rb_realTime:
			rbRealTime.setChecked(true);
			break;
		case R.id.rb_dayTime:
			rbDayTime.setChecked(true);
			break;
		case R.id.rb_defaultTime:
			rbDefaultTime.setChecked(true);
			break;
		case R.id.rb_mothTime:
			rbMonthTime.setChecked(true);
			break;
		case R.id.rb_yearTime:
			rbYearTime.setChecked(true);
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_realTime:
			if (realTimeFragment == null) {
				realTimeFragment = RealTimeFragment.newInstance();
				mFragments.put(FIRST,realTimeFragment);
				index.add(FIRST);
			}
			showHideFragment(FIRST);
			break;
		case R.id.rb_dayTime:
			if (dayTimeFragment == null) {
				dayTimeFragment = DayTimeFragment.newInstance();
				mFragments.put(SECOND,dayTimeFragment);
				index.add(SECOND);
			}
			showHideFragment(SECOND);
			break;
		case R.id.rb_defaultTime:
			if (defaultTimeFragment == null) {
				defaultTimeFragment = DefaultTimeFragment.newInstance();
				mFragments.put(THIRD,defaultTimeFragment);
				index.add(THIRD);
			}
			showHideFragment(THIRD);
			break;
		case R.id.rb_mothTime:
			if (monthTimeFragment == null) {
				monthTimeFragment = MonthTimeFragment.newInstance();
				mFragments.put(FOURTH,monthTimeFragment);
				index.add(FOURTH);
			}
			showHideFragment(FOURTH);
			break;
		case R.id.rb_yearTime:
			if (yearTimeFragment == null) {
				 yearTimeFragment = YearTimeFragment.newInstance();
				 mFragments.put(FIVE,yearTimeFragment);
				 index.add(FIVE);
			}
			showHideFragment(FIVE);
			break;

		default:
			break;
		}
	}
	
	
	//解决app进入后台，因内存不够，重新回到前台fragment覆盖显示的bug
	@Override
	public void onSaveInstanceState(Bundle outState) {
		int checkedRadioButtonId = rgStatistical.getCheckedRadioButtonId();
	    outState.putInt(SELECT_INDEX,checkedRadioButtonId);
		super.onSaveInstanceState(outState);
	}
	/**
	 * 根据传入的要展示的position展示对应的fragment
	 * 该方法实现了初次点击每个fragment首先进行添加(add)操作
	 * 以后每次点击show/hindden
	 * @param showPosition
	 * @author tianfy
	 */
	public void showHideFragment(int showPosition) {
		FragmentTransaction ft = supportFragmentManager.beginTransaction();
		for (int i = 0; i < mFragments.size(); i++) {
			if (showPosition == index.get(i)) {
				//如果维护的索引中有和要展示的position相等，就判断要展示的fragment是否已经被添加到容器中；没有加添加到容器中。
				if (!mFragments.get(showPosition).isAdded()) {//
					ft.add(R.id.ll_fragment_container,mFragments.get(showPosition),mFragments.get(showPosition).getClass().getSimpleName());
					break;
				} else {//如果已经添加到容器中，判断要show的fragment是否隐藏，隐藏就展示。
					if (mFragments.get(showPosition).isHidden()) {
						ft.show(mFragments.get(showPosition));
					}
				}
			} else {//如果维护的索引和要展示的position不相等，就全部隐藏。
				if (!mFragments.get(index.get(i)).isHidden()) {
					ft.hide(mFragments.get(index.get(i)));
				}
			}
		}
		ft.commit();
	}
	
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN) {
			
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
