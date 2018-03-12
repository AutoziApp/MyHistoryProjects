package com.mapuni.android.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;

public class LawRegulationDialog extends Dialog implements DialogInterface{
	
	private List<Object> groupdata;
	private Context mContext;
	private View lawregu_view=null;
	private ArrayList<ArrayList<HashMap<String, String>>> childMapData;
	private ExpandableListView expandListLiew;
	private LawTreeViewAdapter treeListadapter;
	private LawRegulationDetail LawRegulationDetail=null;
	private Button fuhan,xxfl;
	private RelativeLayout two_list_tool_layout;// 标题布局
	/** 环境监察执法手册文件目录 */
	private final String filepath = Global.HJJCZFSC_PATH;
	//目录下的文件
	ArrayList<File> filesForAdapter = null;
	ArrayList<File> fileList = null;
	
	/** 日志记录标志 */
	private final String TAG = "LawRegulationDialog";

	public LawRegulationDialog(Context context) {
		super(context);
		mContext = context;
		/** Dialog按对话框外面取消操作 */
		this.setCanceledOnTouchOutside(true);
	}

	public LawRegulationDialog(Context context, int theme) {
		super(context, theme);

	}

	protected LawRegulationDialog(Context context, boolean cancelable,
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
		if(lawregu_view==null){
			/** 设置框体不显示标题栏 */
			super.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LayoutInflater factory = LayoutInflater.from(this.getContext());
			lawregu_view = factory.inflate(R.layout.lawregulation, null);
			two_list_tool_layout = (RelativeLayout) lawregu_view
					.findViewById(R.id.ln_two_list_tool_layout);
		
			/*queryImg.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					LinearLayout layout = new LinearLayout(mContext);
					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					final EditText editText = new EditText(mContext);
					TextView textview = new TextView(mContext);
					textview.setText("文件名称：");
					layout.addView(textview);
					layout.addView(editText, param);
					AlertDialog.Builder dialog = new Builder(mContext);
					dialog.setIcon(mContext.getResources().getDrawable(
							R.drawable.icon_mapuni_white));
					dialog.setTitle("法律法规文件搜索");
					dialog.setView(layout);
					dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String filename = editText.getText().toString();
//							File file = new File(filepath);
//							if (fileList != null) {
//								fileList = null;
//							}
//							new FileHelper().getAbsFiles(file, filesForAdapter);
//							fileList = new ArrayList<File>();
//							for (File f : filesForAdapter) {
//								String name = f.getName();
//								if (name.contains(filename))
//									fileList.add(f);
//							}
//							filesForAdapter = fileList;
							Intent intent = new Intent(mContext,LNFLFGShow.class);
							intent.putExtra("filename", filename);
							intent.putExtra("path", "");
							
							startActivity(intent);
							
						}
					});
					dialog.setNegativeButton("取消", null);
					AlertDialog alertDialog = dialog.create();
					((Dialog) alertDialog).setCanceledOnTouchOutside(true);
					alertDialog.show();
					
				}
			});*/
			File filespath = new File(filepath);
			groupdata = new ArrayList<Object>();
			childMapData = new ArrayList<ArrayList<HashMap<String, String>>>();
			expandListLiew = (ExpandableListView) lawregu_view
					.findViewById(R.id.expandList);

			treeListadapter = new LawTreeViewAdapter(mContext, 30);
			expandListLiew.setGroupIndicator(getContext().getResources().getDrawable(
					R.layout.expandablelistviewselector));
			getAbsFiles(filespath);
			addTree();

			super.setContentView(lawregu_view);
		}
		super.show();
	}
	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	/**
	 * 递归遍历文件夹
	 * 
	 * @param filepath
	 * @return
	 */
	int i = 0;
	int j = 0;

	public void getAbsFiles(File directory) {
		i++;
		File[] files = directory.listFiles();
		HashMap<String, String> map = null;
		ArrayList<HashMap<String, String>> list = null;
		for (File file : files) {
			// 如果是目录
			if (file.isDirectory()) {
				if (i == 2) {
					if (j == 0) {
						list = new ArrayList<HashMap<String, String>>();
					}
					j++;
					map = new HashMap<String, String>();
					map.put("title", file.getName());
					map.put("id", file.getAbsolutePath());
					list.add(map);
					if (j == file.getParentFile().listFiles().length) {
						i = 1;
						childMapData.add(list);
					}

				}
				if (i == 1) {
					if (j == 0) {
						map = new HashMap<String, String>();
						map.put("title", file.getName());
						map.put("id", file.getAbsolutePath());
						groupdata.add(map);
						getAbsFiles(file);
					}
					j = 0;
				}

			}
		}
	}

	private void addTree() {
		List<LawTreeViewAdapter.TreeNode> treeNode = treeListadapter
				.GetTreeNode();
		for (int i = 0; i < groupdata.size(); i++) {

			LawTreeViewAdapter.TreeNode node = new LawTreeViewAdapter.TreeNode();
			node.parent = groupdata.get(i);
			// 将解析出来的数据遍历加载到树上
			for (HashMap<String, String> treemap : childMapData.get(i)) {
				if (treemap != null) {
					if (treemap.size() > 0) {
						HashMap<String, Object> child = new HashMap<String, Object>();
						child.put("title", treemap.get("title").toString());
						child.put("id", treemap.get("id").toString());
						node.childs.add(child);
					}

				}

			}
			treeNode.add(node);

		}

		treeListadapter.UpdateTreeNode(treeNode);
		expandListLiew.setAdapter(treeListadapter);
//		expandListLiew.expandGroup(0);
		// 给树添加单击事件
		expandListLiew.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView expandablelistview,
					View view, int pid, int cid, long l) {
				TextView textView = (TextView) view
						.findViewById(R.id.flfgtitle);

				String textfilepath = textView.getTag().toString();
				//显示文件列表dialog
				LawRegulationDetail = new LawRegulationDetail(mContext);
				LawRegulationDetail.getWindow().setType(
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				LawRegulationDetail.showView(textfilepath, "");
				return true;
			}
		});
	}
}
