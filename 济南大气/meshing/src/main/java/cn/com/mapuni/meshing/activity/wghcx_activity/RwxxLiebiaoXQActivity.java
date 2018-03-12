package cn.com.mapuni.meshing.activity.wghcx_activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.HashMap;

import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshingtotal.R;

public class RwxxLiebiaoXQActivity extends BaseActivity {
	LinearLayout middleLayout;
	private Button submit_bu;
	EditText rwmc,address,qyxz,jd,wd,xcwt,wtjl,start_time,xuncha_person,xcrzw,xcrdw,
	wtjlsj,wurantype,fkyj,describe;
	String startDate = "", endDate = "";
	String statu = "xcjl";
	HashMap<String, Object> arr;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		statu = (String) getIntent().getSerializableExtra("statu");
		if(statu.endsWith("xcjl")){
			SetBaseStyle("案件处理详情");
		}else{
			SetBaseStyle("案件处理详情");
		}
		
		initView();
	}

	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		// 查询条件
		View mainView = inflater.inflate(R.layout.wd_rwxx_liebiao_layout, null);
		middleLayout.addView(mainView);
		submit_bu = (Button) mainView.findViewById(R.id.submit_bu);
		if(statu.equals("巡查记录")){
			statu = "已完成";
		}
		if(statu.equals("已完成")){
			submit_bu.setVisibility(View.GONE);
		}else{
			submit_bu.setVisibility(View.GONE);
		}
		
		arr = (HashMap<String, Object>) getIntent().getSerializableExtra(
				"arr");
		//获取控件  添加值
		rwmc = (EditText) mainView.findViewById(R.id.rwmc);
		address = (EditText) mainView.findViewById(R.id.address);
		qyxz = (EditText) mainView.findViewById(R.id.qyxz);
		jd = (EditText) mainView.findViewById(R.id.jd);
		wd = (EditText) mainView.findViewById(R.id.wd);
		xcwt = (EditText) mainView.findViewById(R.id.xcwt);
		wtjl = (EditText) mainView.findViewById(R.id.wtjl);
		start_time = (EditText) mainView.findViewById(R.id.start_time);
		xuncha_person = (EditText) mainView.findViewById(R.id.xuncha_person);
		xcrzw = (EditText) mainView.findViewById(R.id.xcrzw);
		xcrdw = (EditText) mainView.findViewById(R.id.xcrdw);
		wtjlsj = (EditText) mainView.findViewById(R.id.wtjlsj);
		wurantype = (EditText) mainView.findViewById(R.id.wurantype);
		fkyj = (EditText) mainView.findViewById(R.id.fkyj);
		describe = (EditText) mainView.findViewById(R.id.describe);
		
		rwmc.setText(arr.get("entname")+"");
		address.setText(arr.get("entaddress")+"");
		qyxz.setText(arr.get("qyxz")+"");
		jd.setText(arr.get("longitude")+"");
		wd.setText(arr.get("latitude")+"");
		xcwt.setText(arr.get("xcwt")+"");
		wtjl.setText(arr.get("wtjl")+"");
		start_time.setText(arr.get("xcwtsj")+"");
		xuncha_person.setText(arr.get("wgfzr")+"");
		xcrzw.setText(arr.get("wgfzrzw")+"");
		xcrdw.setText(arr.get("wgfzrdw")+"");
		wtjlsj.setText(arr.get("wtjlsj")+"");
		wurantype.setText(arr.get("pollutiontype")+"");
		fkyj.setText(arr.get("feedbackattachment")+"");
		describe.setText(arr.get("remark")+"");
	}
	
}
