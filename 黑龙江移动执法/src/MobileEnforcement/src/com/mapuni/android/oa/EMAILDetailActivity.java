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
	 * ������ϸ��Ϣ
	 * @param itemId
	 */
	@Override
	protected void addcontent(String itemId) {
		// �õ���ѯ��Ŀ������		
	    styleDetailed=businessObj.getStyleDetailed(this);
		HashMap<String, Object> querytj=new HashMap<String, Object>();
		querytj.put("id", itemId);
		detaild=businessObj.getDetailed(itemId);//ͨ��id��ѯ������
		
		int itemCount = styleDetailed.size();

		TableRow row = null;
		int counter=0;
		for (int index = 0; index < itemCount; index++) {
			// ������ TableLayout �����ؼ�
			TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rightLayoutParams.setMargins(0, 0, 15, 0);
			Map<String, Object> map = styleDetailed.get(index);			
			if(styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("name") && counter==0){
				counter++;
				continue;//name���Բ���Ҫ��ӵ��б�		
			}
			
			EditText editText=null;
			String value=null;
			if(bundle.getString("forwarding") != null&&index==1){//ת��
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
				editText.setText(textvalue);//����EditText����
//				if(hint2.equals("���ݣ�")){
//					rightLayoutParams = new TableRow.LayoutParams(
//							LayoutParams.WRAP_CONTENT, 400);
//					rightLayoutParams.setMargins(0, 0, 15, 0);
//				}
				row.addView(editText,rightLayoutParams);
				queryTable.addView(row);
			}
			    row = new TableRow(this);			
			    TextView textView = new TextView(this);			
			    //  ���
			    String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT).toString();
			    textView.setTextColor(android.graphics.Color.BLACK);			
			    textView.setGravity(Gravity.RIGHT);
			    textView.setPadding(15, 0, 0, 0);
			    row.addView(textView);
				if(bundle.getString("read") != null){//�Ķ�					
					textView.setText(hint);
					editText = new EditText(this);
					editText.setMaxEms(editText.getWidth());
					value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
					editText.setId(index); // �������ø��� EditText �� Id, ��ȡ���ݹ��� XPath
					// ���ʽʱ����
					editText.setFocusableInTouchMode(false);
					String textvalue=String.valueOf(detaild.get(value));									
					editText.setText(textvalue);//����EditText����
					row.addView(editText,rightLayoutParams);
					
				}
			if(bundle.getString("reply") != null){//�ظ�				
				if("spinner".equals(map.get("style"))){
				textView.setText("�ռ��ˣ�");
				editText = new EditText(this);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				editText.setId(index); // �������ø��� EditText �� Id, ��ȡ���ݹ��� XPath
				editText.setFocusableInTouchMode(false);
				String textvalue=String.valueOf(detaild.get(value));	
				editText.setText(textvalue);//����EditText����
				row.addView(editText,rightLayoutParams);
				}else if("reply".equals(map.get("style"))){
					textView.setText("�ظ����⣺");
					editText = new EditText(this);
					editText.setMaxEms(editText.getWidth());
					value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
					editText.setId(index); // �������ø��� EditText �� Id, ��ȡ���ݹ��� XPath
					editText.setFocusableInTouchMode(true);
					String textvalue=String.valueOf(detaild.get(value));	
					editText.setText(textvalue);//����EditText����
					row.addView(editText,rightLayoutParams);
				}else if("time".equals(map.get("style"))){
					textView.setText(hint);
					editText = new EditText(this);
					editText.setMaxEms(editText.getWidth());
					value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
					editText.setId(index); // �������ø��� EditText �� Id, ��ȡ���ݹ��� XPath
					editText.setFocusableInTouchMode(false);
					String textvalue=String.valueOf(detaild.get(value));	
					editText.setText(textvalue);//����EditText����
					row.addView(editText,rightLayoutParams);
				}else{
					textView.setText(hint);
					editText = new EditText(this);
					editText.setMaxEms(editText.getWidth());
					value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
					editText.setId(index); // �������ø��� EditText �� Id, ��ȡ���ݹ��� XPath
					editText.setFocusableInTouchMode(true);
					if(value.equals("msgtitle")||value.equals("xxneir")){
						editText.setText("");	
					}else{
					String textvalue=String.valueOf(detaild.get(value));									
					editText.setText(textvalue);//����EditText����
					}
//					if(hint.equals("���ݣ�")){
//						rightLayoutParams = new TableRow.LayoutParams(
//								LayoutParams.WRAP_CONTENT, 400);
//						rightLayoutParams.setMargins(0, 0, 15, 0);
//					}
					row.addView(editText,rightLayoutParams);
				}
			  }
			   if(bundle.getString("forwarding") != null){//ת��
				   if(styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString().equals("u_loginname")){ 
					   textView.setText("�ռ��ˣ�");
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
						editText.setId(index); // �������ø��� EditText �� Id, ��ȡ���ݹ��� XPath
						editText.setFocusableInTouchMode(false);						
						String textvalue=String.valueOf(detaild.get(value));									
						editText.setText(textvalue);//����EditText����
//						if(hint.equals("���ݣ�")){
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
	 * �õ��ռ���spinner
	 * 
	 */
	private Spinner getspinner(int index,String value,ArrayList<HashMap<String, Object>> styleDetailed){
		    Spinner spinner = new Spinner(this);				
			spinner.setPrompt("--��ѡ���ռ���--");
			spinner.setId(index);
			value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
			List<String> name=new ArrayList<String>();
			name.add("--��ѡ���ռ���--");
			List<HashMap<String, Object>> data = getAdapterList(
					styleDetailed.get(index).get("datasource").toString());
			for (Map<String, Object> automap : data) {
				if (automap.get("u_loginname").toString().length() <= 0)
					continue;
				name.add(automap.get("u_loginname").toString());
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, name);// spinner������
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);//�����÷�����������ӵ��Զ���ȫ�ؼ���
			return spinner;

	}
	/**
	 * ��ȡ�����б�auto��������
	 * @param AdapterFileName ����
	 * @param querycondition  �ֶ���
	 * @return spinner����
	 */
	public List<HashMap<String, Object>> getAdapterList(String AdapterFileName) {
		List<HashMap<String, Object>> autodata=null;
		autodata=BaseClass.DBHelper.getList(AdapterFileName);
		return autodata;
	}
	//�õ��༭������
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
//		SimpleDateFormat ymdhms =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//������ʱ��
//		SimpleDateFormat ymd =new SimpleDateFormat("yyyy-MM-dd");//ֻ�������յ�ʱ��
//		String hms=ymdhms.format(date);
//		String y=ymd.format(date);
		editevalue.put("userid", userid);

		return editevalue;
		
	}
	//ͨ���ƶ�ִ����id �õ�ʯ��ׯoaϵͳ����û���id
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
		//�Ա��ֶ���
		final String ModuleID="moduleid";
		
		int i=0;
		boolean forwardinglbool=false,replybool=false,deletebool=false,submitbool=false;
		//����ͼƬ
		if(bundle.getString("reply") != null||bundle.getString("forwarding")!=null){
//			for(HashMap<String,Object> imageMap:imageList){
//				if("xzcf".equals(imageMap.get(ModuleID).toString()) && imageMap.get("isview").equals("1")){
					submit.setBackgroundResource(R.drawable.btn_shap);
					submit.setText("��     ��");
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
				forwarding.setText("ת��");
				forwarding.setTextColor(android.graphics.Color.WHITE);
				forwarding.setWidth(1);
				forwarding.setVisibility(View.VISIBLE);
				i++;
				forwardinglbool=true;
				
				
//			}
//			if("gd".equals(imageMap.get(ModuleID).toString()) && imageMap.get("isview").equals("1")){
				reply.setBackgroundResource(R.drawable.btn_shap);
				reply.setText("�ظ�");
				reply.setTextColor(android.graphics.Color.WHITE);
				reply.setWidth(1);
				reply.setVisibility(View.VISIBLE);
				replybool=true;
				i++;
//			}
//			if("gd".equals(imageMap.get(ModuleID).toString()) && imageMap.get("isview").equals("1")){
				delete.setBackgroundResource(R.drawable.btn_shap);
				delete.setText("ɾ��");
				delete.setTextColor(android.graphics.Color.WHITE);
				delete.setWidth(1);
				delete.setVisibility(View.VISIBLE);
				deletebool=true;
				i++;
//			}
			
//		}
		}
		//��ȡ�ֻ��ֱ���
		  DisplayMetrics dm = new DisplayMetrics();
		  getWindowManager().getDefaultDisplay().getMetrics(dm);  
		 //�����ť�ĸ߿�
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
		forwarding.setOnClickListener(new OnClickListener() {//ת������¼�
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
		
		reply.setOnClickListener(new OnClickListener() {//�ظ����¼�
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
		
		delete.setOnClickListener(new OnClickListener() {//ɾ������¼�
			@Override
			public void onClick(View arg0) {
				Builder dialog = new AlertDialog.Builder(EMAILDetailActivity.this);
				dialog.setTitle("ɾ���ʼ�");
				dialog.setMessage("ȷ��Ҫɾ����");
				dialog.setPositiveButton("��", new DialogInterface.OnClickListener(){

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
							Toast.makeText(EMAILDetailActivity.this, "���粻ͨ�������������ã�", Toast.LENGTH_LONG).show();
						}
//							EMAILDetailActivity.this.finish();						
					}
					
				});
				dialog.setNegativeButton("��", null);
				dialog.show();
			}
		});
		submit.setOnClickListener(new OnClickListener() {//�ύ����¼�
			@Override
			public void onClick(View arg0) {
				HashMap<String, Object> editevalue = getEditeValue(styleDetailed);
				if(!editevalue.containsKey("sentid")){
				Toast.makeText(EMAILDetailActivity.this, "��ѡ���ռ��ˣ�", Toast.LENGTH_SHORT).show();	
				return;
				}
				if(Net.checkNet(EMAILDetailActivity.this)){
					ArrayList<HashMap<String, Object>> condition = new ArrayList<HashMap<String,Object>>();
					condition.add(editevalue);
					SentSndDelMsg(condition);
				}else{
				Toast.makeText(EMAILDetailActivity.this, "���粻ͨ�������������ã�", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
	}
	private void SentSndDelMsg(final ArrayList<HashMap<String, Object>> condition){
		handler = new Handler() {// UI�߳�Handler
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
										"ɾ���ɹ���", Toast.LENGTH_SHORT).show();	
							}else{
								if (pd != null)
									pd.cancel();
							Toast.makeText(EMAILDetailActivity.this,
									"���ͳɹ���", Toast.LENGTH_SHORT).show();							
							}
							
							EMAILDetailActivity.this.finish();
							break;
						case FILE:
							
							if (pd != null)
								pd.cancel();
							Toast.makeText(EMAILDetailActivity.this,
									"����ʧ�ܣ����Ժ����ԣ�", Toast.LENGTH_SHORT).show();
							
							break;
						case OVERTIME:
							
							if (pd != null)
								pd.cancel();
							Toast.makeText(EMAILDetailActivity.this,
									"���ӷ�������ʱ�����Ժ����ԣ�", Toast.LENGTH_SHORT).show();
							
							break;
						}
					
				
			}
		};
		if (Net.checkNet(EMAILDetailActivity.this)){
			pd = new ProgressDialog(EMAILDetailActivity.this);
			pd.setMessage("���ڷ��ͣ����Ե�...");
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
			Toast.makeText(EMAILDetailActivity.this,"��������ʧ�ܣ������������ã�", Toast.LENGTH_SHORT).show();
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
