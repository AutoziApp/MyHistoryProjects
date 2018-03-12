package com.jy.environment.fragment;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import com.google.gson.Gson;
import com.jy.environment.R;
import com.jy.environment.model.TongHuanBiBean;
import com.jy.environment.model.TongHuanBiBean.DetailBean.DataBean;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.HttpUtils;
import com.jy.environment.view.vhtableview.VHTableAdapter;
import com.jy.environment.view.vhtableview.VHTableView;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class DefaultTimeFragment extends Fragment {
	
	private String[] timeType={"day","month","year"};
	private VHTableView vht_table;
	private RadioGroup rg;
	Dialog dialog;
	
	public static DefaultTimeFragment newInstance() {
	    Bundle args = new Bundle();
	    DefaultTimeFragment fragment = new DefaultTimeFragment();
	    fragment.setArguments(args);
	    return fragment;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fm_xiaojian,container, false);
		initView(view);
		
		return view;
	}
	private void initView(View v) {
		// TODO Auto-generated method stub
		dialog = CommonUtil.getCustomeDialog(getActivity(),
				R.style.load_dialog, R.layout.custom_progress_dialog);
		dialog.setCanceledOnTouchOutside(true);
        vht_table=(VHTableView)v.findViewById(R.id.vht_table);
        rg=(RadioGroup) v.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_day:
					requestData(timeType[0]);
					break;
				case R.id.rb_month:
					requestData(timeType[1]);
					break;
				case R.id.rb_year:
					requestData(timeType[2]);
					break;

				default:
					break;
				}
			}

			
		});
        rg.check(R.id.rb_day);
	}

	private void requestData(String timeType) {
		// TODO Auto-generated method stub
		dialog.show();
		HttpUtils.getTongHuanBi(timeType, new StringCallback() {
			
			@Override
			public void onResponse(String response, int arg1) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				TongHuanBiBean thbBean=new Gson().fromJson(response, TongHuanBiBean.class);
				if(true==thbBean.getFlag()){//请求数据成功
					List<DataBean> list=thbBean.getDetail().getData();
					initVhTable(list);
				}else{
					Toast.makeText(getActivity(), "数据请求失败", 0).show();
				}
			}
			
			

			@Override
			public void onError(Call arg0, Exception e, int arg2) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Toast.makeText(getActivity(), "数据请求失败", 0).show();
			}
		});
	}
	
	private void initVhTable(List<DataBean> list) {
		// TODO Auto-generated method stub
		if(null==list||list.size()==0){
			return;
		}
      ArrayList<String> titleData=new ArrayList<String>();
      titleData.add("城市");
      titleData.add("SO2");
      titleData.add("NO2");
      titleData.add("CO");
      titleData.add("O3");
      titleData.add("PM10");
      titleData.add("PM2.5");
      titleData.add("综指");
      ArrayList<ArrayList<String>> contentData=new ArrayList<ArrayList<String>>();
      for(DataBean bean:list){
    	  ArrayList<String> contentRowData=new ArrayList<String>();
    	  contentRowData.add(bean.getCITYNAME());
    	  contentRowData.add(bean.getSO2());
    	  contentRowData.add(bean.getNO2());
    	  contentRowData.add(bean.getCO());
    	  contentRowData.add(bean.getO3());
    	  contentRowData.add(bean.getPM10());
    	  contentRowData.add(bean.getPM25());
    	  contentRowData.add(bean.getCOMPOSITE());
    	  contentData.add(contentRowData);
      }
      VHTableAdapter tableAdapter=new VHTableAdapter(getActivity(),titleData,contentData);
      //vht_table.setFirstColumnIsMove(true);//设置第一列是否可移动,默认不可移动
      //vht_table.setShowTitle(false);//设置是否显示标题行,默认显示
      //一般表格都只是展示用的，所以这里没做刷新，真要刷新数据的话，重新setadaper一次吧
      vht_table.setAdapter(tableAdapter);
      
	}
}
