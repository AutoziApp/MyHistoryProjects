package com.mapuni.android.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.DocumentException;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.enterpriseArchives.AddBusinessActivity;

public class HelperDialog extends Dialog implements DialogInterface {

	private final String TAG = "HelperDialog";

	GridView helper_gridview = null;

	// private HelperController HelperC;

	public HelperDialog(Context context) {
		this(context, 0);
		// Dialog按对话框外面取消操作
		this.setCanceledOnTouchOutside(true);
	}

	public HelperDialog(Context context, int theme) {
		super(context, theme);

	}

	protected HelperDialog(Context context, boolean cancelable,
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

	@Override
	public void show() {
		if (helper_gridview == null) {
			/** 设置框体不显示标题栏 */
			super.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LayoutInflater factory = LayoutInflater.from(this.getContext());
			View view = factory.inflate(R.layout.helper, null);
			ArrayList<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();
			/** 从XML文件中读取配置 */
			listData = getMoreMenu("style_MoreMenu_helper.xml", "item");
			helper_gridview = (GridView) view
					.findViewById(R.id.helper_gridview);
			helper_gridview.setAdapter(new HelperListAdapter(this.getContext(),
					listData));
			helper_gridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					switch (arg2) {
					case 0:
						// HelperC = new HelperController(getContext());
						HelperController.getInstance().openDialog(
								HelperController.HANDLE_OPEN_DataSynDialog);
						HelperController.getInstance().hideDialog(
								HelperController.HANDLE_HIDE_HelperDialog);
						break;
					case 1:
						HelperController.getInstance().openDialog(
								HelperController.HANDLE_OPEN_SettingDialog);
						HelperController.getInstance().hideDialog(
								HelperController.HANDLE_HIDE_HelperDialog);
						break;
						// HelperC = new HelperController(getContext());
						/*HelperController.getInstance().openDialog(
								HelperController.HANDLE_OPEN_AddressBookDialog);
						HelperController.getInstance().hideDialog(
								HelperController.HANDLE_HIDE_HelperDialog);
						break;*/
					case 2:
						cancel();
						addCompany();
						
						break;
						// HelperC = new HelperController(getContext());
						/*HelperController.getInstance().openDialog(
								HelperController.HANDLE_OPEN_EnvManualDialog);
						HelperController.getInstance().hideDialog(
								HelperController.HANDLE_HIDE_HelperDialog);
						break;*/
						
					/*	HelperController.getInstance().openDialog(
								HelperController.HANDLE_OPEN_LawKnowAllDialog);
						HelperController.getInstance().hideDialog(
								HelperController.HANDLE_HIDE_HelperDialog);
						break;*/
					case 3:
						// HelperC = new HelperController(getContext());
						HelperController.getInstance().openDialog(
								HelperController.HANDLE_OPEN_SettingDialog);
						HelperController.getInstance().hideDialog(
								HelperController.HANDLE_HIDE_HelperDialog);
						break;
					case 4:
						// HelperC = new HelperController(getContext());
						HelperController.getInstance().openDialog(
								HelperController.HANDLE_OPEN_SettingDialog);
						HelperController.getInstance().hideDialog(
								HelperController.HANDLE_HIDE_HelperDialog);
						break;

					default:
						break;
					}
				}
			});
			super.setContentView(view);
		}
		super.show();
	}
	public void addCompany(){
		try{
			Intent helpint=new Intent(getContext(),AddBusinessActivity.class);
			getContext().startActivity(helpint);
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Override
	public void hide() {
		super.hide();
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
}
