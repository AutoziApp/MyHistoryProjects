package com.mapuni.caremission_ens.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


import com.mapuni.caremission_ens.activity.MainActivity;

import java.util.HashMap;
import java.util.List;


public class SupportFragment extends Fragment {
    // LaunchMode
    public static final int STANDARD = 0;
    public static final int SINGLETOP = 1;
    public static final int SINGLETASK = 2;

    static final String ARG_IS_SHARED_ELEMENT = "fragmentation_arg_is_shared_element";

    public static final int TYPE_ADD = 0;
    public static final int TYPE_ADD_WITH_POP = 1;
    public static final int TYPE_ADD_RESULT = 2;

    private boolean mIsRoot, mIsSharedElement;
    private int mContainerId;   // 该Fragment所处的Container的id

    static final String ARG_IS_ROOT = "fragmentation_arg_is_root";
    static final String FRAGMENTATION_ARG_CONTAINER = "fragmentation_arg_container";
    public HashMap<String,String> stationMap;
    public HashMap<String,String> dataMap;
    public interface OnListListener {
        // TODO: Update argument type and name
    }


    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            mIsRoot = bundle.getBoolean(ARG_IS_ROOT, false);
            mIsSharedElement = bundle.getBoolean(ARG_IS_SHARED_ELEMENT, false);
            mContainerId = bundle.getInt(FRAGMENTATION_ARG_CONTAINER);
        }
    }

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    void loadMultipleRootTransaction(FragmentManager fragmentManager, int containerId, int showPosition, SupportFragment... tos) {
        FragmentTransaction ft = fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        for (int i = 0; i < tos.length; i++) {
            SupportFragment to = tos[i];

    //        bindContainerId(containerId, tos[i]);

            String toName = to.getClass().getName();
            ft.add(containerId, to, toName);

            if (i != showPosition) {
                ft.hide(to);
            }

            Bundle bundle = to.getArguments();
            bundle.putBoolean(ARG_IS_ROOT, true);
        }

        ft.commit();
    }
    MainActivity _mActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this._mActivity = (MainActivity)activity;


    }
    /**
     * 加载多个根Fragment
     */
    public void loadMultipleRootTransaction( int containerId, int showPosition, SupportFragment... tos) {
        FragmentManager fragmentManager = _mActivity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
      //  ft.setCustomAnimations(R.anim.h_fragment_enter,R.anim.h_fragment_exit);
        for (int i = 0; i < tos.length; i++) {
            SupportFragment to = tos[i];

            bindContainerId(containerId, tos[i]);

            String toName = to.getClass().getName();
            ft.add(containerId, to, toName);

            if (i != showPosition) {
                ft.hide(to);
            }

            Bundle bundle = to.getArguments();
            bundle.putBoolean(ARG_IS_ROOT, true);
        }

        ft.commit();
    }

    private void bindContainerId(int containerId, SupportFragment to) {
        Bundle args = to.getArguments();
        if (args == null) {
            args = new Bundle();
            to.setArguments(args);
        }
        args.putInt(FRAGMENTATION_ARG_CONTAINER, containerId);
    }

    @SuppressWarnings("unchecked")
    <T extends SupportFragment> T findStackFragment(FragmentManager fragmentManager, Class<T> fragmentClass, boolean isChild) {
        Fragment fragment = null;
        if (isChild) {
            // 如果是 查找子Fragment,则有可能是在FragmentPagerAdapter/FragmentStatePagerAdapter中,这种情况下,
            // 它们的Tag是以android:switcher开头,所以这里我们使用下面的方式
            List<Fragment> childFragmentList = fragmentManager.getFragments();
            if (childFragmentList == null) return null;

            for (int i = childFragmentList.size() - 1; i >= 0; i--) {
                Fragment childFragment = childFragmentList.get(i);
                if (childFragment instanceof BaseFragment && childFragment.getClass().getName().equals(fragmentClass.getName())) {
                    fragment = childFragment;
                    break;
                }
            }
        } else {
            fragment = fragmentManager.findFragmentByTag(fragmentClass.getName());
        }
        if (fragment == null) {
            return null;
        }
        return (T) fragment;
    }




    public void start(FragmentManager fragmentManager, int containerId, BaseFragment fragment) {
        bindContainerId(containerId, fragment);
        String toName = fragment.getClass().getName();
        FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.add(containerId, fragment, toName);
            Bundle bundle = fragment.getArguments();
            bundle.putBoolean(ARG_IS_ROOT, true);
            ft.commit();
    }

    int getContainerId() {
        return mContainerId;
    }

    /**
     * show一个Fragment,hide一个Fragment ; 主要用于类似微信主页那种 切换tab的情况
     *
     * @param showFragment 需要show的Fragment
     * @param hideFragment 需要hide的Fragment
     */
    void showHideFragment(FragmentManager fragmentManager, BaseFragment showFragment, BaseFragment hideFragment, int show, int hide) {
        if (showFragment == hideFragment) return;
        // 如果show和hide的Fragment不是同一个
        FragmentTransaction ft = fragmentManager.beginTransaction();
//        Toast.makeText(_mActivity,"show:"+show+"hide"+hide, Toast.LENGTH_SHORT).show();
        int anim;
//        if(show<hide){
//            ft.setCustomAnimations(R.anim.h_fragment_enter,R.anim.h_fragment_pop_exit);
//        }else{
//            ft.setCustomAnimations(R.anim.h_fragment_pop_enter,R.anim.h_fragment_exit);
//        }
        if(!hideFragment.isVisible()){
            hideFragment = (BaseFragment) _mActivity.getCurrentFragment();
        }
        ft.hide(hideFragment).show(showFragment).commit();
    }

    /**
     * replace事务, 主要用于子Fragment之间的replace
     */
    void replaceTransaction(int containerId, FragmentManager fm, BaseFragment to, boolean addToBack, int show, int hide) {
        bindContainerId(containerId, to);
        FragmentTransaction ft =fm.beginTransaction();
//        if(show<hide){
//            ft.setCustomAnimations(R.anim.h_fragment_enter,R.anim.h_fragment_pop_exit);
//        }else{
//            ft.setCustomAnimations(R.anim.h_fragment_pop_enter,R.anim.h_fragment_exit);
//        }
//        ft.setCustomAnimations(R.anim.h_fragment_enter, R.anim.h_fragment_exit);
        ft.replace(containerId, to, to.getClass().getName());
        if (addToBack) {
            ft.addToBackStack(to.getClass().getName());
        }
        Bundle bundle = to.getArguments();
        bundle.putBoolean(ARG_IS_ROOT, true);
        ft.commit();
    }
    /**
     * replace事务, 主要用于子Fragment之间的replace
     */
    void replaceTransaction(int containerId, FragmentManager fm, BaseFragment to, boolean addToBack, Bundle bundle) {
        bindContainerId(containerId, to);
        FragmentTransaction ft =fm.beginTransaction();
//        ft.setCustomAnimations(R.anim.h_fragment_pop_enter, R.anim.h_fragment_pop_exit);
        ft.replace(containerId, to, to.getClass().getName());
        if (addToBack) {
            ft.addToBackStack(to.getClass().getName());
        }
        to.setArguments(bundle);
        ft.commit();
    }

//    void showHideFragment(FragmentManager fragmentManager, SupportFragment showFragment, SupportFragment hideFragment, boolean addToBack, Bundle bundle) {
//        if (showFragment == hideFragment) return;
//        // 如果show和hide的Fragment不是同一个
//        FragmentTransaction ft = fragmentManager.beginTransaction();
//        if(!showFragment.isAdded()){
//            ft.add(R.id.fl_tab_container,showFragment,showFragment.getClass().getName());
//        }
//
//        ft.setCustomAnimations(R.anim.h_fragment_pop_enter, R.anim.h_fragment_pop_exit);
//
//        if (addToBack) {
//            ft.addToBackStack(showFragment.getClass().getName());
//        }
//        showFragment.setArguments(bundle);
//
//        ft.hide(hideFragment).show(showFragment).commit();
//
//    }

}
