package cn.com.mapuni.meshing.activity.wd_activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.mapuni.meshing.adapter.XCPlanAdapter;
import cn.com.mapuni.meshing.model.XCPlanModel;
import cn.com.mapuni.meshing.model.XCPlanModel.RowsBean;
import cn.com.mapuni.meshing.util.DateUtils;

import com.example.meshing.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.dataprovider.JsonHelper;

public class XCPlanDetailActivity extends BaseActivity{
	private TextView tvQiyeName,tvQiyeStatus,tvPlanType,tvPlanDescription,tvTime,tvQiyeType;
	private XCPlanModel.RowsBean planBean;
	//企业类型
	String[] qyType={"工业企业","施工工地","餐饮单位","小散乱污","畜禽养殖","敏感点"
};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni1);	
		initView();
	}
	
	private void initView() {
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),"巡查计划详情");	
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout1));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.activity_xcplandetail, null);
		middleLayout.addView(mainView);
		
		tvQiyeName=(TextView) mainView.findViewById(R.id.tvQiyeName);
		tvQiyeStatus=(TextView) mainView.findViewById(R.id.tvQiyeStatus);
		tvPlanType=(TextView) mainView.findViewById(R.id.tvPlanType);
		tvPlanDescription=(TextView) mainView.findViewById(R.id.tvPlanDescription);
		tvTime=(TextView)mainView.findViewById(R.id.tv_time);
		tvQiyeType=(TextView) mainView.findViewById(R.id.tvQiyeType);
		
		planBean=(RowsBean) getIntent().getSerializableExtra("planBean");
		if(planBean!=null){
			tvQiyeName.setText(planBean.getEntname());
			tvQiyeStatus.setText(getStatusType(planBean.getStatus()));
			tvPlanType.setText(getPlanType(planBean.getPlanType()));
			tvPlanDescription.setText(planBean.getDescribe());
			tvTime.setText(DateUtils.long2DateString(planBean.getUpdateTime()));
			tvQiyeType.setText(planBean.getEnterpriseType());
		}
		
	}	
	
	/*
	 * 获取企业状态
	 * */
	private String getStatusType(int type) {
		String statusType = "";
		switch (type) {
		case 0:
			statusType="正常";
			break;
		case 1:
			statusType="停产";
			break;
		case 2:
			statusType="取缔";
			break;
		default:
			break;
		}
		return statusType;
	}
	/*
	 * 获取计划类型
	 * */
	private String getPlanType(int type) {
		String planType = "";
		switch (type) {
		case 0:
			planType="日计划";
			break;
		case 1:
			planType="周计划";
			break;
		case 2:
			planType="月计划";
			break;
		default:
			break;
		}
		return planType;
	}
	

	
	
}
