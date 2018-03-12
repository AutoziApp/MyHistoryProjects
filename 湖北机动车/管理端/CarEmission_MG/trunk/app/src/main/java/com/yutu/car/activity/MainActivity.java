package com.yutu.car.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yutu.car.R;
import com.yutu.car.fragment.BaseFragment;
import com.yutu.car.fragment.MainFragment;
import com.yutu.car.fragment.SupportFragment;

public class MainActivity extends AppCompatActivity {
    BaseFragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            SupportFragment.newInstance().start(getSupportFragmentManager(),
                    R.id.fm_container, MainFragment.newInstance());
        }

    }
    public BaseFragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(BaseFragment currentFragment) {
        this.currentFragment = currentFragment;
    }
}
