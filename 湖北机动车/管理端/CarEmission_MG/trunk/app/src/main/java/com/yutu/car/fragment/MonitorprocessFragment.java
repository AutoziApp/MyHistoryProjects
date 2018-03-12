package com.yutu.car.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yutu.car.R;

import java.util.ArrayList;
import java.util.List;

import static com.yutu.car.R.layout.item;

/**
 * Created by lenovo on 2017/4/11.
 */

public class MonitorprocessFragment extends BaseFragment {
    private ExpandableListView exList;
    private View rootView;

    //    String[] names=new String[]{"环宇通","安安","顺通","东盛源","汇宝"};
//    String[] names2=new String[]{"汽油三号线","汽油二号线"};
    List<String> group;           //组列表  
    List<List<String>> child;     //子列表  


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_monitor, container, false);
        }
        initView(rootView);
        return rootView;
    }


    private void initView(View view) {
        setTitle(view, "检测过程监控");
        initializeData();

        exList = (ExpandableListView) view.findViewById(R.id.exList);
        exList.setAdapter(new CustomAdapter());
    }


    /**
     * 初始化组、子列表数据
     */
    private void initializeData() {
        group = new ArrayList<String>();
        child = new ArrayList<List<String>>();

        addInfo("环宇通", new String[]{"汽油一号线", "汽油二号线", "汽油三号线"});
        addInfo("安安", new String[]{"汽油一号线", "汽油二号线", "汽油三号线"});
        addInfo("顺通", new String[]{"汽油一号线", "汽油二号线", "汽油三号线"});
        addInfo("东盛源", new String[]{"汽油一号线", "汽油二号线", "汽油三号线"});
        addInfo("汇宝", new String[]{"汽油一号线", "汽油二号线", "汽油三号线"});

    }


    /**
     * 模拟给组、子列表添加数据
     *
     * @param g-group
     * @param c-child
     */
    private void addInfo(String g, String[] c) {
        group.add(g);
        List<String> childitem = new ArrayList<String>();
        for (int i = 0; i < c.length; i++) {
            childitem.add(c[i]);
        }
        child.add(childitem);
    }

    public static MonitorprocessFragment newInstance(String s) {
        MonitorprocessFragment fragment = new MonitorprocessFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    class CustomAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return group.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return child.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return group.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return child.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHold hold;
            if (convertView == null) {
                convertView = LayoutInflater.from(mAct).inflate(R.layout.item_group_list, null);
                hold = new GroupHold();
                hold.mText = (TextView) convertView.findViewById(R.id.groupText);
                hold.mImage = (ImageView) convertView.findViewById(R.id.group_arrow);
                convertView.setTag(hold);
            } else {
                hold = (GroupHold) convertView.getTag();
            }
            hold.mText.setText(group.get(groupPosition));
            if (isExpanded) {
                hold.mText.setTextColor(mAct.getResources().getColor(R.color.item_group_text_select));
                hold.mImage.setImageResource(R.mipmap.arrow_down);
            } else {
                hold.mText.setTextColor(mAct.getResources().getColor(R.color.item_group_text));
                hold.mImage.setImageResource(R.mipmap.arrow_right);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHold hold;
            if (convertView == null) {
                convertView = LayoutInflater.from(mAct).inflate(R.layout.item_child_list, null);
                hold = new ChildHold();
                hold.mText = (TextView) convertView.findViewById(R.id.childText);
                convertView.setTag(hold);
                convertView.setTag(R.id.childText, item);
            } else {
                hold = (ChildHold) convertView.getTag();
            }
            hold.mText.setText(child.get(groupPosition).get(childPosition));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public class ChildHold {
            public TextView mText;
        }

        class GroupHold {
            TextView mText;
            ImageView mImage;
        }
    }


}
