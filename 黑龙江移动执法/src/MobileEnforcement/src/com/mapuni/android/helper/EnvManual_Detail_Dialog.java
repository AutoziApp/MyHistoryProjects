package com.mapuni.android.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dom4j.DocumentException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.XmlHelper;

public class EnvManual_Detail_Dialog extends Dialog implements DialogInterface {

	private final String TAG = "EnvManual_Detail_Dialog";
	protected YutuLoading yutuLoading;
	private View view;
	private ScrollView env_detail_scview;
	// 查询得到数据列表
	private final ArrayList<HashMap<String, Object>> listData = null;
	// 每一列数据详细信息
	private final HashMap<String, Object> detailInfo = null;
	private TextView EnvManual_title;
	// 点选项在XML文件中的名称
	private String checkname = "";
	// 点选项字段个数
	private int itemcount;
	// 主键
	private final HashMap<String, String> primaryKey = new HashMap<String, String>();

	private LinearLayout env_detail_layout, line_layout;

	private EnvManualController EnvManualC;
	// 从九宫格的那个Item跳进来的
	private String ItemName;

	public EnvManual_Detail_Dialog(Context context) {
		this(context, 0);
		// Dialog按对话框外面取消操作
		this.setCanceledOnTouchOutside(true);
	}

	public EnvManual_Detail_Dialog(Context context, int theme) {
		super(context, theme);

	}

	protected EnvManual_Detail_Dialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public void showView(HashMap<String, Object> map, int itemCode)
			throws UnsupportedEncodingException {
		/** 设置框体不显示标题栏 */
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater factory = LayoutInflater.from(this.getContext());

		view = factory.inflate(R.layout.envmanual_detail, null);
		env_detail_scview = (ScrollView) view
				.findViewById(R.id.env_detail_scview);
		EnvManual_title = (TextView) view.findViewById(R.id.env_detail_title);
		env_detail_layout = (LinearLayout) view
				.findViewById(R.id.env_detail_layout);
		LinearLayout.LayoutParams layoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		/** 从XML文件中读取配置 */
		ArrayList<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();
		listData = getMoreMenu("style_env_detailed.xml", "item");

		switch (itemCode) {
		case EnvManualController.HBFL:
			ItemName = "HJYJ_FLFGBZ";
			break;
		case EnvManualController.ZDWJ:
			ItemName = "HJYJ_ZYWJBZ";
			break;
		case EnvManualController.WHP:
			ItemName = "HJYJ_WHP";
			break;
		case EnvManualController.YJYA:
			ItemName = "HJYJ_YAJAL";
			break;
		case EnvManualController.ZJK:
			ItemName = "HJYJ_ZJK";
			break;
		case EnvManualController.ADDRBOOKS:
			ItemName = "HELPER_BOOKS";
			break;
		}
		int j = 0;

		while (j < listData.size()) {
			checkname = listData.get(j).get("checkname").toString();
			if (checkname.equals(ItemName)) {
				HashMap<String, Object> listmap = new LinkedHashMap<String, Object>();
				listmap = listData.get(j);
				Env_getDetail(map, listmap);
				break;
			}
			j++;
		}

		super.setContentView(view);
		// super.setTitle("详细信息");
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void cancel() {
		super.cancel();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	/***
	 * 读取XML文件
	 * */
	protected ArrayList<HashMap<String, Object>> getMoreMenu(String xml,
			String nodename) {
		ArrayList<HashMap<String, Object>> MoreMenu = null;
		InputStream stream = null;
		try {
			stream = this.getContext().getResources().getAssets().open(xml);
			MoreMenu = XmlHelper.getList(stream, nodename);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return MoreMenu;

	}

	/***
	 * 显示列表中每个字段的详细信息，添到进布局文件中
	 * */
	private void Env_getDetail(HashMap<String, Object> map,
			HashMap<String, Object> listmap)
			throws UnsupportedEncodingException {

		Iterator iter_listdata = listmap.entrySet().iterator();
		while (iter_listdata.hasNext()) {
			Map.Entry entry1 = (Map.Entry) iter_listdata.next();
			Object listkey = entry1.getKey();
			Object listval = entry1.getValue();

			// 遍历传递过来的map
			Iterator iter_map = map.entrySet().iterator();// 生成迭代器
			while (iter_map.hasNext()) {
				Map.Entry entry2 = (Map.Entry) iter_map.next();
				Object mapkey = entry2.getKey();
				Object mapval = entry2.getValue();

				if (listkey.equals(mapkey)) {
					String getvalue = mapval.toString();

					/** 添加一个线性布局存放标题 和相对应的值 */

					line_layout = new LinearLayout(getContext());
					line_layout.setOrientation(0);
					LinearLayout.LayoutParams line_layoutParams = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					TextView name = new TextView(this.getContext());
					name.setLayoutParams(line_layoutParams);
					name.setGravity(Gravity.RIGHT);
					name.setWidth(140);
					name.setText(listval.toString());
					name.setTextColor(android.graphics.Color.BLACK);
					line_layout.addView(name);

					// 判断如果值为pdf或doc的文档，则使用按钮，点击按钮打开文件
					if (getvalue.contains("pdf") || getvalue.contains("doc")) {
						final String fjname = getvalue;
						Button btnvalue = new Button(this.getContext());
						btnvalue.setText(getvalue);
						btnvalue.setWidth(450);
						btnvalue.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// 文件存放路径
								String targetFilePath = Global.SDCARD_RASK_DATA_PATH
										+ "fj/FLFGBZ" + "/" + fjname;
								File targetFile = new File(targetFilePath);
								// 如果文件存在打开文件
								if (targetFile.exists()) {
									// HelperController
									// .getInstance()
									// .hideDialog(
									// HelperController.HANDLE_HIDE_LawKnowAllDialog);
									DisplayUitl.openfile(targetFilePath,
											getContext());
									// 如果文件不存在给出提示信息
								} else {
									Dialog dialog;
									dialog = new Dialog(getContext());
									AlertDialog.Builder builder = new AlertDialog.Builder(
											getContext());
									TextView tv = new TextView(getContext());
									tv.setBackgroundColor(android.graphics.Color.WHITE);
									tv.setTextColor(android.graphics.Color.BLACK);
									tv.setHeight(50);
									tv.setText("对不起，该文件不存在！");
									tv.setGravity(Gravity.CENTER);
									builder.setView(tv);
									dialog = builder.create();
									dialog.getWindow()
											.setType(
													WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
									dialog.show();
								}

							}
						});
						line_layout.addView(btnvalue);
						// 判断如果值为html
					} else if (getvalue.contains("table")) {
						final String htmlname = getvalue;
						Button btn_other = new Button(this.getContext());
						btn_other.setText("查看");
						btn_other.setWidth(450);
						btn_other
								.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Dialog dialog;
										dialog = new Dialog(getContext());
										AlertDialog.Builder builder = new AlertDialog.Builder(
												getContext());

										WebView webview = new WebView(
												getContext());
										webview.loadDataWithBaseURL(null,
												htmlname, "text/html", "utf-8",
												null);
										builder.setView(webview);
										dialog = builder.create();
										dialog.getWindow()
												.setType(
														WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
										dialog.show();
									}
								});
						line_layout.addView(btn_other);
					} else {
						EditText namevalue = new EditText(this.getContext());
						namevalue.setLayoutParams(line_layoutParams);
						namevalue.setText(getvalue);
						namevalue.setWidth(450);
						namevalue.setFocusable(false);
						line_layout.addView(namevalue);
					}
					env_detail_layout.addView(line_layout);
					break;
				}

			}
		}
	}

}
