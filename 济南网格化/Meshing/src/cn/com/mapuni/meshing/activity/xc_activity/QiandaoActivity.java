package cn.com.mapuni.meshing.activity.xc_activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.meshing.R;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.overlay.MarkerOverlay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.mapuni.meshing.activity.gis.LocationService.CallBackAdrr;
import cn.com.mapuni.meshing.activity.gis.LocationService.CallBackAdrrPoint;
import cn.com.mapuni.meshing.activity.gis.MapBinder;
import cn.com.mapuni.meshing.model.PotrlObject;
import cn.com.mapuni.meshing.model.ShangBaoBuMen;
import cn.com.mapuni.meshing.util.DateTimePickDialogUtil;
import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QiandaoActivity extends BaseActivity implements OnClickListener {
	public static String TAG = "QiandaoActivity";
	public RadioButton rb_1,rb_2;
	private QiandaoFragment fragment1;
	private ZuJiFragment fragment2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SetBaseStyle("签到");
		initView();
	}

	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		// 查询条件
		View mainView = inflater.inflate(R.layout.activity_qiandao, null);
		middleLayout.addView(mainView);
		rb_1=(RadioButton) mainView.findViewById(R.id.rb_1);	//签到
		rb_2=(RadioButton) mainView.findViewById(R.id.rb_2);	//足迹
		rb_1.setOnClickListener(this);
		rb_2.setOnClickListener(this);
		
		fragment1 = new QiandaoFragment();
		FragmentTransaction bt = getSupportFragmentManager().beginTransaction();
		bt.replace(R.id.fl, fragment1);
		bt.commit();
		rb_1.setChecked(true);
	}

	@Override
	public void onClick(View v) {
		FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
		switch(v.getId()){
		case R.id.rb_1:
			hideFragment(beginTransaction);
			fragment1=new QiandaoFragment();		//签到的Fragment
			beginTransaction.replace(R.id.fl,fragment1);
			beginTransaction.commit();	
			break;
		case R.id.rb_2:
			hideFragment(beginTransaction);
			fragment2=new ZuJiFragment();			//足迹的Fragment
			beginTransaction.replace(R.id.fl,fragment2);
			beginTransaction.commit();
			break;
		}
		
	}
	  /*
     * 去除（隐藏）所有的Fragment
     * */
    private void hideFragment(FragmentTransaction transaction) {
        if (fragment1 != null) {
            //transaction.hide(f1);隐藏方法也可以实现同样的效果，不过我一般使用去除
            transaction.remove(fragment1);
        }
        if (fragment2 != null) {
            //transaction.hide(f2);
            transaction.remove(fragment2);
        }
        
    }

	
}
