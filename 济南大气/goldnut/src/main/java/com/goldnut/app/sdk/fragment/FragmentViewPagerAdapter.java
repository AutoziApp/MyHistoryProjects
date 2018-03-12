package com.goldnut.app.sdk.fragment;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> mfragments;
	private FragmentManager fm;

	public FragmentViewPagerAdapter(FragmentManager fm , List<Fragment> fragments) {
		super(fm);
		this.fm = fm;
		this.mfragments = fragments;
	}

	@Override
	public Fragment getItem(int pos) {
		// TODO Auto-generated method stub
		return mfragments != null ? mfragments.get(pos) : null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mfragments != null ? mfragments.size() : 0;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public void setFragments(List<Fragment> fragments) {
		if (this.mfragments != null) {
			FragmentTransaction ft = fm.beginTransaction();
			for (Fragment f : this.mfragments) {
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			fm.executePendingTransactions();
		}
		this.mfragments = fragments;
		notifyDataSetChanged();
	}
}

