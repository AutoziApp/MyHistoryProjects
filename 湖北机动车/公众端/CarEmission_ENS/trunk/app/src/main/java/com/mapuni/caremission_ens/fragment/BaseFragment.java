package com.mapuni.caremission_ens.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.activity.MainActivity;
import com.mapuni.caremission_ens.activity.NewsActivity;
import com.mapuni.caremission_ens.activity.QRActivity;
import com.mapuni.caremission_ens.app.MyApplication;
import com.mapuni.caremission_ens.presenter.NetControl;
import com.mapuni.caremission_ens.qrcode.CaptureActivity;
import com.mapuni.caremission_ens.views.NewsBar;
import com.mapuni.caremission_ens.views.YutuLoading;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class BaseFragment extends SupportFragment implements View.OnClickListener,EasyPermissions.PermissionCallbacks{
    private static final int RC_CAMERA = 122;
    public Activity mAct;
    TextView titleView;
    NewsBar newsBar;
    NetControl mControl = new NetControl();
    YutuLoading yutuLoading ;
    ImageView moreImg;
    Handler handler;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAct = (Activity) context;
        yutuLoading = new YutuLoading(mAct);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    public void setTitle(View view,String title){
         titleView = (TextView) view.findViewById(R.id.titleView);
         newsBar = (NewsBar) view.findViewById(R.id.newsBar);
         moreImg = (ImageView) view.findViewById(R.id.more);
         newsBar.setOnClickListener(this);
         moreImg.setOnClickListener(this);
         newsBar.setMessageCount(MyApplication.getUpDateCount());
         titleView.setText(title);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showFailed(){
        View view = getView();
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.showFailed);
        layout.setOnClickListener(this);
        layout.setVisibility(View.VISIBLE);
    }
    public void requestAgain(){
        View view = getView();
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.showFailed);
        if(layout.getVisibility()==View.VISIBLE)
            layout.setVisibility(View.GONE);
    }
//    延时发送
    public void delayedPost(Runnable runnable,int time){
        if(handler==null)
            handler = new Handler();
        handler.postDelayed(runnable, time);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more:
                showPoupMenu();
                break;
            case R.id.newsBar:
                startActivity(new Intent(mAct, NewsActivity.class));
                break;
        }
    }
   public void showPoupMenu(){
       if(moreImg.getVisibility()==View.GONE||moreImg.getVisibility()==View.INVISIBLE)
           return;
       PopupMenu popup = new PopupMenu(getActivity(), moreImg);

       //Inflating the Popup using xml file
       popup.getMenuInflater()
               .inflate(R.menu.popup_menu, popup.getMenu());
       //registering popup with OnMenuItemClickListener
       popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
           public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.sao:
                        doStartActivity();
                        break;
                }
               return true;
           }
       });

       popup.show(); //showing popup menu
   }
    @AfterPermissionGranted(RC_CAMERA)
    private void doStartActivity() {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
            startActivity(new Intent(mAct, QRActivity.class));
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(mAct,"扫一扫需要相机权限",RC_CAMERA, Manifest.permission.CAMERA);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, mAct);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        startActivity(new Intent(mAct, QRActivity.class));
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
