package cn.com.mapuni.meshing.activity.wd_activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.example.meshing.R;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SqliteUtil;

/**
 * 一企一档数据设置
 * 
 * @author
 * 
 */
public class SetData {
	EmergencyFujianAdapter emergencyfujianadapter;
	MyListView fujian_listview; 
	public ArrayList<HashMap<String, Object>> fujian_list = new ArrayList<HashMap<String, Object>>();
	/** 企业基本信息填充数据 */
	public void InfoFindViewQYJBXX(final Context context,final HashMap<String, Object> arr, View viewQYJBXX) {
		if (null != arr && arr.size() > 0) { 
			EditText qymc = ((EditText) viewQYJBXX.findViewById(R.id.qymc));
			EditText ssqx = ((EditText) viewQYJBXX.findViewById(R.id.ssqx));
			EditText kzjb = ((EditText) viewQYJBXX.findViewById(R.id.kzjb));
			EditText sfsfxy = ((EditText) viewQYJBXX.findViewById(R.id.sfsfxy));
			EditText zdpwdw = ((EditText) viewQYJBXX.findViewById(R.id.zdpwdw));
			EditText qydz = ((EditText) viewQYJBXX.findViewById(R.id.qydz));
			EditText jd = ((EditText) viewQYJBXX.findViewById(R.id.jd));
			EditText wd = ((EditText) viewQYJBXX.findViewById(R.id.wd));
//			EditText wz_xzlb_et = ((EditText) viewQYJBXX.findViewById(R.id.wz_xzlb_et));
//			EditText wz_fxylx_et = ((EditText) viewQYJBXX.findViewById(R.id.wz_fxylx_et));
//			EditText wz_cqmj_et = ((EditText) viewQYJBXX.findViewById(R.id.wz_cqmj_et));
//			EditText wz_dwlx_et = ((EditText) viewQYJBXX.findViewById(R.id.wz_dwlx_et));
//			EditText wz_yabz_et = ((EditText) viewQYJBXX.findViewById(R.id.wz_yabz_et));  
//			EditText wz_yaba_et = ((EditText) viewQYJBXX.findViewById(R.id.wz_yaba_et));
//			EditText wz_hbry_et = ((EditText) viewQYJBXX.findViewById(R.id.wz_hbry_et));
//			EditText wz_zc_et = ((EditText) viewQYJBXX.findViewById(R.id.wz_zc_et));
//			EditText wz_yddh_et = ((EditText) viewQYJBXX.findViewById(R.id.wz_yddh_et));
//			EditText wz_gddh_et = ((EditText) viewQYJBXX.findViewById(R.id.wz_gddh_et));
//			EditText wz_tfsjcs_et = ((EditText) viewQYJBXX.findViewById(R.id.wz_tfsjcs_et));
//			EditText wz_sczt_et = ((EditText) viewQYJBXX.findViewById(R.id.wz_sczt_et));
//			fujian_listview = ((MyListView) viewQYJBXX.findViewById(R.id.fujian_listview));
//			fujian_list = new ArrayList<HashMap<String, Object>>();
//			//根据id查询附件
//			 String sql = "select * from Base_Attachment where FileUuid = '"+ arr.get("uploadfile") +"'";
//	         fujian_list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql.toString());
//	         emergencyfujianadapter = new EmergencyFujianAdapter("ya", fujian_list, context);
//	         fujian_listview.setAdapter(emergencyfujianadapter);
//	         updateData();
//			 //附件列表
//			 fujian_listview.setOnItemClickListener(new OnItemClickListener() {

//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//						long arg3) {
//					// TODO Auto-generated method stub
//					FileHelper fileHelper = new FileHelper();
//					fileHelper.showFileXin("yajal",fujian_list.get(arg2),arr,context);
//				}
//			 });
			//赋值
			qymc.setText(arr.get("entName") + "");
			ssqx.setText(arr.get("qx") + "");
			kzjb.setText("一级"/*arr.get("kzjb") + ""*/);
			sfsfxy.setText("是"/*arr.get("sffxy") + ""*/);
			zdpwdw.setText("否"/*arr.get("zdpwdw") + ""*/);
//			//根据code找名称
//			String sql1 = "select RegionName from T_Code_Region where RegionCode='"+ (arr.get("xzqh") + "").trim() +"'";
//			String xzqh = SqliteUtil.getInstance().getDepidByDepName(sql1);
//			wz_xzqh_et.setText(xzqh);// 行政区划
//			
			qydz.setText(arr.get("entAddress") + "");
			jd.setText(arr.get("longitude") + "");
			wd.setText(arr.get("latitude") + "");
//			//根据code找名称
//			String sql_hylb = "select RegionName from T_Code_Region where RegionCode='"+ (arr.get("hylbdm") + "").trim() +"' and ParentNode = '200000000'";
//			String hylbdm = SqliteUtil.getInstance().getDepidByDepName(sql_hylb);
//			wz_xzlb_et.setText(hylbdm);
//			//根据code找名称
//			String sql2 = "select RegionName from T_Code_Region where RegionCode='"+ (arr.get("fxylbdm") + "").trim() +"' and ParentNode = '300000000'";
//			String fxylx = SqliteUtil.getInstance().getDepidByDepName(sql2);
//			wz_fxylx_et.setText(fxylx);
//			wz_cqmj_et.setText(arr.get("cqmj") + "");
//			//根据code找名称
//			String sql3 = "select RegionName from T_Code_Region where RegionCode='"+ (arr.get("yjjydw") + "").trim() +"' and ParentNode = '400000000'";
//			String yjjydw = SqliteUtil.getInstance().getDepidByDepName(sql3);
//			wz_dwlx_et.setText(yjjydw);
//			//根据code找名称
//			String sql4 = "select RegionName from T_Code_Region where RegionCode='"+ (arr.get("hjyjyabz") + "").trim() +"' and ParentNode = '3000000000'";
//			String hjyjyabz = SqliteUtil.getInstance().getDepidByDepName(sql4);
//			wz_yabz_et.setText(hjyjyabz);
//			//根据code找名称
//			String sql5 = "select RegionName from T_Code_Region where RegionCode='"+ (arr.get("hjyjyaba") + "").trim() +"' and ParentNode = '4000000000'";;
//			String hjyjyaba = SqliteUtil.getInstance().getDepidByDepName(sql5);
//			wz_yaba_et.setText(hjyjyaba);
//			
//			wz_hbry_et.setText(arr.get("hbry1") + "");
//			wz_zc_et.setText(arr.get("zw1") + "");
//			wz_yddh_et.setText(arr.get("yddh1") + "");
//			wz_gddh_et.setText(arr.get("gddh1") + "");
//			wz_tfsjcs_et.setText(arr.get("lnfstfhjsjcs") + "");
//			//根据code找名称
//			String sql_sczt = "select RegionName from T_Code_Region where RegionCode='"+ (arr.get("sczt") + "").trim() +"' and ParentNode = '1600000000'";;
//			String sczt = SqliteUtil.getInstance().getDepidByDepName(sql_sczt);
//			wz_sczt_et.setText(sczt);
			
		}
	}
	/** 生产原料等填充数据 */
	public void InfoFindViewSCYL(final Context context,final int index, final ArrayList<HashMap<String, Object>> arr, View viewSCYL) {
		ListView listView = (ListView) viewSCYL.findViewById(R.id.yqyd_listview);
		YqydAdapter yqydAdapter = new YqydAdapter(index, arr, context); 
		listView.setAdapter(yqydAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
		     @Override
			 public void onItemClick(AdapterView<?> parent, View view, int
			       position, long id) {
		    	 Intent intent = null;
		    	 if(index == 1){
		    		 intent = new Intent(context,YqydScssyxqkxqActivity.class); 
		    		 intent.putExtra("emergency", arr.get(position));
			 		 context.startActivity(intent);
		    	 }else if(index == 2){
		    		 intent = new Intent(context,YqydWrwzlssxqActivity.class);
		    		 intent.putExtra("emergency", arr.get(position));
			 		 context.startActivity(intent);
		    	 }else if(index == 3){
		    		 intent = new Intent(context,YqydPwkxqActivity.class); 
		    		 intent.putExtra("emergency", arr.get(position));
			 		 context.startActivity(intent);
		    	 }
		 		 
		     }
		});
     
	}
	// 根据数据设置listView的高度
	public void setListViewHeightBasedOnChildren(ListView listView) {
		// ListAdapter listAdapter = listView.getAdapter();
		if (emergencyfujianadapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < emergencyfujianadapter.getCount(); i++) {
			View listItem = emergencyfujianadapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (emergencyfujianadapter.getCount() - 1));
		// ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		listView.setLayoutParams(params);
	}
	public void updateData() {
		try{
		    setListViewHeightBasedOnChildren(fujian_listview);
		}catch(Exception e){
			
		}
		emergencyfujianadapter.notifyDataSetChanged();
	}

}
