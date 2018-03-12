
package com.mapuni.android.multitree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mapuni.android.MobileEnforcement.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 底部工具栏数据适配器
 * @author weisc
 */
public class ToolbarAdapter extends BaseAdapter{
	private Context con;
    private List<Map<String, Object>> alls = new ArrayList<Map<String, Object>>();
    private LayoutInflater lif;
    private List<Integer> posClickable = new ArrayList<Integer>();
    
    /// 设置绑定字段 
    public static final String NAME = "name";//文字
    public static final String IMAGE = "image";//图片
    
   /**
    * 构造函数
    * @param context
    * @param name_array 菜单文字数组
    * @param image_array 菜单图片数组
    * @param position 可以点击的菜单位置数组
    */
    public ToolbarAdapter(Context context,
    		String[] name_array,int[]image_array,int[]position) {
    	this.con = context;
    	this.lif = (LayoutInflater) con.
    	getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
    	if(name_array != null && name_array.length>0){
    		for(int i=0;i<name_array.length;i++){
    			Map<String,Object> item = new HashMap<String,Object>();
    			item.put(NAME, name_array[i]);
    			if(image_array!=null && image_array.length==name_array.length){
    				item.put(IMAGE, image_array[i]);
    			}
    			alls.add(item);
        	}
    	}
    	
    	if(position != null && position.length>0){
    		for(int i=0;i<position.length;i++){
    			posClickable.add(position[i]);
        	}
    	}
    	
    }
    
    @Override
	public int getCount() {
		// TODO Auto-generated method stub
    	return alls.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return alls.get(arg0);
	}

	@Override
	public long getItemId(int index) {
		// TODO Auto-generated method stub
		return index;
	}
	
	public String getBh(int index){
		Map<String,Object> item = alls.get(index);
		String bh = item.get(NAME).toString();
		return bh;
	}
	
	@Override
	public View getView(int indexID, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (view == null) {
			view = this.lif.inflate(R.layout.item_toolbar, null);
			Map<String, Object> mapItem = alls.get(indexID);
			
			ViewHolder holder = new ViewHolder();
			//holder.ivImage = (ImageView) view.findViewById(R.id.item_image);
			holder.tvText = (TextView) view.findViewById(R.id.item_text);
			
			if(mapItem != null){
			    String name = mapItem.get(NAME).toString();
			    holder.tvText.setText(name);
			    
			    if(mapItem.get(IMAGE)==null){
			    	//holder.ivImage.setVisibility(View.GONE);
			    }else{
			    	//holder.ivImage.setVisibility(View.VISIBLE);
			    	//holder.ivImage.setImageResource(Integer.valueOf(mapItem.get(IMAGE).toString()));
			    }
			}
			view.setFocusable(false);
			view.setClickable(false);
			//System.out.println("this position is:"+indexID+",text is:"+holder.tvText.getText());
			
			// 控制是否可以点击和获得焦点
			if(!posClickable.contains(indexID)){
				view.setFocusable(true);
				view.setClickable(true);
				//System.out.println("this position is clickable:"+indexID+",text is:"+holder.tvText.getText());
			}
			
		}
		return view;
		
	}
	/**
	 * 
	 * 列表项控件集合
	 *
	 */
	private class ViewHolder{
		//ImageView ivImage;//
		TextView tvText;//
	}
}
