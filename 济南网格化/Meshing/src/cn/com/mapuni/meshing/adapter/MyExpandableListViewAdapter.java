package cn.com.mapuni.meshing.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.meshing.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Map<String, List<String>> dataset = new HashMap<String, List<String>>();
    private List<String> parentList;
    
    private Context context;
    
    public MyExpandableListViewAdapter(Map<String, List<String>> dataset, List<String> parentList, Context context) {
        this.dataset = dataset;
        this.parentList = parentList;
        this.context = context;
    }



    //  ���ĳ�������ĳ������
    @Override
    public Object getChild(int parentPos, int childPos) {
        return dataset.get(parentList.get(parentPos)).get(childPos);
    }

    //  ��ø��������
    @Override
    public int getGroupCount() {
        return dataset.size();
    }

    //  ���ĳ�������������Ŀ
    @Override
    public int getChildrenCount(int parentPos) {
        return dataset.get(parentList.get(parentPos)).size();
    }

    //  ���ĳ������
    @Override
    public Object getGroup(int parentPos) {
        return dataset.get(parentList.get(parentPos));
    }

    //  ���ĳ�������id
    @Override
    public long getGroupId(int parentPos) {
        return parentPos;
    }

    //  ���ĳ�������ĳ�������id
    @Override
    public long getChildId(int parentPos, int childPos) {
        return childPos;
    }

    //  �����������������Ӧ�����Ƿ�����ȶ���id���������Ŀǰһֱ���Ƿ���false��û��ȥ�Ķ���
    @Override
    public boolean hasStableIds() {
        return false;
    }


    //  ���������ʾ��view
    @Override
    public View getChildView(int parentPos, int childPos, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context, R.layout.child_item, null);
        }

		TextView text = (TextView) view.findViewById(R.id.two_class_interface_name_tv);
		RadioButton isSelect = (RadioButton) view.findViewById(R.id.two_class_cb);
        text.setText(dataset.get(parentList.get(parentPos)).get(childPos));
        return view;
    }

    //  �����Ƿ��ѡ�У������Ҫ��������ĵ���¼�����Ҫ����true
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    //  ��ø�����ʾ��view
    @Override
    public View getGroupView(int parentPos, boolean isExpanded, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context,R.layout.parent_item, null);
        }        
        TextView text = (TextView) view.findViewById(R.id.tv_parent_title);
        text.setText(parentList.get(parentPos));
//        //�ж�isExpanded�Ϳ��Կ����ǰ��»��ǹرգ�ͬʱ����ͼƬ
//        if(isExpanded){
//            text.setSelected(false);
//        }else{
//            text.setSelected(true);
//        }
        return view;
    }


}
