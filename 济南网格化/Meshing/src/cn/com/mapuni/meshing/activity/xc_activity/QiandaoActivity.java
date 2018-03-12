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
		SetBaseStyle("ǩ��");
		initView();
	}

	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		// ��ѯ����
		View mainView = inflater.inflate(R.layout.activity_qiandao, null);
		middleLayout.addView(mainView);
		rb_1=(RadioButton) mainView.findViewById(R.id.rb_1);	//ǩ��
		rb_2=(RadioButton) mainView.findViewById(R.id.rb_2);	//�㼣
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
			fragment1=new QiandaoFragment();		//ǩ����Fragment
			beginTransaction.replace(R.id.fl,fragment1);
			beginTransaction.commit();	
			break;
		case R.id.rb_2:
			hideFragment(beginTransaction);
			fragment2=new ZuJiFragment();			//�㼣��Fragment
			beginTransaction.replace(R.id.fl,fragment2);
			beginTransaction.commit();
			break;
		}
		
	}
	  /*
     * ȥ�������أ����е�Fragment
     * */
    private void hideFragment(FragmentTransaction transaction) {
        if (fragment1 != null) {
            //transaction.hide(f1);���ط���Ҳ����ʵ��ͬ����Ч����������һ��ʹ��ȥ��
            transaction.remove(fragment1);
        }
        if (fragment2 != null) {
            //transaction.hide(f2);
            transaction.remove(fragment2);
        }
        
    }

	
}
