package com.mapuni.android.oa;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.QueryListActivity;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;

public class EMAILDetailActivity extends TZGGDetailActivity {
	private ArrayList<HashMap<String, Object>> styleDetailed;
	private HashMap<String, Object> detaild;
	private Intent intent;
	Handler handler;
	Message message;
	ProgressDialog pd;
	private  final int SUCCESS=1;
	private  final int FILE=2;
	private  final int OVERTIME=3;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadDetailBottomMenu();
		downfilecloum="filename";
	}

	/**
	 * 加载详细信息
	 * @param itemId
	 */
	@Override
	protected void addcontent(String itemId) {
		// 得到查询项目的数量		
	    styleDetailed=businessObj.getStyleDetailed(this);
		HashMap<String, Object> querytj=new HashMap<String, Object>();
		querytj.put("id", itemId);
		detaild=businessObj.getDetailed(itemId);//通过id查询出数据
		
		int itemCount = styleDetailed.size();

		TableRow row = null;
		int counter=0;
		for (int index = 0; index < itemCount; index++) {
			// 逐行往 TableLayout 里填充控件
			TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rightLayoutParams.setMargins(0, 0, 15, 0);
			Map<String, Object> map = styleDetailed.get(index);			
			if(styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("name") && counter==0){
				counter++;
				continue;//name属性不需要添加到列表		
			}
			
			EditText editText=null;
			String value=null;
			if(bundle.getString("forwarding") != null&&index==1){//转发
				row = new TableRow(this);
				TextView textView2 = new TextView(this);	
				String hint2 = styleDetailed.get(index).get(XmlHelper.QUERY_HINT).toString();
				textView2.setTextColor(android.graphics.Color.BLACK);			
				textView2.setGravity(Gravity.RIGHT);
				textView2.setPadding(15, 0, 0, 0);
				textView2.setText(hint2);
				row.addView(textView2);
				editText = new EditText(this);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				editText.setFocusableInTouchMode(false);
				String textvalue=String.valueOf(detaild.get(value));									
				editText.setText(textvalue);//设置EditText内容
//				if(hint2.equals("内容：")){
//					rightLayoutParams = new TableRow.LayoutParams(
//							LayoutParams.WRAP_CONTENT, 400);
//					rightLayoutParams.setMargins(0, 0, 15, 0);
//				}
				row.addView(editText,rightLayoutParams);
				queryTable.addView(row);
			}
			    row = new TableRow(this);			
			    TextView textView = new TextView(this);			
			    //  左侧
			    String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT).toString();
			    textView.setTextColor(android.graphics.Color.BLACK);			
			    textView.setGravity(Gravity.RIGHT);
			    textView.setPadding(15, 0, 0, 0);
			    row.addView(textView);
				if(bundle.getString("read") != null){//阅读					
					textView.setText(hint);
					editText = new EditText(this);
					editText.setMaxEms(editText.getWidth());
					value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
					editText.setId(index); // 这里设置各个 EditText 的 Id, 读取数据构造 XPath
					// 表达式时调用
					editText.setFocusableInTouchMode(false);
					String textvalue=String.valueOf(detaild.get(value));									
					editText.setText(textvalue);//设置EditText内容
					row.addView(editText,rightLayoutParams);
					
				}
			if(bundle.getString("reply") != null){//回复				
				if("spinner".equals(map.get("style"))){
				textView.setText("收件人：");
				editText = new EditText(this);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				editText.setId(index); // 这里设置各个 EditText 的 Id, 读取数据构造 XPath
				editText.setFocusableInTouchMode(false);
				String textvalue=String.valueOf(detaild.get(value));	
				editText.setText(textvalue);//设置EditText内容
				row.addView(editText,rightLayoutParams);
				}else if("reply".equals(map.get("style"))){
					textView.setText("回复标题：");
					editText = new EditText(this);
					editText.setMaxEms(editText.getWidth());
					value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
					editText.setId(index); // 这里设置各个 EditText 的 Id, 读取数据构造 XPath
					editText.setFocusableInTouchMode(true);
					String textvalue=String.valueOf(detaild.get(value));	
					editText.setText(textvalue);//设置EditText内容
					row.addView(editText,rightLayoutParams);
				}else if("time".equals(map.get("style"))){
					textView.setText(hint);
					editText = new EditText(this);
					editText.setMaxEms(editText.getWidth());
					value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
					editText.setId(index); // 这里设置各个 EditText 的 Id, 读取数据构造 XPath
					editText.setFocusableInTouchMode(false);
					String textvalue=String.valueOf(detaild.get(value));	
					editText.setText(textvalue);//设置EditText内容
					row.addView(editText,rightLayoutParams);
				}else{
					textView.setText(hint);
					editText = new EditText(this);
					editText.setMaxEms(editText.getWidth());
					value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
					editText.setId(index); // 这里设置各个 EditText 的 Id, 读取数据构造 XPath
					editText.setFocusableInTouchMode(true);
					if(value.equals("msgtitle")||value.equals("xxneir")){
						editText.setText("");	
					}else{
					String textvalue=String.valueOf(detaild.get(value));									
					editText.setText(textvalue);//设置EditText内容
					}
//					if(hint.equals("内容：")){
//						rightLayoutParams = new TableRow.LayoutParams(
//								LayoutParams.WRAP_CONTENT, 400);
//						rightLayoutParams.setMargins(0, 0, 15, 0);
//					}
					row.addView(editText,rightLayoutParams);
				}
			  }
			   if(bundle.getString("forwarding") != null){//转发
				   if(styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString().equals("u_loginname")){ 
					   textView.setText("收件人：");
				   }else{
					   textView.setText(hint);
				   }				
					if("spinner".equals(map.get("style"))){
						Spinner spinner =getspinner(index,value,styleDetailed);
						row.addView(spinner,rightLayoutParams);
					}else{
						editText = new EditText(this);
						editText.setMaxEms(editText.getWidth());
						value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
						editText.setId(index); // 这里设置各个 EditText 的 Id, 读取数据构造 XPath
						editText.setFocusableInTouchMode(false);						
						String textvalue=String.valueOf(detaild.get(value));									
						editText.setText(textvalue);//设置EditText内容
//						if(hint.equals("内容：")){
//							rightLayoutParams = new TableRow.LayoutParams(
//									LayoutParams.WRAP_CONTENT, 400);
//							rightLayoutParams.setMargins(0, 0, 15, 0);
//						}
						row.addView(editText,rightLayoutParams);
					}	
				}			
			    queryTable.addView(row);
		} 
	}

	/**
	 * 得到收件人spinner
	 * 
	 */
	private Spinner getspinner(int index,String value,ArrayList<HashMap<String, Object>> styleDetailed){
		    Spinner spinner = new Spinner(this);				
			spinner.setPrompt("--请选择收件人--");
			spinner.setId(index);
			value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
			List<String> name=new ArrayList<String>();
			name.add("--请选择收件人--");
			List<HashMap<String, Object>> data = getAdapterList(
					styleDetailed.get(index).get("datasource").toString());
			for (Map<String, Object> automap : data) {
				if (automap.get("u_loginname").toString().length() <= 0)
					continue;
				name.add(automap.get("u_loginname").toString());
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, name);// spinner适配器
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);//最后调用方法将数据添加到自动补全控件中
			return spinner;

	}
	/**
	 * 获取下拉列表（auto）的内容
	 * @param AdapterFileName 表名
	 * @param querycondition  字段名
	 * @return spinner内容
	 */
	public List<HashMap<String, Object>> getAdapterList(String AdapterFileName) {
		List<HashMap<String, Object>> autodata=null;
		autodata=BaseClass.DBHelper.getList(AdapterFileName);
		return autodata;
	}
	//得到编辑的内容
	public HashMap<String, Object> getEditeValue(ArrayList<HashMap<String, Object>> styleDetailed){
		 HashMap<String, Object> editevalue = new HashMap<String,Object>();
		int itemCount = styleDetailed.size();
		int counter=0;
		View view = detailedLayout;
		for (int index = 0; index < itemCount; index++) {
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("name")&& counter == 0) {
				counter++;
				continue;
			}
			Map<String, Object> stylecontent = styleDetailed.get(index);
			if(stylecontent.get("style").equals("spinner")){
				if(bundle.getString("reply") != null){
					editevalue.put("sentid", detaild.get("sentid"));
					
				}else{
				Spinner spinner = (Spinner)view.findViewById(index);
				if(spinner.getSelectedItem() != null){
					String content = null;
					List<HashMap<String, Object>> value = getAdapterList(styleDetailed.get(index).get("datasource").toString());
					for(HashMap<String, Object> map : value){
						if(spinner.getSelectedItem().equals(map.get("u_loginname"))){
							String sentid = returnOAid(map.get("userid").toString());
							editevalue.put("sentid", sentid);
						}
					}
				}
				}
			}else if(stylecontent.get("style").equals("reply")){
				EditText edittext = (EditText) view.findViewById(index);
				String title = edittext.getText().toString().trim();
				editevalue.put("title",title);
			}else{
				EditText edittext = (EditText) view.findViewById(index);
				String xxneir = edittext.getText().toString().trim();
				editevalue.put("content",xxneir);
			}
		}
//		Date date = new Date();
		String userid = returnOAid(Global.getGlobalInstance().getUserid()).toString();
//		SimpleDateFormat ymdhms =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//完整的时间
//		SimpleDateFormat ymd =new SimpleDateFormat("yyyy-MM-dd");//只有年月日的时间
//		String hms=ymdhms.format(date);
//		String y=ymd.format(date);
		editevalue.put("userid", userid);

		return editevalue;
		
	}
	//通过移动执法的id 得到石家庄oa系统里边用户的id
	private String returnOAid(String ydzfid){
//		try {
//			String sql = "select * from UserRelationship where ydzfuserid = '"+ydzfid +"'";
//			ArrayList<HashMap<String, Object>> oaid = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
//			if(oaid!=null){
//				return oaid.get(0).get("oauserid");	
//			}
//			
//		} catch (SQLiteException e) {
//			e.printStackTrace();
//		}
//		return ydzfid;
		String userid = ydzfid.substring(5, ydzfid.length());
		return userid;
	}
	public void loadDetailBottomMenu() {
		LinearLayout bottom=(LinearLayout) findViewById(R.id.bottomLayout);
		bottom.removeAllViews();
		Button forwarding,reply,delete,submit; 
		reply=new  Button(this);
		forwarding=new  Button(this);
		delete=new  Button(this);
		submit=new  Button(this);
		submit.setVisibility(View.GONE);
		forwarding.setVisibility(View.GONE);
		reply.setVisibility(View.GONE);
		delete.setVisibility(View.GONE);

		ArrayList<HashMap<String, Object>> imageList=BaseClass.DBHelper.getList(Global.SystemConfig);
		//对比字段名
		final String ModuleID="moduleid";
		
		int i=0;
		boolean forwardinglbool=false,replybool=false,deletebool=false,submitbool=false;
		//加载图片
		if(bundle.getString("reply") != null||bundle.getString("forwarding")!=null){
//			for(HashMap<String,Object> imageMap:imageList){
//				if("xzcf".equals(imageMap.get(ModuleID).toString()) && imageMap.get("isview").equals("1")){
					submit.setBackgroundResource(R.drawable.btn_shap);
					submit.setText("提     交");
					submit.setTextColor(android.graphics.Color.WHITE);
					submit.setWidth(1);
					submit.setVisibility(View.VISIBLE);
					i++;
					submitbool=true;
//					break;
//				}
//			}
		}else{
//		 for(HashMap<String,Object> imageMap:imageList){
//			if("gd".equals(imageMap.get(ModuleID).toString()) && imageMap.get("isview").equals("1")){
				forwarding.setBackgroundResource(R.drawable.btn_shap);
				forwarding.setText("转发");
				forwarding.setTextColor(android.graphics.Color.WHITE);
				forwarding.setWidth(1);
				forwarding.setVisibility(View.VISIBLE);
				i++;
				forwardinglbool=true;
				
				
//			}
//			if("gd".equals(imageMap.get(ModuleID).toString()) && imageMap.get("isview").equals("1")){
				reply.setBackgroundResource(R.drawable.btn_shap);
				reply.setText("回复");
				reply.setTextColor(android.graphics.Color.WHITE);
				reply.setWidth(1);
				reply.setVisibility(View.VISIBLE);
				replybool=true;
				i++;
//			}
//			if("gd".equals(imageMap.get(ModuleID).toString()) && imageMap.get("isview").equals("1")){
				delete.setBackgroundResource(R.drawable.btn_shap);
				delete.setText("删除");
				delete.setTextColor(android.graphics.Color.WHITE);
				delete.setWidth(1);
				delete.setVisibility(View.VISIBLE);
				deletebool=true;
				i++;
//			}
			
//		}
		}
		//获取手机分辨率
		  DisplayMetrics dm = new DisplayMetrics();
		  getWindowManager().getDefaultDisplay().getMetrics(dm);  
		 //算出按钮的高宽
		  //int high=(int)((dm.widthPixels/(double)i)*((double)60/(double)300));
		  int width=(int)(dm.widthPixels/(double)i);
		  
		  if(deletebool){
			  delete.setLayoutParams(new LinearLayout.LayoutParams(
						 width,LinearLayout.LayoutParams.FILL_PARENT,0
			         ));
			  bottom.addView(delete);
		  }
		  if(replybool){
			  reply.setLayoutParams(new LinearLayout.LayoutParams(
						 width,LinearLayout.LayoutParams.FILL_PARENT,0
			         ));
			  bottom.addView(reply);
		  }
		  if(forwardinglbool){
			  forwarding.setLayoutParams(new LinearLayout.LayoutParams(
						 width,LinearLayout.LayoutParams.FILL_PARENT,0
			         ));
			  bottom.addView(forwarding);
		  }
		  if(submitbool){
			  submit.setLayoutParams(new LinearLayout.LayoutParams(
					  width,LinearLayout.LayoutParams.FILL_PARENT,0
			         ));
			  bottom.addView(submit);
		  }
		bottom.setVisibility(View.VISIBLE);
		forwarding.setOnClickListener(new OnClickListener() {//转发点击事件
			@Override
			public void onClick(View arg0) {
				bundle.remove("delete");
				bundle.remove("read");
				bundle.remove("reply");
				bundle.putString("forwarding", "forwarding");
				detailedLayout.removeAllViews();
				loadDetailBottomMenu();
				LoadDetailed(EMAILDetailActivity.this,businessObj.getCurrentID());
				
//				intent = new Intent();
//				intent.putExtras(bundle);
//				intent.setClass(EMAILDetailActivity.this, EMAILDetailActivity.class);	
//				startActivity(intent);				
			}
		});
		
		reply.setOnClickListener(new OnClickListener() {//回复击事件
			@Override
			public void onClick(View v) {
				bundle.putString("reply", "reply");
				bundle.remove("read");
				bundle.remove("forwarding");
				bundle.remove("delete");
				detailedLayout.removeAllViews();
				loadDetailBottomMenu();
				LoadDetailed(EMAILDetailActivity.this,businessObj.getCurrentID());
				
//				intent = new Intent();
//				intent.putExtras(bundle);
//				intent.setClass(EMAILDetailActivity.this, EMAILDetailActivity.class);	
//				startActivity(intent);

			}
		});
		
		delete.setOnClickListener(new OnClickListener() {//删除点击事件
			@Override
			public void onClick(View arg0) {
				Builder dialog = new AlertDialog.Builder(EMAILDetailActivity.this);
				dialog.setTitle("删除邮件");
				dialog.setMessage("确定要删除吗？");
				dialog.setPositiveButton("是", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(Net.checkNet(EMAILDetailActivity.this)){
						    bundle.remove("reply");
						    bundle.remove("read");
						    bundle.remove("forwarding");
						    bundle.putString("delete", "delete");
							String userid = returnOAid(Global.getGlobalInstance().getUserid()).toString();
							String msgid = businessObj.getCurrentID();
							HashMap<String, Object> primaryKey= new HashMap<String, Object>();
							primaryKey.put("userid", userid);
							primaryKey.put("msgid", msgid);	
							ArrayList<HashMap<String, Object>> condition = new ArrayList<HashMap<String,Object>>();
							condition.add(primaryKey);
							
							SentSndDelMsg(condition);
						}else{
							Toast.makeText(EMAILDetailActivity.this, "网络不通，请检查网络设置！", Toast.LENGTH_LONG).show();
						}
//							EMAILDetailActivity.this.finish();						
					}
					
				});
				dialog.setNegativeButton("否", null);
				dialog.show();
			}
		});
		submit.setOnClickListener(new OnClickListener() {//提交点击事件
			@Override
			public void onClick(View arg0) {
				HashMap<String, Object> editevalue = getEditeValue(styleDetailed);
				if(!editevalue.containsKey("sentid")){
				Toast.makeText(EMAILDetailActivity.this, "请选择收件人！", Toast.LENGTH_SHORT).show();	
				return;
				}
				if(Net.checkNet(EMAILDetailActivity.this)){
					ArrayList<HashMap<String, Object>> condition = new ArrayList<HashMap<String,Object>>();
					condition.add(editevalue);
					SentSndDelMsg(condition);
				}else{
				Toast.makeText(EMAILDetailActivity.this, "网络不通，请检查网络设置！", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
	}
	private void SentSndDelMsg(final ArrayList<HashMap<String, Object>> condition){
		handler = new Handler() {// UI线程Handler
			public void handleMessage(android.os.Message msg) {
						switch (msg.what) {
						case SUCCESS:	
							if(bundle.get("delete")!=null){
									
//								EmailXX emailxx = null;
								
									HashMap<String, String> primaryKey= new HashMap<String, String>();
									primaryKey.put("key", "msgid");
									primaryKey.put("keyValue", businessObj.getCurrentID());
								
									SqliteUtil.getInstance().delete(primaryKey, "T_YDZF_EMAIL");
//								
								bundle.remove("read");
								bundle.remove("forwarding");
								bundle.remove("delete");
								bundle.remove("reply");
								intent = new Intent();
								intent.putExtras(bundle);
								intent.setClass(EMAILDetailActivity.this, QueryListActivity.class);	
								startActivity(intent);
								if (pd != null)
									pd.cancel();
								Toast.makeText(EMAILDetailActivity.this,
										"删除成功！", Toast.LENGTH_SHORT).show();	
							}else{
								if (pd != null)
									pd.cancel();
							Toast.makeText(EMAILDetailActivity.this,
									"发送成功！", Toast.LENGTH_SHORT).show();							
							}
							
							EMAILDetailActivity.this.finish();
							break;
						case FILE:
							
							if (pd != null)
								pd.cancel();
							Toast.makeText(EMAILDetailActivity.this,
									"操作失败，请稍后再试！", Toast.LENGTH_SHORT).show();
							
							break;
						case OVERTIME:
							
							if (pd != null)
								pd.cancel();
							Toast.makeText(EMAILDetailActivity.this,
									"链接服务器超时，请稍后再试！", Toast.LENGTH_SHORT).show();
							
							break;
						}
					
				
			}
		};
		if (Net.checkNet(EMAILDetailActivity.this)){
			pd = new ProgressDialog(EMAILDetailActivity.this);
			pd.setMessage("正在发送，请稍等...");
			pd.setCancelable(false);
			pd.show();	
			new Thread(new Runnable() {				
				@Override
				public void run() {
					try {
						String Stringresult = null;
						
						if(bundle.get("reply")!=null||bundle.get("forwarding")!=null){
							 Stringresult = (String)WebServiceProvider.callWebService(Global.NAMESPACE, "SentMsg", condition, Global.getGlobalInstance().getSystemurl()+Global.OAWEBSERVICE_URL, WebServiceProvider.RETURN_STRING, true);	
						}
						if(bundle.get("delete")!=null){
							Stringresult = (String)WebServiceProvider.callWebService(Global.NAMESPACE, "DeleteMsg", condition, Global.getGlobalInstance().getSystemurl()+Global.OAWEBSERVICE_URL, WebServiceProvider.RETURN_STRING, true);	
						}
						if(Stringresult==null||Stringresult.length()<1){
							message = handler.obtainMessage();
							message.what = FILE;
							handler.sendMessage(message);
							
						}else{
						  if(Stringresult.equals("1")){
							message = handler.obtainMessage();
							message.what = SUCCESS;
							handler.sendMessage(message);
						  }
						
						  if(Stringresult.equals("0")){
							message = handler.obtainMessage();
							message.what = FILE;
							handler.sendMessage(message);
						  }
					  }
					} catch (IOException e) {
						message = handler.obtainMessage();
						message.what = OVERTIME;
						handler.sendMessage(message);
						e.printStackTrace();
					}	
			
					
				}
			}).start();
			
		}else{
			pd.cancel();
			Toast.makeText(EMAILDetailActivity.this,"网络连接失败，请检查网络设置！", Toast.LENGTH_SHORT).show();
			return;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
				bundle.remove("read");
				bundle.remove("forwarding");
				bundle.remove("delete");
				bundle.remove("reply");
			    intent = new Intent(EMAILDetailActivity.this,QueryListActivity.class);
				Bundle nextbundle = new Bundle();
				nextbundle = bundle;
				intent.putExtras(nextbundle);
				startActivity(intent);
				EMAILDetailActivity.this.finish();
			
		  }
		return super.onKeyDown(keyCode, event);
	}
	
}
