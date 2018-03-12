package com.yutu.car.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.yutu.car.R;
import com.yutu.car.activity.CareManageActivity;
import com.yutu.car.activity.ChartActivity;
import com.yutu.car.activity.DocActivity;
import com.yutu.car.activity.MapActivity;
import com.yutu.car.activity.SearchCarActivity;
import com.yutu.car.activity.StationActivity;
import com.yutu.car.activity.StationWXActivity;
import com.yutu.car.activity.TimeActivity;
import com.yutu.car.presenter.LineControl;
import com.yutu.car.presenter.StationControl;
import com.yutu.car.presenter.StationWXControl;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lenovo on 2017/4/7.
 */

public class InforqueryStatistics extends BaseFragment {
    private View rootView;
    private  Intent intent;

    private int[] imageRes = {R.mipmap.jianxiujigou,R.mipmap.weixiujigou,R.mipmap.cheliangxinxi,
            R.mipmap.jishuwendang
            };
    private String[]name={"检验机构","维修机构","车辆信息","技术文档"};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(null ==rootView ) {
            rootView = inflater.inflate(R.layout.fragment_infor, container, false);
        }
        setTitle(rootView, "信息查询");
        initview();
        return rootView;
    }
    private void initview() {
        GridView gridview = (GridView)rootView.findViewById(R.id.gridview);
        int length = imageRes.length;
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", imageRes[i]);
            map.put("ItemText", name[i]);
            lstImageItem.add(map);
        }
        SimpleAdapter saImageItems = new SimpleAdapter(getActivity(),
                lstImageItem,
                R.layout.item,
                new String[]{"ItemImage", "ItemText"},
                new int[]{R.id.img_shoukuan, R.id.txt_shoukuan});
        gridview.setAdapter(saImageItems);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (imageRes[position]){
                    case R.mipmap.jianxiujigou:
                        intent=new Intent(getActivity(), StationActivity.class);
                        intent.putExtra("control",new StationControl());
                        startActivity(intent);
                        break;
                    case R.mipmap.weixiujigou:
                        intent=new Intent(getActivity(), StationWXActivity.class);
                        intent.putExtra("control",new StationWXControl());
                        startActivity(intent);
                        break;
                    case R.mipmap.cheliangxinxi:
                        intent=new Intent(getActivity(), SearchCarActivity.class);
                        startActivity(intent);
                        break;
                    case R.mipmap.jishuwendang:
                        intent=new Intent(getActivity(),DocActivity.class);
                        startActivity(intent);
                        break;
//                    case R.mipmap.ditu:
//                        intent=new Intent(getActivity(), MapActivity.class);
//                        startActivity(intent);
//                        break;
                    case  R.mipmap.jiancexitong:
                        intent=new Intent(getActivity(), TimeActivity.class);
                        startActivity(intent);
                        break;
//                    case R.mipmap.tongji:
//                        intent=new Intent(getActivity(), ChartActivity.class);
//                        intent.putExtra("url","file:///android_asset/manager/danyanghuawu.html");
//                        startActivity(intent);
//                        break;
//                    case R.mipmap.jianceguocheng:
//                        intent=new Intent(getActivity(), StationActivity.class);
//                        intent.putExtra("control",new LineControl());
//                        startActivity(intent);
//                        break;
                    default:
                        break;
                }
            }
        });
    }

    public static InforqueryStatistics newInstance(String s) {
        InforqueryStatistics fragment = new InforqueryStatistics();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
