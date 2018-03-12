package com.mapuni.caremission_ens.activity;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.fragment.BaseFragment;
import com.mapuni.caremission_ens.fragment.MainFragment;
import com.mapuni.caremission_ens.fragment.SupportFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private static final int READ_SD = 2;
    BaseFragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            requestPermission();
        }
    }

    @AfterPermissionGranted(READ_SD)
    private void requestPermission() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            SupportFragment.newInstance().start(getSupportFragmentManager(),
                    R.id.fm_container, MainFragment.newInstance());
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, "该应用需要读写SD卡的权限，否则将无法运行。",
                    READ_SD, perms);
        }
    }
    public BaseFragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(BaseFragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        SupportFragment.newInstance().start(getSupportFragmentManager(),
                R.id.fm_container, MainFragment.newInstance());
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                getString(R.string.prompt_we_need_read_sd),
                R.string.setting, R.string.cancel, null, perms);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EasyPermissions.SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            requestPermission();
        }
    }
}
