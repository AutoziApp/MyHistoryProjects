package com.mapuni.android.enforcement;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.SqliteUtil;
/**
 * 执法文书显示多个询问表单或勘察表单
 * 
 *
 */
public class SiteWriteRecordAddMoreActivity extends BaseActivity {

	/** 父页面布局 */
	private RelativeLayout fatherRelativeLayout;
	private LinearLayout middleLayout;
	/** 任务编号 */
	private String rwbh = "";
	private String qyid;
	private ListView mListView;
	private Button mButton;
	private SqliteUtil sqliteUtil;
	private ArrayList<HashMap<String, Object>> dataList;
	private MyListViewAdapter adapter;
	private final String BaseWord = Global.SDCARD_RASK_DATA_PATH + "data/task/";
	String fileNameStr = null;
	String codeStr = null;
	private String jyzfqymc;
	private boolean isSearch;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** 屏幕状态 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);

		Bundle bundle = getIntent().getExtras();
		fileNameStr = bundle.getString("fn");
		codeStr = bundle.getString("code");
		
		isSearch = getIntent().getBooleanExtra("isSearch", false);

		fatherRelativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		SetBaseStyle(fatherRelativeLayout, fileNameStr);
		setTitleLayoutVisiable(true);
		sqliteUtil = SqliteUtil.getInstance();
		rwbh = getIntent().getStringExtra("rwbh");
		qyid = getIntent().getStringExtra("qyid");
		if (qyid == "" && qyid == null && qyid.equals("")) {
			jyzfqymc = getIntent().getStringExtra("jyzfqymc");
		}
		View relativelay = LayoutInflater.from(this).inflate(R.layout.sitewriterecordaddmore, null);
		mListView = (ListView) relativelay.findViewById(R.id.list);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mButton = (Button) relativelay.findViewById(R.id.add);
		RWXX rwxx = new RWXX();
		String rwzt = rwxx.getTaskid(rwbh);

		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showInputNameDialog();
			}

		});
		
		if (codeStr.equals("0")) {
			dataList = getXWData();
		} else if (codeStr.equals("1")) {
			dataList = getKCData();
		} else if (codeStr.equals("3")) {
			dataList = getJCData();
		}
		if (isSearch) {
			mButton.setEnabled(false);
			Toast.makeText(SiteWriteRecordAddMoreActivity.this, "当前企业只能查看！", Toast.LENGTH_SHORT).show();
		}
		
		//判断当前任务师傅上传如果上传不让添加询问笔录  byk
		ArrayList<HashMap<String, Object>> queryTaskData = sqliteUtil.queryBySqlReturnArrayListHashMap("select isexcute from TaskEnpriLink where taskid = '" + rwbh + "' and qyid = '" + qyid + "'");
		String qyzts = queryTaskData.get(0).get("isexcute").toString();
		if (qyzts != null) {
			
			if (qyzts.equalsIgnoreCase("3")) {
				if("3".equals(qyzts)){
					mButton.setEnabled(false);
				Toast.makeText(SiteWriteRecordAddMoreActivity.this, "已上传的执法记录,不能修改！", Toast.LENGTH_SHORT).show();
				}
			}else{
				if (dataList.size() == 0) {
					if (codeStr.equals("0")) {
						dataList = getXWData();
					} else if (codeStr.equals("1")) {
						dataList = getKCData();
					} else if (codeStr.equals("3")) {
						dataList = getJCData();
					}
				}
			}
		}
//		//默认添加一条
//		if ("".equals(rwzt)||rwzt.equals("003") || rwzt.equals("005")) {
//
//			if (dataList.size() == 0) {
//				 Intent intent = new
//				 Intent(SiteWriteRecordAddMoreActivity.this,
//				 TaskSiteEnforcementActivity.class);
//				 intent.putExtra("qyid", qyid);
//				 intent.putExtra("rwbh", rwbh);
//				 bundle = new Bundle();
//				 bundle.putString("fn", fileNameStr);
//				 bundle.putString("code", codeStr);
//				 bundle.putString("guid", "");
//				 intent.putExtras(bundle);
//				 startActivity(intent);
//				
//				
//				String GUID = UUID.randomUUID().toString();
//				ContentValues contentValues = new ContentValues();
//				contentValues.put("guid", GUID);// ID
//				contentValues.put("taskid", rwbh);// 任务ID
//				contentValues.put("entcode", qyid);// 污染源(企业)ID
//				if (codeStr.equals("0")) {
//					sqliteUtil.insert(contentValues, "T_ZFWS_XWBL");
//				} else if (codeStr.equals("1")) {
//					sqliteUtil.insert(contentValues, "T_ZFWS_KCBL");
//				} else if (codeStr.equals("3")) {
//					sqliteUtil.insert(contentValues, "T_ZFWS_JCJL");
//				}
//
//				contentValues = new ContentValues();
//
//				contentValues.put("id", UUID.randomUUID().toString());
//				contentValues.put("taskid", rwbh);// 任务ID
//				contentValues.put("enterid", qyid);// 污染源(企业)ID
//				contentValues.put("updatetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
//				contentValues.put("billid", GUID);
//				if (codeStr.equals("0")) {
//					contentValues.put("templettype", "XWBL");
//
//					contentValues.put("templetname", fileNameStr + 1);
//				} else if (codeStr.equals("1")) {
//					contentValues.put("templettype", "KCBL");
//					contentValues.put("templetname", fileNameStr + 1);
//				} else if (codeStr.equals("3")) {
//					contentValues.put("templettype", "JCJL");
//					contentValues.put("templetname", fileNameStr + 1);
//				}
//				sqliteUtil.insert(contentValues, "ExeLawsTemplet");
				
				
				//进入时默认添加一条,但需要用户输入名
			//	showInputNameDialog();
//				if (codeStr.equals("0")) {
//					dataList = getXWData();
//				} else if (codeStr.equals("1")) {
//					dataList = getKCData();
//				} else if (codeStr.equals("3")) {
//					dataList = getJCData();
//				}

		//	}
//		} else {
////			mButton.setEnabled(false);
////			Toast.makeText(SiteWriteRecordAddMoreActivity.this, "已上传的执法记录,不能修改！", Toast.LENGTH_SHORT).show();
//		}

		adapter = new MyListViewAdapter(this, dataList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				if (qyid != "" && qyid != null && !qyid.equals("")) {
					Intent intent = null;
					if (codeStr.equals("0") || codeStr.equals("1")) {
						intent = new Intent(SiteWriteRecordAddMoreActivity.this, TaskSiteEnforcementActivity.class);
					} else {
						intent = new Intent(SiteWriteRecordAddMoreActivity.this, TaskSiteEnforcementActivity.class);
					}
					intent.putExtra("qyid", qyid);
					intent.putExtra("rwbh", rwbh);
					intent.putExtra("blname",dataList.get(position).get("templetname").toString());
					
					intent.putExtra("isSearch", isSearch);
					Bundle bundle = new Bundle();
					bundle.putString("fn", fileNameStr);
					bundle.putString("code", codeStr);
					bundle.putString("guid", dataList.get(position).get("billid").toString());
					bundle.putString("id", dataList.get(position).get("id").toString());//id,用以当billid异常为空的时候更新exelawstemplet表
					intent.putExtras(bundle);
					startActivity(intent);

				} else {
					Intent intent = new Intent(SiteWriteRecordAddMoreActivity.this, TaskSimplelawEnforcementActivity.class);
					intent.putExtra("blname",dataList.get(position).get("templetname").toString());
					intent.putExtra("rwbh", rwbh);
					intent.putExtra("jyzfqymc", jyzfqymc);
					Bundle bundle = new Bundle();
					bundle.putString("fn", fileNameStr);
					bundle.putString("guid", dataList.get(position).get("billid").toString());
					intent.putExtras(bundle);
					startActivity(intent);
				}

			}

		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				showDelDialog(arg2, arg1);
				return true;
			}
		});

		middleLayout.addView(relativelay);
	}
	/**弹出填写笔录名称的对话框*/
	private void showInputNameDialog() {
		addBL(fileNameStr);
	}
	/**添加笔录
	 * @param name 笔录名称
	 * */
	private void addBL(String name) {
		ContentValues contentValues = new ContentValues();
		String GUID = UUID.randomUUID().toString();
		contentValues.put("guid", GUID);// ID
		contentValues.put("taskid", rwbh);// 任务ID
		if (codeStr.equals("0")) {
			contentValues.put("surveyentcode", qyid);// 污染源(企业)ID
			sqliteUtil.insert(contentValues, "T_ZFWS_XWBL");
		} else if (codeStr.equals("1")) {
			contentValues.put("entcode", qyid);// 污染源(企业)ID
			sqliteUtil.insert(contentValues, "T_ZFWS_KCBL");
		} else if (codeStr.equals("3")) {
			contentValues.put("entcode", qyid);// 污染源(企业)ID
			sqliteUtil.insert(contentValues, "T_ZFWS_JCJL");
		}
		contentValues = new ContentValues();
		contentValues.put("id", UUID.randomUUID().toString());
		contentValues.put("taskid", rwbh);// 任务ID
		contentValues.put("enterid", qyid);// 污染源(企业)ID
		contentValues.put("updatetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		contentValues.put("billid", GUID);
		contentValues.put("templetname", name);
		if (codeStr.equals("0")) {
			contentValues.put("templettype", "XWBL");
			dataList = getXWData();
			if(dataList != null && dataList.size() > 0) {
				contentValues.put("templetname", name + dataList.size());
			} else {
				contentValues.put("templetname", name);
			}
		} else if (codeStr.equals("1")) {
			contentValues.put("templettype", "KCBL");
		} else if (codeStr.equals("3")) {
			contentValues.put("templettype", "JCJL");
		}
		sqliteUtil.insert(contentValues, "ExeLawsTemplet");
		if (codeStr.equals("0")) {
			dataList = getXWData();
		} else if (codeStr.equals("1")) {
			dataList = getKCData();
		} else if (codeStr.equals("3")) {
			dataList = getJCData();
		}
		adapter = new MyListViewAdapter(SiteWriteRecordAddMoreActivity.this, dataList);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	protected void showDelDialog(final int position, final View view) {
		AlertDialog.Builder deleteab = new AlertDialog.Builder(SiteWriteRecordAddMoreActivity.this);
		deleteab.setTitle("删除");
		deleteab.setMessage("你确定要删除吗？");
		deleteab.setIcon(com.mapuni.android.MobileEnforcement.R.drawable.icon_delete);
		deleteab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				String guid = dataList.get(position).get("billid").toString();
				// 根据任务编号查询出该任务执行状态，如果已上传，不能删除
				RWXX rwxx = new RWXX();
				String rwzt = rwxx.getTaskid(rwbh);
				if (rwzt.equals("003") || rwzt.equals("005")) {

					if (codeStr.equals("0")) {
						// 删除目录表单
						String sql1 = "delete from ExeLawsTemplet where TaskId='" + rwbh + "' and EnterID='" + qyid + "' and BillId = '"
								+ guid + "' and TempletType = 'XWBL'";
						SqliteUtil.getInstance().execute(sql1);
						// 删 除企业相关的询问笔录表记录
						String sql3 = "delete from T_ZFWS_XWBL where TaskId='" + rwbh + "' and SurveyEntCode='" + qyid + "' and Guid = '" + guid
								+ "'";
						SqliteUtil.getInstance().execute(sql3);
						// 删除企业相关的一问一答表记录
						String sql5 = "delete from T_ZFWS_XWJLWD where TaskID='" + rwbh + "' and EntID='" + qyid + "' and billid = '"
								+ guid + "'";
						SqliteUtil.getInstance().execute(sql5);
					} else if (codeStr.equals("1")) {
						// 删除目录表单
						String sql1 = "delete from ExeLawsTemplet where TaskId='" + rwbh + "' and EnterID='" + qyid + "' and BillId = '"
								+ guid + "' and TempletType = 'KCBL'";
						SqliteUtil.getInstance().execute(sql1);
						// 删除企业相关勘察笔录表记录
						String sql2 = "delete from T_ZFWS_KCBL where TaskId='" + rwbh + "' and EntCode='" + qyid + "' and Guid = '" + guid
								+ "'";
						SqliteUtil.getInstance().execute(sql2);

					} else if (codeStr.equals("3")) {
						// 删除目录表单
						String sql1 = "delete from ExeLawsTemplet where TaskId='" + rwbh + "' and EnterID='" + qyid + "' and BillId = '"
								+ guid + "' and TempletType = 'JCJL'";
						SqliteUtil.getInstance().execute(sql1);
						// 删除企业相关记录
						String sql2 = "delete from T_ZFWS_JCJL where TaskId='" + rwbh + "' and EntCode='" + qyid + "' and Guid = '" + guid
								+ "'";
						SqliteUtil.getInstance().execute(sql2);

					}

					// 删除完之后重新查询数据
					if (codeStr.equals("0")) {
						dataList = getXWData();
					} else if (codeStr.equals("1")) {
						dataList = getKCData();
					} else if (codeStr.equals("3")) {
						dataList = getJCData();
					}
					adapter = new MyListViewAdapter(SiteWriteRecordAddMoreActivity.this, dataList);
					mListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(SiteWriteRecordAddMoreActivity.this, "不能删除已上传的执法记录！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		deleteab.setNegativeButton("取消", null);
		AlertDialog ad = deleteab.create();
		ad.show();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (codeStr.equals("0")) {
			dataList = getXWData();
		} else if (codeStr.equals("1")) {
			dataList = getKCData();
		} else if (codeStr.equals("3")) {
			dataList = getJCData();
		}
		adapter = new MyListViewAdapter(this, dataList);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 
	 */
	private class MyListViewAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> data;
		Context context;

		public MyListViewAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
			this.data = data;
			this.context = context;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				LayoutInflater mInflater = LayoutInflater.from(context);
				convertView = (LinearLayout) mInflater.inflate(R.layout.listitem, null);
				holder = new ViewHolder();
				holder.headImageView = (ImageView) convertView.findViewById(R.id.listitem_left_image);
				holder.mTextView = (TextView) convertView.findViewById(R.id.listitem_text);
				holder.tailImageView = (ImageView) convertView.findViewById(R.id.listitem_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.headImageView.setImageResource(R.drawable.icon_left_not_checked);

			holder.mTextView.setText(data.get(position).get("templetname").toString());
			if ("现场监察记录单".equals(fileNameStr)) {
				String xcSql = "select sitestaffname from T_ZFWS_JCJL where taskid = '" + rwbh + "' and entcode = '" + qyid
						+ "' and guid = '" + data.get(position).get("billid") + "'";
				String jbqk2 = SqliteUtil.getInstance().getDepidByDepName(xcSql);
				if (jbqk2 != null && !jbqk2.equals("")) {
					holder.headImageView.setImageResource(R.drawable.icon_left_checked);
				}

			} else if ("污染源现场监察记录单".equals(fileNameStr)) {
				String xcSql = "select * from T_YDZF_SiteInspectionRecord where taskid = '" + rwbh + "' and PollutionsourceCode = '" + qyid
						+ "'";
				ArrayList<HashMap<String, Object>> jbqk2 = new ArrayList<HashMap<String, Object>>();
				jbqk2 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(xcSql);
				if (jbqk2.size() > 0) {
					holder.headImageView.setImageResource(R.drawable.icon_left_checked);
				}

			} else if ("调查询问笔录".equals(fileNameStr)) {
				String xwSql = "select surveypeoplecradcode from T_ZFWS_XWBL where taskid = '" + rwbh + "' and SurveyEntCode = '" + qyid
						+ "' and guid = '" + data.get(position).get("billid") + "'";
				String survey = SqliteUtil.getInstance().getDepidByDepName(xwSql);
				if (survey != null && !survey.equals("")) {
					holder.headImageView.setImageResource(R.drawable.icon_left_checked);
				}
			} else if ("现场检查（勘察）笔录".equals(fileNameStr)) {
				String kcSql = "select surveypeoplename from T_ZFWS_KCBL where taskid = '" + rwbh + "' and entcode = '" + qyid
						+ "' and guid = '" + data.get(position).get("billid") + "'";
				String survey = SqliteUtil.getInstance().getDepidByDepName(kcSql);
				if (survey != null && !survey.equals("")) {
					holder.headImageView.setImageResource(R.drawable.icon_left_checked);
				}
			} else if ("环境监察通知书".equals(fileNameStr)) {
				String jsSql = "select BreakBehavior from Survey_JSTZS where taskid = '" + rwbh + "' and entid = '" + qyid + "'";
				String Behavior = SqliteUtil.getInstance().getDepidByDepName(jsSql);
				if (Behavior != null && !Behavior.equals("")) {
					holder.headImageView.setImageResource(R.drawable.icon_left_checked);
				}
			}

			holder.tailImageView.setImageResource(R.drawable.icon_arrow_yellow);

			return convertView;
		}

		class ViewHolder {
			ImageView headImageView;
			TextView mTextView;
			ImageView tailImageView;
		}
	}

	/**
	 * 获取询问笔录数据
	 * 
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> getXWData() {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		/** 查询出笔录表中的数据 */
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * from ExeLawsTemplet where taskid = '" + rwbh + "' and enterid = '" + qyid
						+ "' and templettype = 'XWBL' order by updatetime desc");
		return data;
	}

	/**
	 * 获取勘察笔录数据
	 * 
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> getKCData() {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		/** 查询出笔录表中的数据 */
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * from ExeLawsTemplet where taskid = '" + rwbh + "' and enterid = '" + qyid
						+ "' and templettype = 'KCBL' order by updatetime desc");
		return data;
	}

	/**
	 * 获取监察记录数据
	 * 
	 * @return
	 */
	private ArrayList<HashMap<String, Object>> getJCData() {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		/** 查询出笔录表中的数据 */
		data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * from ExeLawsTemplet where taskid = '" + rwbh + "' and enterid = '" + qyid
						+ "' and templettype = 'JCJL' order by updatetime desc");
		return data;
	}
}
