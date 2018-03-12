package com.goldnut.app.sdk.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.goldnut.app.sdk.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 带checkbox的列表适配器
 * @author zxl
 *
 * @param <T>
 */
public class CheckBoxArrayAdapter<T> extends BaseAdapter{
	
	private List<T> datas;
	private Context mContext;
	private String fieldName;
	private String methodName;
	
	/**
	 * 带checkbox的列表适配器
	 * @param context
	 * @param datas
	 * @param fieldname 要展示的属性的名称，需要是public
	 * 与methodname同时制定时以fieldname为准
	 * @param methodname 要展示的属性的获取方法名
	 */
	public CheckBoxArrayAdapter(Context context,List<T> datas,String fieldname,String methodname){
		this.mContext = context;
		this.datas = datas;
		this.fieldName = fieldname;
		this.methodName = methodname;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
//		if (convertView == null)
//		{
//			convertView = LayoutInflater.from(mContext)
//					.inflate(R.layout.item_checkbox_listadapter, null);
//			holder = new ViewHolder();
//
//			holder.tvName = (TextView) convertView.findViewById(R.id.tv);
//			convertView.setTag(holder);
//		} else
//		{
//			holder = (ViewHolder) convertView.getTag();
//		}
		
		LinearLayout view;
        TextView text = null;
        TouchCheckBox checkBox;

        if (convertView == null) {
            view = new LinearLayout(mContext);
            view.setOrientation(LinearLayout.HORIZONTAL);
            text = new TextView(mContext);
            text.setPadding(5, 5, 5, 5);
            checkBox = new TouchCheckBox(mContext);
            
            LayoutParams p1 = new LayoutParams(0, 
            		LayoutParams.WRAP_CONTENT, 1);
            LayoutParams p2 = new LayoutParams(0, 
            		LayoutParams.WRAP_CONTENT, 5);
            
            view.addView(text,p1);
            view.addView(checkBox, p2);
            
            
        } else {
            view = (LinearLayout) convertView;
        }
		
		Object data = datas.get(position);
		if(fieldName!=null){
			try {
				text.setText(getProperty(data, fieldName).toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(methodName !=null){
			try {
				text.setText(invokeMethod(data, methodName).toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return view;
	}
	
	public static Object invokeMethod(Object owner, String methodName)
			throws Exception {
		// 或得这个类的Class
		Class ownerClass = owner.getClass();
		// 配置参数的Class数组，作为寻找Method的条件
		// 通过Method名和参数的Class数组得到要执行的Method
		Method method = ownerClass.getMethod(methodName, new Class[0]);
		// 执行该Method，invoke方法的参数是执行这个方法的对象，和参数数组。返回值是Object，也既是该方法的返回值
		return method.invoke(owner, new Object[0]);
	}
	
	public static Object getProperty(Object owner, String fieldName) throws Exception { 
        //得到该对象的Class 
        Class ownerClass = owner.getClass(); 
        //通过Class得到类声明的属性 
        Field field = ownerClass.getField(fieldName); 
        //通过对象得到该属性的实例，如果这个属性是非公有的，这里会报IllegalAccessException。 
        Object property = field.get(owner); 
        return property; 
    } 
	
	private class ViewHolder {

		public TextView tvName;
	}

}
