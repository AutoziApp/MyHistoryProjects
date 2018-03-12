package com.mapuni.caremission_ens.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.qrcode.CaptureActivity;

public class QRActivity extends BaseActivity {
    TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivityForResult(new Intent(this, CaptureActivity.class), 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        content = (TextView) findViewById(R.id.content);
        setTitle("扫描结果",true,false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            content.setText(data.getStringExtra(CaptureActivity.EXTRA_RESULT));
        } else {
            content.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.leftIcon:
                finish();
                break;
        }
    }
}
