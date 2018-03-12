package com.mapuni.android.helper;

import java.io.File;
import java.util.ArrayList;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.FileHelper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LawRegulationDetail extends Dialog implements DialogInterface{
	private Context mContext;
	private View lrdetail_view;
	private final String TAG = "LNFLFGShow";
	private ListView mListView;
	private MyListViewAdapter adapter;
	/**Ŀ���ļ����µ��ļ����б�*/
	ArrayList<File> filesForAdapter = new  ArrayList<File>();
	ArrayList<File> fileList = null;
	/** �������ִ���ֲ��ļ�Ŀ¼ */
	private final String filepath = Global.HJJCZFSC_PATH;

	public LawRegulationDetail(Context context) {
		super(context);
		mContext = context;
		/** Dialog���Ի�������ȡ������ */
		this.setCanceledOnTouchOutside(true);
	}
	
	public void showView(String BaseWord,String filename) {
		
			/** ���ÿ��岻��ʾ������ */
			super.requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			if (!BaseWord.equals("") && BaseWord != null) {
				String[] title = BaseWord.split("/");
				Log.i("dizhi", title[8]);
				//SetBaseStyle(titleLayout, title[8]);
			} else{
				//SetBaseStyle(titleLayout, "���ɷ����б�");
			}
			
			LayoutInflater factory = LayoutInflater.from(this.getContext());
			lrdetail_view =factory.inflate(R.layout.ln_flfg_query, null);
			//super.setSearchButtonVisiable(true);
			/*queryImg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					LinearLayout layout = new LinearLayout(mContext);
					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					final EditText editText = new EditText(mContext);
					TextView textview = new TextView(mContext);
					textview.setText("�ļ����ƣ�");
					layout.addView(textview);
					layout.addView(editText, param);
					AlertDialog.Builder dialog = new AlertDialog.Builder(
							mContext);
					dialog.setIcon(mContext.getResources().getDrawable(
							R.drawable.icon_mapuni_white));
					dialog.setTitle("���ɷ����ļ�����");
					dialog.setView(layout);
					dialog.setPositiveButton("ȷ��", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String filename = editText.getText().toString();
							String path = Global.HJJCZFSC_PATH;
							File file = new File(path);
							if (fileList != null) {
								fileList = null;
							}
							filesForAdapter.clear();
							new FileHelper().getAbsFiles(file, filesForAdapter);
							fileList = new ArrayList<File>();
							for (File f : filesForAdapter) {
								String name = f.getName();
								if (name.contains(filename))
									fileList.add(f);
							}
							filesForAdapter = fileList;
							adapter = null;
							adapter = new MyListViewAdapter(filesForAdapter);
							mListView.setAdapter(adapter);
							adapter.notifyDataSetChanged();

							dialog.dismiss();
						}
					});
					dialog.setNegativeButton("ȡ��", null);
					AlertDialog alertDialog = dialog.create();
					((Dialog) alertDialog).setCanceledOnTouchOutside(true);
					alertDialog.show();

				}

			});*/
			mListView = (ListView) lrdetail_view.findViewById(R.id.ln_flfg_list);
			Log.i(TAG, "�����б���");
//			Intent intent = getIntent();
//			if (isAddCompany) {
//				
//			}
//			String BaseWord = intent.getStringExtra("path");
//			String filename = intent.getStringExtra("filename");
			if(!"".equals(filename)&& filename != null){
				File file = new File(filepath);
				if (fileList != null) {
					fileList = null;
				}
				//filesForAdapter.clear();
				new FileHelper().getAbsFiles(file, filesForAdapter);
				fileList = new ArrayList<File>();
				for (File f : filesForAdapter) {
					String name = f.getName();
					if (name.contains(filename))
						fileList.add(f);
				}
				filesForAdapter = fileList;
			}
			if(!"".equals(BaseWord) && BaseWord != null){
				File file = new File(BaseWord);// �õ�Ŀ���ļ���
				File[] files = file.listFiles();
				filesForAdapter = getFileNameList(files);// �õ�Ŀ���ļ����µ��ļ���
				Log.i("dizhi", BaseWord);
//				String[] title = BaseWord.split("/");
//				Log.i("dizhi", title[7]);
//				SetBaseStyle(titleLayout, title[7]);
			}
			
			adapter = new MyListViewAdapter(filesForAdapter);
			mListView.setAdapter(adapter);

			mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					TextView view = (TextView) arg1.findViewById(R.id.list_name_tv);
					String lawfilePath = view.getTag().toString();
					File targetFile = new File(lawfilePath);
					
					
					// 2.���ļ�
					if (targetFile.exists()) {
						DisplayUitl.openfile(lawfilePath, mContext);
						 HelperController
						 .getInstance()
						 .hideDialog(
						 HelperController.HANDLE_HIDE_EnvManualDialog);
						cancel();
					} else {
						Toast.makeText(mContext, "�Բ����ļ������ڣ�", 0).show();
					}
				}
			});

			/** ����ĳһ��ļ����¼� */

			// middleLayout.addView(mListView);

			super.setContentView(lrdetail_view);
		super.show();
	}



	/**
	 * FileName: SiteWriteRecordActivity.java Description:ִ������ͨ�б�������
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
	 *            Create at: 2012-12-4 ����11:16:12
	 */
	private class MyListViewAdapter extends BaseAdapter {
		ArrayList<File> data;

		public MyListViewAdapter(ArrayList<File> data) {
			this.data = data;
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
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			RelativeLayout layout = (RelativeLayout) mInflater.inflate(
					R.layout.list_item_tool, null);

			ImageView headImageView = (ImageView) layout
					.findViewById(R.id.listitem_left_image);
			headImageView.setImageResource(R.drawable.base_icon_bst);

			TextView mTextView = (TextView) layout
					.findViewById(R.id.list_name_tv);
			String fileName = data.get(position).getName();
			mTextView.setTag(data.get(position).getAbsolutePath());
			mTextView.setText(fileName);

			return layout;
		}
	}

	private ArrayList<File> getFileNameList(File[] files) {

		ArrayList<File> fileNameList = new ArrayList<File>();
		for (File f : files) {
			if (!f.isDirectory()) {
				fileNameList.add(f);
			}
		}

		return fileNameList;
	}
}
