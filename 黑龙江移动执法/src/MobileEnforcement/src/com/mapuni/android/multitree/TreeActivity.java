

package com.mapuni.android.multitree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.dataprovider.SqliteUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class TreeActivity extends Activity implements OnItemClickListener{
	
	ListView code_list;
	private LinearLayout toolBar;
	TreeActivity oThis = this;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
     /*   toolBar = (LinearLayout) findViewById(R.id.toolBar);
        code_list = (ListView)findViewById(R.id.code_list);*/
        code_list.setOnItemClickListener(this);
        
        setToolBar(new String[]{ "选中结果","","","退出" },new int[]{0, 3});
        
        setPreson();
    }
    /**
	 * 递归遍历节点
	 * 
	 * @return
	 */
	String serchCondition="HYIndustry";
	Node rootClide;
	Node root;
	Node parentNode;
	boolean rootNodeFlag=true;
	String sql="select t.code,t.name from Industry t  where t.pid='" + serchCondition + "'";
	ArrayList<HashMap<String, Object>> arrayListNode=SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
	private void setPreson(){
		//	for(HashMap<String, Object> hashMapNode:arrayListNode){
		//	root=new Node(hashMapNode.get("name").toString(),hashMapNode.get("code").toString());
		//	root.setIcon(R.drawable.icon_department);//设置图标
		//getAbsFiles(hashMapNode.get("code").toString(),root);

		root=new Node("行业类别","0000");
		//root.setIcon(R.drawable.icon_department);//设置图标
		parentNode=root;
		root=parentNode;
		getAbsTeee("HYIndustry","0000");

		TreeAdapter ta = new TreeAdapter(oThis,root);
		// 设置整个树是否显示复选框
		ta.setCheckBox(true);
		// 设置展开和折叠时图标
		//ta.setExpandedCollapsedIcon(R.drawable.tree_ex, R.drawable.tree_ec);
		// 设置默认展开级别
		ta.setExpandLevel(1);
		code_list.setAdapter(ta);

		//	}

	}
	boolean isleaf=false;
	boolean isparent=true;

	public void getAbsTeee(String serchCondition,String sName){
		String sqlClild="select t.code,t.name from Industry t  where t.pid='" + serchCondition + "'";
		ArrayList<HashMap<String, Object>> arrayListNodeClilde=SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlClild);
		if(arrayListNodeClilde.size()>0){
			isleaf=false;
			for(HashMap<String, Object> hashMapNodeClide:arrayListNodeClilde){	
				//Node leafNode=parentNode;
				if(isleaf && !parentNode.getValue().toString().equals(serchCondition)){
					parentNode=parentNode.getParent();
					if(!parentNode.getValue().toString().equals(serchCondition))
						parentNode=parentNode.getParent();

				}

				rootClide = new Node(hashMapNodeClide.get("name").toString(),hashMapNodeClide.get("code").toString());
				/*if(isleaf && leafNode.getValue().toString().equals(serchCondition))
					rootClide.setIcon(R.drawable.icon_police);//设置图标
				else
					rootClide.setIcon(R.drawable.icon_department);//设置图标
*/
				rootClide.setParent(parentNode);
				parentNode.add(rootClide);
				parentNode=rootClide;
				getAbsTeee(hashMapNodeClide.get("code").toString(),hashMapNodeClide.get("name").toString());

			}
		}else{
			isleaf=true;


		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		
		
		// 这句话写在最后面
		((TreeAdapter)parent.getAdapter()).ExpandOrCollapse(position);
	}
	
	
	
	// 设置底部工具栏
	private void setToolBar(String[] name_array,int[] pos_array){
		toolBar.removeAllViews();
		
		GridView toolbarGrid = new GridView(this);
		toolbarGrid.setNumColumns(4);// 设置每行列数
		toolbarGrid.setGravity(Gravity.CENTER);// 位置居中
		ToolbarAdapter adapter = new ToolbarAdapter(this,name_array,null,pos_array);
		toolbarGrid.setAdapter(adapter);
		toolbarGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0://显示选中结果
					List<Node> nodes = ((TreeAdapter)code_list.getAdapter()).getSeletedNodes();
					String msg = "";
					
					for(int i=0;i<nodes.size();i++){
						Node n = nodes.get(i);
						if(n.isLeaf()){
							if(!msg.equals(""))msg+=",";
							msg += n.getText()+"("+n.getValue()+")";
						}
						
					}
					if(msg.equals("")){
						Toast.makeText(oThis, "没有选中任何项", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(oThis, msg, Toast.LENGTH_SHORT).show();
					}
					
					break;
				case 3://返回
					oThis.finish();
					System.exit(0);
					break;
				}
			}
		});
		toolBar.addView(toolbarGrid);
	}
}