package com.mapuni.mobileenvironment.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.app.DataApplication;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.fragment.HomeFragment;
import com.mapuni.mobileenvironment.fragment.MainFragment;
import com.mapuni.mobileenvironment.fragment.OAFragment;
import com.mapuni.mobileenvironment.fragment.SupportFragment;
import com.mapuni.mobileenvironment.model.LoginModel;
import com.mapuni.mobileenvironment.utils.DialogManager;
import com.mapuni.mobileenvironment.view.BottomBar;
import com.mapuni.mobileenvironment.view.BottomBarTab;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener,SupportFragment.OnFragmentInteractionListener {
    SupportFragment currentFragment;
    DrawerLayout mDrawerLayout;
    NavigationView mNavView;
    Toolbar toolbar;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataApplication.translucentStatusBar(this);
//       监测更新
        LoginModel.update(this,0);
        initView();
        MainFragment fragment = SupportFragment.newInstance();
        fragment.start(getSupportFragmentManager(),
                R.id.fl_container,fragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        outState.putInt("variable", 0);
    }

    private void initView(){

        mNavView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        change
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        title = (TextView) findViewById(R.id.toolTitle);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_title);
        toolbar.setOnMenuItemClickListener(this);
        mNavView.setNavigationItemSelectedListener(new OnMenuCilck());
        setVersion(mNavView);
    }
    public void setVersion(NavigationView mNavView){
        Menu _Menu = mNavView.getMenu();
        MenuItem _MenuItem= _Menu.findItem(R.id.updateBtn);
        String versionName="";
        try {
            PackageInfo pi=getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        _MenuItem.setTitle(_MenuItem.getTitle()+"  v"+versionName);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        change
        getMenuInflater().inflate(R.menu.menu_title,menu);

        return true;
    }
    public SupportFragment getCurrentFragment() {
        return currentFragment;
    }
    public void setCurrentFragment(SupportFragment currentFragment) {
        this.currentFragment = currentFragment;
    }
    private class OnMenuCilck implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case R.id.setAdress:
                    DialogManager.showDialog(R.layout.set_adress_dialog,"设置后台地址",
                            R.mipmap.set_adress,MainActivity.this,new DialogClick());
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                    break;
                case R.id.updateBtn:
                    LoginModel.update(MainActivity.this,1);
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                    break;
            }
            return false;
        }
    }
    private class DialogClick implements DialogManager.DialogListaner{
        @Override
        public void onClick(String s) {
           if(s.equals("-1")){
           }else{
               PathManager.setIp(MainActivity.this,s);
           }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toggle:
                if(!mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
//                change
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
                break;
        }
        return false;
    }

    @Override
    public void onChangeTitle(String s) {
        title.setText(s);
    }

    @Override
    public void onCloseToolbar(boolean isClose) {
        if(isClose){
            toolbar.setVisibility(View.GONE);
        }else{
            toolbar.setVisibility(View.VISIBLE);
        }

    }
}
