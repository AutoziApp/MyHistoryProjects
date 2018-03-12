package com.yutu.car.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yutu.car.R;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.utils.JsonUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

public class CheckNoQualifiedActivity extends AppCompatActivity {
    private NetControl control;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_no_qualified);
        control=new NetControl();
        //control.requestForRowCheckNoQualified();
    }
    public StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
//            String s = e.toString();
        }

        @Override
        public void onResponse(String response, int id) {
            Log.d("lvcheng", "response>>>>>>>>>>" + response);
            Map map = JsonUtil.jsonToMap(response);
            String flag = map.get("flag").toString().substring(0, 1);

            if (flag.equals("1")) {

            }
        }
    } ;

}
